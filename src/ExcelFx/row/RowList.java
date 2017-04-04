/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExcelFx.row;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author seryo
 */
public class RowList {

    List<ExcelRow> rowList = new ArrayList<>();

    public RowList() {
    }

    public RowList(List<ExcelRow> rowList) {
        this.rowList = rowList;
    }

    public RowList(RowList rowList) {
        for (int i = 0; i < rowList.size(); i++) {
            this.rowList.add(rowList.get(i));

        }
       // System.out.println("ExcelFx.row.RowList.<init>()");
    }

    public List<ExcelRow> getRowList() {
        return rowList;
    }

    public void setRowList(List<ExcelRow> rowList) {
        this.rowList = rowList;
    }

    public void add(ExcelRow row) {
        this.rowList.add(row);
    }

    public ExcelRow get(Integer Index) {
        return rowList.get(Index);
    }

    public Integer size() {
        return rowList.size();
    }

    public Integer getName(String Name) {
        int indexCol = -1;
        for (int i = 0; i < rowList.size(); i++) {
            if (rowList.get(i).getCell(Name) != -1) {
                indexCol = i;
            }
        }
        return indexCol;

    }

}
