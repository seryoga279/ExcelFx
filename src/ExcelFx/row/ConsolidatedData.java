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
public class ConsolidatedData {

    private String name;
    private String direction;
    private String type;
    private HashMap<String, String> dateList = new HashMap<>();

    public ConsolidatedData() {
        dateList = new HashMap<>();
    }

    public ConsolidatedData(String name, String direction, HashMap<String, String> dateList) {
        this.name = name;
        this.direction = direction;
        this.dateList.putAll(dateList);
    }

    public String getName() {
        return name;
    }

    public String getDirection() {
        return direction;
    }

    public HashMap<String, String> getDateList() {
        return dateList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setDateList(HashMap<String, String> dateList) {
        this.dateList = dateList;
    }

    public void put(String name, String direction, HashMap<String, String> dateList, String type) {
        this.name = name;
        this.direction = direction;
        this.dateList.putAll(dateList);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
