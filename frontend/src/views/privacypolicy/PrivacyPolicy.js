import React from 'react';

class PrivacyPolicy extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div>
        <br/>
        <h1>Privacy Policy</h1>
        <br/>
        <h2>Information You Share with Us</h2>
        <p>The information we receive from you includes the following: name, images, and other data. This is not an exhaustive list and we receive other information as well. Assume all information we receive from you is public. By using the site, you allow us to share this information as we see fit.</p>
        <h2>Children and our Services</h2>
        <p>Our services are not directed to children, and you may not use our services if you are under the age of 13. You must also be old enough to consent to the processing of your personal data in your country (in some countries we may allow your parent or guardian to do so on your behalf).</p>
        <h2>Changes to our Privacy Policy</h2>
        <p>We may revise this Privacy Policy from time to time. The most current version of the policy will govern our processing of your personal data. It is your responsibility to stay informed of changes to the privacy policy, which we may update at any time. By continuing to access or use the Services after any changes become effective, you agree to be bound by the revised Privacy Policy.</p>
      </div>
    );
  }
}

export default PrivacyPolicy;
