package game;

import org.json.JSONObject;

public abstract class Projectile {
    private double xPos;
    private double yPos;
    private double xVelocity;
    private double yVelocity;
    private double range;
    private int speed;
    private int damage;
    private final int playerID;
    private int radius;


    public Projectile(double xPos, double yPos, double xVelocity, double yVelocity, int playerID, int radius) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        this.playerID = playerID;
        this.radius = radius;
    }

    public double getxPos() {
        return xPos;
    }
    public double getyPos() {
        return yPos;
    }
    public double getxVelocity() {
        return xVelocity;
    }
    public double getyVelocity() {
        return yVelocity;
    }
    public double getRange() {
        return range;
    }
    public int getSpeed() {
        return speed;
    }
    public int getDamage() {
        return damage;
    }
    public int getPlayerID() { return playerID; }
    public void setRange(double range) { this.range = range; }
    public void setSpeed(int speed) { this.speed = speed; }
    public void setDamage(int damage) { this.damage = damage; }
    public int getRadius() {
        return this.radius;
    }

    public JSONObject toJSON() {
        JSONObject message = new JSONObject();
        message.put("id", playerID);
        message.put("name", getClass().toString());
        message.put("xPos", xPos);
        message.put("yPos", yPos);
        message.put("radius", radius);
        return message;
    }

    public boolean updatePosition() {
        xPos += xVelocity * speed;
        yPos -= yVelocity * speed;
        range -= speed;
        if (range <= 0) {
            return true;
        } else {
            return false;
        }
    }

    abstract public boolean checkCollision(Player player);


}
