package com.example.util;

import com.example.data.AppointmentDAO;
import com.example.data.PatientDAO;
import com.example.model.Appointment;
import com.example.model.ds.CustomeLinkedList;

/**
 * Utility class for validating data integrity across the hospital system
 */
public class DataIntegrityValidator {
    
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final PatientDAO patientDAO = new PatientDAO();
    
    /**
     * Check for appointments that reference non-existent patients
     * @return number of invalid appointments found
     */
    public int validateAppointmentPatientReferences() {
        CustomeLinkedList<Appointment> allAppointments = appointmentDAO.getAllAppointments();
        int invalidCount = 0;
        
        System.out.println("=== Data Integrity Check: Appointment-Patient References ===");
        
        for (Appointment appointment : allAppointments) {
            int patientId = appointment.getPatientId();
            if (patientDAO.findById(patientId) == null) {
                System.err.println("WARNING: Appointment " + appointment.getAppointmentId() + 
                                 " references non-existent patient ID: " + patientId);
                invalidCount++;
            }
        }
        
        if (invalidCount == 0) {
            System.out.println("✓ All appointment-patient references are valid");
        } else {
            System.err.println("✗ Found " + invalidCount + " invalid appointment-patient references");
        }
        
        return invalidCount;
    }
    
    /**
     * Check for any other data integrity issues
     */
    public void runFullIntegrityCheck() {
        System.out.println("=== Full Data Integrity Check ===");
        
        int invalidAppointments = validateAppointmentPatientReferences();
        
        // Add more checks here in the future
        
        System.out.println("=== Data Integrity Check Complete ===");
        if (invalidAppointments == 0) {
            System.out.println("✓ All data integrity checks passed");
        } else {
            System.err.println("✗ Data integrity issues found - please review and fix");
        }
    }
}
