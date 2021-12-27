package servlets;

import game.GameEngine;
import game.Player;
import io.socket.engineio.parser.Packet;
import io.socket.engineio.server.EngineIoServer;
import io.socket.engineio.server.EngineIoSocket;
import org.json.JSONObject;

public final class GameSocketServlet extends WebSocketServlet {
    private GameEngine game;
    public GameSocketServlet(EngineIoServer engineIoServer, GameEngine game) {
        super(engineIoServer);
        this.game = game;
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
        JSONObject message = new JSONObject(rawMessage);
        if (message.getString("type").equals("playerjoin")) {
            onPlayerJoin(message.getInt("id"), message.getString("name"), socket);
        } else if (message.getString("type").equals("updateplayer")) {
            game.changePlayerVelocity(message.getInt("id"), message.getDouble("angle"));
        } else if (message.getString("type").equals("shootbullet")){
            game.fireProjectile(message.getInt("id"));
        } else {
            throw new RuntimeException("Invalid message type " + message.getString("type"));
        }
    }

    private void onPlayerJoin(int id, String name, EngineIoSocket socket) {
        socket.on("close", (Object... args) -> {
            System.out.println("disconnected" + id);
            game.removePlayer(id);
        });
        Player player = new Player(id, name, socket);
        System.out.println("adding player");
        game.addPlayer(player);
    }
}
