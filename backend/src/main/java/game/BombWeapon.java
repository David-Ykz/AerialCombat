package game;

public class BombWeapon extends Weapon {
    BombWeapon(int reloadTime, String name) {
        super(reloadTime, name);
    }

    public Projectile shootProjectile(double xPos, double yPos, double angle, int playerID) {
        double xVelocity = 0;
        double yVelocity = -1;
        Bomb projectile = new Bomb(xPos, yPos, xVelocity, yVelocity, playerID, 12);
        return projectile;
    }


}
