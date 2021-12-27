package game;

public class Bullet extends Projectile {
    public Bullet(double xPos, double yPos, double angle, int playerID) {
        super(xPos, yPos, angle, playerID);
        setRange(1000);
        setSpeed(18);
        setDamage(20);
        setRadius(6);
        setName("bullet");
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
                playerX + playerRadius >= getxPos() && playerX  - playerRadius <= getxPos() + getxVelocity() * getSpeed() &&
                playerY + playerRadius >= getyPos() && playerY - playerRadius <= getyPos() - getyVelocity() * getSpeed()) {
            return true;
        } else {
            return false;
        }
    }
}
