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
import model.entities.Salesperson;
import model.exceptions.ValidationException;
import model.services.SalespersonService;

import java.net.URL;
import java.util.*;

public class SalespersonFormController implements Initializable {

    private Salesperson salesperson;

    private SalespersonService salespersonService;

    private final List<DataChangeListener> DATA_CHANGE_LISTENERS = new ArrayList<>();

    public void setSalesperson(Salesperson salesperson) {

        this.salesperson = salesperson;
    }

    public void setSalespersonService(SalespersonService salespersonService) {

        this.salespersonService = salespersonService;
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

        if (salesperson == null) throw new IllegalStateException("Salesperson is null.");

        idTextField.setText(String.valueOf(salesperson.getId()));
        nameTextField.setText(salesperson.getName());
    }

    public void subscribeDataChangeListener(DataChangeListener dataChangeListener) {

        DATA_CHANGE_LISTENERS.add(dataChangeListener);
    }

    private void setErrorMessages(Map<String, String> errors) {

        Set<String> fields = errors.keySet();

        if (fields.contains("Name")) {

            errorLabel.setText(errors.get("Name"));
        }
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
