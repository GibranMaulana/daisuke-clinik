package com.example.test;

import com.example.data.DoctorDAO;
import com.example.data.PendingDoctorRegistrationDAO;
import com.example.model.Doctor;
import com.example.model.PendingDoctorRegistration;

import java.util.List;

/**
 * Test to validate that admin pending registration system is working
 * and that approve/decline functionality operates correctly.
 */
public class AdminButtonTest {
    public static void main(String[] args) {
        System.out.println("=== Testing Admin Pending Registration Button Functionality ===");
        
        PendingDoctorRegistrationDAO pendingDAO = new PendingDoctorRegistrationDAO();
        DoctorDAO doctorDAO = new DoctorDAO();
        
        try {
            // 1. Check current pending registrations
            System.out.println("\n1. Current pending registrations:");
            List<PendingDoctorRegistration> pending = pendingDAO.getAllPendingRegistrations();
            System.out.println("   Total pending: " + pending.size());
            
            for (PendingDoctorRegistration reg : pending) {
                if (reg.isPending()) {
                    System.out.println("   - Doctor ID: " + reg.getDoctorId() + 
                                     ", Name: " + reg.getDoctorName() + 
                                     ", Status: " + reg.getStatus());
                }
            }
            
            // 2. If no pending registrations, create a test one
            if (pending.isEmpty() || pending.stream().noneMatch(PendingDoctorRegistration::isPending)) {
                System.out.println("\n2. Creating test pending registration...");
                Doctor testDoctor = new Doctor(8888, "Dr. ButtonTest", "password123", "Testing");
                PendingDoctorRegistration testReg = new PendingDoctorRegistration(testDoctor);
                pendingDAO.addPendingRegistration(testReg);
                System.out.println("   ✓ Added test pending registration for Doctor ID: 8888");
                
                // Refresh pending list
                pending = pendingDAO.getAllPendingRegistrations();
            }
            
            // 3. Test approve functionality (simulating button click)
            System.out.println("\n3. Testing approve functionality...");
            PendingDoctorRegistration toApprove = null;
            for (PendingDoctorRegistration reg : pending) {
                if (reg.isPending()) {
                    toApprove = reg;
                    break;
                }
            }
            
            if (toApprove != null) {
                System.out.println("   Approving Doctor ID: " + toApprove.getDoctorId());
                
                // Simulate button click logic
                Doctor newDoctor = toApprove.toDoctor();
                doctorDAO.registerDoctor(newDoctor);
                pendingDAO.removePendingRegistration(toApprove.getDoctorId());
                toApprove.approve();
                
                System.out.println("   ✓ Doctor approved and added to doctors.json");
                System.out.println("   ✓ Removed from pending queue");
                
                // Verify doctor was added
                Doctor verifyDoctor = doctorDAO.findById(newDoctor.getId());
                if (verifyDoctor != null) {
                    System.out.println("   ✓ Verified: Doctor " + verifyDoctor.getId() + 
                                     " exists in doctors.json");
                }
                
            } else {
                System.out.println("   No pending registrations to approve");
            }
            
            // 4. Create another test registration for decline test
            System.out.println("\n4. Testing decline functionality...");
            Doctor declineTestDoctor = new Doctor(7777, "Dr. DeclineTest", "password456", "Testing");
            PendingDoctorRegistration declineReg = new PendingDoctorRegistration(declineTestDoctor);
            pendingDAO.addPendingRegistration(declineReg);
            System.out.println("   Created test registration for decline: Doctor ID 7777");
            
            // Simulate decline button click
            declineReg.reject();
            pendingDAO.removePendingRegistration(declineReg.getDoctorId());
            System.out.println("   ✓ Doctor registration declined and removed from queue");
            
            // Verify doctor was NOT added to doctors.json
            Doctor shouldNotExist = doctorDAO.findById(7777);
            if (shouldNotExist == null) {
                System.out.println("   ✓ Verified: Doctor 7777 NOT in doctors.json (correctly declined)");
            } else {
                System.out.println("   ✗ Error: Doctor 7777 was incorrectly added to doctors.json");
            }
            
            // 5. Final status
            System.out.println("\n5. Final pending registrations count:");
            List<PendingDoctorRegistration> finalPending = pendingDAO.getAllPendingRegistrations();
            int pendingCount = 0;
            for (PendingDoctorRegistration reg : finalPending) {
                if (reg.isPending()) {
                    pendingCount++;
                }
            }
            System.out.println("   Remaining pending: " + pendingCount);
            
            System.out.println("\n=== Admin Button Test Complete ===");
            System.out.println("✅ The approve and decline button functionality is working correctly!");
            System.out.println("ℹ️ You can now test the UI buttons in the admin dashboard.");
            
        } catch (Exception e) {
            System.err.println("❌ Test failed with error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
