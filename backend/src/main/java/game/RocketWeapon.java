package game;

public class RocketWeapon extends Weapon {
    public RocketWeapon(int reloadTime) {
        super(reloadTime);
    }

    public Projectile shootProjectile(double xPos, double yPos, double angle, int playerID) {
        double xVelocity = Math.cos(Math.toRadians(angle));
        double yVelocity = Math.sin(Math.toRadians(angle));
        Rocket rocket = new Rocket(xPos, yPos, xVelocity, yVelocity, playerID);
        return rocket;
    }

}
