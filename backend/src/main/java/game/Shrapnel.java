package game;

public class Shrapnel extends Projectile {
    public Shrapnel(double xPos, double yPos, double angle, int playerID) {
        super(xPos, yPos, angle, playerID);
        setRange(900);
        setSpeed(8);
        setDamage(100);
        setRadius(15);
        setName("shrapnel");
    }

    public boolean checkCollision(Player player) {
        double playerX = player.getxPos();
        double playerY = player.getyPos();
        double playerRadius = player.getRadius();
        if (playerX + playerRadius >= getxPos() - getRadius() && playerX - playerRadius <= getxPos() + getRadius() &&
                playerY + playerRadius >= getyPos() && playerY - playerRadius <= getyPos() - getyVelocity() * getSpeed()) {
            return true;
        } else {
            return false;
        }
    }
}
