import React from 'react';
import Form from "react-bootstrap/Form";

import * as constants from './Constants.js';
import Draw from './Draw.js'
import {Socket} from 'engine.io-client';

const SCREEN_WIDTH = 1024;
const SCREEN_HEIGHT = 768;
const SCREEN_MIDDLE_X = SCREEN_WIDTH / 2;
const SCREEN_MIDDLE_Y = SCREEN_HEIGHT / 2;
const NUM_BACKGROUND_CLOUDS = 15;

class Game extends React.Component {
  constructor(props) {
    super(props);

    this.draw = this.draw.bind(this);
    this.onDraw = this.onDraw.bind(this);
    this.getMouseEventAngle = this.getMouseEventAngle.bind(this);
    this.onMouseMove = this.onMouseMove.bind(this);
    this.onMouseDown= this.onMouseDown.bind(this);
    this.onMouseUp = this.onMouseUp.bind(this);
    this.initConnectionToGame = this.initConnectionToGame.bind(this);

    this.backgroundCloudLocations = this.generateBackgroundCloudLocations(NUM_BACKGROUND_CLOUDS);
    this.gameState = {players: [], projectiles: [], powerups: []};
    this.clientSocket = new Socket(constants.GAME_SOCKET_ADDRESS, {path: '/engine.io/game', reconnection: true, reconnectionDelay: 10, reconnectionAttempts: 10});
    this.mouseDownTimeout = null;
    this.isGameOver = false;

    const searchParams = new URLSearchParams(props.location.search);
    this.playerName = searchParams.get('playerName') ? 'john doe' : searchParams.get('playerName');
    this.playerId = Math.floor(Math.random() * 1000000);
  }

  componentDidMount() {
    this.initConnectionToGame(this.clientSocket, this.playerId, this.playerName);
    this.drawer = new Draw();
    this.fps = 100;
    this.canvas = this.refs.canvas;
    const ctx = this.refs.canvas.getContext('2d');
    this.setState({updated:true});
    this.draw(this.canvas, ctx);
  }

  initConnectionToGame(playerId, playerName) {
    console.log('connecting');
    this.clientSocket.on('open', () => {
      console.log('socket opened');
      this.clientSocket.send(JSON.stringify({type: 'playerjoin', id: this.playerId, name: this.playerName}));
      this.clientSocket.on('message', (data) => {
        const msg = JSON.parse(data);
        if (msg.type == 'gameInfo') {
          this.gameState = msg;
        } else if (msg.type == 'gameOver') {
          this.isGameOver = true;
        }
      });
      this.clientSocket.on('close', () => {
        console.log('disconnected');
        if (!this.isGameOver) {
          this.initConnectionToGame(playerId, playerName);
        } else {
          console.log('game over');
        }
      });
      document.addEventListener("mousemove", this.onMouseMove, false);
      document.addEventListener("mousedown", this.onMouseDown, false);
      document.addEventListener("mouseup", this.onMouseUp, false);
    });
  }

  generateBackgroundCloudLocations(numClouds) {
    // TODO: Better way of generating random obstacles within boundaries.
    // Currently, boundaries are hardcoded.
    const clouds = [];
    for (var i =0 ; i < numClouds; i++) {
      clouds.push({xPos: Math.floor(Math.random() * 3000) - 2000, yPos: Math.floor(Math.random() * 1000) - 500});
    }
    return clouds;
  }

  getMouseEventAngle(inputMouseX, inputMouseY) {
    const canvas = document.getElementById('myCanvas');
    var rect = canvas.getBoundingClientRect();
    var mouseX = (inputMouseX - rect.left) - SCREEN_MIDDLE_X;
    var mouseY = (inputMouseY - rect.top) - SCREEN_MIDDLE_Y;
    mouseY *= -1;

    var angle = 0;
    if (mouseX == 0 && mouseY < 0) {
      angle = 0;
    } else if (mouseX == 0 && mouseY > 0) {
      angle = 180;
    } else if (!(mouseX == 0 && mouseY == 0)) {
      angle = Math.atan(mouseY / mouseX) * 180 / Math.PI;
      if (mouseX < 0) {
        angle += 180;
      }
    }
    if (angle < 0) {
      angle = 360 + angle;
    }
    return angle;
  }

  onMouseDown(event) {
    if (event.which != 1) {
      // Only handle left click events.
      return;
    }
    const playerId = this.playerId;
    const angle = this.getMouseEventAngle(event.clientX, event.clientY);
    this.clientSocket.send(JSON.stringify({type: 'shootbullet', id: playerId, angle: angle}));
    this.mouseDownTimeout = setInterval(() => {
      this.clientSocket.send(JSON.stringify({type: 'shootbullet', id: playerId, angle: angle}))
    }, 50);
  }

  onMouseUp(event) {
    if (this.mouseDownTimeout) {
      clearTimeout(this.mouseDownTimeout);
    }
  }

  onMouseMove(event) {
    const angle = this.getMouseEventAngle(event.clientX, event.clientY);
    this.clientSocket.send(JSON.stringify({type: 'updateplayer', id: this.playerId, angle: angle}));
  }

  draw(canvas, ctx) {
      // Use timeout to throttle FPS.
      setTimeout(() => this.onDraw(canvas, ctx), 1000 / this.fps);
  }

  onDraw(canvas, ctx) {
    canvas.style.background = "#87CEFA";
    var ctx = canvas.getContext('2d');

    // Clear screen before redrawing.
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    var myPlayer;
    var myX;
    var myY;

    for (const player of this.gameState.players) {
      if (player.id == this.playerId) {
        myX = player.xPos;
        myY = player.yPos;
        myPlayer = player;
        break;
      }
    }

    // Draw boundaries.
    // TODO: Don't hardcode heights and widths.
    this.drawer.drawRect(ctx, this.gameState.upperXboundary - myX + SCREEN_MIDDLE_X, -10000, 10000, 20000, '#00BFFF');
    this.drawer.drawRect(ctx, this.gameState.lowerXboundary - myX + SCREEN_MIDDLE_X - 20000, -10000, 20000, 20000, '#00BFFF');
    this.drawer.drawRect(ctx, -10000, this.gameState.upperYboundary - myY + SCREEN_MIDDLE_Y - 10000, 20000, 10000, '#00BFFF');
    this.drawer.drawRect(ctx, -10000, this.gameState.lowerYboundary - myY + SCREEN_MIDDLE_Y, 20000, 10000, '#00BFFF');

    for (const backgroundCloudLocation of this.backgroundCloudLocations) {
      this.drawer.drawCloud(ctx, backgroundCloudLocation.xPos - myX + SCREEN_MIDDLE_X, backgroundCloudLocation.yPos - myY + SCREEN_MIDDLE_Y);
    }

    for (const powerup of this.gameState.powerups) {
      this.drawer.drawPowerUp(ctx, powerup, powerup.xPos - myX + SCREEN_MIDDLE_X, powerup.yPos - myY + SCREEN_MIDDLE_Y, powerup.radius * 2, powerup.radius * 2);
    }

    for (const player of this.gameState.players) {
      const airplaneColor = player.id == this.playerId ? 'airplaneBlack' : 'airplaneWhite';
      const color = player.id == this.playerId ? 'green' : 'red';
      this.drawer.drawAirplane(ctx, airplaneColor, player.xPos - myX + SCREEN_MIDDLE_X, player.yPos - myY + SCREEN_MIDDLE_Y, player.angle, player.radius * 2, player.radius * 2);
      this.drawer.drawText(ctx, player.xPos - myX + SCREEN_MIDDLE_X, player.yPos - myY + SCREEN_MIDDLE_Y + 40, player.name, 'center', color, '12px Arial');
      this.drawer.drawHealthBar(ctx, player.xPos - myX + SCREEN_MIDDLE_X, player.yPos - myY + SCREEN_MIDDLE_Y - 10, player.health);
    }

    for (const projectile of this.gameState.projectiles) {
      this.drawer.drawProjectile(ctx, projectile, projectile.xPos - myX + SCREEN_MIDDLE_X, projectile.yPos - myY + SCREEN_MIDDLE_Y);
    }

    // Draw current user weapon.
    this.drawer.drawCurrentUserWeapon(ctx, myPlayer);

    if (this.isGameOver) {
      this.drawer.drawText(ctx, SCREEN_WIDTH / 2, 40, 'Game over. Tough scene.', 'center', 'red');
    }

    window.requestAnimationFrame(() => this.draw(canvas, ctx));
  }

  render() {
    return (
      <div>
        <br/><br/>
        <h1>Aerial Combat</h1>
        <br/>
        <canvas id="myCanvas" ref="canvas" width={SCREEN_WIDTH} height={SCREEN_HEIGHT}/>
        <br/>
        Clouds - Image by rawpixel.com<br/>
        <a target="_blank" href="https://icons8.com/icon/E3jrJhWWAvey/airplane">Airplane</a> icon by <a target="_blank" href="https://icons8.com">Icons8</a><br/>
        <a target="_blank" href="https://icons8.com/icon/iesmzwuTdYDg/bomb">bomb</a> icon by <a target="_blank" href="https://icons8.com">Icons8</a><br/>
        <a target="_blank" href="https://icons8.com/icon/eVRlPdxRxmNe/medical-box">Medical Box</a> icon by <a target="_blank" href="https://icons8.com">Icons8</a><br/>
        <a target="_blank" href="https://icons8.com/icon/117057/missile">Missile</a> icon by <a target="_blank" href="https://icons8.com">Icons8</a><br/>
        <a target="_blank" href="https://icons8.com/icon/12430/parachute">Parachute</a> icon by <a target="_blank" href="https://icons8.com">Icons8</a><br/>
        <br/>
        <br/>
        <br/>
      </div>
    );
  }
}

export default Game;
