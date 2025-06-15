package com.example.ui;

import com.example.data.AppointmentDAO;
import com.example.data.PatientDAO;
import com.example.model.Appointment;
import com.example.model.Patient;
import com.example.model.ds.CustomeLinkedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
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

public class PatientMedicalRecordsController implements Initializable {
    
    // Patient Information Labels
    @FXML private Label patientInfoLabel;
    @FXML private Label nameLabel;
    @FXML private Label patientIdLabel;
    @FXML private Label ageLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneLabel;
    @FXML private Label addressLabel;
    
    // Content Containers
    @FXML private VBox illnessHistoryContainer;
    @FXML private VBox appointmentHistoryContainer;
    
    // Statistics Labels
    @FXML private Label totalAppointmentsLabel;
    @FXML private Label upcomingAppointmentsLabel;
    @FXML private Label totalConditionsLabel;
    
    // Status Label
    @FXML private Label statusLabel;
    
    private final PatientDAO patientDAO = new PatientDAO();
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadPatientMedicalRecords();
    }

    /**
     * Load complete medical records for the current patient
     */
    private void loadPatientMedicalRecords() {
        if (!CurrentPatientHolder.isLoggedIn()) {
            statusLabel.setText("Session expired. Please log in again.");
            statusLabel.setStyle("-fx-text-fill: #DC3545; -fx-font-size: 14px;");
            return;
        }

        try {
            Patient patient = CurrentPatientHolder.getCurrentPatient();
            
            // Load patient information
            loadPatientInformation(patient);
            
            // Load medical history
            loadMedicalHistory(patient);
            
            // Load appointment history
            loadAppointmentHistory(patient.getId());
            
            // Update statistics
            updateMedicalStatistics(patient);
            
            statusLabel.setText("Medical records loaded successfully - Last updated: " + 
                              java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")));
            statusLabel.setStyle("-fx-text-fill: #28A745; -fx-font-size: 12px;");
            
        } catch (Exception e) {
            statusLabel.setText("Error loading medical records. Please try again.");
            statusLabel.setStyle("-fx-text-fill: #DC3545; -fx-font-size: 14px;");
            e.printStackTrace();
        }
    }

    /**
     * Load and display patient information
     */
    private void loadPatientInformation(Patient patient) {
        nameLabel.setText(patient.getFullname());
        patientIdLabel.setText(String.valueOf(patient.getId()));
        ageLabel.setText(patient.getAge() + " years");
        emailLabel.setText(patient.getEmail() != null ? patient.getEmail() : "Not provided");
        phoneLabel.setText(patient.getPhoneNumber() != null ? patient.getPhoneNumber() : "Not provided");
        addressLabel.setText(patient.getAddress() != null ? patient.getAddress() : "Not provided");
        
        patientInfoLabel.setText("Patient: " + patient.getFullname() + " (ID: " + patient.getId() + ")");
    }

    /**
     * Load and display medical history (illness history)
     */
    private void loadMedicalHistory(Patient patient) {
        illnessHistoryContainer.getChildren().clear();
        
        CustomeLinkedList<String> illnessHistory = patient.getIllnessHistory();
        
        if (illnessHistory == null || illnessHistory.size() == 0) {
            Label noHistoryLabel = new Label("No medical history recorded.");
            noHistoryLabel.setStyle("-fx-text-fill: #6C757D; -fx-font-size: 14px; -fx-font-style: italic; -fx-font-family: 'Roboto', 'Arial', 'Helvetica', sans-serif;");
            illnessHistoryContainer.getChildren().add(noHistoryLabel);
            return;
        }

        int index = 1;
        for (String illness : illnessHistory) {
            VBox illnessCard = createMedicalHistoryCard(illness, index++);
            illnessHistoryContainer.getChildren().add(illnessCard);
        }
    }

    /**
     * Create a card for a medical history entry
     */
    private VBox createMedicalHistoryCard(String illness, int index) {
        VBox card = new VBox();
        card.setSpacing(8);
        card.setStyle("-fx-background-color: #FFF5F5; -fx-background-radius: 8; -fx-padding: 15; " +
                     "-fx-border-color: #FED7D7; -fx-border-radius: 8; -fx-border-width: 1;");

        // Header with icon and number
        HBox header = new HBox();
        header.setSpacing(10);
        header.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        FontIcon medicalIcon = new FontIcon();
        medicalIcon.setIconLiteral("fas-notes-medical");
        medicalIcon.setIconSize(16);
        medicalIcon.setIconColor(javafx.scene.paint.Color.web("#DC3545"));

        Label indexLabel = new Label("#" + index);
        indexLabel.setStyle("-fx-text-fill: #DC3545; -fx-font-size: 12px; -fx-font-weight: bold; " +
                           "-fx-font-family: 'Roboto', 'Arial', 'Helvetica', sans-serif;");

        header.getChildren().addAll(medicalIcon, indexLabel);

        // Illness description
        Label illnessLabel = new Label(illness);
        illnessLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #2C2C54; -fx-font-weight: bold; " +
                             "-fx-font-family: 'Roboto', 'Arial', 'Helvetica', sans-serif;");
        illnessLabel.setWrapText(true);

        card.getChildren().addAll(header, illnessLabel);
        return card;
    }

    /**
     * Load and display appointment history
     */
    private void loadAppointmentHistory(int patientId) {
        appointmentHistoryContainer.getChildren().clear();
        
        try {
            CustomeLinkedList<Appointment> allAppointments = appointmentDAO.getAllAppointments();
            
            // Filter appointments for current patient
            List<Appointment> patientAppointments = new ArrayList<>();
            for (Appointment apt : allAppointments) {
                if (apt.getPatientId() == patientId) {
                    patientAppointments.add(apt);
                }
            }
            
            if (patientAppointments.isEmpty()) {
                Label noAppointmentsLabel = new Label("No appointment history found.");
                noAppointmentsLabel.setStyle("-fx-text-fill: #6C757D; -fx-font-size: 14px; -fx-font-style: italic; " +
                                            "-fx-font-family: 'Roboto', 'Arial', 'Helvetica', sans-serif;");
                appointmentHistoryContainer.getChildren().add(noAppointmentsLabel);
                return;
            }
            
            // Sort by date (most recent first)
            patientAppointments.sort((a1, a2) -> a2.getTime().compareTo(a1.getTime()));
            
            for (Appointment appointment : patientAppointments) {
                VBox appointmentCard = createAppointmentHistoryCard(appointment);
                appointmentHistoryContainer.getChildren().add(appointmentCard);
            }
            
        } catch (Exception e) {
            Label errorLabel = new Label("Error loading appointment history.");
            errorLabel.setStyle("-fx-text-fill: #DC3545; -fx-font-size: 14px; -fx-font-family: 'Roboto', 'Arial', 'Helvetica', sans-serif;");
            appointmentHistoryContainer.getChildren().add(errorLabel);
            e.printStackTrace();
        }
    }

    /**
     * Create a card for an appointment history entry
     */
    private VBox createAppointmentHistoryCard(Appointment appointment) {
        VBox card = new VBox();
        card.setSpacing(12);
        card.setStyle("-fx-background-color: #F0F8FD; -fx-background-radius: 8; -fx-padding: 18; " +
                     "-fx-border-color: #C6E3F7; -fx-border-radius: 8; -fx-border-width: 1;");

        // Header with status icon and appointment ID
        HBox header = new HBox();
        header.setSpacing(12);
        header.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        FontIcon statusIcon = new FontIcon();
        statusIcon.setIconSize(18);
        
        String statusText;
        String statusColor;
        if (appointment.getTime().isAfter(java.time.LocalDateTime.now())) {
            statusIcon.setIconLiteral("fas-clock");
            statusIcon.setIconColor(javafx.scene.paint.Color.web("#FFC107"));
            statusText = "Upcoming";
            statusColor = "#FFC107";
        } else {
            statusIcon.setIconLiteral("fas-check-circle");
            statusIcon.setIconColor(javafx.scene.paint.Color.web("#28A745"));
            statusText = "Completed";
            statusColor = "#28A745";
        }

        Label statusLabel = new Label(statusText);
        statusLabel.setStyle("-fx-text-fill: " + statusColor + "; -fx-font-size: 12px; -fx-font-weight: bold; " +
                            "-fx-font-family: 'Roboto', 'Arial', 'Helvetica', sans-serif;");

        Label appointmentIdLabel = new Label("Appointment #" + appointment.getAppointmentId());
        appointmentIdLabel.setStyle("-fx-text-fill: #6C757D; -fx-font-size: 12px; " +
                                   "-fx-font-family: 'Roboto', 'Arial', 'Helvetica', sans-serif;");

        header.getChildren().addAll(statusIcon, statusLabel, appointmentIdLabel);

        // Appointment details
        Label doctorLabel = new Label("Doctor: " + appointment.getDoctorSpecialty() + " (ID: " + appointment.getDoctorId() + ")");
        doctorLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2C2C54; " +
                            "-fx-font-family: 'Roboto', 'Arial', 'Helvetica', sans-serif;");

        Label dateLabel = new Label("Date: " + appointment.getTime().format(DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy 'at' hh:mm a")));
        dateLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #495057; " +
                          "-fx-font-family: 'Roboto', 'Arial', 'Helvetica', sans-serif;");

        Label reasonLabel = new Label("Reason: " + (appointment.getPatientIllness() != null ? appointment.getPatientIllness() : "General consultation"));
        reasonLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #495057; " +
                            "-fx-font-family: 'Roboto', 'Arial', 'Helvetica', sans-serif;");
        reasonLabel.setWrapText(true);

        card.getChildren().addAll(header, doctorLabel, dateLabel, reasonLabel);
        return card;
    }

    /**
     * Update medical statistics
     */
    private void updateMedicalStatistics(Patient patient) {
        try {
            int patientId = patient.getId();
            CustomeLinkedList<Appointment> allAppointments = appointmentDAO.getAllAppointments();
            
            // Count total appointments
            int totalAppointments = 0;
            int upcomingAppointments = 0;
            
            for (Appointment apt : allAppointments) {
                if (apt.getPatientId() == patientId) {
                    totalAppointments++;
                    if (apt.getTime().isAfter(java.time.LocalDateTime.now())) {
                        upcomingAppointments++;
                    }
                }
            }
            
            // Count medical conditions
            int totalConditions = patient.getIllnessHistory() != null ? patient.getIllnessHistory().size() : 0;
            
            // Update labels
            totalAppointmentsLabel.setText(String.valueOf(totalAppointments));
            upcomingAppointmentsLabel.setText(String.valueOf(upcomingAppointments));
            totalConditionsLabel.setText(String.valueOf(totalConditions));
            
        } catch (Exception e) {
            totalAppointmentsLabel.setText("N/A");
            upcomingAppointmentsLabel.setText("N/A");
            totalConditionsLabel.setText("N/A");
            e.printStackTrace();
        }
    }

    // Navigation Methods
    @FXML
    private void onDashboardClicked(ActionEvent event) {
        navigateTo("/com/example/ui/patient_dashboard.fxml");
    }

    @FXML
    private void onBookAppointmentClicked(ActionEvent event) {
        navigateTo("/com/example/ui/patient_appointment.fxml");
    }

    @FXML
    private void onLogoutClicked(ActionEvent event) {
        // Clear the current session
        CurrentPatientHolder.clearSession();
        
        // Navigate back to login
        navigateTo("/com/example/ui/login_view.fxml");
    }

    /**
     * Helper method for navigation
     */
    private void navigateTo(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) nameLabel.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            statusLabel.setText("Navigation error. Please try again.");
            statusLabel.setStyle("-fx-text-fill: #DC3545; -fx-font-size: 14px;");
            e.printStackTrace();
        }
    }
}
