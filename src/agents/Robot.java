package agents;

import colony.Location;

public class Robot extends Agent {

    public static final int MAX_BATTERY = 100;
    public static final int MAX_HEALTH = 100;
    private static final int DRAIN_PER_TICK_ON_MISSION = 5;
    private static final int DRAIN_PER_TICK_IDLE = 1;
    private int battery = MAX_BATTERY;
    private int health = MAX_HEALTH;

    protected Robot(String id, Location location) {
        super(id, location);
    }


    public int getBatteryLevel() { 
        return battery; 
    }

    public boolean hasEnoughBattery(int required) { 
        return battery >= required;
    }

    public void drainBattery(int amount) {
        if (amount < 0) 
            throw new IllegalArgumentException("amount must be >= 0");
        battery = Math.max(0, battery - amount);
    }

    public void recharge(int amount) {
        if (amount < 0) 
            throw new IllegalArgumentException("amount must be >= 0");
        battery = Math.min(MAX_BATTERY, battery + amount);
    }

    
    public int getHealth() { return health; }

    public boolean isBrokenDown() { return health == 0; }

    public boolean isDamaged() { 
        return health < MAX_HEALTH; 
    }

    public void takeDamage(int amount) {
        if (amount < 0)
            throw new IllegalArgumentException("amount must be >= 0");
        health = Math.max(0, health - amount);
    }

    public void breakDown() { health = 0; }

    public void restoreHealth(int amount) {
        if (amount < 0) 
            throw new IllegalArgumentException("amount must be >= 0");
        health = Math.min(MAX_HEALTH, health + amount);
    }

    public void repairFully() { 
        health = MAX_HEALTH; 
    }

    public String getOperationalStatus() {
        if (isBrokenDown()) 
            return "brokenDown";
        if (battery == 0) 
            return "batteryEmpty";
        if (isDamaged()) 
            return "damaged";
        return "active";
    }

    
    @Override
    public boolean isUsable() {
        return health > 0 && battery > 0;
    }

    @Override
    public boolean tick() {
    if (!isUsable()) {
        return false;
    }
    drainBattery(isAssigned() ? DRAIN_PER_TICK_ON_MISSION : DRAIN_PER_TICK_IDLE);
    return true; 
    }

    @Override
    protected String details() {
        return "battery=" + battery + "%, health=" + health + "% (" + getOperationalStatus() + ")";
    }
}
