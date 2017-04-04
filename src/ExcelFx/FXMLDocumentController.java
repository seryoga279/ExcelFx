/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExcelFx;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import ExcelFx.ParseAndWrite.*;
import ExcelFx.row.RowList;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import Json.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.json.simple.parser.ParseException;

/**
 *
 * @author seryo
 */
public class FXMLDocumentController implements Initializable {

    private ExcelFX excelFX;
    private RowList list = new RowList();
    private ObservableList<String> names = FXCollections.observableArrayList("123");
    public static HashMap<String, HashSet<CountedMap>> sortProf = new HashMap<>();

    List<CheckBox> checkList = new ArrayList<>();

    @FXML
    private VBox Vbox;

    @FXML
    private VBox rootVBox;

    @FXML
    private TableView tableView;

    @FXML
    private SplitPane SplitPane;

    @FXML
    private ProgressBar ProgressBar;

    @FXML
    private final ListView<String> footer = new ListView<>(names);

    @FXML
    private MenuItem Print;

    private HashMap<String, HashMap<String, String>> bigGroupe;
    private RowList collegeProf;
    private RowList universityProf;
    private final JsonWrite universityJson = new JsonWrite();
    private final JsonWrite collegeJson = new JsonWrite();
    //private final JsonCodes codesJson = new JsonCodes();
    private final JsonXLSXData jsonXLSXData = new JsonXLSXData();
    private Parent fileOpenParent;
    private JsonWrite initaldata;

    @FXML
    private void Close(ActionEvent event) throws IOException {

        Stage stage = (Stage) this.SplitPane.getScene().getWindow();
        stage.close();

    }

    public void setProgressBar(double val) {
        this.ProgressBar.setProgress(val);

    }

    public ProgressBar getProgressBar() {
        return this.ProgressBar;
    }

    @FXML
    private void Open(ActionEvent event) throws NullPointerException, IOException {

        System.out.println("-----------------------------------------------------------");
        System.out.println("Pressed open Button ");
        File universityFile = new File("university.json");
        File collegeFile = new File("college.json");
        System.out.println("University File emty is " + String.valueOf(universityFile.length() == 0));
        System.out.println("College File emty is " + String.valueOf(collegeFile.length() == 0));

        //File codesFile = new File("codes.json");
        if ((universityFile.exists()) && (collegeFile.exists()) /*&& (codesFile.exists())*/) {
            try {
                this.universityJson.jsonRead("university.json");
                this.collegeJson.jsonRead("college.json");
                //this.codesJson.jsonRead("codes.json");
                this.jsonXLSXData.setAll(universityJson, collegeJson/*, codesJson*/);
            } catch (IOException | ParseException ex) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Ошибка чтения настроек ");
                alert.setContentText(ex.toString());
                alert.showAndWait();
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FileOpenFXML.fxml"));
                this.fileOpenParent = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.DECORATED);
                stage.setTitle("Укажите файлы");
                stage.setScene(new Scene(this.fileOpenParent));

                stage.showAndWait();
            } catch (Exception e) {
                System.err.println(e);

            }

            this.jsonXLSXData.setAll(universityJson, collegeJson/*, codesJson*/);
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("InitalData.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);

        stage.setTitle("Укажите файлы");
        stage.setScene(new Scene(root1));
        stage.showAndWait();
        setProgressBar(-1.);

        Task task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {

                initaldata = (JsonWrite) root1.getUserData();
                System.out.println("Inital Data empty is " + initaldata.isEmpty());

                initaldata.setPage(String.valueOf(Integer.parseInt(initaldata.getPage()) - 1));

                Parser parser = new Parser();

                list = parser.parseInitalData(initaldata.getPatch(), Integer.parseInt(initaldata.getPage()));
                //System.out.println(list.size());
                Sort sort = new Sort(list, jsonXLSXData, initaldata.getYStart(), initaldata.getYEnd());
                HashMap<String, HashMap<String, ArrayList<String>>> hm = sort.professionsGrouping(initaldata.getYStart(), initaldata.getYEnd());

                sortProf = sort.addWay(hm);

                return null;
            }

        };
        mainProcessing(task);

        System.out.println("button open finished");
        System.out.println("-----------------------------------------------------------");

    }

    private void mainProcessing(Task task) {
        final Thread thread = new Thread(null, task, "Background");
        thread.setDaemon(true);
        thread.start();
        new Thread() {
            @Override
            public void run() {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                }
                Platform.runLater(() -> {
                    setProgressBar(0);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Внимание");
                    alert.setHeaderText("");
                    alert.setContentText("Выполнение завершено");
                    alert.showAndWait();

                });

            }
        }.start();

        this.Print.setDisable(false);
    }

    @FXML
    private void TablePatch(ActionEvent event) throws IOException {
        System.out.println("ExcelFx.FXMLDocumentController.TablePatch()");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FileOpenFXML.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("Укажите файлы");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e) {
            System.err.println(e);

        }

    }

    @FXML
    private void Print(ActionEvent event) throws IOException {

        System.out.println("Pressed print button");

        FileChooser chooser = new FileChooser();

        chooser.setTitle("Open File");
        File file = chooser.showSaveDialog(this.SplitPane.getScene().getWindow());
        System.out.println(file.getAbsolutePath());

        printXls(file.getAbsolutePath(), sortProf);

        System.out.println("Print finished");
        System.out.println("-----------------------------------------------------------");

    }

    private void printXls(String patch, HashMap<String, HashSet<CountedMap>> result) throws IOException {

        System.out.println("printXls was started");
        System.out.println("patch name:" + patch);
        System.out.println("result size: " + result.size());

        try (Workbook wb = new XSSFWorkbook()) {
            Integer yStart = null;
            Integer yEnd = null;
            for (Map.Entry<String, HashSet<CountedMap>> entry : result.entrySet()) {
                Sheet sheet = wb.createSheet(entry.getKey());
                Map<String, CellStyle> styles = createStyles(wb);
                Row row;
                Cell cell;
                row = sheet.createRow(0);
                cell = row.createCell(0);
                cell.setCellValue("Профессия");
                cell.setCellStyle(styles.get("header"));
                for (int i = 0; i < list.get(0).size(); i++) {
                    if (list.get(0).get(i).equals(initaldata.getYStart())) {
                        yStart = i;
                    }
                    if (list.get(0).get(i).equals(initaldata.getYEnd())) {
                        yEnd = i;
                    }
                }

                for (int i = yStart; i <= yEnd; i++) {
                    cell = row.createCell((i - yStart) + 1);
                    cell.setCellValue(list.get(0).get(i));
                    cell.setCellStyle(styles.get("header"));

                }

                int i = 1;
                Iterator<CountedMap> itr = entry.getValue().iterator();

                while (itr.hasNext()) {
                    CountedMap next = itr.next();
                    row = sheet.createRow(i);
                    cell = row.createCell(0);
                    cell.setCellValue(next.getName().substring(0, 1).toUpperCase() + next.getName().substring(1));
                    cell.setCellStyle(styles.get("total"));
                    for (int j = 0; j < next.getNeededStaff().size(); j++) {
                        cell = row.createCell(j + 1);//проверить
                        //System.out.println(next.getNeededStaff().get(j));
                        cell.setCellValue(Double.parseDouble(next.getNeededStaff().get(j)));
                        cell.setCellStyle(styles.get("total"));
                    }

                    Iterator<String> itrWay = next.getEdWay().iterator();
                    while (itrWay.hasNext()) {
                        i++;
                        String next1 = itrWay.next();
                        row = sheet.createRow(i);
                        cell = row.createCell(0);
                        cell.setCellValue(next1.substring(0, 1).toUpperCase() + next1.substring(1));

                        cell.setCellStyle(styles.get("normal"));
                        for (int j = 0; j < next.getNeededStaff().size(); j++) {
                            cell = row.createCell(j + 1);//проверить
                            //System.out.println(next.getNeededStaff().get(j));
                            cell.setCellValue(Double.parseDouble(next.getNeededStaff().get(j)) / next.getEdWay().size());
                            cell.setCellStyle(styles.get("normal"));
                        }

                    }
                    i++;

                }
            }
            wb.write(new FileOutputStream(patch + ".xlsx"));
            System.out.println("File saved");
        }
        System.out.println("printXlsx finished");
    }

    private static CellStyle borderedStyleNormal(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());

        return style;
    }

    private static CellStyle borderedStyleTotal(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setBorderRight(CellStyle.BORDER_MEDIUM);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_MEDIUM);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_MEDIUM);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_MEDIUM);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());

        return style;
    }

    private static CellStyle borderedStyleHeader(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());

        return style;
    }

    private static Map<String, CellStyle> createStyles(Workbook wb) {
        Map<String, CellStyle> styles = new HashMap<>();
        DataFormat df = wb.createDataFormat();

        CellStyle styleNormal;
        CellStyle styleTotal;
        CellStyle styleHeader;

        Font font1 = wb.createFont();
        font1.setBoldweight(Font.BOLDWEIGHT_NORMAL);

        Font font2 = wb.createFont();
        font2.setBoldweight(Font.BOLDWEIGHT_BOLD);

        styleNormal = borderedStyleNormal(wb);
        styleNormal.setFont(font1);
        styles.put("normal", styleNormal);

        styleTotal = borderedStyleTotal(wb);
        styleTotal.setFont(font2);
        styleTotal.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        styleTotal.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styles.put("total", styleTotal);

        styleHeader = borderedStyleHeader(wb);
        styleHeader.setFont(font1);
        styleHeader.setRotation((short) 90);
        styles.put("header", styleHeader);

        return styles;
    }

    private void setExtFilters(FileChooser chooser) {
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MS Excel 2003", "*.xls*"),
                new FileChooser.ExtensionFilter("Ms Excel 2007", "*.xlsx")
        );
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.Print.setDisable(true);

        footer.setItems(names);
        JsonWrite jsw = new JsonWrite();
        File universityFile = new File("university.json");
        File collegeFile = new File("college.json");
        //File codesFile = new File("codes.json");

        if ((universityFile.exists()) && (collegeFile.exists()) /*&& (codesFile.exists())*/) {
            try {
                this.universityJson.jsonRead("university.json");
                this.collegeJson.jsonRead("college.json");
                //this.codesJson.jsonRead("codes.json");
                this.jsonXLSXData.setAll(universityJson, collegeJson/*, codesJson*/);
            } catch (IOException | ParseException ex) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Ошибка чтения настроек ");
                alert.setContentText(ex.toString());
                alert.showAndWait();
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FileOpenFXML.fxml"));
                this.fileOpenParent = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.DECORATED);
                stage.setTitle("Укажите файлы");
                stage.setScene(new Scene(this.fileOpenParent));

                stage.showAndWait();
            } catch (Exception e) {
                System.err.println(e);

            }

            this.jsonXLSXData.setAll(universityJson, collegeJson/*, codesJson*/);
        }

    }

    public void eddArr(String args) {
        names.add(args);
    }

    public HashMap<String, HashMap<String, String>> getBigGroupe() {
        return bigGroupe;
    }

    public void setBigGroupe(HashMap<String, HashMap<String, String>> bigGroupe) {
        this.bigGroupe = bigGroupe;
    }

    public RowList getCollegeProf() {
        return collegeProf;
    }

    public void setCollegeProf(RowList collegeProf) {
        this.collegeProf = collegeProf;
    }

    public RowList getUniversityProf() {
        return universityProf;
    }

    public void setUniversityProf(RowList universityProf) {
        this.universityProf = universityProf;
    }

    public void setData(RowList universityProf, RowList collegeProf, HashMap<String, HashMap<String, String>> bigGroupe) {
        this.universityProf = universityProf;
        this.collegeProf = collegeProf;
        this.bigGroupe = bigGroupe;
    }

    public ObservableList<String> getNames() {
        return names;
    }

    public void setNames(ObservableList<String> names) {
        this.names = names;
    }

    public RowList getList() {
        return list;
    }

    public void setList(RowList list) {
        this.list = list;
    }

}
