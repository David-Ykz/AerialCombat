package game;

public class BombWeapon extends Weapon {
    BombWeapon(int reloadTime) {
        super(reloadTime);
    }

    public Projectile shootProjectile(double xPos, double yPos, double angle, int playerID) {
        double xVelocity = 0;
        double yVelocity = 1;
        Bomb projectile = new Bomb(xPos, yPos, xVelocity, yVelocity, playerID);
        return projectile;
    }


}
