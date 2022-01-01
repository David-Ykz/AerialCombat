package game;

public class Rocket extends Projectile {

    public Rocket(double xPos, double yPos, double angle, int playerID) {
        super(xPos, yPos, angle, playerID);
        setRange(3400);
        setSpeed(30);
        setDamage(100);
        setRadius(20);
        setName("rocket");
    }

    public boolean checkCollision(Player player) {
        if (checkLineCollision(player)) {
            if (player.getxPos() >= getxPos() + getxVelocity() * getSpeed() && player.getxPos() <= getxPos() ||
                    player.getxPos() <= getxPos() + getxVelocity() * getSpeed() && player.getxPos() >= getxPos()) {
                return true;
            }
        }
        return false;
    }
}
