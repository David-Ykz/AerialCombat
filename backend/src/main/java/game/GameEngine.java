package game;


import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.common.collect.ImmutableList;
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
    private final int upperXboundary = 4000;
    private final int lowerXboundary = -4000;
    private final int upperYboundary = -500;
    private final int lowerYboundary = 1000;
    private final double accelerationLimit = 1;
    private final double maxAngleChange = 5;

    public synchronized void addPlayer(Player player) {
        players.put(player.getId(), player);
    }

    public synchronized void removePlayer(int playerId) {
        projectiles.removeIf(projectile -> projectile.getPlayerID() == playerId);
        players.remove(playerId);
    }

    public synchronized void changePlayerVelocity(int id, double angle) {
        Player player = players.get(id);
        player.setTargetAngle(angle);
    }



    public synchronized void updatePlayerVelocity() {
        for (Player player : players.values()) {
            double currentAngle = player.getCurrentAngle();
            double targetAngle = player.getTargetAngle();
//            System.out.println("current angle: " + currentAngle + " target angle: " + targetAngle);
            if (Math.abs(targetAngle - currentAngle) <= maxAngleChange) {
                player.setCurrentAngle(targetAngle);
            } else if (targetAngle <= 180) {
                if (currentAngle > targetAngle && currentAngle < targetAngle + 180) {
                    player.setCurrentAngle(currentAngle - maxAngleChange);
                } else {
                    player.setCurrentAngle(currentAngle + maxAngleChange);
                }
            } else {
                if (currentAngle < targetAngle && currentAngle > targetAngle - 180) {
                    player.setCurrentAngle(currentAngle + maxAngleChange);
                } else {
                    player.setCurrentAngle(currentAngle - maxAngleChange);
                }
            }
            player.setCurrentAngle(player.getCurrentAngle() % 360);
            if (player.getCurrentAngle() < 0) {
                player.setCurrentAngle(360 + player.getCurrentAngle());
            }
            double targetXVelocity = player.getSpeed() * Math.cos(Math.toRadians(player.getCurrentAngle()));
            double targetYVelocity = player.getSpeed() * Math.sin(Math.toRadians(player.getCurrentAngle()));
//            if (player.getxPos() + player.getRadius() > upperXboundary || player.getxPos() - player.getRadius() < lowerXboundary
//                    || player.getyPos() + player.getRadius() > lowerYboundary || player.getyPos() - player.getRadius() < upperYboundary) {
//                targetXVelocity /= 2;
//                targetYVelocity /= 2;
//                player.takeDamage(1);
//            }
            player.setxVelocity(-targetXVelocity);
            player.setyVelocity(targetYVelocity);
        }
    }

    public synchronized void fireProjectile(int id) {
        Player player = players.get(id);
        Projectile projectile = player.fireBullet(player.getCurrentAngle());
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

    private void executeGameLoopIteration() {
        updatePlayerVelocity();
        updatePlayerPos();
        updateProjectilePos();
        checkProjectileCollisions();
        checkPlayerCollision();
        sendGameInfo();
    }

    private synchronized void updatePlayerPos() {
        for (Player player : players.values()) {
            player.setxPos(player.getxPos() - player.getxVelocity());
            player.setyPos(player.getyPos() - player.getyVelocity());
        }
    }

    private synchronized void updateProjectilePos() {
        ArrayList<Projectile> removeProjectiles = new ArrayList<>();
        for (Projectile projectile : projectiles) {
            if (projectile.updatePosition()) {
                removeProjectiles.add(projectile);
            }
        }
        projectiles.removeAll(removeProjectiles);
    }

    private synchronized void checkProjectileCollisions() {
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

    private synchronized void checkPlayerCollision() {
        ArrayList<Player> removePlayers = new ArrayList<>();
        for (Player player : players.values()) {
            for (Player otherPlayer : players.values()) {
                if (otherPlayer.getId() != player.getId() && player.checkCollision(otherPlayer)) {
                    if (player.takeDamage(25)) {
                        removePlayers.add(player);
                    }
                }
            }
        }
        for (Player player : removePlayers) {
            players.remove(player.getId());
        }
    }




    private void sendGameInfo() {
        JSONObject gameInfo = new JSONObject();
        ImmutableList<Player> copiedPlayers;
        synchronized (this) {
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
            gameInfo.put("upperXboundary", upperXboundary);
            gameInfo.put("lowerXboundary", lowerXboundary);
            gameInfo.put("upperYboundary", upperYboundary);
            gameInfo.put("lowerYboundary", lowerYboundary);

            copiedPlayers = players.values().stream().collect(toImmutableList());
        }

        for (Player player : copiedPlayers) {
            player.sendInfo(gameInfo.toString());
        }
    }
}
