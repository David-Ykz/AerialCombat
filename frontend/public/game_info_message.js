
function createGameInfoMessage(messageStr, duration) {
  return {
    message: messageStr,
    duration: duration,
    startTime: Math.floor(Date.now())
  };
}

function isGameInfoMessageExpired(message) {
  return Math.floor(Date.now()) > message.startTime + message.duration;
}
