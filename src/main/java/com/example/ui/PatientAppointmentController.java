package com.example.ui;

import com.example.data.AppointmentDAO;
import com.example.data.LoginSessionDAO;
import com.example.model.Appointment;
import com.example.model.LoginSession;
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
 * Lets a registered patient choose a logged-in doctor, pick date/time, and enter illness.
 */
public class PatientAppointmentController {
    @FXML private ListView<LoginSession> doctorListView;
    @FXML private DatePicker datePicker;
    @FXML private TextField timeField;
    @FXML private TextArea illnessArea;
    @FXML private Label statusLabel;

    private final LoginSessionDAO sessionDAO = new LoginSessionDAO();
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();

    // This method is called automatically after FXML is loaded.
    @FXML
    public void initialize() {
        loadLoggedInDoctors();
    }

    private void loadLoggedInDoctors() {
         CustomeLinkedList<LoginSession> sessions = sessionDAO.getAllSessions();

        // 2) Build an ObservableList<LoginSession> by iterating our CustomeLinkedList
        ObservableList<LoginSession> items = FXCollections.observableArrayList();
        for (LoginSession s : sessions) {
            items.add(s);
        }
        doctorListView.setItems(items);

        // 3) Use a cell factory so each LoginSession appears as “ID | Name | Time”
        doctorListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(LoginSession session, boolean empty) {
                super.updateItem(session, empty);
                if (empty || session == null) {
                    setText(null);
                } else {
                    // We rely on LoginSession.toString(): “1234 | Dr. Bob | 2025-06-05T14:22”
                    setText(session.toString());
                }
            }
        });
    }

    @FXML
    private void onScheduleClicked(ActionEvent event) {
        LoginSession selectedSession = doctorListView.getSelectionModel().getSelectedItem();
        if (selectedSession == null) {
            statusLabel.setText("Please select a doctor.");
            return;
        }

        int doctorId = selectedSession.getDoctorId();
        String doctorName = selectedSession.getDoctorName();

        // Now all the date/time/illness checks are unchanged…

        LocalDate date = datePicker.getValue();
        if (date == null) {
            statusLabel.setText("Please pick a date.");
            return;
        }

        String timeText = timeField.getText().trim();
        if (timeText.isEmpty()) {
            statusLabel.setText("Please enter a time (HH:mm).");
            return;
        }
        LocalTime time;
        try {
            time = LocalTime.parse(timeText);
        } catch (Exception ex) {
            statusLabel.setText("Time must be in HH:mm format.");
            return;
        }

        String illness = illnessArea.getText().trim();
        if (illness.isEmpty()) {
            statusLabel.setText("Please describe the illness.");
            return;
        }

        // Generate random 7-digit appointment ID
        int apptId = ThreadLocalRandom.current().nextInt(1_000_000, 10_000_000);
        LocalDateTime apptDateTime = LocalDateTime.of(date, time);

        Appointment appt = new Appointment(
            apptId,
            CurrentPatientHolder.getPatientId(),
            doctorId,
            doctorName,
            apptDateTime,
            illness
        );
        appointmentDAO.scheduleAppointment(appt);

        statusLabel.setStyle("-fx-text-fill: #00ff00;");
        statusLabel.setText(
            String.format("Scheduled appt %d with Dr. %s on %s %s",
                        apptId, doctorName, date, time)
        );
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
