package game;

public class Bullet extends Projectile {
    public Bullet(double xPos, double yPos, double xVelocity, double yVelocity, int playerID) {
        super(xPos, yPos, xVelocity, yVelocity, playerID);
        setRange(1000);
        setSpeed(18);
        setDamage(20);
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

        if (b * b - 4 * a * c >= 0 &&
                playerX >= getxPos() && playerX <= (getxPos() + getxVelocity() * getSpeed()) &&
                playerY >= getyPos() && playerY <= (getyPos() - getyVelocity() * getSpeed())) {
            return true;
        } else {
            return false;
        }
    }
}
