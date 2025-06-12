package com.example.ui;

import com.example.data.AppointmentDAO;
import com.example.model.Appointment;
import com.example.model.Patient;
import com.example.model.ds.CustomeLinkedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PatientDashboardController implements Initializable {
    @FXML private Label patientNameLabel;
    @FXML private Label patientEmailLabel;
    @FXML private Label statusLabel;
    @FXML private VBox recentActivityContainer;
    
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadPatientInfo();
        loadRecentActivity();
    }

    /**
     * Load current patient information into the header
     */
    private void loadPatientInfo() {
        if (CurrentPatientHolder.isLoggedIn()) {
            Patient patient = CurrentPatientHolder.getCurrentPatient();
            patientNameLabel.setText("Welcome, " + patient.getFullname());
            patientEmailLabel.setText(patient.getEmail());
        } else {
            patientNameLabel.setText("Welcome, Guest");
            patientEmailLabel.setText("Not logged in");
            statusLabel.setText("Session expired. Please log in again.");
            statusLabel.setStyle("-fx-text-fill: #DC3545; -fx-font-size: 14px;");
        }
    }

    /**
     * Load recent appointment activity for the current patient
     */
    private void loadRecentActivity() {
        recentActivityContainer.getChildren().clear();
        
        if (!CurrentPatientHolder.isLoggedIn()) {
            Label noDataLabel = new Label("Please log in to view your activity.");
            noDataLabel.setStyle("-fx-text-fill: #6C757D; -fx-font-size: 14px; -fx-font-family: 'Roboto', 'Arial', 'Helvetica', sans-serif;");
            recentActivityContainer.getChildren().add(noDataLabel);
            return;
        }

        try {
            int patientId = CurrentPatientHolder.getPatientId();
            CustomeLinkedList<Appointment> allAppointments = appointmentDAO.getAllAppointments();
            
            // Filter appointments for current patient and get recent ones
            List<Appointment> patientAppointments = new ArrayList<>();
            for (Appointment apt : allAppointments) {
                if (apt.getPatientId() == patientId) {
                    patientAppointments.add(apt);
                }
            }
            
            // Sort by time (most recent first) and limit to 5
            patientAppointments.sort((a1, a2) -> a2.getTime().compareTo(a1.getTime()));
            if (patientAppointments.size() > 5) {
                patientAppointments = patientAppointments.subList(0, 5);
            }

            if (patientAppointments.isEmpty()) {
                Label noDataLabel = new Label("No appointments found. Book your first appointment!");
                noDataLabel.setStyle("-fx-text-fill: #6C757D; -fx-font-size: 14px; -fx-font-family: 'Roboto', 'Arial', 'Helvetica', sans-serif;");
                recentActivityContainer.getChildren().add(noDataLabel);
            } else {
                for (Appointment appointment : patientAppointments) {
                    VBox appointmentCard = createAppointmentCard(appointment);
                    recentActivityContainer.getChildren().add(appointmentCard);
                }
            }

        } catch (Exception e) {
            Label errorLabel = new Label("Error loading recent activity.");
            errorLabel.setStyle("-fx-text-fill: #DC3545; -fx-font-size: 14px; -fx-font-family: 'Roboto', 'Arial', 'Helvetica', sans-serif;");
            recentActivityContainer.getChildren().add(errorLabel);
            e.printStackTrace();
        }
    }

    /**
     * Create a visual card for an appointment
     */
    private VBox createAppointmentCard(Appointment appointment) {
        VBox card = new VBox();
        card.setSpacing(8);
        card.setStyle("-fx-background-color: #F8F9FA; -fx-background-radius: 8; -fx-padding: 15; -fx-border-color: #E1E5E9; -fx-border-radius: 8;");

        // Header with status icon
        HBox header = new HBox();
        header.setSpacing(10);
        header.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        FontIcon statusIcon = new FontIcon();
        statusIcon.setIconSize(16);
        
        String statusText;
        String statusColor;
        if (appointment.getTime().isAfter(java.time.LocalDateTime.now())) {
            statusIcon.setIconLiteral("fas-clock");
            statusIcon.setIconColor(javafx.scene.paint.Color.ORANGE);
            statusText = "Upcoming";
            statusColor = "#FFC107";
        } else {
            statusIcon.setIconLiteral("fas-check-circle");
            statusIcon.setIconColor(javafx.scene.paint.Color.GREEN);
            statusText = "Completed (this data will be archived soon)";
            statusColor = "#28A745";
        }

        Label statusLabel = new Label(statusText);
        statusLabel.setStyle("-fx-text-fill: " + statusColor + "; -fx-font-size: 12px; -fx-font-weight: bold; -fx-font-family: 'Roboto', 'Arial', 'Helvetica', sans-serif;");

        header.getChildren().addAll(statusIcon, statusLabel);

        // Appointment details
        Label doctorLabel = new Label("Dr. " + appointment.getDoctorSpecialty() + " (ID: " + appointment.getDoctorId() + ")");
        doctorLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2C2C54; -fx-font-family: 'Roboto', 'Arial', 'Helvetica', sans-serif;");

        Label dateLabel = new Label(appointment.getTime().format(DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' hh:mm a")));
        dateLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #6C757D; -fx-font-family: 'Roboto', 'Arial', 'Helvetica', sans-serif;");

        if (appointment.getPatientIllness() != null && !appointment.getPatientIllness().trim().isEmpty()) {
            Label reasonLabel = new Label("Reason: " + appointment.getPatientIllness());
            reasonLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #6C757D; -fx-font-family: 'Roboto', 'Arial', 'Helvetica', sans-serif;");
            card.getChildren().addAll(header, doctorLabel, dateLabel, reasonLabel);
        } else {
            card.getChildren().addAll(header, doctorLabel, dateLabel);
        }

        return card;
    }

    @FXML
    private void onBookAppointmentClicked(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/ui/patient_appointment.fxml"));
            Stage stage = (Stage) patientNameLabel.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            statusLabel.setText("Error loading appointment booking page.");
            statusLabel.setStyle("-fx-text-fill: #DC3545; -fx-font-size: 14px;");
            e.printStackTrace();
        }
    }

    @FXML
    private void onBookAppointmentClicked(ActionEvent event) {
        onBookAppointmentClicked((MouseEvent) null);
    }

    @FXML
    private void onMedicalRecordsClicked(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/ui/patient_medical_records.fxml"));
            Stage stage = (Stage) patientNameLabel.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            statusLabel.setText("Error loading medical records page.");
            statusLabel.setStyle("-fx-text-fill: #DC3545; -fx-font-size: 14px;");
            e.printStackTrace();
        }
    }

    @FXML
    private void onMedicalRecordsClicked(ActionEvent event) {
        onMedicalRecordsClicked((MouseEvent) null);
    }

    @FXML
    private void onLogoutClicked(ActionEvent event) {
        // Clear the current session
        CurrentPatientHolder.clearSession();
        
        // Navigate back to login
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/ui/patient_login.fxml"));
            Stage stage = (Stage) patientNameLabel.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
