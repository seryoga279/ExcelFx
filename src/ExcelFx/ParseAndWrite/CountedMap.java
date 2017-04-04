/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExcelFx.ParseAndWrite;

import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author seryo
 */
public class CountedMap {
    private String name;
    private ArrayList<String> neededStaff;
    private HashSet <String> edWay;

    public CountedMap() {
    }

    public CountedMap(String name, ArrayList<String> neededStaff, HashSet<String> edWay) {
        this.name = name;
        this.neededStaff = neededStaff;
        this.edWay = edWay;
    }
    
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getNeededStaff() {
        return neededStaff;
    }

    public void setNeededStaff(ArrayList<String> neededStaff) {
        this.neededStaff = neededStaff;
    }

    public HashSet <String> getEdWay() {
        return edWay;
    }

    public void setEdWay(HashSet <String> edWay) {
        this.edWay = edWay;
    }
    
    public int sizeNeededStaff(){
        return this.neededStaff.size();
    }
    public int sizeEdWay(){
        return this.edWay.size();
    }
}
