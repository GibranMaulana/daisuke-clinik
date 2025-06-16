package com.example.ui;

import com.example.data.AppointmentDAO;
import com.example.data.DoctorDAO;
import com.example.model.Appointment;
import com.example.model.Doctor;
import com.example.model.ds.CustomeLinkedList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Lets a registered patient choose any doctor (online or offline), pick date/time, and enter illness.
 * Includes comprehensive time validation and conflict checking.
 */
public class PatientAppointmentController {
    @FXML private ListView<Doctor> doctorListView;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<LocalTime> timeComboBox;
    @FXML private TextArea illnessArea;
    @FXML private Label statusLabel;

    private final DoctorDAO doctorDAO = new DoctorDAO();
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();

    // This method is called automatically after FXML is loaded.
    @FXML
    public void initialize() {
        loadAllDoctors();
        setupTimeComboBox();
        setupDatePicker();
        
        // Configure ListView for better appearance
        doctorListView.setStyle(doctorListView.getStyle() + "; -fx-cell-size: 80;"); // Set consistent cell height
        
        // Add selection listener to provide feedback
        doctorListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                statusLabel.setText("✓ Selected: Dr. " + newValue.getFullname() + " (" + newValue.getSpecialty() + ")");
                statusLabel.setStyle("-fx-text-fill: #28A745; -fx-font-family: 'Roboto', 'Arial', 'Helvetica', sans-serif; -fx-font-size: 12px; -fx-background-color: #D4F6DD; -fx-padding: 6 12; -fx-background-radius: 12; -fx-font-weight: bold;");
            } else {
                statusLabel.setText("");
                statusLabel.setStyle("");
            }
        });
        
        // Add date selection listener
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateAppointmentStatus();
            }
        });
        
        // Add time selection listener
        timeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateAppointmentStatus();
            }
        });
    }
    
    /**
     * Update status based on current selection state
     */
    private void updateAppointmentStatus() {
        Doctor selectedDoctor = doctorListView.getSelectionModel().getSelectedItem();
        java.time.LocalDate selectedDate = datePicker.getValue();
        java.time.LocalTime selectedTime = timeComboBox.getValue();
        
        if (selectedDoctor != null && selectedDate != null && selectedTime != null) {
            statusLabel.setText("✓ Ready to schedule: Dr. " + selectedDoctor.getFullname() + " on " + 
                               selectedDate.format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy")) + 
                               " at " + selectedTime.toString());
            statusLabel.setStyle("-fx-text-fill: #007BFF; -fx-font-family: 'Roboto', 'Arial', 'Helvetica', sans-serif; -fx-font-size: 12px; -fx-background-color: #E3F2FD; -fx-padding: 6 12; -fx-background-radius: 12; -fx-font-weight: bold;");
        } else if (selectedDoctor != null) {
            statusLabel.setText("✓ Selected: Dr. " + selectedDoctor.getFullname() + " (" + selectedDoctor.getSpecialty() + ")");
            statusLabel.setStyle("-fx-text-fill: #28A745; -fx-font-family: 'Roboto', 'Arial', 'Helvetica', sans-serif; -fx-font-size: 12px; -fx-background-color: #D4F6DD; -fx-padding: 6 12; -fx-background-radius: 12; -fx-font-weight: bold;");
        }
    }

    private void loadAllDoctors() {
        CustomeLinkedList<Doctor> doctors = doctorDAO.getAllDoctors();

        // Build an ObservableList<Doctor> by iterating our CustomeLinkedList
        ObservableList<Doctor> items = FXCollections.observableArrayList();
        for (Doctor doctor : doctors) {
            items.add(doctor);
        }
        doctorListView.setItems(items);

        // Set custom cell factory to display doctor information with enhanced styling
        doctorListView.setCellFactory(listView -> new ListCell<Doctor>() {
            @Override
            protected void updateItem(Doctor doctor, boolean empty) {
                super.updateItem(doctor, empty);
                if (empty || doctor == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                } else {
                    setText(null); // Clear text since we're using custom graphic
                    
                    // Create custom cell layout
                    javafx.scene.layout.HBox cellContent = new javafx.scene.layout.HBox();
                    cellContent.setSpacing(15);
                    cellContent.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                    cellContent.setStyle("-fx-padding: 10 15; -fx-background-color: white; -fx-border-color: #E9ECEF; -fx-border-width: 0 0 1 0;");
                    
                    // Doctor icon
                    org.kordamp.ikonli.javafx.FontIcon doctorIcon = new org.kordamp.ikonli.javafx.FontIcon();
                    doctorIcon.setIconLiteral("fas-user-md");
                    doctorIcon.setIconSize(20);
                    doctorIcon.setIconColor(javafx.scene.paint.Color.web("#007BFF"));
                    
                    // Doctor details container
                    javafx.scene.layout.VBox detailsBox = new javafx.scene.layout.VBox();
                    detailsBox.setSpacing(4);
                    
                    // Doctor name
                    javafx.scene.control.Label nameLabel = new javafx.scene.control.Label("Dr. " + doctor.getFullname());
                    nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2C2C54; -fx-font-family: 'Roboto', 'Arial', 'Helvetica', sans-serif;");
                    
                    // Specialty and ID info
                    javafx.scene.control.Label specialtyLabel = new javafx.scene.control.Label(doctor.getSpecialty() + " • ID: " + doctor.getId());
                    specialtyLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #6C757D; -fx-font-family: 'Roboto', 'Arial', 'Helvetica', sans-serif;");
                    
                    detailsBox.getChildren().addAll(nameLabel, specialtyLabel);
                    
                    // Status indicator
                    javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
                    javafx.scene.layout.HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
                    
                    javafx.scene.layout.HBox statusBox = new javafx.scene.layout.HBox();
                    statusBox.setSpacing(6);
                    statusBox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
                    
                    org.kordamp.ikonli.javafx.FontIcon statusIcon = new org.kordamp.ikonli.javafx.FontIcon();
                    statusIcon.setIconLiteral("fas-circle");
                    statusIcon.setIconSize(8);
                    statusIcon.setIconColor(javafx.scene.paint.Color.web("#28A745"));
                    
                    javafx.scene.control.Label statusLabel = new javafx.scene.control.Label("Available");
                    statusLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #28A745; -fx-font-family: 'Roboto', 'Arial', 'Helvetica', sans-serif; -fx-font-weight: bold;");
                    
                    statusBox.getChildren().addAll(statusIcon, statusLabel);
                    
                    cellContent.getChildren().addAll(doctorIcon, detailsBox, spacer, statusBox);
                    setGraphic(cellContent);
                    
                    // Add hover effects
                    cellContent.setOnMouseEntered(e -> {
                        if (!isSelected()) {
                            setStyle("-fx-background-color: #F8F9FA; -fx-border-color: #007BFF; -fx-border-width: 1; -fx-border-radius: 6; -fx-cursor: hand;");
                        }
                    });
                    
                    cellContent.setOnMouseExited(e -> {
                        if (!isSelected()) {
                            setStyle("-fx-background-color: white; -fx-border-color: #E9ECEF; -fx-border-width: 1; -fx-border-radius: 6;");
                        }
                    });
                    
                    // Selection styling
                    if (isSelected()) {
                        setStyle("-fx-background-color: #E3F2FD; -fx-border-color: #2196F3; -fx-border-width: 2; -fx-border-radius: 6;");
                    } else {
                        setStyle("-fx-background-color: white; -fx-border-color: #E9ECEF; -fx-border-width: 1; -fx-border-radius: 6;");
                    }
                }
            }
            
            @Override
            public void updateSelected(boolean selected) {
                super.updateSelected(selected);
                if (selected) {
                    setStyle("-fx-background-color: #E3F2FD; -fx-border-color: #2196F3; -fx-border-width: 2; -fx-border-radius: 6;");
                } else {
                    setStyle("-fx-background-color: white; -fx-border-color: #E9ECEF; -fx-border-width: 1; -fx-border-radius: 6;");
                }
            }
        });
    }

    private void setupTimeComboBox() {
        ObservableList<LocalTime> timeSlots = FXCollections.observableArrayList();
        timeSlots.addAll(appointmentDAO.getAvailableTimeSlots());
        timeComboBox.setItems(timeSlots);
        
        // Set custom cell factory to display time with better formatting
        timeComboBox.setCellFactory(listView -> new ListCell<LocalTime>() {
            @Override
            protected void updateItem(LocalTime time, boolean empty) {
                super.updateItem(time, empty);
                if (empty || time == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Format time in 12-hour format with AM/PM
                    String formattedTime = time.format(java.time.format.DateTimeFormatter.ofPattern("h:mm a"));
                    setText(formattedTime);
                    
                    // Add a clock icon for visual appeal
                    org.kordamp.ikonli.javafx.FontIcon clockIcon = new org.kordamp.ikonli.javafx.FontIcon();
                    clockIcon.setIconLiteral("fas-clock");
                    clockIcon.setIconSize(12);
                    clockIcon.setIconColor(javafx.scene.paint.Color.web("#6C757D"));
                    setGraphic(clockIcon);
                    
                    setStyle("-fx-font-family: 'Roboto', 'Arial', 'Helvetica', sans-serif; -fx-font-size: 13px; -fx-padding: 8 12;");
                }
            }
        });
        
        // Set button cell for the ComboBox display
        timeComboBox.setButtonCell(timeComboBox.getCellFactory().call(null));
    }

    private void setupDatePicker() {
        // Disable past dates and today
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                
                // Disable dates before tomorrow
                if (date.isBefore(today.plusDays(1))) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        });
        
        // Set default date to tomorrow
        datePicker.setValue(LocalDate.now().plusDays(1));
    }

    @FXML
    private void onScheduleClicked(ActionEvent event) {
        Doctor selectedDoctor = doctorListView.getSelectionModel().getSelectedItem();
        if (selectedDoctor == null) {
            statusLabel.setText("Please select a doctor.");
            statusLabel.setStyle("-fx-text-fill: #dc3545;");
            return;
        }

        LocalDate date = datePicker.getValue();
        if (date == null) {
            statusLabel.setText("Please pick a date.");
            statusLabel.setStyle("-fx-text-fill: #dc3545;");
            return;
        }

        LocalTime time = timeComboBox.getValue();
        if (time == null) {
            statusLabel.setText("Please select a time slot.");
            statusLabel.setStyle("-fx-text-fill: #dc3545;");
            return;
        }

        String illness = illnessArea.getText().trim();
        if (illness.isEmpty()) {
            statusLabel.setText("Please describe the illness.");
            statusLabel.setStyle("-fx-text-fill: #dc3545;");
            return;
        }

        LocalDateTime appointmentDateTime = LocalDateTime.of(date, time);
        
        // Validate appointment time
        if (!appointmentDAO.isValidAppointmentTime(appointmentDateTime)) {
            statusLabel.setText("Invalid time slot. Please select from available times.");
            statusLabel.setStyle("-fx-text-fill: #dc3545;");
            return;
        }

        // Check for doctor time conflicts
        if (appointmentDAO.hasTimeConflict(selectedDoctor.getId(), appointmentDateTime)) {
            statusLabel.setText("Doctor is not available at this time. Please select another slot.");
            statusLabel.setStyle("-fx-text-fill: #dc3545;");
            return;
        }

        // Check for patient time conflicts
        if (appointmentDAO.patientHasTimeConflict(CurrentPatientHolder.getPatientId(), appointmentDateTime)) {
            statusLabel.setText("You already have an appointment at this time. Please select another slot.");
            statusLabel.setStyle("-fx-text-fill: #dc3545;");
            return;
        }

        // Generate random 7-digit appointment ID
        int apptId = ThreadLocalRandom.current().nextInt(1_000_000, 10_000_000);

        Appointment appt = new Appointment(
            apptId,
            CurrentPatientHolder.getPatientId(),
            selectedDoctor.getId(),
            selectedDoctor.getFullname(),
            appointmentDateTime,
            illness
        );
        
        appointmentDAO.scheduleAppointment(appt);

        statusLabel.setStyle("-fx-text-fill: #28a745;");
        statusLabel.setText(
            String.format("Scheduled appointment %d with Dr. %s on %s at %s",
                        apptId, selectedDoctor.getFullname(), date, time)
        );
        
        // Clear form after successful booking
        clearForm();
    }
    
    private void clearForm() {
        doctorListView.getSelectionModel().clearSelection();
        datePicker.setValue(LocalDate.now().plusDays(1));
        timeComboBox.setValue(null);
        illnessArea.clear();
    }

    @FXML
    private void onBackClicked(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/ui/patient_dashboard.fxml"));
            Stage stage = (Stage) doctorListView.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void onMedicalRecordsClicked(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/ui/patient_medical_records.fxml"));
            Stage stage = (Stage) doctorListView.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
