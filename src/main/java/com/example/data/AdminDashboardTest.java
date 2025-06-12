package com.example.data;

import com.example.model.Admin;
import com.example.model.Doctor;
import com.example.model.Patient;
import com.example.model.ds.CustomeLinkedList;

/**
 * Test class to verify Admin dashboard functionality
 * Tests the new admin methods without GUI interaction
 */
public class AdminDashboardTest {
    
    public static void main(String[] args) {
        System.out.println("=== ADMIN DASHBOARD FUNCTIONALITY TEST ===\n");
        
        AdminDashboardTest test = new AdminDashboardTest();
        test.runTests();
    }
    
    private void runTests() {
        testAdminAuthentication();
        testDoctorManagement();
        testPatientManagement();
        testSystemStats();
    }
    
    private void testAdminAuthentication() {
        System.out.println("=== Testing Admin Authentication ===");
        
        AdminService adminService = new AdminService();
        
        try {
            // Test valid login
            Admin admin = adminService.authenticate("admin1", "password123");
            if (admin != null) {
                System.out.println("✓ Admin authentication successful: " + admin.getUsername());
            } else {
                System.out.println("✗ Admin authentication failed");
            }
            
            // Test invalid login
            Admin invalidAdmin = adminService.authenticate("admin1", "wrongpassword");
            if (invalidAdmin == null) {
                System.out.println("✓ Invalid password correctly rejected");
            } else {
                System.out.println("✗ Invalid password incorrectly accepted");
            }
            
        } catch (Exception e) {
            System.out.println("✗ Error in admin authentication: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    private void testDoctorManagement() {
        System.out.println("=== Testing Doctor Management ===");
        
        AdminService adminService = new AdminService();
        
        try {
            // Test getting all doctors
            CustomeLinkedList<Doctor> doctors = adminService.getAllDoctors();
            System.out.println("✓ Total doctors in system: " + doctors.size());
            
            // Test getting currently logged in doctors
            CustomeLinkedList<Doctor> loggedInDoctors = adminService.getCurrentlyLoggedInDoctors();
            System.out.println("✓ Currently logged in doctors: " + loggedInDoctors.size());
            
            // Test getting doctor login history
            CustomeLinkedList<Doctor> doctorHistory = adminService.getAllDoctorLoginHistory();
            System.out.println("✓ Doctors with history data: " + doctorHistory.size());
            
            // Display some doctor details
            if (doctors.size() > 0) {
                System.out.println("✓ Sample doctor info:");
                for (Doctor doctor : doctors) {
                    System.out.println("  - Dr. " + doctor.getName() + 
                                     " (ID: " + doctor.getId() + 
                                     ", Specialty: " + doctor.getSpecialty() + 
                                     ", Login History: " + doctor.getLoginHistory().size() + " entries)");
                    break; // Just show first doctor
                }
            }
            
        } catch (Exception e) {
            System.out.println("✗ Error in doctor management: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println();
    }
    
    private void testPatientManagement() {
        System.out.println("=== Testing Patient Management ===");
        
        AdminService adminService = new AdminService();
        
        try {
            // Test getting all patients
            CustomeLinkedList<Patient> patients = adminService.getAllPatients();
            System.out.println("✓ Total patients in system: " + patients.size());
            
            // Test searching for a specific patient (if any exist)
            if (patients.size() > 0) {
                Patient firstPatient = null;
                for (Patient p : patients) {
                    firstPatient = p;
                    break;
                }
                
                if (firstPatient != null) {
                    Patient foundPatient = adminService.searchPatientById(firstPatient.getId());
                    if (foundPatient != null) {
                        System.out.println("✓ Patient search successful: " + foundPatient.getName() + 
                                         " (ID: " + foundPatient.getId() + ")");
                    } else {
                        System.out.println("✗ Patient search failed");
                    }
                }
            }
            
        } catch (Exception e) {
            System.out.println("✗ Error in patient management: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    private void testSystemStats() {
        System.out.println("=== Testing System Statistics ===");
        
        AdminService adminService = new AdminService();
        
        try {
            AdminService.SystemStats stats = adminService.getSystemStats();
            System.out.println("✓ System Statistics:");
            System.out.println("  - Total Doctors: " + stats.getTotalDoctors());
            System.out.println("  - Total Patients: " + stats.getTotalPatients());
            System.out.println("  - Total Appointments: " + stats.getTotalAppointments());
            System.out.println("  - Logged In Doctors: " + stats.getLoggedInDoctors());
            
        } catch (Exception e) {
            System.out.println("✗ Error getting system stats: " + e.getMessage());
        }
        
        System.out.println();
    }
}
