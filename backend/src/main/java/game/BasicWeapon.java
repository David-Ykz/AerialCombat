package game;

public class BasicWeapon extends Weapon {
    BasicWeapon(int reloadTime, String name) {
        super(reloadTime, name);
    }

    public Projectile shootProjectile(double xPos, double yPos, double angle, int playerID) {
        Bullet projectile = new Bullet(xPos, yPos, angle, playerID);
        return projectile;
    }
}
