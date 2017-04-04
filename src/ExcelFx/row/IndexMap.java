/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExcelFx.row;

import java.util.HashMap;

/**
 *
 * @author seryo
 */


public class IndexMap {
    private char ind;
    private HashMap<String, Integer> hashMap = new HashMap<>();

    /**
     *<p></p>
     * @param hm
     * @param ind
     */
    public IndexMap(HashMap<String,Integer> hm, char ind) {
        this.hashMap.putAll(hm);
        this.ind = ind;
    }

    /**
     * <p>Пустой конструктор класса</p>
     * <p>И по традиции: Ну пусть будет :)</p>
     * 
     */
    public IndexMap() {
    }
    
    /**
     *
     * @param ind
     */
    public void setInd(char ind) {
        this.ind = ind;
    }

    /**
     *
     * @param hm
     */
    public void sethashMap(HashMap<String, Integer> hm) {
        this.hashMap = hm;
    }

    /**
     *
     * @return
     */
    public char getInd() {
        return ind;
    }

    /**
     *
     * @return
     */
    public HashMap<String, Integer> gethashMap() {
        return hashMap;
    }
    
    
    
}
