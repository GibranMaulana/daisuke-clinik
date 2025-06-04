package com.example.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private void onDoctorClicked(ActionEvent event) {
        // Load doctor-login.fxml
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/ui/doctor-login.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onPatientClicked(ActionEvent event) {
        // Load patient-register.fxml
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/ui/patient_register.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
