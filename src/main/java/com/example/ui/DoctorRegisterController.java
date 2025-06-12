package com.example.ui;

// Doctor registration controller for pending registration system
import com.example.data.DoctorDAO;
import com.example.data.PendingDoctorRegistrationDAO;
import com.example.model.Doctor;
import com.example.model.PendingDoctorRegistration;
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

public class DoctorRegisterController {
    @FXML private TextField nameField;
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField specialtyField;
    @FXML private Label statusLabel;

    private final DoctorDAO doctorDAO = new DoctorDAO();
    private final PendingDoctorRegistrationDAO pendingDAO = new PendingDoctorRegistrationDAO();

    @FXML
    private void onRegisterDoctorClicked(ActionEvent event) {
        String name  = nameField.getText().trim();
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String pwd   = passwordField.getText().trim();
        String spec  = specialtyField.getText().trim();

        if (name.isEmpty() || username.isEmpty() || email.isEmpty() || pwd.isEmpty() || spec.isEmpty()) {
            statusLabel.setText("All fields are required.");
            statusLabel.setStyle("-fx-text-fill: #DC3545; -fx-font-size: 14px;");
            return;
        }

        // Basic email validation
        if (!isValidEmail(email)) {
            statusLabel.setText("Please enter a valid email address.");
            statusLabel.setStyle("-fx-text-fill: #DC3545; -fx-font-size: 14px;");
            return;
        }

        // Password validation
        if (pwd.length() < 6) {
            statusLabel.setText("Password must be at least 6 characters long.");
            statusLabel.setStyle("-fx-text-fill: #DC3545; -fx-font-size: 14px;");
            return;
        }

        // Generate a random 4-digit doctor ID (1000–9999)
        int newDoctorId = ThreadLocalRandom.current().nextInt(1000, 10_000);

        // Make sure it's not already taken in doctors or pending registrations
        while (doctorDAO.findById(newDoctorId) != null || pendingDAO.isDoctorPending(newDoctorId)) {
            newDoctorId = ThreadLocalRandom.current().nextInt(1000, 10_000);
        }

        // Create doctor with separate username and email
        Doctor doctor = new Doctor(newDoctorId, username, pwd, name, email, spec);
        
        // Create pending doctor registration instead of directly adding to doctors
        PendingDoctorRegistration pendingRegistration = new PendingDoctorRegistration(doctor);
        pendingDAO.addPendingRegistration(pendingRegistration);

        // Show success message indicating admin verification is needed
        statusLabel.setStyle("-fx-text-fill: #28A745; -fx-font-size: 14px;");
        statusLabel.setText("Registered successfully! Your Doctor ID is " + newDoctorId + 
                           ". Please wait for admin verification before logging in.");

        // Disable fields to prevent double registration
        nameField.setDisable(true);
        usernameField.setDisable(true);
        emailField.setDisable(true);
        passwordField.setDisable(true);
        specialtyField.setDisable(true);
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
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/ui/doctor-login.fxml"));
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
