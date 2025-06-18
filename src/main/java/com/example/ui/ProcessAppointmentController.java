package com.example.ui;

import com.example.data.AppointmentDAO;
import com.example.data.PatientDAO;
import com.example.data.DiagnosisDAO;
import com.example.model.Appointment;
import com.example.model.Patient;
import com.example.model.Diagnosis;
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
    @FXML private TextArea patientComplaintArea;
    @FXML private TextArea recommendedMedicineArea;

    @FXML private Label statusLabel;

    private Appointment currentAppointment;
    private final PatientDAO patientDAO = new PatientDAO();
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final DiagnosisDAO diagnosisDAO = new DiagnosisDAO();

    /**
     * Called by DoctorDashboardController when this FXML is loaded.
     */
    public void setAppointment(Appointment appt) {
        this.currentAppointment = appt;
        // 1) Show appointment ID and date/time
        headerApptIdLabel.setText("Appointment ID: " + appt.getAppointmentId());
        apptDateTimeField.setText(appt.getTime().toString());

        // 2) Load the patient's data
        int pid = appt.getPatientId();
        Patient p = patientDAO.findById(pid);

        apptPatientIdField.setText(String.valueOf(pid));
        
        // Handle case where patient is not found (data inconsistency)
        if (p == null) {
            System.err.println("ERROR: Patient with ID " + pid + " not found! Data inconsistency detected.");
            apptPatientNameField.setText("PATIENT NOT FOUND (ID: " + pid + ")");
            apptPatientAgeField.setText("N/A");
            apptPatientAddressField.setText("N/A");
            apptPatientPhoneField.setText("N/A");
            apptHistoryArea.setText("Cannot load patient history - patient record not found.");
            return;
        }
        
        apptPatientNameField.setText(p.getName());
        apptPatientAgeField.setText(String.valueOf(p.getAge()));
        apptPatientAddressField.setText(p.getAddress());
        apptPatientPhoneField.setText(p.getPhoneNumber());

        // 3) Show the patient's existing illness history
        StringBuilder sb = new StringBuilder();
        CustomeLinkedList<Diagnosis> history = p.getIllnessHistory();
        for (Diagnosis diagnosis : history) {
            sb.append("• ").append(diagnosis.getPatientComplaint()).append(" - ")
              .append(diagnosis.getDoctorDiagnosis()).append("\n");
        }
        apptHistoryArea.setText(sb.toString());
    }

    /**
     * Called when the doctor clicks "Finish Appointment".
     * - Create a diagnosis record with patient complaint, doctor diagnosis, and recommended medicine
     * - Add the diagnosis to the patient's illness history
     * - Remove this appointment from the queue
     * - Return to doctor dashboard
     */
    @FXML
    private void onFinishClicked(ActionEvent event) {
        String doctorNotes = apptDoctorNotesArea.getText().trim();
        String patientComplaint = patientComplaintArea.getText().trim();
        String recommendedMedicine = recommendedMedicineArea.getText().trim();
        
        if (!doctorNotes.isEmpty() && !patientComplaint.isEmpty()) {
            // Create a new diagnosis record
            Diagnosis diagnosis = new Diagnosis(
                diagnosisDAO.generateUniqueId(),
                CurrentDoctorHolder.getDoctor().getId(),
                currentAppointment.getPatientId(),
                patientComplaint,
                doctorNotes,
                recommendedMedicine,
                java.time.LocalDateTime.now(),
                currentAppointment.getAppointmentId()
            );
            
            // Save the diagnosis
            diagnosisDAO.addDiagnosis(diagnosis);
            
            // Add diagnosis to patient's illness history
            int pid = currentAppointment.getPatientId();
            Patient p = patientDAO.findById(pid);
            if (p != null) {
                p.getIllnessHistory().add(diagnosis);
                patientDAO.updatePatient(p);
            } else {
                System.err.println("WARNING: Cannot update patient history - patient with ID " + pid + " not found");
            }
        }

        // 2) Remove this appointment from the queue
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
        String doctorNotes = apptDoctorNotesArea.getText().trim();
        String patientComplaint = patientComplaintArea.getText().trim();
        String recommendedMedicine = recommendedMedicineArea.getText().trim();
        
        if (!doctorNotes.isEmpty() && !patientComplaint.isEmpty()) {
            // Create a new diagnosis record
            Diagnosis diagnosis = new Diagnosis(
                diagnosisDAO.generateUniqueId(),
                CurrentDoctorHolder.getDoctor().getId(),
                currentAppointment.getPatientId(),
                patientComplaint,
                doctorNotes,
                recommendedMedicine,
                java.time.LocalDateTime.now(),
                currentAppointment.getAppointmentId()
            );
            
            // Save the diagnosis
            diagnosisDAO.addDiagnosis(diagnosis);
            
            // Add diagnosis to patient's illness history
            int pid = currentAppointment.getPatientId();
            Patient p = patientDAO.findById(pid);
            if (p != null) {
                p.getIllnessHistory().add(diagnosis);
                patientDAO.updatePatient(p);
            } else {
                System.err.println("WARNING: Cannot update patient history - patient with ID " + pid + " not found");
            }
            
            statusLabel.setText("Diagnosis saved successfully!");
            statusLabel.setStyle("-fx-text-fill: #28a745;");
        } else {
            statusLabel.setText("Please fill in both patient complaint and doctor diagnosis.");
            statusLabel.setStyle("-fx-text-fill: #ffc107;");
        }
    }

    /**
     * Cancel appointment processing and return to dashboard
     */
    @FXML
    private void onCancelClicked(ActionEvent event) {
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
}
