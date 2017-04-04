/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TableVievData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author seryo
 */
public class TableVievData {

    private final SimpleStringProperty Name;
    private ArrayList<String> nameList;
    private HashSet<String> set;

    private TableVievData(String CellKey, String CellVal) {
        this.Name = new SimpleStringProperty(CellKey);

    }

    public String getCellKey() {
        return Name.get();
    }

    public void setCellKey(String key) {
        Name.set(key);
    }
    private void ExcelDataToObsList (){
        ObservableList<ArrayList<SimpleStringProperty>> list = FXCollections.observableArrayList(); 
        List<SimpleStringProperty> tmp = new ArrayList<>();
        //tmp.add(Name)
    }

   

}
