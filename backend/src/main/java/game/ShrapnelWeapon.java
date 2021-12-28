package game;

public class ShrapnelWeapon extends Weapon {
    public ShrapnelWeapon(int reloadTime, String name) {
        super(reloadTime, name);
    }

    public Projectile shootProjectile(double xPos, double yPos, double angle, int playerID) {
        Shrapnel shrapnel = new Shrapnel(xPos, yPos, angle, playerID);
        return shrapnel;
    }

}
