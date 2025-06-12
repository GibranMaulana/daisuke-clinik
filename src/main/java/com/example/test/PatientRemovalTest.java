package com.example.test;

import com.example.data.AdminService;
import com.example.data.PatientDAO;
import com.example.model.Patient;
import com.example.model.ds.CustomeBST;
import com.example.model.ds.CustomeLinkedList;

/**
 * Quick test to verify the patient removal fix works correctly
 */
public class PatientRemovalTest {
    
    public static void main(String[] args) {
        System.out.println("Testing Patient Removal Fix...");
        
        // Step 1: Get initial patient count
        PatientDAO initialDAO = new PatientDAO();
        CustomeBST<Patient> initialBST = initialDAO.getAllPatientsBST();
        CustomeLinkedList<Patient> initialPatients = initialBST.inOrderList();
        int initialCount = initialPatients.size();
        
        System.out.println("Initial patient count: " + initialCount);
        
        if (initialCount == 0) {
            System.out.println("No patients found. Cannot test removal.");
            return;
        }
        
        // Step 2: Get a patient to remove (first one in the list)
        Patient patientToRemove = null;
        for (Patient p : initialPatients) {
            patientToRemove = p;
            break;
        }
        
        if (patientToRemove == null) {
            System.out.println("Could not find a patient to remove.");
            return;
        }
        
        System.out.println("Patient to remove: ID=" + patientToRemove.getId() + 
                         ", Name=" + patientToRemove.getName());
        
        // Step 3: Remove the patient using AdminService
        AdminService adminService = new AdminService();
        boolean removed = adminService.removePatientById(patientToRemove.getId());
        
        if (!removed) {
            System.out.println("❌ Failed to remove patient!");
            return;
        }
        
        System.out.println("✓ Patient removed via AdminService");
        
        // Step 4: Create a fresh PatientDAO instance (simulating AdminController behavior)
        PatientDAO freshDAO = new PatientDAO();
        CustomeBST<Patient> freshBST = freshDAO.getAllPatientsBST();
        CustomeLinkedList<Patient> freshPatients = freshBST.inOrderList();
        int newCount = freshPatients.size();
        
        System.out.println("New patient count after fresh load: " + newCount);
        
        // Step 5: Verify the patient is no longer in the fresh data
        boolean foundInFreshData = false;
        for (Patient p : freshPatients) {
            if (p.getId() == patientToRemove.getId()) {
                foundInFreshData = true;
                break;
            }
        }
        
        // Step 6: Verify results
        if (newCount == initialCount - 1 && !foundInFreshData) {
            System.out.println("✅ SUCCESS: Patient removal fix is working correctly!");
            System.out.println("   - Patient count decreased from " + initialCount + " to " + newCount);
            System.out.println("   - Removed patient is not found in fresh data");
        } else {
            System.out.println("❌ FAILURE: Patient removal fix is not working");
            System.out.println("   - Expected count: " + (initialCount - 1) + ", Actual: " + newCount);
            System.out.println("   - Patient found in fresh data: " + foundInFreshData);
        }
        
        // Step 7: Add the patient back for future tests
        try {
            PatientDAO restoreDAO = new PatientDAO();
            restoreDAO.registerPatient(patientToRemove);
            System.out.println("✓ Patient restored for future tests");
        } catch (Exception e) {
            System.out.println("⚠ Warning: Could not restore patient for future tests");
        }
    }
}
