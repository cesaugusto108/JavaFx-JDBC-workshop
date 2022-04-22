package gui;

import application.Main;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Salesperson;
import model.services.SalespersonService;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class SalespersonListController implements Initializable, DataChangeListener {

    private SalespersonService salespersonService;

    public void setSalespersonService(SalespersonService salespersonService) {

        this.salespersonService = salespersonService;
    }

    @FXML
    private Button buttonNew;

    @FXML
    private TableView<Salesperson> salespersonTableView;

    @FXML
    private TableColumn<Salesperson, Integer> salespersonIdTableColumn;

    @FXML
    private TableColumn<Salesperson, String> salespersonNameTableColumn;

    @FXML
    private TableColumn<Salesperson, String> salespersonEmailTableColumn;

    @FXML
    private TableColumn<Salesperson, Date> salespersonBirthDateTableColumn;

    @FXML
    private TableColumn<Salesperson, Double> salespersonBaseSalaryTableColumn;

    @FXML
    private TableColumn<Salesperson, Salesperson> tableColumnEdit;

    @FXML
    private TableColumn<Salesperson, Salesperson> tableColumnRemove;

    private ObservableList<Salesperson> observableList;

    @FXML
    public void setButtonNewAction(ActionEvent actionEvent) {

        Stage parentStage = Utils.currentStage(actionEvent);
        Salesperson salesperson = new Salesperson();
        createDialogForm(salesperson, "/gui/SalespersonForm.fxml", parentStage);
    }

    public void updateTableView() {

        if (salespersonService == null) throw new IllegalStateException("Service is null");

        List<Salesperson> list = salespersonService.findAll();
        observableList = FXCollections.observableArrayList(list);
        salespersonTableView.setItems(observableList);
        initEditButtons();
        initRemoveButtons();
    }

    private void createDialogForm(Salesperson salesperson, String path, Stage parentStage) {

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
            Pane pane = fxmlLoader.load();

            SalespersonFormController salespersonFormController = fxmlLoader.getController();
            salespersonFormController.setSalesperson(salesperson);
            salespersonFormController.setSalespersonService(new SalespersonService());
            salespersonFormController.subscribeDataChangeListener(this);
            salespersonFormController.updateFormData();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter salesperson data");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();
        } catch (IOException e) {

            Alerts.showAlert("IO Exception", "Error loading view.", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void removeEntity(Salesperson salesperson) {

        Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Do you really wish to delete this item?");

        if (result.get() == ButtonType.OK) {

            if (salespersonService == null) {

                throw new IllegalStateException("Salesperson Service is null.");
            }

            try {

                salespersonService.remove(salesperson);
                updateTableView();
            } catch (Exception e) {

                Alerts.showAlert("Error removing object", null, e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @Override
    public void updateDataChanged() {

        updateTableView();
    }

    private void initEditButtons() {

        tableColumnEdit.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue()));
        tableColumnEdit.setCellFactory(x -> new TableCell<Salesperson, Salesperson>() {

            private final Button BUTTON = new Button("Edit");

            @Override
            protected void updateItem(Salesperson salesperson, boolean empty) {

                super.updateItem(salesperson, empty);

                if (salesperson == null) {

                    setGraphic(null);

                    return;
                }

                setGraphic(BUTTON);
                BUTTON.setOnAction(event -> createDialogForm(salesperson, "/gui/SalespersonForm.fxml", Utils.currentStage(event)));
            }
        });
    }

    private void initRemoveButtons() {

        tableColumnRemove.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue()));
        tableColumnRemove.setCellFactory(x -> new TableCell<Salesperson, Salesperson>() {
            private final Button BUTTON = new Button("Remove");

            @Override
            protected void updateItem(Salesperson salesperson, boolean empty) {

                super.updateItem(salesperson, empty);

                if (salesperson == null) {

                    setGraphic(null);

                    return;
                }

                setGraphic(BUTTON);
                BUTTON.setOnAction(event -> removeEntity(salesperson));
            }
        });
    }

    private void initializeNodes() {

        salespersonIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        salespersonNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        salespersonEmailTableColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        salespersonBirthDateTableColumn.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        Utils.formatTableColumnDate(salespersonBirthDateTableColumn, "MM/dd/yyyy");

        salespersonBaseSalaryTableColumn.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
        Utils.formatTableColumnDouble(salespersonBaseSalaryTableColumn, 2);

        Stage stage = (Stage) Main.getMainScene().getWindow();
        salespersonTableView.prefHeightProperty().bind(stage.heightProperty());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeNodes();
    }
}
