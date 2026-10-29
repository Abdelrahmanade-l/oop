/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package missions;

/**
 *
 * @author Sky Hawk
 */
import agents.Agent;
import resources.ResourceType;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public abstract class Mission {
    protected String id;
    protected int duration;
    protected int elapsedTime;
    protected MissionState state;
    protected List<Agent> assignedAgents;
    protected Map<ResourceType, Integer> requiredResources;
    protected String failureReason;
    
    public Mission(String id, int duration){
        this.id=id;
        this.duration=duration;
        this.assignedAgents=new ArrayList<>();
        this.requiredResources=new EnumMap<>(ResourceType.class);
    }
    
    public abstract String getType();
    
    public String getId(){return id;}
    public MissionState getState(){return state;}
    public String getFailureReason(){return failureReason;}
    public List<Agent>getAssignedAgents(){return assignedAgents;}
    public Map<ResourceType,Integer>getRequiredResources(){return requiredResources;}
    public void addRequiredResource(ResourceType type,int amount){
        requiredResource.put(type,amount);
    }
    
    public void assign(List<Agent>agents){
        this.assignedAgents=agents;
        for(Agent a: agents){
            a.setAvailable(false);
        }
        this.state=MissionState.ASSIGNED;
    }
    public void start(){this.state=MissionState.IN_PROGRESS;}
    
    public void tick(){
        if(state!=MissionState.IN_PROGRESS)return;
        elapsedTime++;
        if(elapsedTime>=duration)complete();
    }
    
    public void complete(){
        this.state=MissionState.COMPLETED;
        releaseAgents();
    }
    
    public void fail(String reason){
        this.state=MissionState.FAILED;
        this.failureReason=reason;
        releaseAgents();
    }
    
    private void releaseAgents(){
        for(Agent a :assignedAgents){a.setAvailable(true);}
        assignedAgents.clear();
    }
}
