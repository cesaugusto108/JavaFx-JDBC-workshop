package gui;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;

import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentListController implements Initializable {

    @FXML
    private Button buttonNew;

    @FXML
    private TableView<Department> departmentTableView;

    @FXML
    private TableColumn<Department, Integer> departmentIdTableColumn;

    @FXML
    private TableColumn<Department, String> departmentNameTableColumn;

    @FXML
    public void onButtonNewAction() {
        System.out.println("Working");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        departmentIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        departmentNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));

        Stage stage = (Stage) Main.getMainScene().getWindow();
        departmentTableView.prefHeightProperty().bind(stage.heightProperty());
    }
}
