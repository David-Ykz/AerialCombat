package game;

public abstract class Weapon {
    private int reloadTime;

    Weapon(int reloadTime) {
        this.reloadTime = reloadTime;
    }

    abstract Projectile shootProjectile(double xPos, double yPos, double angle, int playerID);


}


