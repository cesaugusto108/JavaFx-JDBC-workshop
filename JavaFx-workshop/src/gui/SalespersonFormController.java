package gui;

import db.DBException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Salesperson;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SalespersonService;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class SalespersonFormController implements Initializable {

    private Salesperson salesperson;

    private SalespersonService salespersonService;

    private DepartmentService departmentService;

    private final List<DataChangeListener> DATA_CHANGE_LISTENERS = new ArrayList<>();

    public void setSalesperson(Salesperson salesperson) {

        this.salesperson = salesperson;
    }

    public void setServices(SalespersonService salespersonService, DepartmentService departmentService) {

        this.salespersonService = salespersonService;
        this.departmentService = departmentService;
    }

    @FXML
    private TextField idTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private DatePicker BirthdateDatePicker;

    @FXML
    private TextField baseSalaryTextField;

    @FXML
    private ComboBox<Department> departmentComboBox;

    @FXML
    private Label nameErrorLabel;

    @FXML
    private Label emailErrorLabel;

    @FXML
    private Label birthdateErrorLabel;

    @FXML
    private Label baseSalaryErrorLabel;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private ObservableList<Department> observableList;

    @FXML
    public void setSaveButtonAction(ActionEvent event) {

        if (salesperson == null) {

            throw new IllegalStateException("Salesperson is null.");
        }

        if (salespersonService == null) {

            throw new IllegalStateException("SalespersonService is null.");
        }
        try {

            salesperson = getFormData();
            salespersonService.saveOrUpdate(salesperson);
            Utils.currentStage(event).close();
            notifyDataChangeListeners();
        } catch (DBException e) {

            Alerts.showAlert("Error saving data", null, e.getMessage(), Alert.AlertType.ERROR);
        } catch (ValidationException e) {

            setErrorMessages(e.getERRORS());
        }
    }

    private void notifyDataChangeListeners() {

        for (DataChangeListener dataChangeListener : DATA_CHANGE_LISTENERS) {

            dataChangeListener.updateDataChanged();
        }
    }

    public Salesperson getFormData() {

        Salesperson dep = new Salesperson();
        ValidationException validationException = new ValidationException("Validation error");

        dep.setId(Utils.stringParseInt(idTextField.getText()));

        // Name
        if (nameTextField.getText() == null || nameTextField.getText().trim().equals("")) {

            validationException.addError("Name", "Field must not be empty.");
        }
        dep.setName(nameTextField.getText());

        // Email
        if (emailTextField.getText() == null || emailTextField.getText().trim().equals("")) {

            validationException.addError("Email", "Field must not be empty.");
        }
        dep.setEmail(emailTextField.getText());

        //Birthdate
        if (BirthdateDatePicker.getValue() == null) {

            validationException.addError("Birthdate", "Field must not be empty.");
        } else {

            Instant instant = Instant.from(BirthdateDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()));
            dep.setBirthdate(Date.from(instant));
        }

        // Base salary
        if (baseSalaryTextField.getText() == null || baseSalaryTextField.getText().trim().equals("")) {

            validationException.addError("Base salary", "Field must not be empty.");
        }
        dep.setBaseSalary(Utils.stringParseDouble(baseSalaryTextField.getText()));

        // Department
        dep.setDepartment(departmentComboBox.getValue());

        if (validationException.getERRORS().size() > 0) {

            throw validationException;
        }

        return dep;
    }

    @FXML
    public void setCancelButtonAction(ActionEvent event) {

        Utils.currentStage(event).close();
    }

    public void updateFormData() {

        if (salesperson == null) {

            throw new IllegalStateException("Salesperson is null.");
        };

        idTextField.setText(String.valueOf(salesperson.getId()));
        nameTextField.setText(salesperson.getName());
        emailTextField.setText(salesperson.getEmail());

        if (salesperson.getBirthdate() != null) {

            BirthdateDatePicker.setValue(LocalDate.ofInstant(salesperson.getBirthdate().toInstant(), ZoneId.systemDefault()));
        }

        Locale.setDefault(Locale.US);
        baseSalaryTextField.setText(String.format("%.2f", salesperson.getBaseSalary()));

        if (salesperson.getDepartment() == null) {

            departmentComboBox.getSelectionModel().selectFirst();
        } else {

            departmentComboBox.setValue(salesperson.getDepartment());
        }
    }

    public void loadAssociatedObjects() {

        if (departmentService == null) {

            throw new IllegalStateException("Department Service is null.");
        }

        List<Department> list = departmentService.findAll();
        observableList = FXCollections.observableArrayList(list);
        departmentComboBox.setItems(observableList);
    }

    private void initializeComboBoxDepartment() {

        Callback<ListView<Department>, ListCell<Department>> factory = listView -> new ListCell<>() {

            @Override
            protected void updateItem(Department department, boolean empty) {

                super.updateItem(department, empty);

                setText(empty ? "" : department.getName());
            }
        };

        departmentComboBox.setCellFactory(factory);
        departmentComboBox.setButtonCell(factory.call(null));
    }

    public void subscribeDataChangeListener(DataChangeListener dataChangeListener) {

        DATA_CHANGE_LISTENERS.add(dataChangeListener);
    }

    private void setErrorMessages(Map<String, String> errors) {

        Set<String> fields = errors.keySet();

        // Name
        nameErrorLabel.setText(fields.contains("Name") ? errors.get("Name") : "");

        // Email
        emailErrorLabel.setText(fields.contains("Email") ? errors.get("Email") : "");

        // Birthdate
        birthdateErrorLabel.setText(fields.contains("Birthdate") ? errors.get("Birthdate") : "");

        // Base salary
        baseSalaryErrorLabel.setText(fields.contains("Base salary") ? errors.get("Base salary") : "");
    }

    private void initializeNodes() {

        Constraints.setTextFieldInteger(idTextField);
        Constraints.setTextFieldMaxLength(nameTextField, 80);
        Constraints.setTextFieldDouble(baseSalaryTextField);
        Utils.formatDatePicker(BirthdateDatePicker, "MM/dd/yyyy");
        initializeComboBoxDepartment();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeNodes();
    }
}
