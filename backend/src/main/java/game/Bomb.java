package game;

public class Bomb extends Projectile {
    public Bomb(double xPos, double yPos, double angle, int playerID) {
        super(xPos, yPos, angle, playerID);
        setRange(1500);
        setSpeed(9);
        setDamage(100);
        setRadius(12);
        setName("bomb");
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
