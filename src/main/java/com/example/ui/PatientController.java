package com.example.ui;

import com.example.data.PatientDAO;
import com.example.model.Patient;
import com.example.model.ds.CustomeLinkedList;
import com.example.model.ds.CustomeBST;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.concurrent.ThreadLocalRandom;

/**
 * PatientController (auto‐generate int ID)
 */
public class PatientController {

    // ─────────────────────────────────────────────────────────────────────────
    // 1) FXML fields (no addIdField now)
    // ─────────────────────────────────────────────────────────────────────────

    @FXML private TextField addNameField;
    @FXML private TextField addAgeField;
    @FXML private TextField addAddressField;
    @FXML private TextField addPhoneField;
    @FXML private Button addButton;

    @FXML private TableView<Patient> patientTable;
    @FXML private TableColumn<Patient, Integer> colId;
    @FXML private TableColumn<Patient, String> colName;
    @FXML private TableColumn<Patient, Integer> colAge;
    @FXML private TableColumn<Patient, String> colAddress;
    @FXML private TableColumn<Patient, String> colPhone;

    @FXML private TextField searchIdField;
    @FXML private ListView<String> searchResultsList;

    @FXML private Label footerLabel;

    // ─────────────────────────────────────────────────────────────────────────
    // 2) DAO & in‐memory BST
    // ─────────────────────────────────────────────────────────────────────────

    private final PatientDAO patientDAO = new PatientDAO();
    private CustomeBST<Patient> patientsBST;

    // ─────────────────────────────────────────────────────────────────────────
    // 3) Initialize: set up columns and load existing patients
    // ─────────────────────────────────────────────────────────────────────────
    @FXML
    public void initialize() {
        // Configure TableView columns
        colId.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colName.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        colAge.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getAge()).asObject());
        colAddress.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAddress()));
        colPhone.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPhoneNumber()));

        // Load all patients from DAO into BST
        patientsBST = patientDAO.getAllPatientsBST();

        // Populate the main TableView
        refreshPatientTable();

        // Listen for changes in the “ID prefix” field
        searchIdField.textProperty().addListener((obs, oldText, newText) -> {
            onSearchKeyTyped(newText);
        });

        footerLabel.setText("Ready");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 4) Add Patient Button Handler (auto‐generate ID)
    // ─────────────────────────────────────────────────────────────────────────
    @FXML
    private void onAddPatientClicked(ActionEvent event) {
        String name        = addNameField.getText().trim();
        String ageText     = addAgeField.getText().trim();
        String address     = addAddressField.getText().trim();
        String phoneNumber = addPhoneField.getText().trim();

        if (name.isEmpty() || ageText.isEmpty() || address.isEmpty() || phoneNumber.isEmpty()) {
            footerLabel.setText("All fields are required.");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException ex) {
            footerLabel.setText("Age must be an integer.");
            return;
        }

        // Generate a random 7-digit ID (1_000_000 – 9_999_999)
        int newId = ThreadLocalRandom.current().nextInt(1_000_000, 10_000_000);
        // Ensure uniqueness
        while (patientDAO.findById(newId) != null) {
            newId = ThreadLocalRandom.current().nextInt(1_000_000, 10_000_000);
        }

        // Create and persist new Patient
        Patient newPatient = new Patient(newId, name, age, address, phoneNumber);
        patientDAO.registerPatient(newPatient);

        // Update in‐memory BST and refresh TableView
        patientsBST.insert(newPatient);
        refreshPatientTable();

        // Clear input fields & show success
        addNameField.clear();
        addAgeField.clear();
        addAddressField.clear();
        addPhoneField.clear();
        footerLabel.setText("Added patient with ID: " + newId);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 5) Refresh the main TableView from the BST (in‐order)
    // ─────────────────────────────────────────────────────────────────────────
    private void refreshPatientTable() {
        CustomeLinkedList<Patient> inOrderList = patientsBST.inOrderList();
        ObservableList<Patient> obs = FXCollections.observableArrayList();
        for (Patient p : inOrderList) {
            obs.add(p);
        }
        patientTable.setItems(obs);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 6) Live “search by ID prefix” as user types
    // ─────────────────────────────────────────────────────────────────────────
    private void onSearchKeyTyped(String newText) {
        searchResultsList.getItems().clear();
        String prefixText = newText.trim();
        if (prefixText.isEmpty()) {
            return;
        }

        // Ensure prefixText contains only digits
        for (char c : prefixText.toCharArray()) {
            if (!Character.isDigit(c)) {
                footerLabel.setText("Search prefix must be digits only.");
                return;
            }
        }

        // Build a CustomeLinkedList<String> of matches
        CustomeLinkedList<String> matches = new CustomeLinkedList<>();
        CustomeLinkedList<Patient> allPatients = patientsBST.inOrderList();
        for (Patient p : allPatients) {
            String idStr = Integer.toString(p.getId());
            if (idStr.startsWith(prefixText)) {
                matches.add(idStr + " | " + p.getName());
            }
        }

        // Transfer matches into an ObservableList<String>
        ObservableList<String> items = FXCollections.observableArrayList();
        for (String s : matches) {
            items.add(s);
        }
        searchResultsList.setItems(items);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 7) Handle clicking on a search result: scroll the TableView to that row
    // ─────────────────────────────────────────────────────────────────────────
    @FXML
    private void onSearchResultClicked() {
        String selected = searchResultsList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        // “selected” looks like “1234567 | Alice”
        String[] parts = selected.split("\\s+");
        int id = Integer.parseInt(parts[0]);

        // Find and select that patient in the TableView
        ObservableList<Patient> tableItems = patientTable.getItems();
        for (int i = 0; i < tableItems.size(); i++) {
            if (tableItems.get(i).getId() == id) {
                patientTable.getSelectionModel().select(i);
                patientTable.scrollTo(i);
                break;
            }
        }
    }
}
