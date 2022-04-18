package gui;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentFormController implements Initializable {

    private Department department;

    public void setDepartment(Department department) {
        this.department = department;
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
    public void onSaveButtonAction() {
        System.out.println("Good");
    }

    @FXML
    public void onCancelButtonAction() {
        System.out.println("Good");
    }

    public void updateFormData() {
        if (department == null) throw new IllegalStateException("Department is null.");

        idTextField.setText(String.valueOf(department.getId()));
        nameTextField.setText(department.getName());
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
