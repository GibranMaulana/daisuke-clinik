package com.example.data;

import com.example.model.Doctor;
import com.example.model.DoctorSession;
import com.example.model.ds.CustomeLinkedList;

import java.time.LocalDateTime;

/**
 * Service class for managing doctor login/logout sessions.
 * Provides high-level methods for session management.
 */
public class DoctorSessionService {
    private final DoctorSessionDAO sessionDAO;
    private final DoctorDAO doctorDAO;

    public DoctorSessionService() {
        this.sessionDAO = new DoctorSessionDAO();
        this.doctorDAO = new DoctorDAO();
    }

    /**
     * Log in a doctor and create a new session
     */
    public boolean loginDoctor(int doctorId) {
        try {
            // Get doctor information
            Doctor doctor = doctorDAO.findById(doctorId);
            if (doctor == null) {
                return false; // Doctor not found
            }

            // Check if already logged in
            if (sessionDAO.isDoctorLoggedIn(doctorId)) {
                return false; // Already logged in
            }

            // Create new session
            DoctorSession session = new DoctorSession(
                doctorId, 
                doctor.getName(), 
                LocalDateTime.now()
            );

            // Add session to DAO
            sessionDAO.addLoginSession(session);

            // Update doctor's login time and history (maintaining existing behavior)
            doctor.setCurrentLoginTime(LocalDateTime.now());
            doctorDAO.updateDoctor(doctor);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Log out a doctor and complete their session
     */
    public boolean logoutDoctor(int doctorId) {
        return sessionDAO.logoutDoctor(doctorId);
    }

    /**
     * Get all currently logged-in doctors
     */
    public CustomeLinkedList<DoctorSession> getCurrentlyLoggedInDoctors() {
        return sessionDAO.getActiveSessions();
    }

    /**
     * Check if a specific doctor is currently logged in
     */
    public boolean isDoctorLoggedIn(int doctorId) {
        return sessionDAO.isDoctorLoggedIn(doctorId);
    }

    /**
     * Get current session for a doctor
     */
    public DoctorSession getCurrentSession(int doctorId) {
        return sessionDAO.getCurrentSession(doctorId);
    }

    /**
     * Get session history for a specific doctor
     */
    public CustomeLinkedList<DoctorSession> getDoctorSessionHistory(int doctorId) {
        return sessionDAO.getDoctorSessionHistory(doctorId);
    }

    /**
     * Get all session history
     */
    public CustomeLinkedList<DoctorSession> getAllSessionHistory() {
        return sessionDAO.loadSessionHistory();
    }

    /**
     * Get currently logged-in doctors in a format compatible with existing AdminService
     */
    public CustomeLinkedList<Doctor> getCurrentlyLoggedInDoctorsAsDoctor() {
        CustomeLinkedList<DoctorSession> activeSessions = sessionDAO.getActiveSessions();
        CustomeLinkedList<Doctor> loggedInDoctors = new CustomeLinkedList<>();

        for (DoctorSession session : activeSessions) {
            Doctor doctor = doctorDAO.findById(session.getDoctorId());
            if (doctor != null) {
                loggedInDoctors.add(doctor);
            }
        }

        return loggedInDoctors;
    }

    /**
     * Force logout all doctors (useful for system shutdown or end of day)
     */
    public void logoutAllDoctors() {
        CustomeLinkedList<DoctorSession> activeSessions = sessionDAO.getActiveSessions();
        for (DoctorSession session : activeSessions) {
            sessionDAO.logoutDoctor(session.getDoctorId());
        }
    }

    /**
     * Get statistics about sessions
     */
    public String getSessionStatistics() {
        CustomeLinkedList<DoctorSession> activeSessions = sessionDAO.getActiveSessions();
        CustomeLinkedList<DoctorSession> history = sessionDAO.loadSessionHistory();
        
        return String.format(
            "Session Statistics:\n" +
            "- Currently logged in: %d doctors\n" +
            "- Total historical sessions: %d\n",
            activeSessions.size(),
            history.size()
        );
    }
}
