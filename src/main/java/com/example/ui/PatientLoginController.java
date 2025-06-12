package com.example.ui;

import com.example.data.PatientDAO;
import com.example.model.Patient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class PatientLoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;
    
    private final PatientDAO patientDAO = new PatientDAO();

    @FXML
    private void onLoginClicked(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter both username and password.");
            statusLabel.setStyle("-fx-text-fill: #DC3545; -fx-font-size: 14px;");
            return;
        }

        try {
            // Find patient by username
            Patient patient = findPatientByUsername(username);
            
            if (patient == null) {
                statusLabel.setText("No account found with this username.");
                statusLabel.setStyle("-fx-text-fill: #DC3545; -fx-font-size: 14px;");
                return;
            }

            // Verify password
            if (!password.equals(patient.getPassword())) {
                statusLabel.setText("Incorrect password. Please try again.");
                statusLabel.setStyle("-fx-text-fill: #DC3545; -fx-font-size: 14px;");
                return;
            }

            // Successful login
            statusLabel.setText("Login successful! Redirecting to dashboard...");
            statusLabel.setStyle("-fx-text-fill: #28A745; -fx-font-size: 14px;");

            // Store patient info for the session
            CurrentPatientHolder.setCurrentPatient(patient);

            // Navigate to patient dashboard
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/com/example/ui/patient_dashboard.fxml"));
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.getScene().setRoot(root);
            } catch (IOException e) {
                statusLabel.setText("Error loading dashboard. Please try again.");
                statusLabel.setStyle("-fx-text-fill: #DC3545; -fx-font-size: 14px;");
                e.printStackTrace();
            }

        } catch (Exception e) {
            statusLabel.setText("Login error. Please try again.");
            statusLabel.setStyle("-fx-text-fill: #DC3545; -fx-font-size: 14px;");
            e.printStackTrace();
        }
    }

    /**
     * Find patient by username
     */
    private Patient findPatientByUsername(String username) {
        try {
            for (Patient patient : patientDAO.getAllPatients()) {
                if (username.equals(patient.getUsername())) {
                    return patient;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @FXML
    private void onRegisterClicked(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/ui/patient_register.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onBackClicked(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/ui/login_view.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onButtonHover(ActionEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle(button.getStyle() + "; -fx-opacity: 0.9;");
    }

    @FXML
    private void onButtonExited(ActionEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle(button.getStyle().replace("; -fx-opacity: 0.9;", ""));
    }
}
