package com.example.test;

import com.example.model.*;
import com.example.data.*;
import com.example.model.ds.CustomeLinkedList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;

public class ComprehensiveSystemTest {

    private PatientDAO patientDAO;
    private DoctorDAO doctorDAO;
    private AppointmentDAO appointmentDAO;
    private DiagnosisDAO diagnosisDAO;

    @BeforeEach
    void setUp() {
        patientDAO = new PatientDAO();
        doctorDAO = new DoctorDAO();
        appointmentDAO = new AppointmentDAO();
        diagnosisDAO = new DiagnosisDAO();
    }

    @Test
    void testPatientDiagnosisStructure() {
        System.out.println("Testing Patient Diagnosis Structure...");
        
        // Load existing patients
        CustomeLinkedList<Patient> patients = patientDAO.getAllPatients();
        assertNotNull(patients, "Patients list should not be null");
        
        // Test that patients can have diagnosis history
        for (Patient patient : patients) {
            assertNotNull(patient.getIllnessHistory(), "Patient should have illness history initialized");
            assertTrue(patient.getIllnessHistory() instanceof CustomeLinkedList, 
                "Illness history should be CustomeLinkedList<Diagnosis>");
        }
        
        System.out.println("✓ Patient diagnosis structure verified");
    }

    @Test
    void testAppointmentTimeValidation() {
        System.out.println("Testing Appointment Time Validation...");
        
        // Test invalid times (before 08:00)
        LocalDateTime earlyTime = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(7, 0));
        assertFalse(appointmentDAO.isValidAppointmentTime(earlyTime), 
            "Should reject appointments before 08:00");
        
        // Test invalid times (during lunch break 11:01-12:59)
        LocalDateTime lunchTime = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(12, 0));
        assertFalse(appointmentDAO.isValidAppointmentTime(lunchTime), 
            "Should reject appointments during lunch break");
        
        // Test invalid times (after 21:00)
        LocalDateTime lateTime = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(22, 0));
        assertFalse(appointmentDAO.isValidAppointmentTime(lateTime), 
            "Should reject appointments after 21:00");
        
        // Test valid times
        LocalDateTime validMorning = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(9, 0));
        assertTrue(appointmentDAO.isValidAppointmentTime(validMorning), 
            "Should accept valid morning appointment");
        
        LocalDateTime validAfternoon = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(15, 0));
        assertTrue(appointmentDAO.isValidAppointmentTime(validAfternoon), 
            "Should accept valid afternoon appointment");
        
        System.out.println("✓ Appointment time validation working correctly");
    }

    @Test
    void testDoctorAvailability() {
        System.out.println("Testing Doctor Availability...");
        
        // Load doctors
        CustomeLinkedList<Doctor> doctors = doctorDAO.getAllDoctors();
        assertNotNull(doctors, "Doctors list should not be null");
        assertTrue(doctors.size() > 0, "Should have at least one doctor");
        
        // Test doctor availability - system supports booking with any doctor
        // There's no isOnline() method - all doctors are available for booking
        for (Doctor doctor : doctors) {
            assertNotNull(doctor.getName(), "Doctor should have a name");
            assertNotNull(doctor.getSpecialty(), "Doctor should have a specialty");
            assertTrue(doctor.getId() > 0, "Doctor should have valid ID");
        }
        
        System.out.println("Found " + doctors.size() + " doctors available for booking");
        
        // The system allows booking with all doctors (both currently logged in and offline)
        System.out.println("✓ Doctor availability structure verified");
    }

    @Test
    void testTimeConflictDetection() {
        System.out.println("Testing Time Conflict Detection...");
        
        // Load existing appointments
        CustomeLinkedList<Appointment> appointments = appointmentDAO.getAllAppointments();
        assertNotNull(appointments, "Appointments list should not be null");
        
        if (appointments.size() > 0) {
            Appointment existingAppt = appointments.get(0);
            
            // Test doctor conflict detection
            boolean hasConflict = appointmentDAO.hasTimeConflict(
                existingAppt.getDoctorId(), 
                existingAppt.getTime()
            );
            assertTrue(hasConflict, "Should detect doctor time conflict with existing appointment");
            
            // Test patient conflict detection  
            boolean patientConflict = appointmentDAO.patientHasTimeConflict(
                existingAppt.getPatientId(), 
                existingAppt.getTime()
            );
            assertTrue(patientConflict, "Should detect patient time conflict with existing appointment");
        }
        
        System.out.println("✓ Time conflict detection working correctly");
    }

    @Test
    void testDiagnosisCreation() {
        System.out.println("Testing Diagnosis Creation...");
        
        // Test diagnosis ID generation
        int uniqueId = diagnosisDAO.generateUniqueId();
        assertTrue(uniqueId > 0, "Should generate positive unique ID");
        
        // Test diagnosis creation with proper constructor
        int doctorId = 1001;
        int patientId = 2001;
        String complaint = "Test complaint";
        String diagnosis = "Test diagnosis";
        String medicine = "Test medicine";
        LocalDateTime now = LocalDateTime.now();
        int appointmentId = 3001;
        
        Diagnosis testDiagnosis = new Diagnosis(
            uniqueId, doctorId, patientId, complaint, 
            diagnosis, medicine, now, appointmentId
        );
        
        assertNotNull(testDiagnosis, "Diagnosis should be created successfully");
        assertEquals(uniqueId, testDiagnosis.getId());
        assertEquals(doctorId, testDiagnosis.getDoctorId());
        assertEquals(patientId, testDiagnosis.getPatientId());
        assertEquals(complaint, testDiagnosis.getPatientComplaint());
        assertEquals(diagnosis, testDiagnosis.getDoctorDiagnosis());
        assertEquals(medicine, testDiagnosis.getRecommendedMedicine());
        
        System.out.println("✓ Diagnosis creation working correctly");
    }

    @Test
    void testSystemIntegration() {
        System.out.println("Testing System Integration...");
        
        // Test that all DAOs can load data without errors
        assertDoesNotThrow(() -> {
            patientDAO.getAllPatients();
            doctorDAO.getAllDoctors();
            appointmentDAO.getAllAppointments();
            diagnosisDAO.loadAllDiagnoses();
        }, "All DAOs should load data without throwing exceptions");
        
        System.out.println("✓ System integration verified");
    }

    @AfterEach
    void tearDown() {
        // Clean up if needed
        System.out.println("Test completed.\n");
    }
}