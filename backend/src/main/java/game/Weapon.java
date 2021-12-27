package game;

public abstract class Weapon {
    private int reloadTime;
    private int currentReload;
    private String name;

    Weapon(int reloadTime, String name) {
        this.reloadTime = reloadTime;
        this.currentReload = reloadTime;
        this.name = name;
    }

    public boolean readyToFire() {
        if (currentReload >= reloadTime) {
            return true;
        } else {
            return false;
        }
    }

    public void increaseCurrentReoad() {
        currentReload++;
    }

    public void setReload() {
        this.currentReload = 0;
    }



    abstract Projectile shootProjectile(double xPos, double yPos, double angle, int playerID);


    public String getName() {
        return this.name;
    }
}


