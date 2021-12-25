import * as constants from './Constants.js';
import openSocket from 'socket.io-client';

import React from 'react';
import Form from "react-bootstrap/Form";

class Game extends React.Component {
  constructor(props) {
    super(props);

    // In React, we "bind" class functions to the class itself. You need to do
    // this when you add new functions but otherwise you can ignore this
    // section.
    this.drawCircle = this.drawCircle.bind(this);
    this.draw = this.draw.bind(this);
    this.onDraw = this.onDraw.bind(this);
    this.onKeyUp = this.onKeyUp.bind(this);
    this.onKeyDown = this.onKeyDown.bind(this);
    this.onMouseMove = this.onMouseMove.bind(this);
    this.onMouseClick = this.onMouseClick.bind(this);

    // this.clientSocket = openSocket(constants.SOCKET_SERVER_ADDRESS);
  }

  componentDidMount() {
    this.circleX = 5;
    this.circleY = 5;
    this.dx = 0;
    this.dy = 0;
    this.fps = 30;

    this.canvas = this.refs.canvas;
    const ctx = this.refs.canvas.getContext('2d');

    document.addEventListener("keydown", this.onKeyDown, false);
    document.addEventListener("keyup", this.onKeyUp, false);
    document.addEventListener("mousemove", this.onMouseMove, false);
    document.addEventListener("click", this.onMouseClick, false);

    this.clientSocket.on('broadcast', function(data){
      console.log('received', data);
    });

    this.draw(this.canvas, ctx);
  }

  onKeyDown(event) {
    var keyCode = event.keyCode;
    switch (keyCode) {
      case 68: //d
        this.dx = 2;
        break;
      case 83: //s
        this.dy = 2;
        break;
      case 65: //a
        this.dx = -2;
        break;
      case 87: //w
        this.dy = -2;
        break;
    }
  }

  onKeyUp(event) {
    var keyCode = event.keyCode;

    switch (keyCode) {
      case 68: //d
        this.dx = 0;
        break;
      case 83: //s
        this.dy = 0;
        break;
      case 65: //a
        this.dx = 0;
        break;
      case 87: //w
        this.dy = 0;
        break;
    }
  }

  onMouseMove(event) {
    this.mouseX = event.pageX - this.canvas.offsetLeft;
    this.mouseY = event.pageY - this.canvas.offsetTop;
  }

  onMouseClick(event) {
    console.log(event.pageX - this.canvas.offsetLeft, event.pageY - this.canvas.offsetTop);
    this.clientSocket.emit('channel', {
      clickX: event.pageX - this.canvas.offsetLeft,
      clickY: event.pageY - this.canvas.offsetTop
  });
  }

  drawCircle(ctx, x, y) {
      ctx.beginPath();
      ctx.arc(x, y, 10, 0, Math.PI*2);
      ctx.fillStyle = "#0095DD";
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

    this.drawCircle(ctx, this.circleX, this.circleY);

    this.circleX += this.dx;
    this.circleY += this.dy;

    window.requestAnimationFrame(() => this.draw(canvas, ctx));
  }

  render() {
    return (
      <div>
        <br/><br/>
        <h1>Dogfight.</h1>
        <br/>
        <canvas ref="canvas" width={1024} height={768}/>
      </div>
    );
  }
}

export default Game;
