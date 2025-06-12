package com.example.data;

import com.example.model.*;
import com.example.model.ds.CustomeLinkedList;
import com.example.model.ds.CustomeQueue;

/**
 * Service class for Admin operations
 * Provides business logic for admin functionalities
 */
public class AdminService {
    private final AdminDAO adminDAO = new AdminDAO();
    private final DoctorDAO doctorDAO = new DoctorDAO();
    private final PatientDAO patientDAO = new PatientDAO();
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final LoginSessionDAO sessionDAO = new LoginSessionDAO();

    // ─────────────────────────────────────────
    // Admin Authentication & Management
    // ─────────────────────────────────────────
    
    /**
     * Register a new admin
     */
    public Admin registerAdmin(String username, String password) {
        if (adminDAO.usernameExists(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Generate new ID
        CustomeLinkedList<Admin> all = adminDAO.getAllAdmins();
        int maxId = 0;
        for (Admin a : all) {
            if (a.getId() > maxId) {
                maxId = a.getId();
            }
        }
        int nextId = maxId + 1;

        Admin newAdmin = new Admin(nextId, username, password);
        adminDAO.registerAdmin(newAdmin);
        return newAdmin;
    }

    /**
     * Authenticate admin
     */
    public Admin authenticate(String username, String password) {
        Admin admin = adminDAO.findByUsername(username);
        if (admin != null && admin.getPassword().equals(password)) {
            return admin;
        }
        return null;
    }

    /**
     * Authenticate admin by ID
     */
    public Admin authenticate(int id, String password) {
        Admin admin = adminDAO.findById(id);
        if (admin != null && admin.getPassword().equals(password)) {
            return admin;
        }
        return null;
    }

    // ─────────────────────────────────────────
    // Appointment Management
    // ─────────────────────────────────────────
    
    /**
     * View all appointments in the system
     */
    public CustomeLinkedList<Appointment> getAllAppointments() {
        return appointmentDAO.getAllAppointments();
    }

    /**
     * Get appointments by status
     */
    public CustomeLinkedList<Appointment> getAppointmentsByStatus(String status) {
        CustomeLinkedList<Appointment> allAppointments = appointmentDAO.getAllAppointments();
        CustomeLinkedList<Appointment> filteredAppointments = new CustomeLinkedList<>();
        
        for (Appointment appointment : allAppointments) {
            if (status.equalsIgnoreCase(appointment.getStatus())) {
                filteredAppointments.add(appointment);
            }
        }
        return filteredAppointments;
    }

    // ─────────────────────────────────────────
    // Doctor Management
    // ─────────────────────────────────────────
    
    /**
     * View all doctors in the system
     */
    public CustomeLinkedList<Doctor> getAllDoctors() {
        return doctorDAO.getAllDoctors();
    }

    /**
     * Get currently logged in doctors
     */
    public CustomeLinkedList<Doctor> getCurrentlyLoggedInDoctors() {
        // Use session data from loginSessions.json for accurate logged-in status
        CustomeLinkedList<LoginSession> activeSessions = sessionDAO.getAllSessions();
        CustomeLinkedList<Doctor> allDoctors = doctorDAO.getAllDoctors();
        CustomeLinkedList<Doctor> loggedInDoctors = new CustomeLinkedList<>();
        
        for (Doctor doctor : allDoctors) {
            // Check if doctor has an active session in loginSessions.json
            boolean hasActiveSession = false;
            for (LoginSession session : activeSessions) {
                if (session.getDoctorId() == doctor.getId()) {
                    hasActiveSession = true;
                    break;
                }
            }
            
            // Only include doctors with active sessions (no fallback to currentLoginTime)
            if (hasActiveSession) {
                loggedInDoctors.add(doctor);
            }
        }
        return loggedInDoctors;
    }

    /**
     * Get all doctor login history
     */
    public CustomeLinkedList<Doctor> getAllDoctorLoginHistory() {
        return doctorDAO.getAllDoctors(); // Login history is embedded in Doctor objects
    }

    /**
     * Remove doctor by ID - Super admin only functionality
     */
    public boolean removeDoctorById(int doctorId) {
        return doctorDAO.deleteDoctor(doctorId);
    }

    /**
     * Search doctor by ID
     */
    public Doctor searchDoctorById(int doctorId) {
        return doctorDAO.findById(doctorId);
    }

    // ─────────────────────────────────────────
    // Pending Doctor Registration Management
    // ─────────────────────────────────────────
    
    /**
     * Add a pending doctor registration request
     */
    public void addPendingDoctorRegistration(Admin admin, String doctorName, String doctorPassword, String specialty) {
        // Generate request ID
        int requestId = generateNewRequestId();
        
        PendingDoctorRegistration registration = new PendingDoctorRegistration(
            requestId, doctorName, doctorPassword, specialty
        );
        
        admin.addPendingDoctorRegistration(registration);
        adminDAO.updateAdmin(admin);
    }

    /**
     * Get all pending doctor registrations for an admin
     */
    public CustomeQueue<PendingDoctorRegistration> getPendingDoctorRegistrations(Admin admin) {
        return admin.getPendingDoctorRegistrations();
    }

    /**
     * Accept a pending doctor registration
     */
    public Doctor acceptDoctorRegistration(Admin admin) {
        PendingDoctorRegistration registration = admin.processNextPendingRegistration();
        if (registration == null) {
            return null;
        }

        registration.approve();
        
        // Create the doctor using DoctorService
        DoctorService doctorService = new DoctorService();
        Doctor newDoctor = doctorService.registerDoctor(
            registration.getDoctorName(),
            registration.getDoctorPassword(),
            registration.getSpecialty()
        );

        adminDAO.updateAdmin(admin);
        return newDoctor;
    }

    /**
     * Decline a pending doctor registration
     */
    public boolean declineDoctorRegistration(Admin admin) {
        PendingDoctorRegistration registration = admin.processNextPendingRegistration();
        if (registration == null) {
            return false;
        }

        registration.reject();
        adminDAO.updateAdmin(admin);
        return true;
    }

    /**
     * Peek at the next pending registration without processing it
     */
    public PendingDoctorRegistration getNextPendingRegistration(Admin admin) {
        return admin.getNextPendingRegistration();
    }

    // ─────────────────────────────────────────
    // Patient Management
    // ─────────────────────────────────────────
    
    /**
     * Remove patient by ID
     */
    public boolean removePatientById(int patientId) {
        return patientDAO.deletePatient(patientId);
    }

    /**
     * Search patient by ID
     */
    public Patient searchPatientById(int patientId) {
        return patientDAO.findById(patientId);
    }

    /**
     * Display patient details by ID
     */
    public Patient displayPatientById(int patientId) {
        return patientDAO.findById(patientId);
    }

    /**
     * Get all patients
     */
    public CustomeLinkedList<Patient> getAllPatients() {
        return patientDAO.getAllPatients();
    }

    // ─────────────────────────────────────────
    // System Statistics
    // ─────────────────────────────────────────
    
    /**
     * Get system statistics
     */
    public SystemStats getSystemStats() {
        int totalDoctors = doctorDAO.getAllDoctors().size();
        int totalPatients = patientDAO.getAllPatients().size();
        int totalAppointments = appointmentDAO.getAllAppointments().size();
        int loggedInDoctors = getCurrentlyLoggedInDoctors().size();
        
        return new SystemStats(totalDoctors, totalPatients, totalAppointments, loggedInDoctors);
    }

    // ─────────────────────────────────────────
    // Helper Methods
    // ─────────────────────────────────────────
    
    private int generateNewRequestId() {
        // Simple ID generation - you might want to make this more sophisticated
        return (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
    }

    /**
     * Inner class for system statistics
     */
    public static class SystemStats {
        private final int totalDoctors;
        private final int totalPatients;
        private final int totalAppointments;
        private final int loggedInDoctors;

        public SystemStats(int totalDoctors, int totalPatients, int totalAppointments, int loggedInDoctors) {
            this.totalDoctors = totalDoctors;
            this.totalPatients = totalPatients;
            this.totalAppointments = totalAppointments;
            this.loggedInDoctors = loggedInDoctors;
        }

        public int getTotalDoctors() { return totalDoctors; }
        public int getTotalPatients() { return totalPatients; }
        public int getTotalAppointments() { return totalAppointments; }
        public int getLoggedInDoctors() { return loggedInDoctors; }

        @Override
        public String toString() {
            return "SystemStats{" +
                    "totalDoctors=" + totalDoctors +
                    ", totalPatients=" + totalPatients +
                    ", totalAppointments=" + totalAppointments +
                    ", loggedInDoctors=" + loggedInDoctors +
                    '}';
        }
    }
}
