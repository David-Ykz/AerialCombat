package game;

import org.json.JSONObject;

public class Railgun extends Projectile {
    private double startX;
    private double startY;
    public Railgun(double xPos, double yPos, double angle, int playerID, double startX, double startY) {
        super(xPos, yPos, angle, playerID);
        setRange(4000);
        setSpeed(400);
        setDamage(100);
        setRadius(4);
        setName("railgun");

        this.startX = startX;
        this.startY = startY;
    }

    public boolean checkCollision(Player player) {

        if (checkLineCollision(player)) {
            if (player.getxPos() >= startX && player.getxPos() <= getxPos() || player.getxPos() <= startX && player.getxPos() >= getxPos()) {
                return true;
            }
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
