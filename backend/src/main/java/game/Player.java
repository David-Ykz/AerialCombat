package game;

import io.socket.engineio.parser.Packet;
import io.socket.engineio.server.EngineIoSocket;
import org.json.JSONObject;

public class Player {
    private final int id;
    private final String name;
    private int speed = 8;
    private int health = 100;
    private EngineIoSocket socket;
    private double xPos;
    private double yPos;
    private double xVelocity;
    private double yVelocity;
    private Weapon weapon;
    private int radius = 20;
    private double currentAngle;
    private double targetAngle;


    public Player(int id, String name, EngineIoSocket socket) {
        this.id = id;
        this.name = name;
        this.socket = socket;
        this.weapon = new BasicWeapon(15, "basicweapon");
        this.currentAngle = 0;
    }

    public int getId() { return id; }
    public String getName() {
        return name;
    }
    public int getSpeed() { return speed; }
    public int getHealth() {
        return health;
    }
    public boolean takeDamage(int health) {
        this.health -= health;
        if (this.health <= 0) {
            return true;
        } else {
            return false;
        }
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public double getxPos() {
        return xPos;
    }
    public double getyPos() {
        return yPos;
    }

    public void setxPos(double xPos) {
        this.xPos = xPos;
    }

    public void setyPos(double yPos) {
        this.yPos = yPos;
    }

    public Projectile fireBullet(double angle) {
        return weapon.shootProjectile(xPos, yPos, angle, id);
    }

    public double getCurrentAngle() {
        return currentAngle;
    }
    public void setCurrentAngle(double currentAngle) {
        this.currentAngle = currentAngle;
    }
    public double getTargetAngle() {
        return targetAngle;
    }
    public void setTargetAngle(double targetAngle) {
        this.targetAngle = targetAngle;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public double getxVelocity() {
        return xVelocity;
    }
    public double getyVelocity() {
        return yVelocity;
    }
    public void setxVelocity(double xVelocity) {
        this.xVelocity = xVelocity;
    }
    public void setyVelocity(double yVelocity) {
        this.yVelocity = yVelocity;
    }
    public int getRadius() { return radius; }


    public JSONObject toJSON() {
        JSONObject message = new JSONObject();
        message.put("id", id);
        message.put("name", name);
        message.put("xPos", xPos);
        message.put("yPos", yPos);
        message.put("health", health);
        message.put("angle", currentAngle);
        message.put("radius", radius);
        message.put("weapon", weapon.getName());

        return message;
    }

    public boolean checkCollision(Player player) {
        double playerX = player.getxPos();
        double playerY = player.getyPos();
        double playerRadius = player.getRadius();

        double distance = Math.sqrt((xPos - playerX) * (xPos - playerX) + (yPos - playerY) * (yPos - playerY));

        if (distance <= playerRadius + radius) {
            return true;
        } else {
            return false;
        }
    }

    public void sendGameOver() {
        JSONObject message = new JSONObject();
        message.put("type", "gameOver");
        message.put("id", id);
        sendInfo(message.toString());
    }


    public void sendInfo(String message) {
        socket.send(new Packet<>(Packet.MESSAGE, message));
    }

}



