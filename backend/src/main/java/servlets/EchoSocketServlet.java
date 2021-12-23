package servlets;

import io.socket.engineio.parser.Packet;
import io.socket.engineio.server.EngineIoServer;
import io.socket.engineio.server.EngineIoSocket;

public final class EchoSocketServlet extends WebSocketServlet {
  public EchoSocketServlet(EngineIoServer engineIoServer) {
    super(engineIoServer);

    engineIoServer.on("connection", (Object... args) -> {
      onConnect((EngineIoSocket) args[0]);
    });
  }

  private void onConnect(EngineIoSocket socket) {
    socket.on("message", (Object... args) -> {
      String rawMessage = (String) args[0];
      onMessage(socket, rawMessage);
    });
  }

  private void onMessage(EngineIoSocket socket, String rawMessage) {
    socket.send(new Packet<>(Packet.MESSAGE, "you sent - " + rawMessage));
  }
}
