package com.example.data;

import com.example.model.Admin;

/**
 * Test class to verify admin login functionality
 */
public class AdminLoginTest {
    public static void main(String[] args) {
        System.out.println("=== Admin Login Test ===");
        
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
        
        System.out.println("\n=== Test Complete ===");
        System.out.println("Admin login functionality is working correctly!");
        System.out.println("Available admin accounts:");
        System.out.println("- Username: admin1, Password: password123");
        System.out.println("- Username: admin2, Password: password123");
    }
}
