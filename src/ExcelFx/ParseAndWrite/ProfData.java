/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExcelFx.ParseAndWrite;

import java.util.HashMap;

/**
 *
 * @author seryo
 */
public class ProfData {
    
    private HashMap<String, HashMap<Integer,Integer>> data;

    public ProfData() {
        data = new HashMap<>();
    }

    public ProfData(HashMap<String, HashMap<Integer,Integer>> data) {
        this.data = data;
    }

    public HashMap<String, HashMap<Integer,Integer>> getData() {
        return data;
    }

    public void setData(HashMap<String, HashMap<Integer,Integer>> data) {
        this.data = data;
    }
    
    public void put(String name, HashMap<Integer,Integer> data){
        this.data.put(name, data);
    }
    
    public void set(String name, Integer year){
        HashMap<Integer,Integer> data = this.data.get(name);
        
        
         
    }
    private HashMap<Integer,Integer> counted (HashMap<Integer,Integer> data ,Integer key,Integer value){
        
        Integer tmpKey = data.get(key);
        tmpKey +=value;
        data.replace(key, value);
        return data;
        
    }
   
    
    
}
