package game;

import org.json.JSONObject;

public class Railgun extends Projectile {
    private double startX;
    private double startY;
    public Railgun(double xPos, double yPos, double angle, int playerID, double startX, double startY) {
        super(xPos, yPos, angle, playerID);
        setRange(4000);
        setSpeed(200);
        setDamage(100);
        setRadius(4);
        setName("railgun");

        this.startX = startX;
        this.startY = startY;
    }

    public boolean checkCollision(Player player) {
        double playerX = player.getxPos();
        double playerY = player.getyPos();
        double playerRadius = player.getRadius();
        double a, b, c;
        if (getxVelocity() == 0) {
            double lineX = getxPos();
            a = 1;
            b = -2 * playerY;
            c = playerY * playerY + (lineX - playerX) * (lineX - playerX) - playerRadius * playerRadius;
        } else {
            double slope = getyVelocity()/getxVelocity();
            double yIntercept = getyPos() - slope * getxPos();
            a = slope * slope + 1;
            b = 2 * (slope * (yIntercept - playerY) - playerX);
            c = playerX * playerX + (yIntercept - playerY) * (yIntercept - playerY) - playerRadius * playerRadius;
        }

        if (b * b - 4 * a * c >= 0) {
            if (playerX >= startX && playerX <= getxPos() || playerX <= startX && playerX >= getxPos()) {
                return true;
            }
        return false;
    }

    public JSONObject toJSON() {
        JSONObject message = new JSONObject();
        message.put("id", getPlayerID());
        message.put("name", getName());
        message.put("xPos", getxPos());
        message.put("yPos", getyPos());
        message.put("radius", getRadius());
        message.put("angle", getAngle());
        message.put("startX", startX);
        message.put("startY", startY);
        return message;
    }



}
