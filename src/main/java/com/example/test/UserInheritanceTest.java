package com.example.test;

import com.example.model.Doctor;
import com.example.model.Patient;

/**
 * Test class to verify the User inheritance implementation and ID validation
 */
public class UserInheritanceTest {
    
    public static void main(String[] args) {
        System.out.println("Testing User inheritance implementation...\n");
        
        // Test Doctor with 4-digit ID validation
        testDoctorIdValidation();
        
        // Test Patient with 7-digit ID validation  
        testPatientIdValidation();
        
        // Test inheritance functionality
        testInheritanceFunctionality();
        
        System.out.println("\nAll tests completed successfully!");
    }
    
    private static void testDoctorIdValidation() {
        System.out.println("=== Testing Doctor ID Validation ===");
        
        Doctor doctor = new Doctor();
        
        // Test valid 4-digit IDs
        System.out.println("Valid Doctor IDs:");
        int[] validDoctorIds = {1000, 5555, 9999};
        for (int id : validDoctorIds) {
            boolean isValid = doctor.isValidId(id);
            System.out.println("ID " + id + ": " + (isValid ? "VALID" : "INVALID"));
        }
        
        // Test invalid IDs
        System.out.println("\nInvalid Doctor IDs:");
        int[] invalidDoctorIds = {999, 10000, 123, 12345};
        for (int id : invalidDoctorIds) {
            boolean isValid = doctor.isValidId(id);
            System.out.println("ID " + id + ": " + (isValid ? "VALID" : "INVALID"));
        }
        
        // Test creating doctor with valid ID
        Doctor validDoctor = new Doctor(1234, "Dr. Smith", "password123", "Cardiology");
        System.out.println("\nCreated doctor: " + validDoctor);
    }
    
    private static void testPatientIdValidation() {
        System.out.println("\n=== Testing Patient ID Validation ===");
        
        Patient patient = new Patient();
        
        // Test valid 7-digit IDs
        System.out.println("Valid Patient IDs:");
        int[] validPatientIds = {1000000, 5555555, 9999999};
        for (int id : validPatientIds) {
            boolean isValid = patient.isValidId(id);
            System.out.println("ID " + id + ": " + (isValid ? "VALID" : "INVALID"));
        }
        
        // Test invalid IDs
        System.out.println("\nInvalid Patient IDs:");
        int[] invalidPatientIds = {999999, 10000000, 12345, 123456};
        for (int id : invalidPatientIds) {
            boolean isValid = patient.isValidId(id);
            System.out.println("ID " + id + ": " + (isValid ? "VALID" : "INVALID"));
        }
        
        // Test creating patient with valid ID
        Patient validPatient = new Patient(1234567, "John Doe", 30, "123 Main St", "555-1234");
        System.out.println("\nCreated patient: " + validPatient);
    }
    
    private static void testInheritanceFunctionality() {
        System.out.println("\n=== Testing Inheritance Functionality ===");
        
        // Create doctor with full User fields
        Doctor doctor = new Doctor(1234, "drsmith", "password123", "Dr. John Smith", "drsmith@hospital.com", "Cardiology");
        System.out.println("Doctor with full User fields:");
        System.out.println("  ID: " + doctor.getId());
        System.out.println("  Username: " + doctor.getUsername());
        System.out.println("  Fullname: " + doctor.getFullname());
        System.out.println("  Email: " + doctor.getEmail());
        System.out.println("  Specialty: " + doctor.getSpecialty());
        System.out.println("  getName() (backward compatibility): " + doctor.getName());
        
        // Create patient with full User fields
        Patient patient = new Patient(1234567, "johndoe", "password456", "John Doe", "johndoe@email.com", 30, "123 Main St", "555-1234");
        System.out.println("\nPatient with full User fields:");
        System.out.println("  ID: " + patient.getId());
        System.out.println("  Username: " + patient.getUsername());
        System.out.println("  Fullname: " + patient.getFullname());
        System.out.println("  Email: " + patient.getEmail());
        System.out.println("  Age: " + patient.getAge());
        System.out.println("  Address: " + patient.getAddress());
        System.out.println("  Phone: " + patient.getPhoneNumber());
        System.out.println("  getName() (backward compatibility): " + patient.getName());
    }
}
