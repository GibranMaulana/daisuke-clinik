package com.example.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
        // Load patient-register.fxml (for appointment booking without login)
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/ui/patient_register.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onPatientLoginClicked(ActionEvent event) {
        // Load patient login screen
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/ui/patient_login.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onAdminClicked(ActionEvent event) {
        // Load admin-login.fxml
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/ui/admin-login.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
