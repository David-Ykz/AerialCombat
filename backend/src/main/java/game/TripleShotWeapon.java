package game;

public class TripleShotWeapon extends Weapon {
    TripleShotWeapon(int reloadTime, String name) {
        super(reloadTime, name);
    }

    public Projectile shootProjectile(double xPos, double yPos, double angle, int playerID) {
            double xVelocity = Math.cos(Math.toRadians(angle));
            double yVelocity = Math.sin(Math.toRadians(angle));
            Bullet projectile = new Bullet(xPos, yPos, xVelocity, yVelocity, playerID, 5);
        return projectile;
    }
}
