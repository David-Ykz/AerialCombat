import React from 'react';
import Form from "react-bootstrap/Form";

import * as constants from './Constants.js';
const { Socket } = require('engine.io-client');

const SCREEN_WIDTH = 1024;
const SCREEN_HEIGHT = 768;
const SCREEN_MIDDLE_X = SCREEN_WIDTH / 2;
const SCREEN_MIDDLE_Y = SCREEN_HEIGHT / 2;

class Game extends React.Component {
  constructor(props) {
    super(props);

    this.drawCircle = this.drawCircle.bind(this);
    this.draw = this.draw.bind(this);
    this.onDraw = this.onDraw.bind(this);
    this.getMouseEventAngle = this.getMouseEventAngle.bind(this);
    this.onMouseMove = this.onMouseMove.bind(this);
    this.onMouseUp = this.onMouseUp.bind(this);

    this.obstacles = [{xPos: 10, yPos: 20}, {xPos: 100, yPos: 200}];
    this.gameState = {players: [], projectiles: []};
    this.clientSocket = new Socket(constants.GAME_SOCKET_ADDRESS, {path: '/engine.io/game'});
    const searchParams = new URLSearchParams(props.location.search);
    this.playerId = searchParams.get('playerId') ? Math.floor(Math.random() * 1000000) : searchParams.get('playerId');
    this.playerName = searchParams.get('playerName') ? 'john doe' : searchParams.get('playerName');
    this.initConnectionToGame(this.clientSocket, this.playerId, this.playerName);
  }

  componentDidMount() {
    this.fps = 30;
    this.canvas = this.refs.canvas;
    const ctx = this.refs.canvas.getContext('2d');
    this.draw(this.canvas, ctx);
  }

  initConnectionToGame(playerId, playerName) {
    console.log('connecting');
    this.clientSocket.on('open', () => {
      console.log('socket opened');
      this.clientSocket.send(JSON.stringify({type: 'playerjoin', id: playerId, name: playerName}));
      this.clientSocket.on('message', (data) => {
        console.log('got', data);
        this.gameState = JSON.parse(data);
        console.log(this.gameState);
      });
      this.clientSocket.on('close', () => { console.log('socket closed') });
      console.log('adding listeners');
      document.addEventListener("mousemove", this.onMouseMove, false);
      document.addEventListener("mouseup", this.onMouseUp, false);
      console.log('added listeners');
    });
  }

  getMouseEventAngle(inputMouseX, inputMouseY) {
    var rect = this.canvas.getBoundingClientRect();
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

  onMouseUp(event) {
    if (event.which != 1) {
      // Only handle left click events.
      return;
    }
    const angle = this.getMouseEventAngle(event.clientX, event.clientY);
    this.socket.send(JSON.stringify({type: 'shootbullet', id: this.playerId, angle: angle}));
  }

  onMouseMove(event) {
    const angle = this.getMouseEventAngle(event.clientX, event.clientY);
    this.socket.send(JSON.stringify({type: 'updateplayer', id: this.playerId, angle: angle}));
  }

  drawCircle(ctx, x, y, r, color='#b8e015') {
      ctx.beginPath();
      ctx.arc(x, y, r, 0, Math.PI*2);
      ctx.fillStyle = color;
      ctx.fill();
      ctx.closePath();
  }

  draw(canvas, ctx) {
      // Use timeout to throttle FPS.
      setTimeout(() => this.onDraw(canvas, ctx), 1000 / this.fps);
  }

  onDraw(canvas, ctx) {
    // Clear screen before redrawing.
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    var myX;
    var myY;

    for (const player of this.gameState.players) {
      if (player.id == this.playerId) {
        myX = player.xPos;
        myY = player.yPos;
        break;
      }
    }

    for (const player of this.gameState.players) {
      this.drawCircle(ctx, player.xPos - myX + SCREEN_MIDDLE_X, player.yPos - myY + SCREEN_MIDDLE_Y, 10);
    }
    for (const projectile of this.gameState.projectiles) {
      this.drawCircle(ctx, projectile.xPos - myX + SCREEN_MIDDLE_X, projectile.yPos - myY + SCREEN_MIDDLE_Y, 2, 'black');
    }
    for (const obstacle of this.obstacles) {
      this.drawCircle(ctx, obstacle.xPos - myX + SCREEN_MIDDLE_X, obstacle.yPos - myY + SCREEN_MIDDLE_Y, 10, 'blue');
    }

    window.requestAnimationFrame(() => this.draw(canvas, ctx));
  }

  render() {
    return (
      <div>
        <br/><br/>
        <h1>Dogfight</h1>
        <br/>
        <canvas ref="canvas" width={{SCREEN_WIDTH}} height={{SCREEN_HEIGHT}}/>
      </div>
    );
  }
}

export default Game;
