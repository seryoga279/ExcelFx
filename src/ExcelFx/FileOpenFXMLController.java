/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExcelFx;

import Json.JsonCodes;
import Json.JsonWrite;
import Json.JsonXLSXData;
import Json.Type;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author seryo
 */
public class FileOpenFXMLController implements Initializable {

    @FXML
    private GridPane gridPane;
    @FXML
    private TextField universityField;
    @FXML
    private TextField collegeField;
//    @FXML
//    TextField codesField;
    @FXML
    Button universitySetting;
    @FXML
    Button collegeSetting;
//    @FXML
//    TextField codesPage;
    private final JsonWrite json = new JsonWrite();
    private Parent rootUniversity;
    private Parent rootCollege;
    private JsonWrite universityJson = new JsonWrite();
    private JsonWrite collegeJson = new JsonWrite();
    private JsonCodes codesJson = new JsonCodes();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            File universityFile = new File("university.json");
            if (universityFile.exists()) {
                this.universityJson.jsonRead("university.json");
                this.universityField.setText(this.universityJson.getPatch());
            }
            File collegeFile = new File("college.json");
            if (collegeFile.exists()) {
                this.collegeJson.jsonRead("college.json");
                this.collegeField.setText(this.collegeJson.getPatch());
            }
//            File codesFile = new File("codes.json");
//            if (codesFile.exists()) {
//                this.codesJson.jsonRead("codes.json");
//                this.codesField.setText(this.codesJson.getPatch());
//                String pageList = "";
//                for (int i = 0; i < this.codesJson.getPageList().size(); i++) {
//                    pageList += "," + this.codesJson.getPageList().get(i);
//                }
//                this.codesPage.setText(pageList.substring(1));
//            }

        } catch (Exception ex) {
            Logger.getLogger(FileOpenFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    protected void okButton(ActionEvent event) throws IOException {

        DropShadow ds = new DropShadow();
        ds.setColor(Color.RED);

        universityField.setEffect(null);
        collegeField.setEffect(null);
//        codesField.setEffect(null);
//        codesPage.setEffect(null);

        String err = "";
        boolean err2 = false;
        try {

            if (universityField.getText().length() == 0) {
                err += "Список вузов ";
                universityField.setEffect(ds);

            }
        } catch (Exception e) {
            err += "Список вузов ";
            universityField.setEffect(ds);
        }

        try {
            if (collegeField.getText().length() == 0) {
                err += "Список сузов ";
                collegeField.setEffect(ds);

            }
        } catch (Exception e) {
            err += "Список сузов ";
            collegeField.setEffect(ds);
        }
//        try {
//            if (codesField.getText().length() == 0) {
//                err += "Список кодов ";
//                codesField.setEffect(ds);
//            }
//        } catch (Exception ex) {
//            err += "Страницы для чтения кодов ";
//            codesPage.setEffect(ds);
//        }
//        try {
//            if (codesPage.getText().length() == 0) {
//                err += "Страницы для чтения кодов ";
//                codesPage.setEffect(ds);
//            }
//        } catch (Exception ex) {
//            err += "Страницы для чтения кодов ";
//            codesPage.setEffect(ds);
//        }
        if (err.length() != 0) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Ошибка Формата Страницы в документах");
            alert.setContentText(err);
            alert.showAndWait();

        }

        try {
            if (rootUniversity != null) {
                universityJson = (JsonWrite) rootUniversity.getUserData();
            }

            if (((universityJson.getEddWay().length() == 0) || (universityJson.getPage().length() == 0) || (universityJson.getProffName().length() == 0)/**||(universityJson.getEddName().length( )== 0)**/) && (universityField.getText().length() != 0)) {

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Ошибка настроек ");
                alert.setContentText("список вузов");
                alert.showAndWait();
                universitySetting.setEffect(ds);
                err2 = true;
            } else {
                universityJson.setPatch(universityField.getText());
                universityJson.jsonCreate("university.json");

            }
        } catch (Exception e) {
            err2 = true;
            universitySetting.setEffect(ds);

        }

        try {
            if (rootCollege != null) {
                collegeJson = (JsonWrite) rootCollege.getUserData();
            }

            if (((collegeJson.getEddWay().length() == 0) || (collegeJson.getPage().length() == 0) || (collegeJson.getProffName().length() == 0)/**||(collegeJson.getEddName().length() == 0)**/) && (collegeField.getText().length() != 0)) {

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Ошибка настроек ");
                alert.setContentText("список сузов");
                alert.showAndWait();
                universitySetting.setEffect(ds);
                err2 = true;
            } else {
                collegeJson.setPatch(collegeField.getText());
                collegeJson.jsonCreate("college.json");

            }
        } catch (Exception e) {
            err2 = true;
            collegeSetting.setEffect(ds);

        }

        if ((err.length() == 0) && (err2 == false)) {
            gridPane.setUserData(createData());

//            JsonCodes jc = new JsonCodes(codesField.getText(), new ArrayList<>(Arrays.asList(codesPage.getText().split(","))));
//            jc.jsonCreate("codes.json");
            this.universityJson.jsonCreate("university.json");
            this.collegeJson.jsonCreate("college.json");
            Stage stage = (Stage) gridPane.getScene().getWindow();

            stage.close();
        }

    }

    @FXML
    protected void cancelButton(ActionEvent event) {

        Stage stage = (Stage) gridPane.getScene().getWindow();

        stage.close();
    }

    @FXML
    protected void unirsetyButton(ActionEvent evernt) {
        universityField.setText(getFilePatch());
    }

    @FXML
    protected void collegeButton(ActionEvent evernt) {
        collegeField.setText(getFilePatch());
    }

//    @FXML
//    protected void codesButton(ActionEvent evernt) {
//
//        codesField.setText(getFilePatch());
//    }

    @FXML
    protected void universitySetting(ActionEvent evernt) throws IOException {
        Type type = new Type("university");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("XLSSetting.fxml"));
        this.rootUniversity = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Настройки файла университетов");
        stage.setScene(new Scene(this.rootUniversity));
        stage.showAndWait();

    }

    @FXML
    protected void collegeSetting(ActionEvent evernt) {
        Type type = new Type("college");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("XLSSetting.fxml"));
            this.rootCollege = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("Настройки файла университетов");
            stage.setScene(new Scene(this.rootCollege));
            stage.showAndWait();

        } catch (Exception e) {
            System.err.println(e);

        }
    }

    @FXML
    protected void codesSetting(ActionEvent evernt) {

    }

    /**
     * создает и возвращает объект типа Json.JsonXLSData который будет передан
     * выше для обработки
     *
     * @return
     */
    public JsonXLSXData createData() {

//        ArrayList<String> pageCodes = new ArrayList<>(Arrays.asList(codesPage.getText().split(",")));
//        JsonCodes codes = new JsonCodes(codesField.getText(), pageCodes);

        JsonXLSXData data = new JsonXLSXData(universityJson, collegeJson, new JsonCodes()); // изменить new JsonCodes на codes
        return data;

    }

    private String getFilePatch() {
        FileChooser chooser = new FileChooser();
        setExtFilters(chooser);
        chooser.setTitle("Open File");
        File file = chooser.showOpenDialog(gridPane.getScene().getWindow());
        if (file != null) {
            return file.getAbsolutePath();
        }
        return "Error";
    }

    private void setExtFilters(FileChooser chooser) {
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MS Excel 2003", "*.xls*"),
                new FileChooser.ExtensionFilter("Ms Excel 2007", "*.xlsx")
        );
    }

}
