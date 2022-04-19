package gui;

import db.DBException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentService;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DepartmentFormController implements Initializable {

    private Department department;

    private DepartmentService departmentService;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @FXML
    private TextField idTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    public void onSaveButtonAction(ActionEvent event) {
        if (department == null) {
            throw new IllegalStateException("Department is null.");
        }

        if (departmentService == null) {
            throw new IllegalStateException("DeparmentService is null.");
        }
        try {
            department = getFormData();
            departmentService.saveOrUpdate(department);
            Utils.currentStage(event).close();
            notifyDataChangeListeners();
        } catch (DBException e) {
            Alerts.showAlert("Error saving data", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener dataChangeListener : dataChangeListeners) {
            dataChangeListener.onDataChanged();
        }
    }

    public Department getFormData() {
        Department dep = new Department();
        dep.setId(Utils.stringParseInt(idTextField.getText()));
        dep.setName(nameTextField.getText());

        return dep;
    }

    @FXML
    public void onCancelButtonAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }

    public void updateFormData() {
        if (department == null) throw new IllegalStateException("Department is null.");

        idTextField.setText(String.valueOf(department.getId()));
        nameTextField.setText(department.getName());
    }

    public void subscribeDataChangeListener(DataChangeListener dataChangeListener) {
        dataChangeListeners.add(dataChangeListener);
    }

    private void initializeNodes() {
        Constraints.setTextFieldInteger(idTextField);
        Constraints.setTextFieldMaxLength(nameTextField, 30);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }
}
