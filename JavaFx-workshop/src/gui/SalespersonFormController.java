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
    private DatePicker birthDateDatePicker;

    @FXML
    private TextField baseSalaryTextField;

    @FXML
    private ComboBox<Department> departmentComboBox;

    @FXML
    private Label nameErrorLabel;

    @FXML
    private Label emailErrorLabel;

    @FXML
    private Label birthDateErrorLabel;

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

        if (nameTextField.getText() == null || nameTextField.getText().trim().equals("")) {

            validationException.addError("Name", "Field must not be empty.");
        }

        dep.setName(nameTextField.getText());

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

        if (salesperson.getBirthDate() != null) {

            birthDateDatePicker.setValue(LocalDate.ofInstant(salesperson.getBirthDate().toInstant(), ZoneId.systemDefault()));
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

        if (fields.contains("Name")) {

            nameErrorLabel.setText(errors.get("Name"));
        }
    }

    private void initializeNodes() {

        Constraints.setTextFieldInteger(idTextField);
        Constraints.setTextFieldMaxLength(nameTextField, 80);
        Constraints.setTextFieldDouble(baseSalaryTextField);
        Utils.formatDatePicker(birthDateDatePicker, "MM/dd/yyyy");
        initializeComboBoxDepartment();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeNodes();
    }
}
