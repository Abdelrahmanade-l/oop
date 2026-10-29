package agents;

import colony.Location;
import emergencies.Emergency;
import missions.Mission;

public class Scientist extends CrewMember {

    public Scientist(String id, String name, Location location, String... skills) {
        super(id, name, location, skills);
    }

    @Override
    public boolean canPerform(Mission mission) {
        return missionIs(mission, "exploration", "research");
    }

    @Override
    public boolean canHandle(Emergency emergency) {
        return false;
    }

    public void performResearch(Mission mission) {
        System.out.println("Scientist " + getName() + " is performing research" + (mission == null ? "." : " for mission " + mission.getId() + "."));
    }
    public boolean performDuty() {
    if (!isUsable()) {
        System.out.println(getName() + " cannot perform research (depleted or broken down).");
        return false;
    }
    System.out.println("performing research.");
    return true;
}
}
