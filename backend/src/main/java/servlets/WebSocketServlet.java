package servlets;

import io.socket.engineio.server.EngineIoServer;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class WebSocketServlet extends HttpServlet {
    private final EngineIoServer engineIoServer;

    public WebSocketServlet(EngineIoServer engineIoServer) {
      this.engineIoServer = engineIoServer;
    }

    @Override
    protected void service(
        HttpServletRequest request, HttpServletResponse response) throws IOException {
      engineIoServer.handleRequest(request, response);
    }
}
