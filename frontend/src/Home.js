import openSocket from 'socket.io-client';

import React from 'react';
import Button from 'react-bootstrap/Button'
import Form from "react-bootstrap/Form";

class Home extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      playerName: 'Player'
    };

    this.onNameChange = this.onNameChange.bind(this);
    this.detectEnterSubmit = this.detectEnterSubmit.bind(this);
    this.playGame = this.playGame.bind(this);
  }

  componentDidMount() {
  }

  onNameChange(event) {
    this.setState({ playerName: event.target.value });
  }

  detectEnterSubmit(event) {
    if (event.keyCode === 13) {
      event.preventDefault();
      this.playGame();
    }
  }

  playGame() {
    window.location.href = '/game?playerName=' + this.state.playerName;
  }

  render() {
    return (
      <div>
        <br/><br/>
        <h1>Aerial Combat</h1>
        <Form>
          <Form.Group>
            <Form.Label>Your Name:</Form.Label>
            <Form.Control type="text" onChange={this.onNameChange} onKeyDown={this.detectEnterSubmit} value={this.state.playerName} />
          </Form.Group>
        </Form>
        <br/>
        <Button onClick={this.playGame}>Play!</Button>
      </div>
    );
  }
}

export default Home;
