/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package colony;

/**
 *
 * @author Sky Hawk
 */
public class Location {
    private final String name;
    public Location(String name){this.name=name;}
    public String getName(){return name;}
    @Override public String toString(){return name;}
}
