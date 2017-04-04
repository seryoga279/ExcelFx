/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExcelFx.row;

/**
 *
 * @author seryo
 */
public class BigGroupeData {

    private final String BigGroup;
    private final RowList TypesEducation;

    /**
     * <p>
     * пустой конструктор CodeList</p>
     * <p>
     * ну Пусть будет :)</p>
     *
     */
    public BigGroupeData() {
        this.TypesEducation = new RowList();
        this.BigGroup = null;
    }

    /**
     * Создает новую укрупненную группу
     *
     * @param groupeName Имя группы
     */
    public BigGroupeData(String groupeName) {
        this.TypesEducation = new RowList();
        this.BigGroup = groupeName;

    }

    /**
     *
     * @return ИМя укрупненной группы
     */
    public String getBigGroup() {
        return BigGroup;
    }

    /**
     *
     * @return массив направлений подготовки укрупненной группы
     */
    public RowList getTypesEducation() {
        return TypesEducation;
    }

    /**
     *
     * @param Data Массив с данными по направлению подготовкм
     */
    public void addTypes(ExcelRow Data) {
        this.TypesEducation.add(Data);

    }

    

}
