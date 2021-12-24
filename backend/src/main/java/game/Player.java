package game;

import io.socket.engineio.parser.Packet;
import io.socket.engineio.server.EngineIoSocket;
import org.json.JSONObject;

public class Player {
    private final int id;
    private final String name;
    private int speed = 6;
    private int health;
    private EngineIoSocket socket;
    private double xPos;
    private double yPos;
    private double xVelocity;
    private double yVelocity;
    private Weapon weapon;
    private int radius = 10;

    public Player(int id, String name, EngineIoSocket socket) {
        this.id = id;
        this.name = name;
        this.socket = socket;
        this.weapon = new BasicWeapon(5);
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

        return message;
    }

    public void sendInfo(String message) {
        socket.send(new Packet<>(Packet.MESSAGE, message));
    }

}



