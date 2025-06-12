package com.example.ui;

import com.example.data.AppointmentDAO;
import com.example.data.PatientDAO;
import com.example.model.Appointment;
import com.example.model.Patient;
import com.example.model.ds.CustomeLinkedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for process-appointment-view.fxml:
 *   - Receives an Appointment object from the dashboard
 *   - Displays appointment + patient details
 *   - Lets the doctor add a note (or append to patient history)
 *   - Finishes the flow and returns to dashboard
 */
public class ProcessAppointmentController {

    @FXML private Label headerApptIdLabel;
    @FXML private TextField apptDateTimeField;
    @FXML private TextField apptPatientIdField;
    @FXML private TextField apptPatientNameField;
    @FXML private TextField apptPatientAgeField;
    @FXML private TextField apptPatientAddressField;
    @FXML private TextField apptPatientPhoneField;

    @FXML private TextArea apptHistoryArea;
    @FXML private TextArea apptDoctorNotesArea;

    @FXML private Label statusLabel;

    private Appointment currentAppointment;
    private final PatientDAO patientDAO = new PatientDAO();
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();

    /**
     * Called by DoctorDashboardController when this FXML is loaded.
     */
    public void setAppointment(Appointment appt) {
        this.currentAppointment = appt;

        // 1) Show appointment ID and date/time
        headerApptIdLabel.setText("Appointment ID: " + appt.getAppointmentId());
        apptDateTimeField.setText(appt.getTime().toString());

        // 2) Load the patient’s data
        int pid = appt.getPatientId();
        Patient p = patientDAO.findById(pid);

        apptPatientIdField.setText(String.valueOf(pid));
        apptPatientNameField.setText(p.getName());
        apptPatientAgeField.setText(String.valueOf(p.getAge()));
        apptPatientAddressField.setText(p.getAddress());
        apptPatientPhoneField.setText(p.getPhoneNumber());

        // 3) Show the patient’s existing illness history
        StringBuilder sb = new StringBuilder();
        CustomeLinkedList<String> history = p.getIllnessHistory();
        for (String entry : history) {
            sb.append(entry).append("\n");
        }
        apptHistoryArea.setText(sb.toString());
    }

    /**
     * Called when the doctor clicks “Finish Appointment”.
     * - Take whatever the doctor typed into apptDoctorNotesArea:
     *   if non‐empty, append it to the patient’s history and save.
     * - Process the current appointment.
     * - Then return to the dashboard.
     */
    @FXML
    private void onFinishClicked(ActionEvent event) {
        String notes = apptDoctorNotesArea.getText().trim();
        int pid = currentAppointment.getPatientId();
        if (!notes.isEmpty()) {
            // 1) Append notes to patient's illnessHistory
            Patient p = patientDAO.findById(pid);
            p.addIllness(notes);
            patientDAO.updatePatient(p);
        }

        // 2) Process (dequeue) this appointment
        appointmentDAO.processNextAppointment(CurrentDoctorHolder.getDoctor().getId());

        // 3) Return to doctor dashboard
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ui/doctor_dashboard.fxml"));
            Parent root = loader.load();
            DoctorDashboardController dashCtrl = loader.getController();
            dashCtrl.setLoggedInDoctor(CurrentDoctorHolder.getDoctor());
            Stage stage = (Stage) headerApptIdLabel.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * “Cancel” just returns to the dashboard without saving notes.
     */
    @FXML
    private void onCancelClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ui/doctor_dashboard.fxml"));
            Parent root = loader.load();
            DoctorDashboardController dashCtrl = loader.getController();
            // Reload the same doctor
            dashCtrl.setLoggedInDoctor(CurrentDoctorHolder.getDoctor());
            Stage stage = (Stage) headerApptIdLabel.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Navigation method to return to doctor dashboard
     */
    @FXML
    private void onBackToDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ui/doctor_dashboard.fxml"));
            Parent root = loader.load();
            DoctorDashboardController dashCtrl = loader.getController();
            dashCtrl.setLoggedInDoctor(CurrentDoctorHolder.getDoctor());
            Stage stage = (Stage) headerApptIdLabel.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Navigation method to view all patients
     */
    @FXML
    private void onViewAllPatients(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ui/doctor_patient_view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) headerApptIdLabel.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Save appointment notes without finishing the appointment
     */
    @FXML
    private void onSaveClicked(ActionEvent event) {
        String notes = apptDoctorNotesArea.getText().trim();
        if (!notes.isEmpty()) {
            int pid = currentAppointment.getPatientId();
            Patient p = patientDAO.findById(pid);
            p.addIllness(notes);
            patientDAO.updatePatient(p);
            statusLabel.setText("Notes saved successfully!");
            statusLabel.setStyle("-fx-text-fill: #28a745;");
        } else {
            statusLabel.setText("No notes to save.");
            statusLabel.setStyle("-fx-text-fill: #ffc107;");
        }
    }
}
