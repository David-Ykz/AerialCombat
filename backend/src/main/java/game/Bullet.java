package game;

public class Bullet extends Projectile {
    public Bullet(double xPos, double yPos, double xVelocity, double yVelocity, int playerID) {
        super(xPos, yPos, xVelocity, yVelocity, playerID);
        setRange(1000);
        setSpeed(10);
        setDamage(100);
    }

    public boolean checkCollision(Player player) {
        double playerX = player.getxPos();
        double playerY = player.getyPos();
        double playerRadius = player.getRadius();

        double slope = getyVelocity()/getxVelocity();
        double yIntercept = getyPos() - slope * getxPos();

        double a = slope * slope + 1;
        double b = 2 * (slope * (yIntercept - playerY) - playerX);
        double c = playerX * playerX + (yIntercept - playerY) * (yIntercept - playerY) - playerRadius * playerRadius;

        if (b * b - 4 * a * c >= 0) {
            return true;
        } else {
            return false;
        }
    }
}
