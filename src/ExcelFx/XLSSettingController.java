/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExcelFx;

import ExcelFx.ParseAndWrite.Sort;
import Json.JsonWrite;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
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
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * FXML Controller class
 *
 * @author seryo
 */
public class XLSSettingController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    TextField pageField;
    @FXML
    TextField eddWayField;
    @FXML
    TextField proffField;
//    @FXML
//    TextField eddNameField;

    @FXML
    private GridPane gridPane;
    private JsonWrite json = new JsonWrite();
    private String type;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        FileOpenFXMLController fXMLController = new FileOpenFXMLController();
        JSONParser parser = new JSONParser();

        try {
            JSONObject object = (JSONObject) parser.parse(new FileReader("type"));
            this.type = object.get("type").toString();

            switch (type) {
                case "university": {
                    File file = new File("university.json");
                    if (file.exists()) {
                        try {
                            JsonWrite jsonWrite = new JsonWrite();
                            jsonWrite.jsonRead("university.json");
                            jsonRead(jsonWrite);
                        } catch (IOException | ParseException ex) {
                            Logger.getLogger(XLSSettingController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                    break;
                }
                case "college": {
                    File file = new File("college.json");
                    if (file.exists()) {
                        try {
                            JsonWrite jsonWrite = new JsonWrite();
                            jsonWrite.jsonRead("college.json");
                            jsonRead(jsonWrite);
                        } catch (IOException | ParseException ex) {
                            Logger.getLogger(XLSSettingController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                    break;
                }
            }

        } catch (IOException | ParseException ex) {
            Logger.getLogger(XLSSettingController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void jsonRead(JsonWrite jsonWrite) {
        this.pageField.setText(jsonWrite.getPage());
        this.eddWayField.setText(jsonWrite.getEddWay());
        this.proffField.setText(jsonWrite.getProffName());
//        this.eddNameField.setText(jsonWrite.getEddName());

    }

    @FXML
    protected void okButton(ActionEvent event) throws IOException {

        DropShadow ds = new DropShadow();
        ds.setColor(Color.RED);

        String err = "";
        try {
            if (pageField.getText().length() == 0) {
                err += "Страница ";
                pageField.setEffect(ds);

            }
        } catch (Exception ex) {
            err += "Страница ";
            pageField.setEffect(ds);
        }
        try {
            if (eddWayField.getText().length() == 0) {
                err += "Направление подготовки ";
                eddWayField.setEffect(ds);

            }
        } catch (Exception e) {
            err += "Направление подготовки ";
            eddWayField.setEffect(ds);
        }
        try {
            if (proffField.getText().length() == 0) {
                err += "профессия ";
                proffField.setEffect(ds);
            }
        } catch (Exception ex) {
            err += "профессия ";
            proffField.setEffect(ds);
        }
//        try {
//            if (eddNameField.getText().length() == 0) {
//                err += "Название направления подготовки ";
//                eddNameField.setEffect(ds);
//            }
//        } catch (Exception ex) {
//            err += "профессия ";
//            eddNameField.setEffect(ds);
//        }

        if (err.length() != 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Поля пусты");
            alert.setContentText(err);
            alert.showAndWait();

        } else {

            json.setPage(pageField.getText().trim().toLowerCase());
            json.setEddWay(eddWayField.getText().trim().toLowerCase());
            json.setProffName(proffField.getText().trim().toLowerCase());
//            json.setEddName(this.eddNameField.getText().trim().toLowerCase());
            gridPane.setUserData(json);
            Stage stage = (Stage) gridPane.getScene().getWindow();
            stage.close();

        }

        switch (type) {
            case "university": {
                json.jsonCreate("university.json");
                break;

            }
            case "college": {
                json.jsonCreate("college.json");
                break;

            }

        }

    }

    @FXML
    protected void cancelButton(ActionEvent event) throws IOException {
        Stage stage = (Stage) gridPane.getScene().getWindow();
        stage.close();

    }

}
