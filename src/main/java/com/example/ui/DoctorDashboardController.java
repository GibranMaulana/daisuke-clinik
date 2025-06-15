package com.example.ui;

import com.example.data.AppointmentDAO;
import com.example.data.LoginSessionDAO;
import com.example.model.Appointment;
import com.example.model.Doctor;
import com.example.model.ds.CustomeLinkedList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.io.IOException;

public class DoctorDashboardController {
    @FXML private Label welcomeLabel;
    @FXML private Label nextAppointmentLabel;
    @FXML private Label statusLabel;
    @FXML private Label totalQueueLabel;
    @FXML private ListView<String> appointmentListView;

    private Doctor loggedInDoctor;
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final LoginSessionDAO sessionDAO = new LoginSessionDAO();

    /**
     * Called from DoctorLoginController when a doctor has just logged in.
     */
    public void setLoggedInDoctor(Doctor d) {
        this.loggedInDoctor = d;
        welcomeLabel.setText("Welcome, Dr. " + d.getName() + " (ID " + d.getId() + ")");
        refreshDashboard();
    }

    private void refreshDashboard() {
        refreshAppointmentList();
        updateStatistics();
        updateNextAppointment();
    }

    private void refreshAppointmentList() {
        appointmentListView.getItems().clear();
        CustomeLinkedList<Appointment> queue = appointmentDAO.getQueueForDoctor(loggedInDoctor.getId());
        ObservableList<String> items = FXCollections.observableArrayList();
        for (Appointment a : queue) {
            String entry = String.format("%d | P:%d | %s | %s",
                    a.getAppointmentId(),
                    a.getPatientId(),
                    a.getTime().toString(),
                    a.getPatientIllness());
            items.add(entry);
        }
        appointmentListView.setItems(items);
    }

    private void updateStatistics() {
        // Get all pending appointments for this doctor
        CustomeLinkedList<Appointment> pendingAppointments = appointmentDAO.getQueueForDoctor(loggedInDoctor.getId());
        
        // Total pending appointments count (all scheduled appointments for this doctor)
        int totalPendingCount = pendingAppointments.size();
        
        // Total Queue: all pending appointments for this doctor
        totalQueueLabel.setText(String.valueOf(totalPendingCount));
    }

    private void updateNextAppointment() {
        Appointment nextAppointment = appointmentDAO.peekNextAppointment(loggedInDoctor.getId());
        if (nextAppointment != null) {
            nextAppointmentLabel.setText(String.format("ID: %d | Patient: %d | %s", 
                nextAppointment.getAppointmentId(),
                nextAppointment.getPatientId(),
                nextAppointment.getTime().toString()));
        } else {
            nextAppointmentLabel.setText("No appointments");
        }
    }

    @FXML
    private void onProcessNextClicked(ActionEvent event) {
        Appointment nextAppointment = appointmentDAO.peekNextAppointment(loggedInDoctor.getId());
        if (nextAppointment == null) {
            System.out.println("No appointments to process.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ui/process_appointment_view.fxml"));
            Parent root = loader.load();
            ProcessAppointmentController controller = loader.getController();
            controller.setAppointment(nextAppointment);

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * ─────────────────────────────────────────────────────────────────────────
     * If the doctor just wants to return to the landing page (so a patient
     * can make an appointment), use this method. It does NOT remove the session.
     * ─────────────────────────────────────────────────────────────────────────
     */
    @FXML
    private void onBackToLandingClicked(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/ui/login_view.fxml"));
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * ─────────────────────────────────────────────────────────────────────────
     * Proper “Logout”: remove doctor’s login session, then return to login screen.
     * ─────────────────────────────────────────────────────────────────────────
     */
    @FXML
    private void onLogoutClicked(ActionEvent event) {
        // Remove this doctor from the “logged‐in” list
            if (loggedInDoctor != null) {
            sessionDAO.removeSession(loggedInDoctor.getId());
        }

        // Load the doctor‐login.fxml screen
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/ui/login_view.fxml"));
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void onRefreshClicked(ActionEvent event) {
        refreshDashboard();
    }

    @FXML
    private void onViewPatientsClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ui/doctor_patient_view.fxml"));
            Parent root = loader.load();
            

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
