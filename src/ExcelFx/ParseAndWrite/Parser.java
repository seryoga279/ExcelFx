/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExcelFx.ParseAndWrite;

import ExcelFx.row.ExcelRow;
import ExcelFx.row.RowList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author seryo
 */
public class Parser {

    private Workbook wb;

    private final RowList list = new RowList();

    /**
     * Выполняет парсинг исходного списка профессий
     *
     * @param patchName Путь к xls/xlsx файлу
     * @param pageNumber Номер страницы с которой читать данные
     * @return прочитанный построчно Excel файл
     * @throws NullPointerException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public RowList parseInitalData(String patchName, Integer pageNumber) throws NullPointerException, IOException {

        System.out.println("parse inital data in " + patchName + " at page " + pageNumber);

        File file = new File(patchName);

        if (!file.exists()) {
            System.out.println("Error file on patch not exists");
        }
        FileInputStream in = new FileInputStream(file);

        switch (patchName.substring(patchName.lastIndexOf("."), patchName.length())) {
            case ".xls": {
                this.wb = new HSSFWorkbook(in);
                break;
            }
            case ".xlsx": {
                this.wb = new XSSFWorkbook(in);
                break;
            }
            default: {
                System.out.println("error wrong file format");
            }

        }

        Sheet sheet = this.wb.getSheetAt(pageNumber);

        for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
            int cellType;
            this.list.add(new ExcelRow());
            Row row = sheet.getRow(i);
            int jMax = sheet.getRow(0).getPhysicalNumberOfCells();
            try {
                for (int j = 0; j < jMax; j++) {

                    Cell cell = row.getCell(j);

                    try {
                        cellType = cell.getCellType();
                    } catch (NullPointerException e) {
                        this.list.get(i).add("null");

                        continue;
                    }

                    switch (cellType) {
                        case Cell.CELL_TYPE_STRING:
                            this.list.get(i).add(cell.getStringCellValue().trim().toLowerCase());
                            //System.out.println(cell.getStringCellValue());

                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            this.list.get(i).add(String.valueOf(cell.getNumericCellValue()).trim().toLowerCase());
                            //System.out.println(cell.getNumericCellValue());
                            break;
                        default:

                            this.list.get(i).add("null");
                            //System.out.println("null");
                            break;
                    }
                }
            } catch (NullPointerException e) {
                System.err.println(e + " in " + i + " row");

            }

        }
        System.out.println("parse complited just read " + this.list.size() + " row ");
        System.out.println("-----------------------------------------------------------");
        return this.list;

    }

    /**
     * парсинг Укрупненных групп
     *
     * @param patchName Путь к xls/xlsx файлу
     * @param sheetNumber Номер страницы с которой читать данные
     * @return массив содержащий название группы и и подгруппы(направления)
     * которые в нее входят
     * @throws FileNotFoundException
     * @throws IOException
     */
    public HashMap<String, String> parseBigGroupe(String patchName, Integer sheetNumber) throws FileNotFoundException, IOException {
        HashMap<String, String> codeListData = new HashMap<>();
        String nameBigGr = null;
        InputStream in;
        in = new FileInputStream(patchName);
        if (".xls".equals(patchName.substring(patchName.lastIndexOf("."), patchName.length()))) {
            wb = new HSSFWorkbook(in);
        } else {
            wb = new XSSFWorkbook(in);
        }

        Sheet sheet = wb.getSheetAt(sheetNumber);
        int jMax = sheet.getRow(0).getPhysicalNumberOfCells();

        for (int i = 3; i < sheet.getPhysicalNumberOfRows(); i++) {
            int cellBlankVal = 0;
            int cellType;
            HashMap<String, String> hm = new HashMap<>();

            Row row = sheet.getRow(i);
            Cell cell;
            for (int j = 0; j < jMax; j++) {
                cell = row.getCell(j);
                cellType = cell.getCellType();
                if (cellType == Cell.CELL_TYPE_BLANK) {
                    cellBlankVal++;
                }
            }

            switch (cellBlankVal) {
                case 0: {

                    cell = row.getCell(0);

                    cellType = cell.getCellType();

                    switch (cellType) {
                        case Cell.CELL_TYPE_STRING:

                            codeListData.put(cell.getStringCellValue(), nameBigGr);

                            break;
                        case Cell.CELL_TYPE_NUMERIC:

                            codeListData.put(cell.getStringCellValue(), nameBigGr);

                    }

                }
                break;
                case 2: {
                    for (int j = 0; j < jMax; j++) {
                        cell = row.getCell(j);
                        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                            nameBigGr = cell.getStringCellValue();

                        }
                    }
                }
                break;

            }
        }
        return codeListData;

    }

}
