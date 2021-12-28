package game;

public class RailgunWeapon extends Weapon {
    public RailgunWeapon(int reloadTime, String name) {
        super(reloadTime, name);
    }



    public Projectile shootProjectile(double xPos, double yPos, double angle, int playerID) {
        Railgun railgun = new Railgun(xPos, yPos, angle, playerID, xPos, yPos);
        return railgun;
    }

}
