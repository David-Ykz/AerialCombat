package game;

public class Bomb extends Projectile {
    private int radius = 5;
    public Bomb(double xPos, double yPos, double xVelocity, double yVelocity, int playerID) {
        super(xPos, yPos, xVelocity, yVelocity, playerID);
        setRange(1500);
        setSpeed(12);
        setDamage(100);
    }

    public boolean checkCollision(Player player) {
        double playerX = player.getxPos();
        double playerY = player.getyPos();
        double playerRadius = player.getRadius();

        if (playerX + playerRadius >= getxPos() - radius && playerX  - playerRadius <= getxPos() + radius &&
                playerY + playerRadius >= getyPos() && playerY - playerRadius <= getyPos() - getyVelocity() * getSpeed()) {
            return true;
        } else {
            return false;
        }
    }
}
