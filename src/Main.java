import agents.Agent;
import agents.Engineer;
import agents.ExplorationRover;
import agents.RepairRobot;
import agents.Scientist;
import colony.Colony;
import colony.Environment;
import colony.Location;
import emergencies.OxygenLeak;
import facilities.Habitat;
import facilities.Storage;
import missions.ExplorationMission;
import missions.Mission;
import resources.ResourceType;

import java.util.ArrayList;
import java.util.List;

public class Main {

    // Mission keeps (and later clears) the list it receives, so always give it a fresh ArrayList
    private static List<Agent> team(Agent... members) {
        List<Agent> list = new ArrayList<>();
        for (Agent a : members) {
            list.add(a);
        }
        return list;
    }

    public static void main(String[] args) {
        // ----- setup -----
        Environment environment = new Environment();
        Colony colony = new Colony(environment);

        Location habitatLocation = new Location("Habitat A");
        Location storageLocation = new Location("Storage");

        Habitat habitat = new Habitat(10);
        Storage storage = new Storage(5);
        habitat.AddResource(ResourceType.OXYGEN, 300);
        storage.AddResource(ResourceType.OXYGEN, 200);
        storage.AddResource(ResourceType.BATTERY, 100);
        storage.AddResource(ResourceType.SPARE_PARTS, 10);
        storage.AddResource(ResourceType.FUEL, 20);

        Scientist scientist = new Scientist("S1", "Sara", habitatLocation, "exploration");
        Engineer engineer = new Engineer("E1", "Omar", habitatLocation, "repair");
        ExplorationRover rover = new ExplorationRover("R1", storageLocation);
        RepairRobot repairBot = new RepairRobot("RB1", storageLocation);

        colony.addFacility(habitat);
        colony.addFacility(storage);
        colony.addAgent(scientist);
        colony.addAgent(engineer);
        colony.addAgent(rover);
        colony.addAgent(repairBot);

        // ----- Scenario 1: successful exploration -----
        System.out.println("\n##### Scenario 1: Successful Exploration #####");
        colony.printStatusReport();
        Mission m1 = new ExplorationMission("M1", 3);
        m1.addRequiredResource(ResourceType.BATTERY, 10);
        colony.submitMission(m1, team(scientist, rover));
        colony.advanceTime();
        colony.advanceTime();
        colony.advanceTime();
        colony.printStatusReport();

        // ----- Scenario 2: rejection (engineer cannot explore) -----
        System.out.println("\n##### Scenario 2: Mission Rejection #####");
        Mission m2 = new ExplorationMission("M2", 3);
        colony.submitMission(m2, team(engineer, rover));
        System.out.println("M2 state: " + m2.getState() + " | reason: " + m2.getFailureReason());

        // ----- Scenario 3: resource shortage -----
        System.out.println("\n##### Scenario 3: Resource Shortage #####");
        Mission m3 = new ExplorationMission("M3", 3);
        m3.addRequiredResource(ResourceType.FUEL, 9999);
        System.out.println("Fuel before: " + colony.getTotalResource(ResourceType.FUEL));
        colony.submitMission(m3, team(scientist, rover));
        System.out.println("Fuel after:  " + colony.getTotalResource(ResourceType.FUEL));

        // ----- Scenario 4: oxygen leak -----
        System.out.println("\n##### Scenario 4: Emergency Handling #####");
        System.out.println("Oxygen before: " + colony.getTotalResource(ResourceType.OXYGEN));
        OxygenLeak leak = new OxygenLeak("L1", habitat, 50);
        colony.handleEmergency(leak);
        System.out.println("Oxygen after:  " + colony.getTotalResource(ResourceType.OXYGEN));
        colony.printStatusReport();

        // ----- Scenario 5: dust storm -----
        System.out.println("\n##### Scenario 5: Environmental Effect #####");
        environment.setCurrentCondition(Environment.Condition.DUST_STORM);
        Mission m5 = new ExplorationMission("M5", 3);
        colony.submitMission(m5, team(scientist, rover));
        System.out.println("M5 state: " + m5.getState() + " | reason: " + m5.getFailureReason());
    }
}