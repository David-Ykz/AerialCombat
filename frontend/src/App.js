import {
  BrowserRouter as Router,
  Switch,
  Route
} from "react-router-dom";
import Container from 'react-bootstrap/Container';
import {FixedBottom} from 'react-fixed-bottom';

import Game from './Game.js';
import Home from './Home.js';
import PrivacyPolicy from './views/privacypolicy/PrivacyPolicy.js';
import TermsOfService from './views/termsofservice/TermsOfService.js';

import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';

function App() {
  const footerStyle = {
    backgroundColor: "#F8F8F8",
    borderTop: "1px solid #E7E7E7",
    textAlign: "center",
    padding: "20px",
    width: "100%",
  }
  return (
    <div style={{height: '100vh'}}>
    <Router>
      <Container style={{height: '100vh'}}>
        <div id="app" style={{height:'100%'}}>
          <Route path="/privacy_policy">
            <PrivacyPolicy />
          </Route>
          <Route path="/terms_of_service">
            <TermsOfService />
          </Route>
          <Switch>
           <Route path="/game" component={Game} exact={true} />
          </Switch>
          <Switch>
           <Route path="/" exact component={Home} />
          </Switch>
        </div>
      </Container>
    </Router>
    <FixedBottom offset={0}>
      <div style={footerStyle}>
        By using this website, you agree to our <a href='/privacy_policy'>Privacy Policy</a> and <a href='/terms_of_service'>Terms of Service</a>. Learn more about us on our <a href='/'>Homepage</a>!
      </div>
    </FixedBottom>
    </div>
  );
}

export default App;
