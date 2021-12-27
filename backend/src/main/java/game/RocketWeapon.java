package game;

public class RocketWeapon extends Weapon {
    public RocketWeapon(int reloadTime, String name) {
        super(reloadTime, name);
    }

    public Projectile shootProjectile(double xPos, double yPos, double angle, int playerID) {
        Rocket rocket = new Rocket(xPos, yPos, angle, playerID);
        return rocket;
    }

}
