package com.example.data;

import com.example.model.DoctorSession;
import com.example.model.ds.CustomeLinkedList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;

/**
 * Manages doctor sessions with complete login/logout tracking.
 * Stores both active sessions and completed session history.
 */
public class DoctorSessionDAO {
    private static final String ACTIVE_SESSIONS_FILE = "activeDoctorSessions.json";
    private static final String SESSION_HISTORY_FILE = "doctorSessionHistory.json";

    private final ObjectMapper mapper;

    public DoctorSessionDAO() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * Load all active sessions and auto-logout expired ones
     */
    public CustomeLinkedList<DoctorSession> loadActiveSessions() {
        try {
            File f = new File(ACTIVE_SESSIONS_FILE);
            if (!f.exists() || f.length() == 0) {
                return new CustomeLinkedList<>();
            }

            // Read active sessions
            DoctorSession[] arr = mapper.readValue(f, DoctorSession[].class);
            CustomeLinkedList<DoctorSession> activeSessions = new CustomeLinkedList<>();
            CustomeLinkedList<DoctorSession> expiredSessions = new CustomeLinkedList<>();

            // Check each session for expiration
            for (DoctorSession session : arr) {
                session.checkAndAutoLogout();
                
                if (session.isActive()) {
                    activeSessions.add(session);
                } else {
                    // Session was auto-logged out, move to history
                    expiredSessions.add(session);
                }
            }

            // Move expired sessions to history
            if (expiredSessions.size() > 0) {
                addToHistory(expiredSessions);
            }

            // Save updated active sessions
            saveActiveSessions(activeSessions);

            return activeSessions;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new CustomeLinkedList<>();
        }
    }

    /**
     * Save active sessions to file
     */
    private void saveActiveSessions(CustomeLinkedList<DoctorSession> sessions) {
        try {
            int n = sessions.size();
            DoctorSession[] arr = new DoctorSession[n];
            int idx = 0;
            for (DoctorSession s : sessions) {
                arr[idx++] = s;
            }
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(ACTIVE_SESSIONS_FILE), arr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Add a new login session
     */
    public void addLoginSession(DoctorSession session) {
        CustomeLinkedList<DoctorSession> activeSessions = loadActiveSessions();
        
        // Check if doctor already has an active session
        for (DoctorSession existing : activeSessions) {
            if (existing.getDoctorId() == session.getDoctorId()) {
                // Doctor already logged in, ignore new login attempt
                return;
            }
        }
        
        activeSessions.add(session);
        saveActiveSessions(activeSessions);
    }

    /**
     * Logout a doctor by ID
     */
    public boolean logoutDoctor(int doctorId) {
        CustomeLinkedList<DoctorSession> activeSessions = loadActiveSessions();
        DoctorSession toLogout = null;
        
        for (DoctorSession session : activeSessions) {
            if (session.getDoctorId() == doctorId) {
                toLogout = session;
                break;
            }
        }
        
        if (toLogout != null) {
            // Mark as logged out
            toLogout.logout();
            
            // Remove from active sessions
            activeSessions.remove(toLogout);
            saveActiveSessions(activeSessions);
            
            // Add to history
            CustomeLinkedList<DoctorSession> singleSession = new CustomeLinkedList<>();
            singleSession.add(toLogout);
            addToHistory(singleSession);
            
            return true;
        }
        
        return false;
    }

    /**
     * Get all currently active sessions
     */
    public CustomeLinkedList<DoctorSession> getActiveSessions() {
        return loadActiveSessions();
    }

    /**
     * Load session history
     */
    public CustomeLinkedList<DoctorSession> loadSessionHistory() {
        try {
            File f = new File(SESSION_HISTORY_FILE);
            if (!f.exists() || f.length() == 0) {
                return new CustomeLinkedList<>();
            }

            DoctorSession[] arr = mapper.readValue(f, DoctorSession[].class);
            CustomeLinkedList<DoctorSession> history = new CustomeLinkedList<>();
            for (DoctorSession session : arr) {
                history.add(session);
            }
            return history;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new CustomeLinkedList<>();
        }
    }

    /**
     * Add sessions to history
     */
    private void addToHistory(CustomeLinkedList<DoctorSession> newSessions) {
        try {
            CustomeLinkedList<DoctorSession> history = loadSessionHistory();
            
            // Add new sessions to history
            for (DoctorSession session : newSessions) {
                history.add(session);
            }
            
            // Save updated history
            int n = history.size();
            DoctorSession[] arr = new DoctorSession[n];
            int idx = 0;
            for (DoctorSession s : history) {
                arr[idx++] = s;
            }
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(SESSION_HISTORY_FILE), arr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Get session history for a specific doctor
     */
    public CustomeLinkedList<DoctorSession> getDoctorSessionHistory(int doctorId) {
        CustomeLinkedList<DoctorSession> allHistory = loadSessionHistory();
        CustomeLinkedList<DoctorSession> doctorHistory = new CustomeLinkedList<>();
        
        for (DoctorSession session : allHistory) {
            if (session.getDoctorId() == doctorId) {
                doctorHistory.add(session);
            }
        }
        
        return doctorHistory;
    }

    /**
     * Check if a doctor is currently logged in
     */
    public boolean isDoctorLoggedIn(int doctorId) {
        CustomeLinkedList<DoctorSession> activeSessions = loadActiveSessions();
        for (DoctorSession session : activeSessions) {
            if (session.getDoctorId() == doctorId) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get current session for a doctor (if active)
     */
    public DoctorSession getCurrentSession(int doctorId) {
        CustomeLinkedList<DoctorSession> activeSessions = loadActiveSessions();
        for (DoctorSession session : activeSessions) {
            if (session.getDoctorId() == doctorId) {
                return session;
            }
        }
        return null;
    }
}
