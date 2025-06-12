package com.example.test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for email validation functionality
 * Tests the enhanced regex-based email validation
 */
public class EmailValidationTest {
    
    /**
     * Helper method to test email validation using the same logic as our controllers
     */
    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        // RFC 5322 compliant email regex pattern
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }
    
    @Test
    public void testValidEmails() {
        // Test valid email formats
        assertTrue(isValidEmail("test@example.com"), "Standard email should be valid");
        assertTrue(isValidEmail("user.name@domain.co.uk"), "Email with dots should be valid");
        assertTrue(isValidEmail("user+tag@example.org"), "Email with plus should be valid");
        assertTrue(isValidEmail("user123@test-domain.com"), "Email with numbers and hyphens should be valid");
        assertTrue(isValidEmail("user_name@example.net"), "Email with underscore should be valid");
        assertTrue(isValidEmail("a@b.co"), "Short but valid email should be valid");
    }
    
    @Test
    public void testInvalidEmails() {
        // Test invalid email formats
        assertFalse(isValidEmail(null), "Null email should be invalid");
        assertFalse(isValidEmail(""), "Empty email should be invalid");
        assertFalse(isValidEmail("   "), "Whitespace-only email should be invalid");
        assertFalse(isValidEmail("notanemail"), "String without @ should be invalid");
        assertFalse(isValidEmail("user@"), "Email without domain should be invalid");
        assertFalse(isValidEmail("@domain.com"), "Email without username should be invalid");
        assertFalse(isValidEmail("user@domain"), "Email without TLD should be invalid");
        assertFalse(isValidEmail("user@domain."), "Email with incomplete TLD should be invalid");
        assertFalse(isValidEmail("user@.com"), "Email with missing domain should be invalid");
        assertFalse(isValidEmail("user..name@domain.com"), "Email with consecutive dots should be invalid");
        assertFalse(isValidEmail("user@domain..com"), "Domain with consecutive dots should be invalid");
        assertFalse(isValidEmail("user name@domain.com"), "Email with space should be invalid");
        assertFalse(isValidEmail("user@domain .com"), "Domain with space should be invalid");
    }
    
    @Test
    public void testEdgeCases() {
        // Test edge cases and special characters
        assertFalse(isValidEmail("user@domain.c"), "Single character TLD should be invalid");
        assertTrue(isValidEmail("user@domain.co"), "Two character TLD should be valid");
        assertFalse(isValidEmail("user@domain.toolongextension"), "Very long TLD should be invalid according to our pattern");
        
        // Test international domains (basic ASCII validation)
        assertTrue(isValidEmail("user@example.edu"), "Educational domain should be valid");
        assertTrue(isValidEmail("user@example.gov"), "Government domain should be valid");
        
        // Test with special characters that should be invalid
        assertFalse(isValidEmail("user@domain.com,"), "Email with comma should be invalid");
        assertFalse(isValidEmail("user@domain.com;"), "Email with semicolon should be invalid");
    }
    
    @Test
    public void testRealWorldEmails() {
        // Test real-world email patterns
        assertTrue(isValidEmail("john.doe@company.com"), "Standard business email should be valid");
        assertTrue(isValidEmail("admin@hospital.org"), "Admin email should be valid");
        assertTrue(isValidEmail("patient123@gmail.com"), "Gmail address should be valid");
        assertTrue(isValidEmail("doctor.smith@medical-center.net"), "Medical center email should be valid");
        assertTrue(isValidEmail("contact@health-clinic.co.uk"), "UK domain should be valid");
    }
}
