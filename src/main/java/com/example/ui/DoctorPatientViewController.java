package com.example.ui;

import com.example.data.PatientDAO;
import com.example.model.Patient;
import com.example.model.ds.CustomeBST;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for doctor-patient-view.fxml (search‐only version).
 * Doctors can search ANY patient by ID prefix or partial name.
 */
public class DoctorPatientViewController {

    @FXML private TextField searchIdField;
    @FXML private TextField searchNameField;

    @FXML private TableView<Patient> patientTable;
    @FXML private TableColumn<Patient, Integer> colPid;
    @FXML private TableColumn<Patient, String>  colPname;
    @FXML private TableColumn<Patient, Integer> colPage;
    @FXML private TableColumn<Patient, String>  colAddress;
    @FXML private TableColumn<Patient, String>  colPhone;

    @FXML private Label statusLabel;

    private final PatientDAO patientDAO = new PatientDAO();
    private CustomeBST<Patient> patientsBST;

    @FXML
    public void initialize() {
        // 1) Configure columns
        colPid.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colPname.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        colPage.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getAge()).asObject());
        colAddress.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAddress()));
        colPhone.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPhoneNumber()));

        // 2) Load all patients into BST
        patientsBST = patientDAO.getAllPatientsBST();

        // 3) Populate the table initially (no filters)
        applyFilters();

        // 4) Listen for changes in search fields
        ChangeListener<String> filterListener = (ObservableValue<? extends String> obs, String oldText, String newText) -> {
            applyFilters();
        };
        searchIdField.textProperty().addListener(filterListener);
        searchNameField.textProperty().addListener(filterListener);

        statusLabel.setText("Ready");
    }

    // Re‐traverse BST, only show patients whose ID starts with searchIdField
    // AND whose name contains searchNameField (case‐insensitive).
    private void applyFilters() {
        String idPrefix = searchIdField.getText().trim();
        String namePart = searchNameField.getText().trim().toLowerCase();

        ObservableList<Patient> matches = FXCollections.observableArrayList();

        patientsBST.inOrderTraversal(p -> {
            String idStr = Integer.toString(p.getId());
            String nameLower = p.getName().toLowerCase();

            boolean idMatches = idPrefix.isEmpty() || idStr.startsWith(idPrefix);
            boolean nameMatches = namePart.isEmpty() || nameLower.contains(namePart);

            if (idMatches && nameMatches) {
                matches.add(p);
            }
        });

        patientTable.setItems(matches);
        statusLabel.setText("Showing " + matches.size() + " patients");
    }

    @FXML
    private void onBackClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ui/doctor_dashboard.fxml"));
            Parent root = loader.load();
            DoctorDashboardController dashCtrl = loader.getController();
            dashCtrl.setLoggedInDoctor(CurrentDoctorHolder.getDoctor());
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
