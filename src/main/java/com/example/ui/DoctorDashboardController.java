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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class DoctorDashboardController {
    @FXML private Label welcomeLabel;
    @FXML private ListView<String> appointmentListView;

    private Doctor loggedInDoctor;
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final LoginSessionDAO sessionDAO = new LoginSessionDAO();

    /**
     * Called right after FXML is loaded by DoctorLoginController.
     */
    public void setLoggedInDoctor(Doctor d) {
        this.loggedInDoctor = d;
        welcomeLabel.setText("Welcome, Dr. " + d.getName() + " (ID " + d.getId() + ")");
        refreshAppointmentList();
    }

    private void refreshAppointmentList() {
        appointmentListView.getItems().clear();
        CustomeLinkedList<Appointment> queue = appointmentDAO.getQueueForDoctor(loggedInDoctor.getId());
        ObservableList<String> items = FXCollections.observableArrayList();
        for (Appointment a : queue) {
            // Format each appointment as “ApptID | PatientID | Time | Illness”
            String entry = String.format("%d | P:%d | %s | %s",
                    a.getAppointmentId(),
                    a.getPatientId(),
                    a.getTime().toString(),
                    a.getPatientIllness());
            items.add(entry);
        }
        appointmentListView.setItems(items);
    }

    @FXML
    private void onProcessNextClicked(ActionEvent event) {
        Appointment processed = appointmentDAO.processNextAppointment(loggedInDoctor.getId());
        if (processed != null) {
            // Optionally show a pop‐up or console message
            System.out.println("Processed: " + processed);
        } else {
            System.out.println("No appointments to process.");
        }
        refreshAppointmentList();
    }

    @FXML
    private void onLogoutClicked(ActionEvent event) {
        sessionDAO.removeSession(loggedInDoctor.getId());
     
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/ui/doctor-login.fxml"));
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
