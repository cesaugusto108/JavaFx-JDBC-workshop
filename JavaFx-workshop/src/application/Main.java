package application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Main extends javafx.application.Application {

    private static Scene mainScene;

    public static Scene getMainScene() {
        return mainScene;
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));

            ScrollPane scrollPane = fxmlLoader.load();
            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(true);

            mainScene = new Scene(scrollPane);
            stage.setScene(mainScene);
            stage.setTitle("JavaFx Application");
            stage.show();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
