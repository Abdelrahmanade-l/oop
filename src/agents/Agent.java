package agents;

import colony.Location;
import emergencies.Emergency;
import missions.Mission;


public abstract class Agent {

        private final String id;
        private Location location;
        private boolean available = true;

        protected Agent(String id, Location location) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("id must not be null or empty");
        }
        this.id = id;
        this.location = location;
    }

        public String getId(){
            return id; 
        }
        public Location getLocation() {
            return location; 
        }
        public void setLocation(Location location) {
            this.location = location; 
        }
        public boolean isAvailable() { 
            return available && isUsable(); 
        }
        public void setAvailable(boolean available) { 
            this.available = available; 
        }
        protected boolean isAssigned() { 
            return !available; 
        }
        public boolean requiresCommunication() { 
            return false; 
        }
        public boolean operatesOnSurface() { 
            return false; 
        }
        public abstract boolean isUsable();
        public abstract boolean canPerform(Mission mission);
        public abstract boolean canHandle(Emergency emergency);
        public abstract boolean performDuty();
        public abstract boolean tick();
        
        protected static boolean missionIs(Mission mission, String... types) {
        if (mission == null) 
            return false;
        for (String t : types) {
            if (t.equalsIgnoreCase(mission.getType()))
                return true;
        }
        return false;
        }
        
        protected abstract String details();

        protected String label() { return id; }

        @Override
        public String toString() {
            return getClass().getSimpleName() + ":" + getId() + " | " + location + " | " + " | available=" + isAvailable() + details();
        }


    
}
