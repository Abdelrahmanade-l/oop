package agents;

import colony.Location;
import emergencies.Emergency;
import missions.Mission;

public class ExplorationRover extends Robot {
    
    public ExplorationRover(String id, Location location) {
        super(id, location);
    }

    @Override
    public boolean canPerform(Mission mission) {
        return missionIs(mission, "exploration", "rescue");
    }

    @Override
    public boolean canHandle(Emergency emergency) {
        return false;
    }

    @Override
    public boolean performDuty() {
        if (!isUsable()) {
            System.out.println("cannot perform exploration.");
            return false;
        }
        System.out.println("Exploring the area.");
        return true;
    }
    
}
