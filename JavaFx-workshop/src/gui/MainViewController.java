package gui;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    @FXML
    private MenuItem salespersonMenuItem;

    @FXML
    private MenuItem departmentMenuItem;

    @FXML
    private MenuItem aboutMenuItem;

    @FXML
    public void onSalespersonMenuItemAction() {

    }

    @FXML
    public void onDepartmentMenuItemAction() {
        loadView("/gui/DepartmentList.fxml");
    }

    @FXML
    public void onAboutMenuItemAction() {
        loadView("/gui/About.fxml");
    }

    @FXML
    public  synchronized void loadView(String path) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
        try {
            VBox vBox = fxmlLoader.load();

            Scene mainScene = Main.getMainScene();
            VBox mainVbox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
            Node mainMenu = mainVbox.getChildren().get(0);
            mainVbox.getChildren().clear();
            mainVbox.getChildren().add(mainMenu);
            mainVbox.getChildren().addAll(vBox.getChildren());
        } catch (IOException e) {
            Alerts.showAlert("IOException", "Error loading view.", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
