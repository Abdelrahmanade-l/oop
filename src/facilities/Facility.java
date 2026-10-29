/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package facilities;

/**
 *
 * @author Sky Hawk
 */
import resources.ResourceType;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

public abstract class Facility {
    private enum location{A,B,C};
    private int personnel_capacity;
    private enum operational_status{destroyed,unavailable,available};
    private enum EquipmentType{life_support,container};
    private Dictionary<EquipmentType,Integer> equipment = new Hashtable<>();
    private Dictionary<ResourceType,Integer> resources = new Hashtable<>();
    
    public Facility(int personnel_capacity)
    {
        InitializeAmounts(personnel_capacity);
    }
    
    private void InitializeAmounts(int personnel_capacity)
    {
        this.personnel_capacity = personnel_capacity;
        Enumeration<ResourceType> r = resources.keys();
        while(r.hasMoreElements())
        {
            ResourceType R = r.nextElement();
            resources.put(R, 0);
        }
        
        Enumeration<EquipmentType> e = equipment.keys();
        while(e.hasMoreElements())
        {
            EquipmentType E = e.nextElement();
            equipment.put(E, 0);
        }
    }
    
    public int GetResource(ResourceType resource)
    {
        System.out.println("Amount of "+  resource+" "+resources.get(resource));
        return resources.get(resource);
    }
    public void AddResource(ResourceType resource, int amount)
    {
        int newAmount = resources.get(resource) + amount;
        resources.put(resource, newAmount);
        System.out.println("Amount of "+ resource+" "+ resources.get(resource));
    }
    public void ConsumeResource(ResourceType resource, int amount)
    {
        int newAmount = resources.get(resource) - amount;
        if(newAmount<0)
        {
            newAmount=0;
        }
        resources.put(resource, newAmount);
        System.out.println("Amount of "+ resource+" "+ resources.get(resource));
    }
    
    public void AddEquipment(EquipmentType equipment_type, int amount)
    {
        int newAmount = equipment.get(equipment_type) + amount;
        equipment.put(equipment_type, newAmount);
        System.out.println("Amount of "+ equipment_type+" "+equipment.get(equipment_type));
    }
    public void RemoveEquipment(EquipmentType equipment_type, int amount)
    {
        int newAmount = equipment.get(equipment_type) - amount;
        if(newAmount<0)
        {
            newAmount=0;
        }
        equipment.put(equipment_type, newAmount);
        System.out.println("Amount of "+ equipment_type+" "+ equipment.get(equipment_type));
    }
    public void SetPersonnelCapacity(int capacity)
    {
        this.personnel_capacity = capacity;
        System.out.println("All Facilities' maximum personnel capacity is: "+ capacity);
    }
    public int GetPersonnelCapacity()
    {
        int capacity = this.personnel_capacity;
        System.out.println("All Facilities' maximum personnel capacity is: "+ capacity);
        return capacity;
    }
}