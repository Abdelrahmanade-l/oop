package agents;

import colony.Location;
import emergencies.Emergency;
import emergencies.Fire;
import emergencies.OxygenLeak;
import emergencies.RoverBreakdown;
import missions.Mission;

public class RepairRobot extends Robot {

    public RepairRobot(String id, Location location) {
        super(id, location);
    }

    @Override
    public boolean canPerform(Mission mission) {
        return missionIs(mission, "repair");
    }

    @Override
    public boolean canHandle(Emergency emergency) {
        return emergency instanceof OxygenLeak || emergency instanceof Fire || emergency instanceof RoverBreakdown;
    }

    public void repairEquipment(Mission mission) {
        System.out.println("Repair robot " + getId() + " is repairing" + (mission == null ? "." : " for mission " + mission.getId() + "."));
    }

    @Override
    public boolean performDuty() {
    if (!isUsable()) {
        System.out.println("cannot perform repairs.");
        return false;
    }
    System.out.println("Repairing damaged equipment inside the facility.");
    return true;
}

    
}
