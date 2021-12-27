package game;

public class TripleShotWeapon extends Weapon {
    TripleShotWeapon(int reloadTime, String name) {
        super(reloadTime, name);
    }

    public Projectile shootProjectile(double xPos, double yPos, double angle, int playerID) {
            Bullet projectile = new Bullet(xPos, yPos, angle, playerID);
        return projectile;
    }
}
