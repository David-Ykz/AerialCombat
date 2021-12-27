package game;

public class Powerup {
    private Weapon storedWeapon;
    private double xPos, yPos;
    private double radius;
    private double gravity;

    public Powerup(Weapon storedWeapon, double xPos, double yPos) {
        this.storedWeapon = storedWeapon;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public double getxPos() {
        return xPos;
    }

    public double getyPos() {
        return yPos;
    }

    public void setyPos(double yPos) {
        this.yPos = yPos;
    }

    public double getRadius() {
        return radius;
    }

    public double getGravity() {
        return gravity;
    }




}
