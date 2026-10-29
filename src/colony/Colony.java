package colony;

import agents.Agent;
import agents.CrewMember;
import agents.Robot;
import emergencies.Emergency;
import emergencies.OxygenLeak;
import facilities.Facility;
import missions.Mission;
import missions.MissionState;
import resources.ResourceType;

import java.util.ArrayList;
import java.util.List;

public class Colony {

    protected List<Agent> agents;
    protected List<Facility> facilities;
    protected Mission activeMission;
    protected Environment environment;

    private static final int LEAK_SPARE_PARTS = 2;
    private static final int LEAK_BATTERY = 5;

    public Colony(Environment environment) {
        if (environment == null) {
            throw new IllegalArgumentException("Environment cannot be null");
        }
        this.agents = new ArrayList<>();
        this.facilities = new ArrayList<>();
        this.activeMission = null;
        this.environment = environment;
    }

    public void addAgent(Agent agent) {
        if (agent == null) {
            throw new IllegalArgumentException("Agent cannot be null");
        }
        for (Agent existing : agents) {
            if (existing.getId().equals(agent.getId())) {
                throw new IllegalArgumentException("Agent already exists");
            }
        }

        agents.add(agent);
    }

    public void addFacility(Facility facility) {
        if (facility == null) {
            throw new IllegalArgumentException("Facility cannot be null");
        }
        facilities.add(facility);
    }

    

    
    public int getTotalResource(ResourceType type) {
        int total = 0;
        for (Facility f : facilities) {
            total += f.GetResource(type);
        }
        return total;
    }

    // (policy: first added = first used)
    private void consumeResource(ResourceType type, int amount) {
        for (Facility f : facilities) {
            int take = Math.min(f.GetResource(type), amount);
            f.ConsumeResource(type, take);
            amount -= take;
        }
    }

    

    public boolean submitMission(Mission mission, List<Agent> missionAgents) {
        if (mission == null || missionAgents == null
                || missionAgents.isEmpty()) {
            return false;
        }
        if (activeMission != null) {
            return reject(mission, "Another mission is already active.");
        }
         if (mission.getState() != null
                && mission.getState() != MissionState.PENDING) {
            return reject(mission, "Mission cannot be submitted in its current state.");
        }

        Environment.Condition condition = environment.getCurrentCondition();
        if (condition == Environment.Condition.DUST_STORM && mission.getType().equalsIgnoreCase("Exploration")) {
            return reject(mission, "Surface operations are not allowed during a dust storm.");
        }

        for (Agent a : missionAgents) {
            if (!a.isAvailable()) {
                return reject(mission, "Agent " + a.getId() + " is not available.");
            }
            if (!a.canPerform(mission)) {
                return reject(mission, "Agent does not possess the required capability.");
            }
            if (condition == Environment.Condition.COMM_DISRUPTION && a.requiresCommunication()) {
                return reject(mission, "Communication is disrupted.");
            }
        }

        
        for (ResourceType type : ResourceType.values()) {
            Integer needed = mission.getRequiredResources().get(type);
            if (needed != null && getTotalResource(type) < needed) {
                return reject(mission, "Insufficient " + type + ".");
            }
        }
        for (ResourceType type : ResourceType.values()) {
            Integer needed = mission.getRequiredResources().get(type);
            if (needed != null) {
                consumeResource(type, needed);
            }
        }

        mission.assign(missionAgents);
        activeMission = mission;
        System.out.println("Mission " + mission.getId() + " ASSIGNED.");
        return true;
    }

    private boolean reject(Mission mission, String reason) {
         if (mission == null) {
            System.out.println("Mission cannot be assigned. " + reason);
            return false;
        }
        mission.fail(reason);
        System.out.println("Mission " + mission.getId() + " cannot be assigned.");
        System.out.println("Reason: " + reason);
        return false;
    }



    public void advanceTime() {
        for (Agent a : agents) {
            a.tick();
        }

        if (activeMission == null) {
            return;
        }

        // a dust storm fails a running exploration mission
        if (environment.getCurrentCondition() == Environment.Condition.DUST_STORM
                && activeMission.getType().equalsIgnoreCase("Exploration")) {
            activeMission.fail("Dust storm.");
        } else {
            if (activeMission.getState() == MissionState.ASSIGNED) {
                activeMission.start();
            }
            activeMission.tick();
        }

        if (activeMission.getState() == MissionState.COMPLETED
                || activeMission.getState() == MissionState.FAILED) {
            System.out.println("Mission " + activeMission.getId() + " " + activeMission.getState());
            activeMission = null;
        }
    }

   

    public void handleEmergency(Emergency emergency) {
        if (emergency == null || emergency.isResolved()) {
            return;
        }
        if (emergency instanceof OxygenLeak) {
            handleOxygenLeak((OxygenLeak) emergency);
        }
    }

    private void handleOxygenLeak(OxygenLeak leak) {
        
        leak.getHabitat().ConsumeResource(ResourceType.OXYGEN, leak.getOxygenLoss());

        
        CrewMember human = null;
        Robot robot = null;
        for (Agent a : agents) {
            if (a.isAvailable() && a.canHandle(leak)) {
                if (human == null && a instanceof CrewMember) {
                    human = (CrewMember) a;
                } else if (robot == null && a instanceof Robot) {
                    robot = (Robot) a;
                }
            }
        }

        
        String reason = null;
        if (human == null || robot == null) {
            reason = "Required responders are unavailable.";
        } else if (getTotalResource(ResourceType.SPARE_PARTS) < LEAK_SPARE_PARTS
                || getTotalResource(ResourceType.BATTERY) < LEAK_BATTERY) {
            reason = "Insufficient response resources.";
        }

        
        if (reason != null) {
            leak.getHabitat().ConsumeResource(ResourceType.OXYGEN, leak.getOxygenLoss());
            leak.setResolved(false);
            System.out.println("Emergency " + leak.getId() + " UNRESOLVED. Reason: " + reason);
            return;
        }

        
        human.setLocation(leak.getLocation());
        robot.setLocation(leak.getLocation());
        human.setAvailable(false);
        robot.setAvailable(false);
        consumeResource(ResourceType.SPARE_PARTS, LEAK_SPARE_PARTS);
        consumeResource(ResourceType.BATTERY, LEAK_BATTERY);
        leak.setResolved(true);
        human.setAvailable(true);
        robot.setAvailable(true);
        System.out.println("Emergency " + leak.getId() + " RESOLVED.");
    }

    

    public void printStatusReport() {
        System.out.println("===== COLONY STATUS =====");
        System.out.println("Environment: " + environment.getCurrentCondition());
        System.out.println("Active mission: " + (activeMission == null ? "none" : activeMission.getId()));
        for (ResourceType type : ResourceType.values()) {
            System.out.println(type + ": " + getTotalResource(type));
        }
        for (Agent a : agents) {
            System.out.println(a);
        }
        System.out.println("\n--- Facilities ---");

        for (Facility f : facilities) {
            System.out.println(f.getClass().getSimpleName());
        }

        System.out.println("=========================");
    }
}