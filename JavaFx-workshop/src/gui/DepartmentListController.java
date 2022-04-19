package gui;

import application.Main;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DepartmentListController implements Initializable, DataChangeListener {

    private DepartmentService departmentService;

    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @FXML
    private Button buttonNew;

    @FXML
    private TableView<Department> departmentTableView;

    @FXML
    private TableColumn<Department, Integer> departmentIdTableColumn;

    @FXML
    private TableColumn<Department, String> departmentNameTableColumn;

    private ObservableList<Department> observableList;

    @FXML
    public void onButtonNewAction(ActionEvent actionEvent) {
        Stage parentStage = Utils.currentStage(actionEvent);
        Department department = new Department();
        createDialogForm(department, "/gui/DepartmentForm.fxml", parentStage);
    }

    public void updateTableView() {
        if (departmentService == null) throw new IllegalStateException("Service is null");

        List<Department> list = departmentService.findAll();
        observableList = FXCollections.observableArrayList(list);
        departmentTableView.setItems(observableList);
    }

    private void createDialogForm(Department department, String path, Stage parentStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
            Pane pane = fxmlLoader.load();

            DepartmentFormController departmentFormController = fxmlLoader.getController();
            departmentFormController.setDepartment(department);
            departmentFormController.setDepartmentService(new DepartmentService());
            departmentFormController.subscribeDataChangeListener(this);
            departmentFormController.updateFormData();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter department data");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();
        } catch (IOException e) {
            Alerts.showAlert("IO Exception", "Error loading view.", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void initializeNodes() {
        departmentIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        departmentNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));

        Stage stage = (Stage) Main.getMainScene().getWindow();
        departmentTableView.prefHeightProperty().bind(stage.heightProperty());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }
}
