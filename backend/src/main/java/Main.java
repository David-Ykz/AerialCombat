import io.socket.engineio.server.EngineIoServer;
import io.socket.engineio.server.JettyWebSocketHandler;
import java.util.EnumSet;
import javax.servlet.DispatcherType;
import org.eclipse.jetty.http.pathmap.ServletPathSpec;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.websocket.server.WebSocketUpgradeFilter;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import servlets.EchoSocketServlet;

public class Main {

  public static void main(String[] args) throws Exception {
    Server server = new Server(1738);

    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    server.setHandler(context);

    EngineIoServer echoServer = new EngineIoServer();
    WebSocketUpgradeFilter webSocketUpgradeFilter = WebSocketUpgradeFilter.configureContext(
        context);
    webSocketUpgradeFilter.addMapping(new ServletPathSpec("/engine.io/echo/*"),
        (ServletUpgradeRequest servletUpgradeRequest, ServletUpgradeResponse servletUpgradeResponse) -> new JettyWebSocketHandler(
            echoServer));

    context.addServlet(new ServletHolder(new EchoSocketServlet(echoServer)),
        "/engine.io/echo/*");

//    FilterHolder cors = context.addFilter(CrossOriginFilter.class,"/*", EnumSet.of(DispatcherType.REQUEST));
//    cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
//    cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
//    cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST,HEAD,SERVICE");
//    cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");

    System.out.println("server start");

    server.start();
    server.join();
  }
}
