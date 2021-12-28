package game;

public class Railgun extends Projectile {
    public Railgun(double xPos, double yPos, double angle, int playerID) {
        super(xPos, yPos, angle, playerID);
        setRange(10000);
        setSpeed(100);
        setDamage(100);
        setRadius(4);
        setName("railgun");
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
            return true;
        } else {
            return false;
        }
    }
}
