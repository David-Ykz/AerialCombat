package game;

public class Bullet extends Projectile {
    public Bullet(double xPos, double yPos, double angle, int playerID) {
        super(xPos, yPos, angle, playerID);
        setRange(1200);
        setSpeed(18);
        setDamage(10);
        setRadius(8);
        setName("bullet");
    }

    public boolean checkCollision(Player player) {
        if (checkLineCollision(player)) {
            if (player.getxPos() >= getxPos() + getxVelocity() * getSpeed() && player.getxPos() <= getxPos() ||
                    player.getxPos() <= getxPos() + getxVelocity() && player.getxPos() >= getxPos()) {
                return true;
            }
        }
        return false;
    }
}
