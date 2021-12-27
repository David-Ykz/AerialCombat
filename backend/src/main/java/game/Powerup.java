package game;

import org.json.JSONObject;

public class Powerup {
    private Weapon storedWeapon;
    private String name;
    private double xPos, yPos;
    private double radius;
    private double gravity;

    public Powerup(Weapon storedWeapon, double xPos, double yPos, String name) {
        this.storedWeapon = storedWeapon;
        this.xPos = xPos;
        this.yPos = yPos;
        this.name = name;
        this.gravity = 1;
        this.radius = 15;
    }

    public double getxPos() {
        return xPos;
    }

    public double getyPos() {
        return yPos;
    }

    public void setyPos(double yPos) {
        this.yPos = yPos;
    }

    public double getRadius() {
        return radius;
    }

    public double getGravity() {
        return gravity;
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



    public void acquirePowerup(Player player) {
        if (storedWeapon == null) {
            player.setHealth(100);
        } else {
            player.setWeapon(storedWeapon);
        }
    }


    public JSONObject toJSON() {
        JSONObject message = new JSONObject();
        message.put("name", name);
        message.put("xPos", xPos);
        message.put("yPos", yPos);
        message.put("radius", radius);
        return message;
    }


}
