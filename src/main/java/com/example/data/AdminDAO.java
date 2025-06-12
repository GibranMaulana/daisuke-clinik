package com.example.data;

import com.example.model.Admin;
import com.example.model.ds.CustomeLinkedList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;

/**
 * Data Access Object for Admin entities
 */
public class AdminDAO {
    private static final String FILE = "admins.json";
    private final ObjectMapper mapper;
    private CustomeLinkedList<Admin> adminList;

    public AdminDAO() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        this.adminList = loadAllAdmins();
    }

    /** JSON → Admin[] → CustomeLinkedList */
    private CustomeLinkedList<Admin> loadAllAdmins() {
        try {
            File f = new File(FILE);
            if (!f.exists() || f.length() == 0) {
                return new CustomeLinkedList<>();
            }
            Admin[] arr = mapper.readValue(f, Admin[].class);
            CustomeLinkedList<Admin> list = new CustomeLinkedList<>();
            for (Admin a : arr) {
                list.add(a);
            }
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new CustomeLinkedList<>();
        }
    }

    /** CustomeLinkedList → Admin[] → JSON */
    private void saveAllAdmins() {
        try {
            int n = adminList.size();
            Admin[] arr = new Admin[n];
            int idx = 0;
            for (Admin a : adminList) {
                arr[idx++] = a;
            }
            mapper.writeValue(new File(FILE), arr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // ─────────────────────────────────────────
    // CRUD Operations
    // ─────────────────────────────────────────
    
    /**
     * Register a new admin
     */
    public void registerAdmin(Admin admin) {
        adminList.add(admin);
        saveAllAdmins();
    }

    /**
     * Find admin by ID
     */
    public Admin findById(int id) {
        for (Admin admin : adminList) {
            if (admin.getId() == id) {
                return admin;
            }
        }
        return null;
    }

    /**
     * Find admin by username
     */
    public Admin findByUsername(String username) {
        for (Admin admin : adminList) {
            if (username.equals(admin.getUsername())) {
                return admin;
            }
        }
        return null;
    }

    /**
     * Update existing admin
     */
    public void updateAdmin(Admin admin) {
        Admin existing = findById(admin.getId());
        if (existing != null) {
            // Remove old and add updated
            adminList.remove(existing);
            adminList.add(admin);
            saveAllAdmins();
        }
    }

    /**
     * Delete admin by ID
     */
    public boolean deleteAdmin(int id) {
        Admin admin = findById(id);
        if (admin != null) {
            adminList.remove(admin);
            saveAllAdmins();
            return true;
        }
        return false;
    }

    /**
     * Get all admins
     */
    public CustomeLinkedList<Admin> getAllAdmins() {
        return adminList;
    }

    /**
     * Check if admin exists
     */
    public boolean adminExists(int id) {
        return findById(id) != null;
    }

    /**
     * Check if username is taken
     */
    public boolean usernameExists(String username) {
        return findByUsername(username) != null;
    }

    /**
     * Get admin count
     */
    public int getAdminCount() {
        return adminList.size();
    }
}
