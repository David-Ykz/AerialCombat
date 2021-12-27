package game;

import com.google.common.collect.ImmutableList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class GameEngine {
    private final HashMap<Integer, Player> players = new HashMap<>();
    private final ArrayList<Projectile> projectiles = new ArrayList<>();
    private ArrayList<Powerup> powerups = new ArrayList<>();
    private final int upperXboundary = 2000;
    private final int lowerXboundary = -2000;
    private final int upperYboundary = -500;
    private final int lowerYboundary = 1000;
    private final double accelerationLimit = 1;
    private final double maxAngleChange = 5;
    private final Timer gameLoopTimer = new Timer();
    private TimerTask currentGameLoopTask;

    public synchronized void addPlayer(Player player) {
        if (players.isEmpty()) {
            startGame();
        }
        players.put(player.getId(), player);
    }

    public synchronized void removePlayer(int playerId) {
        removePlayer(playerId, false);
    }

    public synchronized void removePlayer(int playerId, boolean alsoRemovePlayerProjectiles) {
        if (alsoRemovePlayerProjectiles) {
            projectiles.removeIf(projectile -> projectile.getPlayerID() == playerId);
        }
        players.remove(playerId);
        if (players.isEmpty()) {
            endGame();
        }
    }

    public synchronized void changePlayerVelocity(int id, double angle) {
        Player player = players.get(id);
        player.setTargetAngle(angle);
    }

    public synchronized void updatePlayerVelocity() {
        ArrayList<Player> removePlayers = new ArrayList<>();
        for (Player player : players.values()) {
            double currentAngle = player.getCurrentAngle();
            double targetAngle = player.getTargetAngle();
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
            if (player.getxPos() + player.getRadius() > upperXboundary || player.getxPos() - player.getRadius() < lowerXboundary
                    || player.getyPos() + player.getRadius() > lowerYboundary || player.getyPos() - player.getRadius() < upperYboundary) {
                targetXVelocity /= 2;
                targetYVelocity /= 2;
                if (player.takeDamage(2)) {
                    removePlayers.add(player);
                }
            }
            player.setxVelocity(-targetXVelocity);
            player.setyVelocity(targetYVelocity);
        }
        for (Player player : removePlayers) {
            player.sendGameOver();
            removePlayer(player.getId());
        }
    }

    public synchronized void fireProjectile(int id) {
        Player player = players.get(id);
        if (player.getWeapon().readyToFire()) {
            if (player.getWeapon().getName().equals("tripleshotweapon")) {
                for (int i = 1; i <= 3; i++) {
                    Projectile projectile = player.fireBullet(player.getCurrentAngle() + i * 15 - 30);
                    projectiles.add(projectile);
                }
            } else {
                Projectile projectile = player.fireBullet(player.getCurrentAngle());
                projectiles.add(projectile);
            }
            player.getWeapon().setReload();
        }
    }

    private synchronized void startGame() {
        System.out.println("Starting game.");
        currentGameLoopTask = new TimerTask() {
            @Override
            public void run() {
                executeGameLoopIteration();
            }
        };
        gameLoopTimer.schedule(currentGameLoopTask, 0, 15);
    }

    private synchronized void endGame() {
        System.out.println("Ending game.");
        currentGameLoopTask.cancel();
    }

    private void executeGameLoopIteration() {
        updatePlayerVelocity();
        updatePlayerPos();
        updateProjectilePos();
        updatePowerupPos();
        checkProjectileCollisions();
        checkPlayerCollision();
        checkPowerupCollision();
        if (Math.random() > 0.98) {
            createPowerup();
        }
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

    private synchronized void updatePowerupPos() {
        ArrayList<Powerup> removePowerups = new ArrayList<>();
        for (Powerup powerup : powerups) {
            powerup.setyPos(powerup.getyPos() + powerup.getGravity());
            if (powerup.getyPos() >= lowerYboundary) {
                removePowerups.add(powerup);
            }
        }
        powerups.removeAll(removePowerups);
    }

    private synchronized void createPowerup() {
        int powerupChoice = (int)(Math.random() * 6);
        double randomXPos = Math.random() * 2 * upperXboundary + lowerXboundary;
        Powerup powerup;

        if (powerupChoice == 1) {
            powerup = new Powerup(new RocketWeapon(70, "rocketweapon"), randomXPos, upperYboundary, "rocket");
        } else if (powerupChoice == 2) {
            powerup = new Powerup(new BombWeapon(40, "bombweapon"), randomXPos, upperYboundary, "bomb");
        } else if (powerupChoice == 3) {
            powerup = new Powerup(new TripleShotWeapon(20, "tripleshotweapon"), randomXPos, upperYboundary, "tripleshot");
        } else {
            powerup = new Powerup(null, randomXPos, upperYboundary, "medkit");
        }
        powerups.add(powerup);
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
            projectiles.removeAll(removeProjectiles);
        }
        for (Player player : removePlayers) {
            player.sendGameOver();
            removePlayer(player.getId());
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
            player.sendGameOver();
            removePlayer(player.getId());
        }
    }

    public synchronized void checkPowerupCollision() {
        ArrayList<Powerup> removePowerups = new ArrayList<>();
        for (Player player : players.values()) {
            for (Powerup powerup : powerups) {
                if (powerup.checkCollision(player)) {
                    powerup.acquirePowerup(player);
                    removePowerups.add(powerup);
                }
            }
            powerups.removeAll(removePowerups);
        }
    }




    private void sendGameInfo() {
        JSONObject gameInfo = new JSONObject();
        gameInfo.put("type", "gameInfo");
        ImmutableList<Player> copiedPlayers;
        synchronized (this) {
            JSONArray playerInfo = new JSONArray();
            JSONArray projectileInfo = new JSONArray();
            JSONArray powerupInfo = new JSONArray();
            for (Player player : players.values()) {
                playerInfo.put(player.toJSON());
            }
            gameInfo.put("players", playerInfo);
            for (Projectile projectile : projectiles) {
                projectileInfo.put(projectile.toJSON());
            }
            gameInfo.put("projectiles", projectileInfo);
            for (Powerup powerup : powerups) {
                powerupInfo.put(powerup.toJSON());
            }
            gameInfo.put("powerups", powerupInfo);

            gameInfo.put("upperXboundary", upperXboundary);
            gameInfo.put("lowerXboundary", lowerXboundary);
            gameInfo.put("upperYboundary", upperYboundary);
            gameInfo.put("lowerYboundary", lowerYboundary);

            copiedPlayers = ImmutableList.copyOf(players.values());
        }

        for (Player player : copiedPlayers) {
            player.sendInfo(gameInfo.toString());
        }
    }
}
