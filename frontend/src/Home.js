import * as constants from './Constants.js';
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
    this.playGame = this.playGame.bind(this);
  }

  componentDidMount() {
  }

  onNameChange(event) {
    this.setState({ playerName: event.target.value });
  }

  playGame() {
    window.location.href = constants.GAME_WEB_ADDRESS + '?playerName=' + this.state.playerName;
  }

  render() {
    return (
      <div>
        <br/><br/>
        <h1>Dogfight</h1>
        <Form>
          <Form.Group>
            <Form.Label>Your Name:</Form.Label>
            <Form.Control type="text" onChange={this.onNameChange} value={this.state.playerName} />
          </Form.Group>
        </Form>
        <br/>
        <Button onClick={this.playGame}>Play!</Button>
      </div>
    );
  }
}

export default Home;
