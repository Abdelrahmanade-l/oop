package agents;

import colony.Location;

public abstract class CrewMember extends Agent {

    private final String name;
    private int health = MAX_HEALTH;
    private int fatigue = 0;
    private String skills = "";
    public static final int MAX_HEALTH = 100;
    public static final int MAX_FATIGUE = 100;
    public static final int MIN_USABLE_HEALTH = 40;
    private static final int FATIGUE_PER_TICK_ON_MISSION = 5;
    private static final int RECOVERY_PER_TICK_RESTING = 5; 



    protected CrewMember(String id, String name, Location location, String... skillsArray) {
    super(id, location);
    
    if (name == null || name.trim().isEmpty()) {
        throw new IllegalArgumentException("name must not be null or empty");
    }
    this.name = name;
    
    for (String s : skillsArray) {
        if (s != null && !s.trim().isEmpty()) {
            addSkill(s);
        }
    }
}

    public String getName() { 
        return name; 
    }

    @Override
    protected String label() { 
        return getId() + " '" + name + "'"; 
    }

    public int getHealth() { 
        return health; 
    }

    public String getHealthStatus() {
        if (health >= 70) 
            return "healthy";
        if (health >= MIN_USABLE_HEALTH) 
            return "injured";
        if (health > 0) 
            return "inDANGER";
        return "disabled";
    }

    public void injure(int amount) {
        if (amount < 0)
            throw new IllegalArgumentException("amount must be >= 0");
        health = Math.max(0, health - amount);
    }

    public void heal(int amount) {
        if (amount < 0) 
            throw new IllegalArgumentException("amount must be >= 0");
        health = Math.min(MAX_HEALTH, health + amount);
    }

    public int getFatigue() { 
        return fatigue;
    }

    public void increaseFatigue(int amount) {
        if (amount < 0)
            throw new IllegalArgumentException("amount must be >= 0");
        fatigue = Math.min(MAX_FATIGUE, fatigue + amount);
    }

    public void rest(int amount) {
        if (amount < 0) 
            throw new IllegalArgumentException("amount must be >= 0");
        fatigue = Math.max(0, fatigue - amount);
    }

    
    public String getSkills() { 
        return skills; 
    }

    public boolean hasSkill(String skill) {
        if (skill == null || skills.isEmpty())
            return false;
        String[] skillList = skills.split(",\\s*");
        for (String s : skillList) {
            if (s.equalsIgnoreCase(skill.trim())) 
                return true;
        }
        return false;
    }

    public void addSkill(String skill) {
        if (skill != null && !skill.trim().isEmpty()) {
            String trimmed = skill.trim();
            if (!hasSkill(trimmed)) {
                if (skills.isEmpty()) {
                    skills = trimmed;
                } 
                else {
                    skills += ", " + trimmed;
                }
            }
        }
    }
    @Override
    public boolean isUsable() {
        return health >= MIN_USABLE_HEALTH && fatigue < MAX_FATIGUE;
    }

    @Override
    public boolean tick() {
        if (!isUsable()) {
            return false; 
    }
        if (isAssigned()) {
            increaseFatigue(FATIGUE_PER_TICK_ON_MISSION);
    } else {
        rest(RECOVERY_PER_TICK_RESTING);
    }
    return true;
}

    @Override
    protected String details() {
        return "health=" + health + " (" + getHealthStatus() + "), fatigue=" + fatigue
                + (skills.isEmpty() ? "" : ", skills=[" + skills + "]");
    }


}
