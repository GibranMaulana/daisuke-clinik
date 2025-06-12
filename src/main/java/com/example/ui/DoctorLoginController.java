package com.example.ui;

import com.example.data.DoctorDAO;
import com.example.data.PendingDoctorRegistrationDAO;
import com.example.model.Doctor;
import com.example.model.LoginSession;
import com.example.data.LoginSessionDAO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class DoctorLoginController {
    @FXML private TextField doctorIdField;
    @FXML private PasswordField passwordField;

    private final DoctorDAO doctorDAO = new DoctorDAO();
    private final LoginSessionDAO sessionDAO = new LoginSessionDAO();
    private final PendingDoctorRegistrationDAO pendingDAO = new PendingDoctorRegistrationDAO();

    @FXML
    private void onLoginClicked(ActionEvent event) {
        String idText = doctorIdField.getText().trim();
        String pwd    = passwordField.getText().trim();

        if (idText.isEmpty() || pwd.isEmpty()) {
            System.out.println("ID and password are required.");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException ex) {
            System.out.println("Doctor ID must be an integer.");
            return;
        }

        Doctor d = doctorDAO.findById(id);
        if (d == null) {
            // Check if the doctor is in pending registrations
            if (pendingDAO.isDoctorPending(id)) {
                System.out.println("Your account is pending admin verification. Please wait for approval before logging in.");
                return;
            }
            System.out.println("No such doctor found.");
            return;
        }
        if (!d.getPassword().equals(pwd)) {
            System.out.println("Incorrect password.");
            return;
        }

        // Logged in successfully → set login time & proceed to dashboard
        d.setCurrentLoginTime(java.time.LocalDateTime.now());
        doctorDAO.updateDoctor(d);

        // You’d now load doctor-dashboard.fxml. For now, print to console:
        System.out.println("Doctor " + id + " logged in.");

        LoginSession session = new LoginSession(
                d.getId(),
                d.getName(),
                d.getCurrentLoginTime()
        );
        sessionDAO.addSession(session);

        CurrentDoctorHolder.setDoctor(d); // Store the logged-in doctor        

        try {
             FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ui/doctor_dashboard.fxml"));
            Parent root = loader.load();

            DoctorDashboardController ctrl = loader.getController();
            ctrl.setLoggedInDoctor(d);   // <-- THIS must run
            Stage stage = (Stage) doctorIdField.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onRegisterClicked(ActionEvent event) {
        // Load the registration screen
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/ui/doctor_register.fxml"));
            Stage stage = (Stage) doctorIdField.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onBackClicked(ActionEvent event) {
        // Load the login screen
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/ui/login_view.fxml"));
            Stage stage = (Stage) doctorIdField.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
