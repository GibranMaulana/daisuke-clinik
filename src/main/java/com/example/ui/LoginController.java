package com.example.ui;

import com.example.data.AdminService;
import com.example.data.DoctorDAO;
import com.example.data.PatientDAO;
import com.example.data.LoginSessionDAO;
import com.example.data.PendingDoctorRegistrationDAO;
import com.example.model.Admin;
import com.example.model.Doctor;
import com.example.model.Patient;
import com.example.model.LoginSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;

public class LoginController {
    
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;
    
    private final AdminService adminService = new AdminService();
    private final DoctorDAO doctorDAO = new DoctorDAO();
    private final PatientDAO patientDAO = new PatientDAO();
    private final LoginSessionDAO sessionDAO = new LoginSessionDAO();
    private final PendingDoctorRegistrationDAO pendingDAO = new PendingDoctorRegistrationDAO();

    @FXML
    private void onLoginClicked(ActionEvent event) {
        String usernameOrId = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (usernameOrId.isEmpty() || password.isEmpty()) {
            showStatus("Please enter both username/ID and password.", true);
            return;
        }

        // Try admin login first
        if (tryAdminLogin(usernameOrId, password, event)) {
            return;
        }

        // Try doctor login (by ID)
        if (tryDoctorLogin(usernameOrId, password, event)) {
            return;
        }

        // Try patient login (by username)
        if (tryPatientLogin(usernameOrId, password, event)) {
            return;
        }

        // If all fail
        showStatus("Invalid credentials. Please check your username/ID and password.", true);
    }

    private boolean tryAdminLogin(String username, String password, ActionEvent event) {
        try {
            Admin admin = adminService.authenticate(username, password);
            if (admin != null) {
                showStatus("Admin login successful! Loading dashboard...", false);
                
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ui/admin_dashboard.fxml"));
                Parent root = loader.load();
                
                AdminController adminController = loader.getController();
                adminController.setCurrentAdmin(admin);
                
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.getScene().setRoot(root);
                return true;
            }
        } catch (Exception e) {
            // Not an admin, continue to other login attempts
        }
        return false;
    }

    private boolean tryDoctorLogin(String idOrUsername, String password, ActionEvent event) {
        Doctor doctor = null;
        
        // Try to parse as numeric ID first
        try {
            int id = Integer.parseInt(idOrUsername);
            doctor = doctorDAO.findById(id);
        } catch (NumberFormatException e) {
            // If not a number, try to find by username
            doctor = doctorDAO.findByUsername(idOrUsername);
        }
        
        if (doctor != null) {
            if (!doctor.getPassword().equals(password)) {
                return false; // Wrong password but valid doctor
            }
            
            // Check if doctor is still pending approval
            if (pendingDAO.isDoctorPending(doctor.getId())) {
                showStatus("Your account is pending admin verification. Please wait for approval.", true);
                return true; // Return true to prevent further login attempts
            }
            
            // Successful doctor login
            try {
                doctor.setCurrentLoginTime(LocalDateTime.now());
                doctorDAO.updateDoctor(doctor);
                
                LoginSession session = new LoginSession(doctor.getId(), doctor.getName(), doctor.getCurrentLoginTime());
                sessionDAO.addSession(session);
                CurrentDoctorHolder.setDoctor(doctor);
                
                showStatus("Doctor login successful! Loading dashboard...", false);
                
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ui/doctor_dashboard.fxml"));
                Parent root = loader.load();
                
                DoctorDashboardController ctrl = loader.getController();
                ctrl.setLoggedInDoctor(doctor);
                
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.getScene().setRoot(root);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                showStatus("Error loading doctor dashboard.", true);
                return false;
            }
        }
        
        return false; // No doctor found with given ID or username
    }

    private boolean tryPatientLogin(String username, String password, ActionEvent event) {
        try {
            Patient patient = findPatientByUsername(username);
            if (patient != null && patient.getPassword().equals(password)) {
                showStatus("Patient login successful! Loading dashboard...", false);
                
                CurrentPatientHolder.setCurrentPatient(patient);
                
                Parent root = FXMLLoader.load(getClass().getResource("/com/example/ui/patient_dashboard.fxml"));
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.getScene().setRoot(root);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

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

    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        if (isError) {
            statusLabel.setStyle("-fx-text-fill: #DC3545; -fx-font-size: 12px;");
        } else {
            statusLabel.setStyle("-fx-text-fill: #28A745; -fx-font-size: 12px;");
        }
    }

    @FXML
    private void onPatientRegisterClicked(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/ui/patient_register.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onDoctorRegisterClicked(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/ui/doctor_register.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
