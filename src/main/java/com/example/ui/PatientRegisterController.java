package com.example.ui;

import com.example.data.PatientDAO;
import com.example.data.DoctorDAO;
import com.example.model.Patient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class PatientRegisterController {
    @FXML private TextField nameField;
    @FXML private TextField ageField;
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField addressField;
    @FXML private TextField phoneField;
    @FXML private Label statusLabel;

    private final PatientDAO patientDAO = new PatientDAO();
    private final DoctorDAO doctorDAO = new DoctorDAO();

    @FXML
    private void onRegisterPatientClicked(ActionEvent event) {
        String fullname = nameField.getText().trim();
        String ageText = ageField.getText().trim();
        String username = usernameField.getText().trim();
        String email   = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String address = addressField.getText().trim();
        String phone   = phoneField.getText().trim();

        if (fullname.isEmpty() || ageText.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            statusLabel.setText("All fields are required.");
            statusLabel.setStyle("-fx-text-fill: #DC3545; -fx-font-size: 14px;");
            return;
        }

        // Enhanced email validation
        if (!isValidEmail(email)) {
            statusLabel.setText("Please enter a valid email address.");
            statusLabel.setStyle("-fx-text-fill: #DC3545; -fx-font-size: 14px;");
            return;
        }

        // Password validation
        if (password.length() < 6) {
            statusLabel.setText("Password must be at least 6 characters long.");
            statusLabel.setStyle("-fx-text-fill: #DC3545; -fx-font-size: 14px;");
            return;
        }

        // Check for duplicate username across patients and doctors
        if (patientDAO.findByUsername(username) != null || doctorDAO.findByUsername(username) != null) {
            statusLabel.setText("Username is already taken.");
            statusLabel.setStyle("-fx-text-fill: #DC3545; -fx-font-size: 14px;");
            return;
        }

        // Check for duplicate email across patients and doctors
        if (patientDAO.findByEmail(email) != null || doctorDAO.findByEmail(email) != null) {
            statusLabel.setText("Email is already registered.");
            statusLabel.setStyle("-fx-text-fill: #DC3545; -fx-font-size: 14px;");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageText);
            if (age <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            statusLabel.setText("Age must be a positive integer.");
            statusLabel.setStyle("-fx-text-fill: #DC3545; -fx-font-size: 14px;");
            return;
        }

        // Generate a random 7-digit patient ID (1_000_000 – 9_999_999)
        int newPatientId = ThreadLocalRandom.current().nextInt(1_000_000, 10_000_000);

        // Ensure uniqueness
        while (patientDAO.findById(newPatientId) != null) {
            newPatientId = ThreadLocalRandom.current().nextInt(1_000_000, 10_000_000);
        }

        // Create patient with separate username and email
        Patient p = new Patient(newPatientId, username, password, fullname, email, age, address, phone);
        patientDAO.registerPatient(p);

        CurrentPatientHolder.setPatientId(newPatientId);

        statusLabel.setText("Registration successful! Patient ID: " + newPatientId);
        statusLabel.setStyle("-fx-text-fill: #28A745; -fx-font-size: 14px;");

        // Load the appointment screen
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/ui/patient_appointment.fxml"));
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Optionally disable fields to prevent re‐submitting
        nameField.setDisable(true);
        ageField.setDisable(true);
        usernameField.setDisable(true);
        emailField.setDisable(true);
        passwordField.setDisable(true);
        addressField.setDisable(true);
        phoneField.setDisable(true);
    }

    // Enhanced email validation method with regex
    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        // RFC 5322 compliant email regex pattern
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
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
