package com.example.data;

import com.example.model.Doctor;
import com.example.model.Patient;
import com.example.model.Appointment;
import com.example.model.ds.CustomeLinkedList;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

public class DAOTest {
    public static void main(String[] args) {
        try {
            // ────────────────────────────────────────────────────────────────────
            // 1) PatientDAO with a random 7-digit ID
            // ────────────────────────────────────────────────────────────────────
            new File("patients.json").delete();
            PatientDAO pdao = new PatientDAO();

            // Generate a random 7-digit patient ID (from 1_000_000 to 9_999_999)
            int randomPatientId = ThreadLocalRandom.current().nextInt(1_000_000, 10_000_000);
            Patient p = new Patient(
                randomPatientId,
                "Alice",
                30,
                "123 Main St",
                "555-1234"
            );
            pdao.registerPatient(p);

            Patient fetched = pdao.findById(randomPatientId);
            System.out.println("Generated patient ID: " + randomPatientId);
            System.out.println("Fetched patient: " + fetched);

            // ────────────────────────────────────────────────────────────────────
            // 2) DoctorDAO with a random 4-digit ID
            // ────────────────────────────────────────────────────────────────────
            new File("doctors.json").delete();
            DoctorDAO ddao = new DoctorDAO();

            // Generate a random 4-digit doctor ID (from 1000 to 9999)
            int randomDoctorId = ThreadLocalRandom.current().nextInt(1000, 10_000);
            Doctor d = new Doctor(
                randomDoctorId,
                "Dr. Bob",
                "password123",
                "Cardiology"
            );
            ddao.registerDoctor(d);

            Doctor dfetched = ddao.findById(randomDoctorId);
            System.out.println("Generated doctor ID: " + randomDoctorId);
            System.out.println("Fetched doctor: " + dfetched);

            // ────────────────────────────────────────────────────────────────────
            // 3) AppointmentDAO with a random 7-digit appointment ID
            // ────────────────────────────────────────────────────────────────────
            File apptFile = new File("allAppointments.json");
            apptFile.delete();
            AppointmentDAO adao = new AppointmentDAO();

            // Generate a random 7-digit appointment ID (from 1_000_000 to 9_999_999)
            int randomApptId = ThreadLocalRandom.current().nextInt(1_000_000, 10_000_000);
            Appointment appt = new Appointment(
                randomApptId,          // random appointmentId
                randomPatientId,       // patientId (the one we generated above)
                randomDoctorId,        // doctorId
                d.getSpecialty(),
                LocalDateTime.of(2025, 6, 10, 14, 30),
                "Mild fever"
            );
            adao.scheduleAppointment(appt);

            System.out.println("\n--- After scheduleAppointment(...) ---");
            printFileContents("allAppointments.json");

            CustomeLinkedList<Appointment> queue = adao.getQueueForDoctor(randomDoctorId);
            System.out.println("Doctor " + randomDoctorId + " queue: " + queue);

            Appointment processed = adao.processNextAppointment(randomDoctorId);
            System.out.println("Processed: " + processed);

            System.out.println("\n--- After processNextAppointment(...) ---");
            printFileContents("allAppointments.json");

            CustomeLinkedList<Appointment> queueAfter = adao.getQueueForDoctor(randomDoctorId);
            System.out.println("Queue now: " + queueAfter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Utility: read and print the entire contents of the given file path.
     */
    private static void printFileContents(String path) {
        File f = new File(path);
        if (!f.exists()) {
            System.out.println("File \"" + path + "\" does not exist.");
            return;
        }
        try {
            byte[] bytes = Files.readAllBytes(f.toPath());
            String content = new String(bytes);
            System.out.println("Contents of \"" + f.getAbsolutePath() + "\":");
            System.out.println(content.trim().isEmpty() ? "(empty file)" : content);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
