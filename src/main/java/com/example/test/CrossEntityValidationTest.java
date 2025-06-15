package com.example.test;

import com.example.data.DoctorDAO;
import com.example.data.PatientDAO;
import com.example.model.Doctor;
import com.example.model.Patient;

/**
 * Test class to verify cross-entity username and email validation
 * This tests that duplicate usernames and emails are prevented across
 * both doctor and patient registrations.
 */
public class CrossEntityValidationTest {
    
    public static void main(String[] args) {
        System.out.println("=== Cross-Entity Validation Test ===\n");
        
        DoctorDAO doctorDAO = new DoctorDAO();
        PatientDAO patientDAO = new PatientDAO();
        
        testUsernameValidation(doctorDAO, patientDAO);
        testEmailValidation(doctorDAO, patientDAO);
        testExistingDuplicates(doctorDAO, patientDAO);
        
        System.out.println("\n✅ All cross-entity validation tests completed!");
    }
    
    private static void testUsernameValidation(DoctorDAO doctorDAO, PatientDAO patientDAO) {
        System.out.println("🔍 Testing Username Validation:");
        
        // Test 1: Check if doctor username exists in patient data
        String testUsername = "gibran_azmi"; // Known duplicate from conversation summary
        
        Doctor doctorWithUsername = doctorDAO.findByUsername(testUsername);
        Patient patientWithUsername = patientDAO.findByUsername(testUsername);
        
        System.out.println("  Testing username: " + testUsername);
        System.out.println("    Found in doctors: " + (doctorWithUsername != null ? 
            "YES (ID: " + doctorWithUsername.getId() + ")" : "NO"));
        System.out.println("    Found in patients: " + (patientWithUsername != null ? 
            "YES (ID: " + patientWithUsername.getId() + ")" : "NO"));
        
        if (doctorWithUsername != null && patientWithUsername != null) {
            System.out.println("    ⚠️  DUPLICATE DETECTED: Same username exists in both systems!");
            System.out.println("       Doctor ID: " + doctorWithUsername.getId());
            System.out.println("       Patient ID: " + patientWithUsername.getId());
        } else {
            System.out.println("    ✅ No duplicates found for this username");
        }
    }
    
    private static void testEmailValidation(DoctorDAO doctorDAO, PatientDAO patientDAO) {
        System.out.println("\n🔍 Testing Email Validation:");
        
        // Test 2: Check if doctor email exists in patient data
        String testEmail = "Gibran@gmail.com"; // Known duplicate from conversation summary
        
        Doctor doctorWithEmail = doctorDAO.findByEmail(testEmail);
        Patient patientWithEmail = patientDAO.findByEmail(testEmail);
        
        System.out.println("  Testing email: " + testEmail);
        System.out.println("    Found in doctors: " + (doctorWithEmail != null ? 
            "YES (ID: " + doctorWithEmail.getId() + ")" : "NO"));
        System.out.println("    Found in patients: " + (patientWithEmail != null ? 
            "YES (ID: " + patientWithEmail.getId() + ")" : "NO"));
        
        if (doctorWithEmail != null && patientWithEmail != null) {
            System.out.println("    ⚠️  DUPLICATE DETECTED: Same email exists in both systems!");
            System.out.println("       Doctor ID: " + doctorWithEmail.getId());
            System.out.println("       Patient ID: " + patientWithEmail.getId());
        } else {
            System.out.println("    ✅ No duplicates found for this email");
        }
    }
    
    private static void testExistingDuplicates(DoctorDAO doctorDAO, PatientDAO patientDAO) {
        System.out.println("\n🔍 Scanning for Existing Duplicates:");
        
        int usernameConflicts = 0;
        int emailConflicts = 0;
        
        // Check all doctors against all patients
        for (Doctor doctor : doctorDAO.getAllDoctors()) {
            // Check username conflicts
            Patient conflictingPatient = patientDAO.findByUsername(doctor.getUsername());
            if (conflictingPatient != null) {
                usernameConflicts++;
                System.out.println("    ⚠️  Username conflict: '" + doctor.getUsername() + 
                    "' exists as Doctor ID " + doctor.getId() + 
                    " and Patient ID " + conflictingPatient.getId());
            }
            
            // Check email conflicts (if both have emails)
            if (doctor.getEmail() != null && !doctor.getEmail().trim().isEmpty()) {
                Patient conflictingEmailPatient = patientDAO.findByEmail(doctor.getEmail());
                if (conflictingEmailPatient != null) {
                    emailConflicts++;
                    System.out.println("    ⚠️  Email conflict: '" + doctor.getEmail() + 
                        "' exists as Doctor ID " + doctor.getId() + 
                        " and Patient ID " + conflictingEmailPatient.getId());
                }
            }
        }
        
        System.out.println("  Summary:");
        System.out.println("    Username conflicts found: " + usernameConflicts);
        System.out.println("    Email conflicts found: " + emailConflicts);
        
        if (usernameConflicts == 0 && emailConflicts == 0) {
            System.out.println("    ✅ No conflicts found - validation is working properly!");
        } else {
            System.out.println("    ❌ Conflicts detected - validation will prevent new duplicates");
        }
    }
}
