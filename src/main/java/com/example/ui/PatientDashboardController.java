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
import com.example.model.ds.CustomeLinkedList;
import java.util.ResourceBundle;

public class PatientDashboardController implements Initializable {
    @FXML private Label patientNameLabel;
    @FXML private Label patientEmailLabel;
    @FXML private Label statusLabel;
    @FXML private VBox recentActivityContainer;
    
    // New patient information labels for the dashboard
    @FXML private Label dashboardNameLabel;
    @FXML private Label dashboardPatientIdLabel;
    @FXML private Label dashboardAgeLabel;
    @FXML private Label dashboardEmailLabel;
    @FXML private Label dashboardPhoneLabel;
    @FXML private Label dashboardAddressLabel;
    
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadPatientInfo();
        loadPatientDetailsInfo();
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
     * Load detailed patient information into the patient information section
     */
    private void loadPatientDetailsInfo() {
        if (CurrentPatientHolder.isLoggedIn()) {
            Patient patient = CurrentPatientHolder.getCurrentPatient();
            
            dashboardNameLabel.setText(patient.getFullname());
            dashboardPatientIdLabel.setText(String.valueOf(patient.getId()));
            dashboardAgeLabel.setText(patient.getAge() + " years");
            dashboardEmailLabel.setText(patient.getEmail() != null ? patient.getEmail() : "Not provided");
            dashboardPhoneLabel.setText(patient.getPhoneNumber() != null ? patient.getPhoneNumber() : "Not provided");
            dashboardAddressLabel.setText(patient.getAddress() != null ? patient.getAddress() : "Not provided");
        } else {
            // Set default values when not logged in
            dashboardNameLabel.setText("Guest User");
            dashboardPatientIdLabel.setText("N/A");
            dashboardAgeLabel.setText("N/A");
            dashboardEmailLabel.setText("Please log in");
            dashboardPhoneLabel.setText("Please log in");
            dashboardAddressLabel.setText("Please log in");
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
            CustomeLinkedList<Appointment> patientAppointments = new CustomeLinkedList<>();
            for (Appointment apt : allAppointments) {
                if (apt.getPatientId() == patientId) {
                    patientAppointments.add(apt);
                }
            }
            
            // Convert to array for sorting, then back to CustomeLinkedList
            // Sort by time (most recent first) and limit to 5
            CustomeLinkedList<Appointment> sortedAppointments = new CustomeLinkedList<>();
            if (patientAppointments.size() > 0) {
                // Simple bubble sort to sort by time (most recent first)
                for (int i = 0; i < patientAppointments.size(); i++) {
                    Appointment latest = null;
                    
                    // Find the latest appointment that hasn't been added yet
                    for (int j = 0; j < patientAppointments.size(); j++) {
                        Appointment current = patientAppointments.get(j);
                        boolean alreadyAdded = false;
                        
                        // Check if already added to sorted list
                        for (Appointment sorted : sortedAppointments) {
                            if (sorted == current) {
                                alreadyAdded = true;
                                break;
                            }
                        }
                        
                        if (!alreadyAdded && (latest == null || current.getTime().isAfter(latest.getTime()))) {
                            latest = current;
                        }
                    }
                    
                    if (latest != null) {
                        sortedAppointments.add(latest);
                        if (sortedAppointments.size() >= 5) break; // Limit to 5
                    }
                }
            }

            if (sortedAppointments.isEmpty()) {
                Label noDataLabel = new Label("No appointments found. Book your first appointment!");
                noDataLabel.setStyle("-fx-text-fill: #6C757D; -fx-font-size: 14px; -fx-font-family: 'Roboto', 'Arial', 'Helvetica', sans-serif;");
                recentActivityContainer.getChildren().add(noDataLabel);
            } else {
                for (Appointment appointment : sortedAppointments) {
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
    private void onBookAppointmentCardClicked(MouseEvent event) {
        // Navigate to appointment booking from card click
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
    private void onBookAppointmentButtonClicked(ActionEvent event) {
        // Navigate to appointment booking from navigation button
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
    private void onMedicalRecordsClicked(ActionEvent event) {
        // Navigate to medical records from navigation button
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
    private void onMedicalRecordsCardClicked(MouseEvent event) {
        // Navigate to medical records from card click
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
    private void onLogoutClicked(ActionEvent event) {
        // Clear the current session
        CurrentPatientHolder.clearSession();
        
        // Navigate back to login
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/ui/login_view.fxml"));
            Stage stage = (Stage) patientNameLabel.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
