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
public class ExcelRow {

    private List<String> row = new ArrayList<>();

    public ExcelRow() {
    }

    public ExcelRow(List<String> row) {
        this.row = row;
    }

    public ExcelRow(ExcelRow row) {
        for (int i = 0; i < row.size(); i++) {
            this.row.add(row.get(i));
        }

    }

    public List<String> getRow() {
        return row;
    }

    public void setRow(List<String> row) {
        this.row = row;
    }

    public Integer size() {
        return row.size();
    }

    public String get(Integer i) {
        return row.get(i);

    }

    public void add(String cell) {
        this.row.add(cell);
    }

    public boolean isUniversity(Integer nameColumn) {

        return (this.row.get(nameColumn)).toLowerCase().equals("вп");

    }

    public boolean isCollege(Integer nameColumn) {

        return (this.row.get(nameColumn)).toLowerCase().equals("сп");

    }

    public int columnNumberEducationLevel(String str) {

        int NumberColumn = -1;
        for (int i = 0; i < this.row.size(); i++) {
            if (str.toLowerCase().equals(this.row.get(i).toLowerCase())) {
                NumberColumn = i;
                break;
            }

        }
        return NumberColumn;

    }

    public int getCell(String cell) {
        int indexCol = -1;
        for (int i = 0; i < row.size(); i++) {
            if (row.get(i).equals(cell)) {
            }
        }
        return indexCol;
    }

}
