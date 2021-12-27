package game;

public class Bomb extends Projectile {
    public Bomb(double xPos, double yPos, double xVelocity, double yVelocity, int playerID, int radius) {
        super(xPos, yPos, xVelocity, yVelocity, playerID, radius);
        setRange(1500);
        setSpeed(12);
        setDamage(100);
    }

    public boolean checkCollision(Player player) {
        double playerX = player.getxPos();
        double playerY = player.getyPos();
        double playerRadius = player.getRadius();

        if (playerX + playerRadius >= getxPos() - getRadius() && playerX  - playerRadius <= getxPos() + getRadius() &&
                playerY + playerRadius >= getyPos() && playerY - playerRadius <= getyPos() - getyVelocity() * getSpeed()) {
            return true;
        } else {
            return false;
        }
    }
}
