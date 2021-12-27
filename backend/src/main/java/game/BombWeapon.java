package game;

public class BombWeapon extends Weapon {
    BombWeapon(int reloadTime, String name) {
        super(reloadTime, name);
    }

    public Projectile shootProjectile(double xPos, double yPos, double angle, int playerID) {
        Bomb projectile = new Bomb(xPos, yPos, 270, playerID);
        return projectile;
    }


}
