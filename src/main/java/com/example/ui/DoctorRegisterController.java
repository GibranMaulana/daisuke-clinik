package com.example.ui;

import com.example.data.DoctorDAO;
import com.example.model.Doctor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class DoctorRegisterController {
    @FXML private TextField nameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField specialtyField;
    @FXML private Label statusLabel;

    private final DoctorDAO doctorDAO = new DoctorDAO();

    @FXML
    private void onRegisterDoctorClicked(ActionEvent event) {
        String name = nameField.getText().trim();
        String pwd  = passwordField.getText().trim();
        String spec = specialtyField.getText().trim();

        if (name.isEmpty() || pwd.isEmpty() || spec.isEmpty()) {
            statusLabel.setText("All fields are required.");
            return;
        }

        // Generate a random 4-digit doctor ID (1000–9999)
        int newDoctorId = ThreadLocalRandom.current().nextInt(1000, 10_000);

        // Make sure it’s not already taken; if taken, regenerate until unique
        while (doctorDAO.findById(newDoctorId) != null) {
            newDoctorId = ThreadLocalRandom.current().nextInt(1000, 10_000);
        }

        // Create and save doctor
        Doctor d = new Doctor(newDoctorId, name, pwd, spec);
        doctorDAO.registerDoctor(d);

        // Show success in statusLabel
        statusLabel.setStyle("-fx-text-fill: #00ff00; -fx-font-size: 14px;");
        statusLabel.setText("Registered! Your Doctor ID is " + newDoctorId);

        // Optionally: disable fields & button to prevent double‐click
        nameField.setDisable(true);
        passwordField.setDisable(true);
        specialtyField.setDisable(true);
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
