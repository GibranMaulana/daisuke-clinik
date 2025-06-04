package com.example.ui;

import com.example.data.PatientDAO;
import com.example.model.Patient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class PatientRegisterController {
    @FXML private TextField nameField;
    @FXML private TextField ageField;
    @FXML private TextField addressField;
    @FXML private TextField phoneField;
    @FXML private Label statusLabel;

    private final PatientDAO patientDAO = new PatientDAO();

    @FXML
    private void onRegisterPatientClicked(ActionEvent event) {
        String name    = nameField.getText().trim();
        String ageText = ageField.getText().trim();
        String address = addressField.getText().trim();
        String phone   = phoneField.getText().trim();

        if (name.isEmpty() || ageText.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            statusLabel.setText("All fields are required.");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageText);
            if (age <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            statusLabel.setText("Age must be a positive integer.");
            return;
        }

        // Generate a random 7-digit patient ID (1_000_000 – 9_999_999)
        int newPatientId = ThreadLocalRandom.current().nextInt(1_000_000, 10_000_000);

        // Ensure uniqueness
        while (patientDAO.findById(newPatientId) != null) {
            newPatientId = ThreadLocalRandom.current().nextInt(1_000_000, 10_000_000);
        }

        Patient p = new Patient(newPatientId, name, age, address, phone);
        patientDAO.registerPatient(p);

        statusLabel.setStyle("-fx-text-fill: #00ff00; -fx-font-size: 14px;");
        statusLabel.setText("Registered! Your Patient ID is " + newPatientId);

        // Optionally disable fields to prevent re‐submitting
        nameField.setDisable(true);
        ageField.setDisable(true);
        addressField.setDisable(true);
        phoneField.setDisable(true);
    }

    @FXML
    private void onBackClicked(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/ui/login_view.fxml"));
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
