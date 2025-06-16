package com.example.data;

import com.example.model.LoginSession;
import com.example.model.DoctorSession;
import com.example.model.ds.CustomeLinkedList;

/**
 * Simplified LoginSessionDAO that delegates to DoctorSessionService.
 * Maintains backward compatibility with existing UI controllers without redundant file management.
 */
public class LoginSessionDAO {
    private final DoctorSessionService sessionService;

    public LoginSessionDAO() {
        sessionService = new DoctorSessionService();
    }

    /**
     * Load all sessions - delegates to the new session service
     */
    public CustomeLinkedList<LoginSession> loadAllSessions() {
        return convertToLoginSessions(sessionService.getCurrentlyLoggedInDoctors());
    }

    /**
     * Add a new session - uses DoctorSessionService
     */
    public void addSession(LoginSession session) {
        sessionService.loginDoctor(session.getDoctorId());
    }

    /**
     * Remove a session by doctorId (logout) - uses DoctorSessionService
     */
    public void removeSession(int doctorId) {
        sessionService.logoutDoctor(doctorId);
    }

    /**
     * Return a list of all currently logged‐in doctors' sessions
     */
    public CustomeLinkedList<LoginSession> getAllSessions() {
        return loadAllSessions();
    }

    /**
     * Convert DoctorSession objects to LoginSession objects for backward compatibility
     */
    private CustomeLinkedList<LoginSession> convertToLoginSessions(CustomeLinkedList<DoctorSession> doctorSessions) {
        CustomeLinkedList<LoginSession> loginSessions = new CustomeLinkedList<>();
        
        for (DoctorSession doctorSession : doctorSessions) {
            LoginSession loginSession = new LoginSession(
                doctorSession.getDoctorId(),
                doctorSession.getDoctorName(),
                doctorSession.getLoginTime()
            );
            loginSessions.add(loginSession);
        }
        
        return loginSessions;
    }
}
