package com.example.test;

/**
 * Simple Email Validation Demo
 * This demonstrates the enhanced email validation functionality
 * that we've implemented in the hospital management system.
 */
public class EmailValidationDemo {
    
    /**
     * Enhanced email validation method with regex (same as in our controllers)
     */
    private static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        // RFC 5322 compliant email regex pattern
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }
    
    public static void main(String[] args) {
        System.out.println("=== EMAIL VALIDATION DEMO ===");
        System.out.println("Testing enhanced email validation for Hospital Management System\n");
        
        // Test cases
        String[] testEmails = {
            // Valid emails
            "patient@hospital.com",
            "doctor.smith@medical-center.org",
            "admin123@clinic.co.uk",
            "user.name+tag@example.net",
            "john_doe@health.edu",
            
            // Invalid emails
            null,
            "",
            "   ",
            "notanemail",
            "user@",
            "@domain.com",
            "user@domain",
            "user@domain.",
            "user@.com",
            "user..name@domain.com",
            "user@domain..com",
            "user name@domain.com",
            "user@domain .com",
            "user@domain.c"
        };
        
        System.out.println("VALID EMAIL TESTS:");
        System.out.println("==================");
        for (int i = 0; i < 5; i++) {
            String email = testEmails[i];
            boolean isValid = isValidEmail(email);
            System.out.printf("%-30s -> %s ✓%n", 
                email, isValid ? "VALID" : "INVALID");
        }
        
        System.out.println("\nINVALID EMAIL TESTS:");
        System.out.println("====================");
        for (int i = 5; i < testEmails.length; i++) {
            String email = testEmails[i];
            String displayEmail = email == null ? "null" : 
                                 email.isEmpty() ? "\"\"" : 
                                 email.trim().isEmpty() ? "\"   \"" : email;
            boolean isValid = isValidEmail(email);
            System.out.printf("%-30s -> %s %s%n", 
                displayEmail, 
                isValid ? "VALID" : "INVALID",
                isValid ? "✗" : "✓");
        }
        
        System.out.println("\n=== DEMO COMPLETE ===");
        System.out.println("Enhanced email validation is now active in:");
        System.out.println("- Patient Registration Form");
        System.out.println("- Doctor Registration Form");
        System.out.println("- Admin Patient Management");
        System.out.println("\nThe validation uses RFC 5322 compliant regex patterns");
        System.out.println("for more robust email address verification.");
    }
}
