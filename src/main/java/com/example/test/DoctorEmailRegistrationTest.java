package com.example.test;

import com.example.data.DoctorDAO;
import com.example.data.PendingDoctorRegistrationDAO;
import com.example.model.Doctor;
import com.example.model.PendingDoctorRegistration;

/**
 * Test to verify that doctor email is properly saved during registration
 */
public class DoctorEmailRegistrationTest {
    
    public static void main(String[] args) {
        System.out.println("=== Testing Doctor Email Registration ===");
        
        PendingDoctorRegistrationDAO pendingDAO = new PendingDoctorRegistrationDAO();
        DoctorDAO doctorDAO = new DoctorDAO();
        
        // Test 1: Create a doctor with email
        System.out.println("\n1. Creating doctor with email...");
        Doctor testDoctor = new Doctor(7777, "dr_test_email", "password123", "Dr. Test Email", "test@hospital.com", "General Medicine");
        
        System.out.println("Original doctor:");
        System.out.println("  ID: " + testDoctor.getId());
        System.out.println("  Username: " + testDoctor.getUsername());
        System.out.println("  Fullname: " + testDoctor.getFullname());
        System.out.println("  Email: " + testDoctor.getEmail());
        System.out.println("  Specialty: " + testDoctor.getSpecialty());
        
        // Test 2: Create pending registration
        System.out.println("\n2. Creating pending registration...");
        PendingDoctorRegistration pendingReg = new PendingDoctorRegistration(testDoctor);
        pendingDAO.addPendingRegistration(pendingReg);
        
        System.out.println("Pending registration:");
        System.out.println("  Doctor ID: " + pendingReg.getDoctorId());
        System.out.println("  Doctor Name: " + pendingReg.getDoctorName());
        System.out.println("  Doctor Username: " + pendingReg.getDoctorUsername());
        System.out.println("  Doctor Email: " + pendingReg.getDoctorEmail());
        System.out.println("  Specialty: " + pendingReg.getSpecialty());
        
        // Test 3: Simulate approval process (toDoctor)
        System.out.println("\n3. Simulating approval process...");
        Doctor reconstructedDoctor = pendingReg.toDoctor();
        
        System.out.println("Reconstructed doctor:");
        System.out.println("  ID: " + reconstructedDoctor.getId());
        System.out.println("  Username: " + reconstructedDoctor.getUsername());
        System.out.println("  Fullname: " + reconstructedDoctor.getFullname());
        System.out.println("  Email: " + reconstructedDoctor.getEmail());
        System.out.println("  Specialty: " + reconstructedDoctor.getSpecialty());
        
        // Test 4: Add to doctors.json and verify
        System.out.println("\n4. Adding to doctors.json...");
        doctorDAO.registerDoctor(reconstructedDoctor);
        
        Doctor verifyDoctor = doctorDAO.findById(7777);
        if (verifyDoctor != null) {
            System.out.println("✓ Verified doctor from doctors.json:");
            System.out.println("  ID: " + verifyDoctor.getId());
            System.out.println("  Username: " + verifyDoctor.getUsername());
            System.out.println("  Fullname: " + verifyDoctor.getFullname());
            System.out.println("  Email: " + verifyDoctor.getEmail());
            System.out.println("  Specialty: " + verifyDoctor.getSpecialty());
            
            if ("test@hospital.com".equals(verifyDoctor.getEmail())) {
                System.out.println("✅ SUCCESS: Email is properly preserved!");
            } else {
                System.out.println("❌ FAILED: Email was not preserved. Got: " + verifyDoctor.getEmail());
            }
        } else {
            System.out.println("❌ FAILED: Doctor not found in doctors.json");
        }
        
        // Cleanup
        System.out.println("\n5. Cleanup...");
        doctorDAO.deleteDoctor(7777);
        pendingDAO.removePendingRegistration(7777);
        System.out.println("✓ Test data cleaned up");
        
        System.out.println("\n=== Test Complete ===");
    }
}
