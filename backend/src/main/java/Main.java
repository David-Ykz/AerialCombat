import game.GameEngine;
import io.socket.engineio.server.EngineIoServer;
import io.socket.engineio.server.JettyWebSocketHandler;
import java.util.EnumSet;
import javax.servlet.DispatcherType;
import javax.servlet.Servlet;
import org.eclipse.jetty.http.pathmap.ServletPathSpec;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.server.WebSocketUpgradeFilter;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import servlets.EchoSocketServlet;
import servlets.GameSocketServlet;

public class Main {

  public static void main(String[] args) throws Exception {
    GameEngine gameEngine = new GameEngine();
    Server server = new Server(1700);

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

    EngineIoServer gameSocket = new EngineIoServer();
    webSocketUpgradeFilter.addMapping(new ServletPathSpec("/engine.io/game/*"),
            (ServletUpgradeRequest servletUpgradeRequest, ServletUpgradeResponse servletUpgradeResponse) -> new JettyWebSocketHandler(
                    gameSocket));

    context.addServlet(new ServletHolder(new GameSocketServlet(gameSocket, gameEngine)),
            "/engine.io/game/*");

    // HTTPS configuration
    HttpConfiguration https = new HttpConfiguration();
    https.addCustomizer(new SecureRequestCustomizer());

    // Configuring SSL
    SslContextFactory.Server sslServer = new SslContextFactory.Server();

    // Defining keystore path and passwords
    sslServer.setKeyStorePath("/home/ec2-user/cert.pkcs12");
    sslServer.setKeyStorePassword("longGOOGshortVIX");
    sslServer.setKeyManagerPassword("longGOOGshortVIX");

    // Configuring the connector
    ServerConnector sslConnector = new ServerConnector(server, new SslConnectionFactory(sslServer, "http/1.1"), new HttpConnectionFactory(https));
    sslConnector.setPort(1700);

    // Setting HTTP and HTTPS connectors
    server.setConnectors(new Connector[]{sslConnector});

    context.setErrorHandler(new ErrorHandler());

//    FilterHolder cors = context.addFilter(CrossOriginFilter.class,"/*", EnumSet.of(DispatcherType.REQUEST));
//    cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
//    cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
//    cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST,HEAD,SERVICE");
//    cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");

    gameEngine.runGame();
    System.out.println("server start");

    server.start();
    server.join();
  }
}
