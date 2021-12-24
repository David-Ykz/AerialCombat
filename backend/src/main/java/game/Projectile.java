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


    public Projectile(double xPos, double yPos, double xVelocity, double yVelocity, int playerID) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
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


    public JSONObject toJSON() {
        JSONObject message = new JSONObject();
        message.put("id", playerID);
        message.put("name", getClass().toString());
        message.put("xPos", xPos);
        message.put("yPos", yPos);
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
