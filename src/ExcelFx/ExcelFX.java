/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExcelFx;

import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author seryo
 */
public class ExcelFX extends Application {

    private Stage prStage;
    private Scene scene;
    private Parent root;

    @Override
    public void start(Stage stage) throws Exception {

        this.prStage = stage;
        this.prStage.setTitle("ExcelFX");
        initRootLayout();

    }

    public void initRootLayout() throws IOException {

        root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        scene = new Scene(root);
        prStage.setScene(scene);
        prStage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);

    }

}
