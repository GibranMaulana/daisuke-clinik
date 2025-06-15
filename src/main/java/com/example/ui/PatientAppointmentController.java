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
    }

    private void loadAllDoctors() {
        CustomeLinkedList<Doctor> doctors = doctorDAO.getAllDoctors();

        // Build an ObservableList<Doctor> by iterating our CustomeLinkedList
        ObservableList<Doctor> items = FXCollections.observableArrayList();
        for (Doctor doctor : doctors) {
            items.add(doctor);
        }
        doctorListView.setItems(items);

        // Set custom cell factory to display doctor information nicely
        doctorListView.setCellFactory(listView -> new ListCell<Doctor>() {
            @Override
            protected void updateItem(Doctor doctor, boolean empty) {
                super.updateItem(doctor, empty);
                if (empty || doctor == null) {
                    setText(null);
                } else {
                    setText(String.format("Dr. %s - %s (ID: %d)", 
                           doctor.getFullname(), doctor.getSpecialty(), doctor.getId()));
                }
            }
        });
    }

    private void setupTimeComboBox() {
        ObservableList<LocalTime> timeSlots = FXCollections.observableArrayList();
        timeSlots.addAll(appointmentDAO.getAvailableTimeSlots());
        timeComboBox.setItems(timeSlots);
        
        // Set custom cell factory to display time nicely
        timeComboBox.setCellFactory(listView -> new ListCell<LocalTime>() {
            @Override
            protected void updateItem(LocalTime time, boolean empty) {
                super.updateItem(time, empty);
                if (empty || time == null) {
                    setText(null);
                } else {
                    setText(time.toString());
                }
            }
        });
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
}
