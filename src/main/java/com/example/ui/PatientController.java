package com.example.ui;

import com.example.data.PatientSearchTreeManagement;
import com.example.model.Patient;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class PatientController {

    // ───────────────────────────
    // Form fields (Center → GridPane)
    // ───────────────────────────
    @FXML private TextField addIdField;
    @FXML private TextField addNameField;
    @FXML private TextField addAgeField;
    @FXML private TextField addAddressField;
    @FXML private TextField addPhoneField;
    @FXML private Button addButton;

    // ───────────────────────────
    // Patient TableView (Center)
    // ───────────────────────────
    @FXML private TableView<Patient> patientTable;
    @FXML private TableColumn<Patient, String> colId;
    @FXML private TableColumn<Patient, String> colName;
    @FXML private TableColumn<Patient, Integer> colAge;
    @FXML private TableColumn<Patient, String> colAddress;
    @FXML private TableColumn<Patient, String> colPhone;

    // ───────────────────────────
    // Search (Right)
    // ───────────────────────────
    @FXML private TextField searchIdField;
    @FXML private ListView<String> searchResultsListView;

    // ───────────────────────────
    // Footer (Bottom)
    // ───────────────────────────
    @FXML private Label footerLabel;

    private final PatientSearchTreeManagement bstMgr = PatientSearchTreeManagement.getInstance();
    private ObservableList<Patient> tableData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // 1) Configure TableView columns
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        // 2) Load initial patient data into tableData and bind to table
        refreshInOrderTable();
        patientTable.setItems(tableData);

        // 3) Live search as you type
        searchIdField.textProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(
                    ObservableValue<? extends String> obs,
                    String oldValue,
                    String newValue
            ) {
                filterByIdPrefix(newValue);
            }
        });

        // 4) Initialize search results empty
        filterByIdPrefix("");

        // 5) Footer initial text
        footerLabel.setText("Ready");
    }

    @FXML
    private void handleAddPatient() {
        String id = addIdField.getText().trim();
        String name = addNameField.getText().trim();
        String ageText = addAgeField.getText().trim();
        String address = addAddressField.getText().trim();
        String phone = addPhoneField.getText().trim();

        if (id.isEmpty() || name.isEmpty() || ageText.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            showAlert(AlertType.WARNING, "All fields are required to add a patient.");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            showAlert(AlertType.WARNING, "Age must be a valid integer.");
            return;
        }

        Patient newPatient = new Patient(id, name, age, address, phone);
        if (bstMgr.searchPatient(id) != null) {
            showAlert(AlertType.ERROR, "A patient with ID \"" + id + "\" already exists.");
            return;
        }

        try {
            bstMgr.insertPatient(newPatient);
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert(AlertType.ERROR, "Failed to add patient:\n" + ex.getMessage());
            return;
        }

        addIdField.clear();
        addNameField.clear();
        addAgeField.clear();
        addAddressField.clear();
        addPhoneField.clear();

        refreshInOrderTable();
        filterByIdPrefix(searchIdField.getText().trim());
        footerLabel.setText("Patient \"" + id + "\" added successfully.");
    }

    private void refreshInOrderTable() {
        tableData.clear();
        List<Patient> sorted = bstMgr.inOrderDisplay();
        tableData.addAll(sorted);
    }

    private void filterByIdPrefix(String prefix) {
        prefix = prefix.trim();
        searchResultsListView.getItems().clear();
        if (prefix.isEmpty()) {
            return;
        }
        for (Patient p : bstMgr.inOrderDisplay()) {
            if (p.getId().startsWith(prefix)) {
                searchResultsListView.getItems().add(p.getId() + "  |  " + p.getName());
            }
        }
        if (searchResultsListView.getItems().isEmpty()) {
            searchResultsListView.getItems().add("No matches for \"" + prefix + "\".");
        }
    }

    private void showAlert(AlertType type, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
