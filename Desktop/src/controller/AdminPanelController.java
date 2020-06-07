package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminPanelController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Menu bicyclesMenu;

    @FXML
    private Menu activitiesMenu;

    @FXML
    private Menu settingsMenu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bicyclesMenu.setOnAction(e -> startView("bicycles.fxml"));
        activitiesMenu.setOnAction(e -> startView("activities.fxml"));
        settingsMenu.setOnAction(e -> startView("settings.fxml"));

        startView("bicycles.fxml");
    }

    private void startView(String filename) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/" + filename));
            //loader.setController(this);
            borderPane.setCenter(loader.load());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
