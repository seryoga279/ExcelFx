/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExcelFx;

import Json.JsonCodes;
import Json.JsonWrite;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.hssf.record.PageBreakRecord;
import org.json.simple.parser.ParseException;

/**
 * FXML Controller class
 *
 * @author seryo
 */
public class InitalDataController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TextField initalField;
    @FXML
    private TextField initalPage;
    @FXML
    private TextField yStart;
    @FXML
    private TextField yEnd;
    @FXML
    private GridPane gridPane;

    private final JsonWrite codes = new JsonWrite();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            File initalFile = new File("inital.json");
            if (initalFile.exists()) {
                this.codes.jsonRead("inital.json");
                this.initalField.setText(this.codes.getPatch());
                this.initalPage.setText(this.codes.getPage());
                this.yStart.setText(this.codes.getYStart());
                this.yEnd.setText(this.codes.getYEnd());
            }

        } catch (Exception ex) {
            Logger.getLogger(FileOpenFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    protected void okButton(ActionEvent event) throws IOException {
        DropShadow ds = new DropShadow();
        ds.setColor(Color.RED);

        String err = "";
        if (initalField.getText().length() == 0) {
            err += "Выберите файл ";
            initalField.setEffect(ds);

        }
        if (initalPage.getText().length() == 0) {
            err += "Укажите страницу в excel файле ";
            initalPage.setEffect(ds);

        }
        if (yStart.getText().length() == 0) {
            err += "Укажите год начала чтения ";
            yStart.setEffect(ds);

        }
        if (yEnd.getText().length() == 0) {
            err += "Укажите год конца чтения ";
            yEnd.setEffect(ds);

        }

        if (err.length() != 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Неверно заполненны поля");
            alert.setContentText(err);
            alert.showAndWait();

        } else {
            Stage stage = (Stage) gridPane.getScene().getWindow();
            stage.close();
            this.codes.setPatch(this.initalField.getText());
            this.codes.setPage((this.initalPage.getText()).trim().toLowerCase());
            this.codes.setYStart(this.yStart.getText().trim().toLowerCase());
            this.codes.setYEnd(this.yEnd.getText().trim().toLowerCase());
            this.codes.jsonCreate("inital.json");

        }
        this.gridPane.setUserData(this.codes);

    }

    @FXML
    protected void cancelButton(ActionEvent event) throws IOException {
        Stage stage = (Stage) gridPane.getScene().getWindow();
        stage.close();

    }

    @FXML
    protected void initalButton(ActionEvent event) throws IOException {
        String str = getFilePatch();
        if (!str.isEmpty()) {
            this.initalField.setText(str);
        }

    }

    private String getFilePatch() {
        FileChooser chooser = new FileChooser();
        setExtFilters(chooser);
        chooser.setTitle("Open File");
        File file = chooser.showOpenDialog(gridPane.getScene().getWindow());
        if (file != null) {
            return file.getAbsolutePath();
        }
        return "";
    }

    private void setExtFilters(FileChooser chooser) {
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MS Excel 2003", "*.xls*"),
                new FileChooser.ExtensionFilter("Ms Excel 2007", "*.xlsx")
        );
    }

}
