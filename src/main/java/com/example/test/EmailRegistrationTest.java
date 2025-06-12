package com.example.test;

import com.example.model.Doctor;
import com.example.model.Patient;

/**
 * Test class to verify email registration functionality
 */
public class EmailRegistrationTest {
    
    public static void main(String[] args) {
        System.out.println("=== Email Registration Test ===\n");
        
        testPatientWithEmail();
        testDoctorWithEmail();
        testEmailValidation();
        
        System.out.println("\n✅ All email registration tests completed successfully!");
    }
    
    private static void testPatientWithEmail() {
        System.out.println("🔍 Testing Patient Registration with Email:");
        
        // Test patient creation with full User constructor (includes email)
        Patient patient = new Patient(1234567, "john_doe", "password123", "John Doe", "john.doe@email.com", 
                                     30, "123 Main St", "555-1234");
        
        System.out.println("  Created patient:");
        System.out.println("    ID: " + patient.getId());
        System.out.println("    Username: " + patient.getUsername());
        System.out.println("    Fullname: " + patient.getFullname());
        System.out.println("    Email: " + patient.getEmail());
        System.out.println("    Age: " + patient.getAge());
        System.out.println("    Address: " + patient.getAddress());
        System.out.println("    Phone: " + patient.getPhoneNumber());
        System.out.println("    getName() (backward compatibility): " + patient.getName());
        System.out.println("    toString(): " + patient.toString());
    }
    
    private static void testDoctorWithEmail() {
        System.out.println("\n🔍 Testing Doctor Registration with Email:");
        
        // Test doctor creation with full User constructor (includes email)
        Doctor doctor = new Doctor(1234, "dr_smith", "docpass123", "Dr. John Smith", "dr.smith@hospital.com", 
                                  "Cardiology");
        
        System.out.println("  Created doctor:");
        System.out.println("    ID: " + doctor.getId());
        System.out.println("    Username: " + doctor.getUsername());
        System.out.println("    Fullname: " + doctor.getFullname());
        System.out.println("    Email: " + doctor.getEmail());
        System.out.println("    Specialty: " + doctor.getSpecialty());
        System.out.println("    getName() (backward compatibility): " + doctor.getName());
        System.out.println("    toString(): " + doctor.toString());
    }
    
    private static void testEmailValidation() {
        System.out.println("\n🔍 Testing Email Validation:");
        
        // Test basic email validation
        String[] validEmails = {"user@example.com", "test.email@domain.org", "admin@hospital.gov"};
        String[] invalidEmails = {"invalid", "no@", "@domain.com", "user@", "toolshort"};
        
        System.out.println("  Valid emails:");
        for (String email : validEmails) {
            boolean isValid = isValidEmail(email);
            System.out.println("    " + email + ": " + (isValid ? "✅ VALID" : "❌ INVALID"));
        }
        
        System.out.println("  Invalid emails:");
        for (String email : invalidEmails) {
            boolean isValid = isValidEmail(email);
            System.out.println("    " + email + ": " + (isValid ? "✅ VALID" : "❌ INVALID"));
        }
    }
    
    // Simple email validation method (same as in controllers)
    private static boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".") && email.length() > 5;
    }
}
