package game;

public class Rocket extends Projectile {

    public Rocket(double xPos, double yPos, double xVelocity, double yVelocity, int playerID) {
        super(xPos, yPos, xVelocity, yVelocity, playerID);
        setRange(1000);
        setSpeed(10);
        setDamage(100);
    }




}
