package com.example.test;

import com.example.data.PendingDoctorRegistrationDAO;
import com.example.data.DoctorDAO;
import com.example.model.Doctor;
import com.example.model.PendingDoctorRegistration;
import java.util.List;

/**
 * Simple test to verify the pending registration system works
 */
public class PendingRegistrationTest {
    
    public static void main(String[] args) {
        System.out.println("=== Testing Pending Doctor Registration System ===");
        
        PendingDoctorRegistrationDAO pendingDAO = new PendingDoctorRegistrationDAO();
        DoctorDAO doctorDAO = new DoctorDAO();
        
        // Test 1: Create a pending registration
        System.out.println("\n1. Creating a test pending registration...");
        Doctor testDoctor = new Doctor(9999, "Dr. Test", "password123", "General Medicine");
        PendingDoctorRegistration pendingReg = new PendingDoctorRegistration(testDoctor);
        
        pendingDAO.addPendingRegistration(pendingReg);
        System.out.println("✓ Added pending registration for Doctor ID: " + testDoctor.getId());
        
        // Test 2: Check if doctor is pending
        System.out.println("\n2. Checking if doctor is in pending queue...");
        boolean isPending = pendingDAO.isDoctorPending(9999);
        System.out.println("✓ Doctor 9999 is pending: " + isPending);
        
        // Test 3: Try to find doctor in regular doctors.json (should not exist)
        System.out.println("\n3. Checking if doctor exists in doctors.json...");
        Doctor foundDoctor = doctorDAO.findById(9999);
        System.out.println("✓ Doctor 9999 in doctors.json: " + (foundDoctor != null ? "EXISTS" : "NOT FOUND"));
        
        // Test 4: Get all pending registrations
        System.out.println("\n4. Getting all pending registrations...");
        List<PendingDoctorRegistration> allPending = pendingDAO.getAllPendingRegistrations();
        System.out.println("✓ Total pending registrations: " + allPending.size());
        
        for (PendingDoctorRegistration reg : allPending) {
            System.out.println("  - Doctor ID: " + reg.getDoctorId() + 
                             ", Name: " + reg.getDoctorName() + 
                             ", Status: " + reg.getStatus());
        }
        
        // Test 5: Simulate approval process
        System.out.println("\n5. Simulating approval process...");
        PendingDoctorRegistration toApprove = pendingDAO.getPendingByDoctorId(9999);
        if (toApprove != null) {
            // Convert to doctor and add to doctors.json
            Doctor approvedDoctor = toApprove.toDoctor();
            doctorDAO.registerDoctor(approvedDoctor);
            
            // Remove from pending queue
            pendingDAO.removePendingRegistration(9999);
            
            System.out.println("✓ Approved and moved Doctor 9999 to doctors.json");
            
            // Verify doctor now exists in doctors.json
            Doctor verifyDoctor = doctorDAO.findById(9999);
            System.out.println("✓ Doctor 9999 now in doctors.json: " + (verifyDoctor != null ? "YES" : "NO"));
            
            // Verify doctor no longer in pending queue
            boolean stillPending = pendingDAO.isDoctorPending(9999);
            System.out.println("✓ Doctor 9999 still pending: " + stillPending);
        }
        
        System.out.println("\n=== Test Complete ===");
    }
}
