package com.example.ui;

import com.example.data.AppointmentDAO;
import com.example.data.LoginSessionDAO;
import com.example.model.Appointment;
import com.example.model.Doctor;
import com.example.model.ds.CustomeLinkedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.geometry.Pos;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class DoctorDashboardController {
    @FXML private Label welcomeLabel;
    @FXML private Label nextAppointmentLabel;
    @FXML private Label statusLabel;
    @FXML private Label totalQueueLabel;
    @FXML private VBox appointmentCardsContainer; // Changed from ListView to VBox

    private Doctor loggedInDoctor;
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final LoginSessionDAO sessionDAO = new LoginSessionDAO();

    /**
     * Called from LoginController when a doctor has just logged in.
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
        // Add null check to prevent NullPointerException during initialization
        if (appointmentCardsContainer == null) {
            return;
        }
        
        appointmentCardsContainer.getChildren().clear();
        CustomeLinkedList<Appointment> queue = appointmentDAO.getQueueForDoctor(loggedInDoctor.getId());
        
        // Debug output
        System.out.println("DEBUG: Doctor ID " + loggedInDoctor.getId() + " has " + queue.size() + " appointments");
        for (Appointment a : queue) {
            System.out.println("  - " + a.getAppointmentId() + " at " + a.getTime() + " for patient " + a.getPatientId());
        }
        
        // Create appointment cards
        for (Appointment appointment : queue) {
            VBox appointmentCard = createAppointmentCard(appointment);
            appointmentCardsContainer.getChildren().add(appointmentCard);
        }
        
        // If no appointments, show a message
        if (queue.isEmpty()) {
            Label noAppointmentsLabel = new Label("No upcoming appointments");
            noAppointmentsLabel.setStyle(
                "-fx-text-fill: #6C757D; -fx-font-size: 14px; " +
                "-fx-font-family: 'Roboto', 'Arial', 'Helvetica', sans-serif; " +
                "-fx-padding: 20;"
            );
            appointmentCardsContainer.getChildren().add(noAppointmentsLabel);
        }
    }
    
    private VBox createAppointmentCard(Appointment appointment) {
        VBox card = new VBox();
        card.setSpacing(12);
        
        // Determine priority color based on how soon the appointment is
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        long hoursUntil = java.time.Duration.between(now, appointment.getTime()).toHours();
        String priorityColor = hoursUntil <= 24 ? "#FF6B6B" : hoursUntil <= 72 ? "#4ECDC4" : "#45B7D1";
        String bgColor = hoursUntil <= 24 ? "#FFF5F5" : hoursUntil <= 72 ? "#F0FDFA" : "#F0F9FF";
        
        card.setStyle(
            "-fx-background-color: " + bgColor + "; " +
            "-fx-border-color: " + priorityColor + "; " +
            "-fx-border-width: 0 0 0 4; " +
            "-fx-border-radius: 0 8 8 0; " +
            "-fx-background-radius: 8; " +
            "-fx-padding: 18; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);"
        );
        
        // Priority badge and header
        HBox header = new HBox();
        header.setSpacing(12);
        header.setAlignment(Pos.CENTER_LEFT);
        
        // Priority badge
        Label priorityBadge = new Label(hoursUntil <= 24 ? "URGENT" : hoursUntil <= 72 ? "SOON" : "SCHEDULED");
        priorityBadge.setStyle(
            "-fx-background-color: " + priorityColor + "; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 10px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 4 8; " +
            "-fx-background-radius: 12; " +
            "-fx-font-family: 'Roboto', 'Arial', 'Helvetica', sans-serif;"
        );
        
        // Time with enhanced formatting
        FontIcon timeIcon = new FontIcon();
        timeIcon.setIconLiteral("fas-clock");
        timeIcon.setIconSize(18);
        timeIcon.setIconColor(javafx.scene.paint.Color.valueOf(priorityColor));
        
        VBox timeInfo = new VBox();
        timeInfo.setSpacing(2);
        
        Label timeLabel = new Label(appointment.getTime().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
        timeLabel.setStyle(
            "-fx-font-size: 15px; -fx-font-weight: bold; " +
            "-fx-text-fill: #2C2C54; " +
            "-fx-font-family: 'Roboto', 'Arial', 'Helvetica', sans-serif;"
        );
        
        Label hourLabel = new Label(appointment.getTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        hourLabel.setStyle(
            "-fx-font-size: 20px; -fx-font-weight: bold; " +
            "-fx-text-fill: " + priorityColor + "; " +
            "-fx-font-family: 'Roboto', 'Arial', 'Helvetica', sans-serif;"
        );
        
        timeInfo.getChildren().addAll(timeLabel, hourLabel);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        
        // Appointment ID with styled badge
        Label idBadge = new Label("#" + appointment.getAppointmentId());
        idBadge.setStyle(
            "-fx-background-color: #E9ECEF; " +
            "-fx-text-fill: #6C757D; " +
            "-fx-font-size: 11px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 6 10; " +
            "-fx-background-radius: 15; " +
            "-fx-font-family: 'Roboto Mono', 'Courier New', monospace;"
        );
        
        header.getChildren().addAll(priorityBadge, timeIcon, timeInfo, spacer, idBadge);
        
        // Patient info with enhanced styling
        HBox patientSection = new HBox();
        patientSection.setSpacing(12);
        patientSection.setAlignment(Pos.CENTER_LEFT);
        patientSection.setStyle("-fx-padding: 8 0;");
        
        FontIcon patientIcon = new FontIcon();
        patientIcon.setIconLiteral("fas-user-circle");
        patientIcon.setIconSize(20);
        patientIcon.setIconColor(javafx.scene.paint.Color.valueOf("#28a745"));
        
        VBox patientInfo = new VBox();
        patientInfo.setSpacing(3);
        
        Label patientLabel = new Label("Patient ID: " + appointment.getPatientId());
        patientLabel.setStyle(
            "-fx-font-size: 14px; -fx-font-weight: bold; " +
            "-fx-text-fill: #495057; " +
            "-fx-font-family: 'Roboto', 'Arial', 'Helvetica', sans-serif;"
        );
        
        // Duration estimate
        Label durationLabel = new Label("Est. Duration: 30 min");
        durationLabel.setStyle(
            "-fx-font-size: 11px; -fx-text-fill: #6C757D; " +
            "-fx-font-family: 'Roboto', 'Arial', 'Helvetica', sans-serif;"
        );
        
        patientInfo.getChildren().addAll(patientLabel, durationLabel);
        patientSection.getChildren().addAll(patientIcon, patientInfo);
        
        // Illness info with medical icon
        HBox illnessSection = new HBox();
        illnessSection.setSpacing(12);
        illnessSection.setAlignment(Pos.TOP_LEFT);
        illnessSection.setStyle("-fx-padding: 8 0;");
        
        FontIcon medicalIcon = new FontIcon();
        medicalIcon.setIconLiteral("fas-notes-medical");
        medicalIcon.setIconSize(16);
        medicalIcon.setIconColor(javafx.scene.paint.Color.valueOf("#FF6B6B"));
        
        VBox illnessInfo = new VBox();
        illnessInfo.setSpacing(4);
        
        Label complaintTitle = new Label("Chief Complaint:");
        complaintTitle.setStyle(
            "-fx-font-size: 12px; -fx-font-weight: bold; " +
            "-fx-text-fill: #6C757D; " +
            "-fx-font-family: 'Roboto', 'Arial', 'Helvetica', sans-serif;"
        );
        
        Label illnessLabel = new Label(appointment.getPatientIllness());
        illnessLabel.setStyle(
            "-fx-font-size: 14px; -fx-text-fill: #2C2C54; " +
            "-fx-font-family: 'Roboto', 'Arial', 'Helvetica', sans-serif; " +
            "-fx-wrap-text: true; " +
            "-fx-font-style: italic;"
        );
        illnessLabel.setMaxWidth(400);
        illnessLabel.setWrapText(true);
        
        illnessInfo.getChildren().addAll(complaintTitle, illnessLabel);
        illnessSection.getChildren().addAll(medicalIcon, illnessInfo);
        
        // Time until appointment display
        HBox timeUntilRow = new HBox();
        timeUntilRow.setAlignment(Pos.CENTER_LEFT);
        timeUntilRow.setStyle("-fx-padding: 8 0 0 0;");
        
        Label timeUntilLabel = new Label(getTimeUntilText(hoursUntil));
        timeUntilLabel.setStyle(
            "-fx-font-size: 12px; " +
            "-fx-text-fill: " + priorityColor + "; " +
            "-fx-font-weight: bold; " +
            "-fx-font-family: 'Roboto', 'Arial', 'Helvetica', sans-serif;"
        );
        
        timeUntilRow.getChildren().add(timeUntilLabel);
        
        card.getChildren().addAll(header, patientSection, illnessSection, timeUntilRow);
        
        return card;
    }
    
    private String getTimeUntilText(long hoursUntil) {
        if (hoursUntil < 1) {
            return "⚡ Starting soon";
        } else if (hoursUntil < 24) {
            return "🕐 In " + hoursUntil + " hours";
        } else {
            long days = hoursUntil / 24;
            return "📅 In " + days + " day" + (days > 1 ? "s" : "");
        }
    }

    private void updateStatistics() {
        // Add null check for UI components
        if (totalQueueLabel == null || loggedInDoctor == null) {
            return;
        }
        
        // Get all pending appointments for this doctor
        CustomeLinkedList<Appointment> pendingAppointments = appointmentDAO.getQueueForDoctor(loggedInDoctor.getId());
        
        // Total pending appointments count (all scheduled appointments for this doctor)
        int totalPendingCount = pendingAppointments.size();
        
        // Total Queue: all pending appointments for this doctor
        totalQueueLabel.setText(String.valueOf(totalPendingCount));
    }

    private void updateNextAppointment() {
        // Add null check for UI components
        if (nextAppointmentLabel == null || loggedInDoctor == null) {
            return;
        }
        
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
