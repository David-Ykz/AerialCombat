package game;


import io.socket.engineio.parser.Packet;
import io.socket.engineio.server.EngineIoSocket;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;

public class GameEngine {
    private HashMap<Integer, Player> players = new HashMap<>();
    private ArrayList<Projectile> projectiles = new ArrayList<>();
    private final double acceleration = 0.5;

    public synchronized void addPlayer(Player player) {
        players.put(player.getId(), player);
    }

    public synchronized void removePlayer(int playerId) {
        for (Projectile projectile : projectiles) {
            if (projectile.getPlayerID() == playerId) {
                projectiles.remove(projectile);
            }
        }
        players.remove(playerId);
    }

    public synchronized void addProjectile(Projectile projectile) {
        projectiles.add(projectile);
    }


    public synchronized void updatePlayerVelocity(int id, double angle) {
        Player player = players.get(id);
        double targetXVelocity = player.getSpeed() * Math.cos(Math.toRadians(angle));
        double targetYVelocity = player.getSpeed() * Math.sin(Math.toRadians(angle));

        player.setxVelocity(-targetXVelocity);
        player.setyVelocity(targetYVelocity);

//        if (targetXVelocity - player.getxVelocity() <= acceleration) {
//            player.setxVelocity(targetXVelocity);
//        } else {
//            player.setxVelocity(acceleration * targetXVelocity / Math.abs(targetXVelocity));
//        }
//        if (targetYVelocity - player.getyVelocity() <= acceleration) {
//            player.setyVelocity(targetYVelocity);
//        } else {
//            player.setyVelocity(acceleration * targetYVelocity / Math.abs(targetYVelocity));
//        }
    }

    public synchronized void fireProjectile(int id, double angle) {
        Player player = players.get(id);
        Projectile projectile = player.fireBullet(angle);
        projectiles.add(projectile);
    }

    public void runGame() {
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                executeGameLoopIteration();
            }
        }, 0, 20);
    }

    private synchronized void executeGameLoopIteration() {
        updatePlayerPos();
        updateProjectilePos();
        checkProjectileCollisions();
        sendGameInfo();
    }

    public synchronized void updatePlayerPos() {
        for (Player player : players.values()) {
            player.setxPos(player.getxPos() - player.getxVelocity());
            player.setyPos(player.getyPos() - player.getyVelocity());
        }
    }

    public synchronized void updateProjectilePos() {
        ArrayList<Projectile> removeProjectiles = new ArrayList<>();
        for (Projectile projectile : projectiles) {
            if (projectile.updatePosition()) {
                removeProjectiles.add(projectile);
            }
        }
        projectiles.removeAll(removeProjectiles);
    }

    public synchronized void checkProjectileCollisions() {
        ArrayList<Projectile> removeProjectiles = new ArrayList<>();
        ArrayList<Player> removePlayers = new ArrayList<>();
        for (Player player : players.values()) {
            for (Projectile projectile : projectiles) {
                if (projectile.getPlayerID() != player.getId() && projectile.checkCollision(player)) {
                    removeProjectiles.add(projectile);
                    if (player.takeDamage(projectile.getDamage())) {
                        removePlayers.add(player);
                    }
                }
            }
        }
        projectiles.removeAll(removeProjectiles);
        for (Player player : removePlayers) {
            players.remove(player.getId());
        }
    }



    public synchronized void sendGameInfo() {
        JSONObject gameInfo = new JSONObject();
        JSONArray playerInfo = new JSONArray();
        JSONArray projectileInfo = new JSONArray();
        for (Player player : players.values()) {
            playerInfo.put(player.toJSON());
        }
        gameInfo.put("players", playerInfo);
        for (Projectile projectile : projectiles) {
            projectileInfo.put(projectile.toJSON());
        }
        gameInfo.put("projectiles", projectileInfo);

        for (Player player : players.values()) {
            player.sendInfo(gameInfo.toString());
        }

    }
}
