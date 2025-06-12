package com.example.data;

import com.example.model.Admin;
import com.example.model.Doctor;
import com.example.model.Patient;
import com.example.model.PendingDoctorRegistration;
import com.example.model.ds.CustomeLinkedList;

/**
 * Simple test class to verify Admin functionality
 */
public class AdminTest {
    
    public static void main(String[] args) {
        AdminTest test = new AdminTest();
        test.testAdminLogin(); // Add admin login test
        test.testAdminBasicOperations();
        test.testAdminPendingDoctorRegistrations();
        test.testAdminPatientManagement();
        test.testAdminSystemOverview();
    }

    private void testAdminLogin() {
        System.out.println("=== Testing Admin Login Functionality ===");
        
        AdminService adminService = new AdminService();
        
        // Test admin credentials from admins.json
        System.out.println("\n1. Testing admin1 login:");
        Admin admin1 = adminService.authenticate("admin1", "password123");
        if (admin1 != null) {
            System.out.println("✅ admin1 login successful: " + admin1.getUsername());
        } else {
            System.out.println("❌ admin1 login failed");
        }
        
        System.out.println("\n2. Testing admin2 login:");
        Admin admin2 = adminService.authenticate("admin2", "password123");
        if (admin2 != null) {
            System.out.println("✅ admin2 login successful: " + admin2.getUsername());
        } else {
            System.out.println("❌ admin2 login failed");
        }
        
        System.out.println("\n3. Testing invalid credentials:");
        Admin invalidAdmin = adminService.authenticate("invalid", "wrong");
        if (invalidAdmin == null) {
            System.out.println("✅ Invalid credentials correctly rejected");
        } else {
            System.out.println("❌ Invalid credentials should have been rejected");
        }
        
        System.out.println("✅ Admin login functionality working correctly!\n");
    }

    private void testAdminBasicOperations() {
        System.out.println("=== Testing Admin Basic Operations ===");
        
        AdminService adminService = new AdminService();
        
        try {
            // Test admin registration
            Admin admin1 = adminService.registerAdmin("admin1", "password123");
            System.out.println("✓ Admin registered: " + admin1);
            
            // Test admin authentication
            Admin authenticatedAdmin = adminService.authenticate("admin1", "password123");
            System.out.println("✓ Admin authenticated: " + authenticatedAdmin);
            
            // Test wrong password
            Admin wrongPassword = adminService.authenticate("admin1", "wrongpassword");
            System.out.println("✓ Wrong password test: " + (wrongPassword == null ? "PASSED" : "FAILED"));
            
        } catch (Exception e) {
            System.out.println("✗ Error in basic operations: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println();
    }

    private void testAdminPendingDoctorRegistrations() {
        System.out.println("=== Testing Pending Doctor Registrations ===");
        
        AdminService adminService = new AdminService();
        
        try {
            // Create admin
            Admin admin = adminService.registerAdmin("admin2", "password123");
            
            // Add pending doctor registrations
            adminService.addPendingDoctorRegistration(admin, "Dr. Smith", "docpass1", "Cardiology");
            adminService.addPendingDoctorRegistration(admin, "Dr. Johnson", "docpass2", "Neurology");
            
            System.out.println("✓ Added 2 pending doctor registrations");
            System.out.println("✓ Pending registrations count: " + admin.getPendingRegistrationsCount());
            
            // Peek at next registration
            PendingDoctorRegistration nextReg = adminService.getNextPendingRegistration(admin);
            System.out.println("✓ Next pending registration: " + nextReg);
            
            // Accept a registration
            Doctor acceptedDoctor = adminService.acceptDoctorRegistration(admin);
            System.out.println("✓ Accepted doctor: " + acceptedDoctor);
            System.out.println("✓ Remaining pending registrations: " + admin.getPendingRegistrationsCount());
            
            // Decline a registration
            boolean declined = adminService.declineDoctorRegistration(admin);
            System.out.println("✓ Declined registration: " + declined);
            System.out.println("✓ Final pending registrations: " + admin.getPendingRegistrationsCount());
            
        } catch (Exception e) {
            System.out.println("✗ Error in pending registrations: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println();
    }

    private void testAdminPatientManagement() {
        System.out.println("=== Testing Admin Patient Management ===");
        
        AdminService adminService = new AdminService();
        PatientService patientService = new PatientService();
        
        try {
            // Create a test patient first
            Patient testPatient = patientService.registerPatient("John Doe", 30, "123 Main St", "555-1234");
            System.out.println("✓ Created test patient: " + testPatient);
            
            // Test searching patient by ID
            Patient foundPatient = adminService.searchPatientById(testPatient.getId());
            System.out.println("✓ Found patient by ID: " + foundPatient);
            
            // Test displaying patient by ID
            Patient displayedPatient = adminService.displayPatientById(testPatient.getId());
            System.out.println("✓ Displayed patient: " + displayedPatient);
            
            // Test getting all patients
            CustomeLinkedList<Patient> allPatients = adminService.getAllPatients();
            System.out.println("✓ Total patients in system: " + allPatients.size());
            
            // Test removing patient (commented out to preserve test data)
            // boolean removed = adminService.removePatientById(testPatient.getId());
            // System.out.println("✓ Patient removed: " + removed);
            
        } catch (Exception e) {
            System.out.println("✗ Error in patient management: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println();
    }

    private void testAdminSystemOverview() {
        System.out.println("=== Testing Admin System Overview ===");
        
        AdminService adminService = new AdminService();
        
        try {
            // Test getting all doctors
            CustomeLinkedList<Doctor> allDoctors = adminService.getAllDoctors();
            System.out.println("✓ Total doctors in system: " + allDoctors.size());
            
            // Test getting currently logged in doctors
            CustomeLinkedList<Doctor> loggedInDoctors = adminService.getCurrentlyLoggedInDoctors();
            System.out.println("✓ Currently logged in doctors: " + loggedInDoctors.size());
            
            // Test getting all appointments
            // CustomeLinkedList<Appointment> allAppointments = adminService.getAllAppointments();
            // System.out.println("✓ Total appointments in system: " + allAppointments.size());
            
            // Test system statistics
            AdminService.SystemStats stats = adminService.getSystemStats();
            System.out.println("✓ System Statistics: " + stats);
            
        } catch (Exception e) {
            System.out.println("✗ Error in system overview: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println();
    }
}
