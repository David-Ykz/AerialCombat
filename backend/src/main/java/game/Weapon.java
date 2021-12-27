package game;

public abstract class Weapon {
    private int reloadTime;
    private int currentReload = 0;
    private String name;

    Weapon(int reloadTime, String name) {
        this.reloadTime = reloadTime;
        this.name = name;
    }

    public boolean readyToFire() {
        currentReload++;
        if (currentReload >= reloadTime) {
            return true;
        } else {
            return false;
        }

    }

    public void setReload() {
        this.currentReload = 0;
    }



    abstract Projectile shootProjectile(double xPos, double yPos, double angle, int playerID);


    public String getName() {
        return this.name;
    }
}


