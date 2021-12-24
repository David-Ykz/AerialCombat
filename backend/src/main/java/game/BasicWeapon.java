package game;

public class BasicWeapon extends Weapon {
    BasicWeapon(int reloadTime) {
        super(reloadTime);
    }

    public Projectile shootProjectile(double xPos, double yPos, double angle, int playerID) {
        double xVelocity = Math.cos(Math.toRadians(angle));
        double yVelocity = Math.sin(Math.toRadians(angle));
        Bullet projectile = new Bullet(xPos, yPos, xVelocity, yVelocity, playerID);
        return projectile;
    }
}
