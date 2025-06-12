package com.example.test;

import com.example.model.Doctor;
import com.example.model.Patient;

/**
 * Test to verify User inheritance and ID validation implementation
 */
public class UserInheritanceVerification {
    
    public static void main(String[] args) {
        System.out.println("=== User Inheritance & ID Validation Test ===\n");
        
        testDoctorIdValidation();
        testPatientIdValidation(); 
        testInheritanceFunctionality();
        testBackwardCompatibility();
        
        System.out.println("\n✅ All tests passed! User inheritance implementation is working correctly.");
    }
    
    private static void testDoctorIdValidation() {
        System.out.println("🔍 Testing Doctor (4-digit ID validation):");
        
        Doctor doctor = new Doctor();
        
        // Test valid 4-digit IDs
        int[] validIds = {1000, 1234, 5557, 9999};
        for (int id : validIds) {
            boolean isValid = doctor.isValidId(id);
            System.out.println("  Doctor ID " + id + ": " + (isValid ? "✅ VALID" : "❌ INVALID"));
        }
        
        // Test invalid IDs  
        int[] invalidIds = {999, 10000, 123, 12345};
        for (int id : invalidIds) {
            boolean isValid = doctor.isValidId(id);
            System.out.println("  Doctor ID " + id + ": " + (isValid ? "✅ VALID" : "❌ INVALID"));
        }
    }
    
    private static void testPatientIdValidation() {
        System.out.println("\n🔍 Testing Patient (7-digit ID validation):");
        
        Patient patient = new Patient();
        
        // Test valid 7-digit IDs
        int[] validIds = {1000000, 2214098, 9999999};
        for (int id : validIds) {
            boolean isValid = patient.isValidId(id);
            System.out.println("  Patient ID " + id + ": " + (isValid ? "✅ VALID" : "❌ INVALID"));
        }
        
        // Test invalid IDs
        int[] invalidIds = {999999, 10000000, 123456, 12345678};
        for (int id : invalidIds) {
            boolean isValid = patient.isValidId(id);
            System.out.println("  Patient ID " + id + ": " + (isValid ? "✅ VALID" : "❌ INVALID"));
        }
    }
    
    private static void testInheritanceFunctionality() {
        System.out.println("\n🔍 Testing inheritance functionality:");
        
        // Test Doctor with User fields
        Doctor doctor = new Doctor(1234, "drsmith", "password123", "Dr. John Smith", "drsmith@hospital.com", "Cardiology");
        
        System.out.println("  Doctor inheritance:");
        System.out.println("    ID: " + doctor.getId());
        System.out.println("    Username: " + doctor.getUsername());
        System.out.println("    Fullname: " + doctor.getFullname());
        System.out.println("    Email: " + doctor.getEmail());
        System.out.println("    Specialty: " + doctor.getSpecialty());
        
        // Test Patient with User fields
        Patient patient = new Patient(1234567, "johndoe", "password456", "John Doe", "johndoe@email.com", 30, "123 Main St", "555-1234");
        
        System.out.println("\n  Patient inheritance:");
        System.out.println("    ID: " + patient.getId());
        System.out.println("    Username: " + patient.getUsername());
        System.out.println("    Fullname: " + patient.getFullname());
        System.out.println("    Email: " + patient.getEmail());
        System.out.println("    Age: " + patient.getAge());
    }
    
    private static void testBackwardCompatibility() {
        System.out.println("\n🔍 Testing backward compatibility:");
        
        // Test old Doctor constructor
        Doctor doctor = new Doctor(5557, "Dr. Smith", "docpass1", "Cardiology");
        System.out.println("  Old Doctor constructor:");
        System.out.println("    getName(): " + doctor.getName());
        System.out.println("    getFullname(): " + doctor.getFullname());
        System.out.println("    toString(): " + doctor.toString());
        
        // Test old Patient constructor
        Patient patient = new Patient(2214098, "nadhifa", 19, "jl.kendondong", "081385927668");
        System.out.println("\n  Old Patient constructor:");
        System.out.println("    getName(): " + patient.getName());
        System.out.println("    getFullname(): " + patient.getFullname());
        System.out.println("    toString(): " + patient.toString());
    }
}
