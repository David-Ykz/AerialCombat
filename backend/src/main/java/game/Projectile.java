package game;

import org.json.JSONObject;

public abstract class Projectile {
    private double xPos;
    private double yPos;
    private double angle;
    private double xVelocity;
    private double yVelocity;
    private double range;
    private int speed;
    private int damage;
    private final int playerID;
    private double radius;
    private String name;


    public Projectile(double xPos, double yPos, double angle, int playerID) {
        this.xVelocity = Math.cos(Math.toRadians(angle));
        this.yVelocity = Math.sin(Math.toRadians(angle));
        this.angle = angle;
        this.xPos = xPos;
        this.yPos = yPos;
        this.playerID = playerID;
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
    public double getAngle() {
        return this.angle;
    }
    public double getRadius() {
        return this.radius;
    }
    public void setRadius(double radius) {
        this.radius = radius;
    }

    public String getName() { return this.name; }

    public void setName(String name) {
        this.name = name;
    }

    public JSONObject toJSON() {
        JSONObject message = new JSONObject();
        message.put("id", playerID);
        message.put("name", name);
        message.put("xPos", xPos);
        message.put("yPos", yPos);
        message.put("radius", radius);
        message.put("angle", angle);
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
