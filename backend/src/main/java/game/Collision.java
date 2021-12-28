package game;

import org.json.JSONObject;

public class Collision {
    private final double xPos;
    private final double yPos;


    Collision(double xPos, double yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }


    public JSONObject toJSON() {
        JSONObject message = new JSONObject();
        message.put("xPos", xPos);
        message.put("yPos", yPos);
        return message;

    }


}
