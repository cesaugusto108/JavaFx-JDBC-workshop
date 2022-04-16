package application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends javafx.application.Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));

            Scene mainScene = new Scene(fxmlLoader.load());
            stage.setScene(mainScene);
            stage.setTitle("JavaFx Application");
            stage.show();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
