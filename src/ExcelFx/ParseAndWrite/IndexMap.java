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


public class IndexMap {
    private char ind;
    private HashMap<String, Integer> hashMap = new HashMap<>();

    public IndexMap(HashMap<String,Integer> hm, char ind) {
        this.hashMap.putAll(hm);
        this.ind = ind;
    }

    public IndexMap() {
    }
    
    public void setInd(char ind) {
        this.ind = ind;
    }

    public void sethashMap(HashMap<String, Integer> hm) {
        this.hashMap = hm;
    }

    public char getInd() {
        return ind;
    }

    public HashMap<String, Integer> gethashMap() {
        return hashMap;
    }
    
    
    
}
