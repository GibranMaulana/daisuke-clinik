package com.example.data;

import com.example.model.LoginSession;
import com.example.model.ds.CustomeLinkedList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;

/**
 * Manages the list of currently logged-in doctors.
 * Uses a JSON file “loginSessions.json” which stores an array of LoginSession objects.
 */
public class LoginSessionDAO {
    private static final String FILE = "loginSessions.json";
    private final ObjectMapper mapper;

    public LoginSessionDAO() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * Load all sessions into a CustomeLinkedList<LoginSession>.
     * If file does not exist or is empty, return an empty list.
     */
    public CustomeLinkedList<LoginSession> loadAllSessions() {
        try {
            File f = new File(FILE);
            if (!f.exists() || f.length() == 0) {
                return new CustomeLinkedList<>();
            }
            LoginSession[] arr = mapper.readValue(f, LoginSession[].class);
            CustomeLinkedList<LoginSession> list = new CustomeLinkedList<>();
            for (LoginSession s : arr) {
                list.add(s);
            }
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new CustomeLinkedList<>();
        }
    }

    /**
     * Save the given linked list of sessions back to JSON.
     */
    private void saveAllSessions(CustomeLinkedList<LoginSession> all) {
        try {
            int n = all.size();
            LoginSession[] arr = new LoginSession[n];
            int idx = 0;
            for (LoginSession s : all) {
                arr[idx++] = s;
            }
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE), arr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Add a new session (if doctor not already logged-in).
     */
    public void addSession(LoginSession session) {
        CustomeLinkedList<LoginSession> all = loadAllSessions();
        // If a session for this doctorId already exists, do nothing
        for (LoginSession s : all) {
            if (s.getDoctorId() == session.getDoctorId()) {
                return;
            }
        }
        all.add(session);
        saveAllSessions(all);
    }

    /**
     * Remove a session by doctorId (on logout).
     */
    public void removeSession(int doctorId) {
        CustomeLinkedList<LoginSession> all = loadAllSessions();
        LoginSession toRemove = null;
        for (LoginSession s : all) {
            if (s.getDoctorId() == doctorId) {
                toRemove = s;
                break;
            }
        }
        if (toRemove != null) {
            all.remove(toRemove);
            saveAllSessions(all);
        }
    }

    /**
     * Return a list of all currently logged-in doctors’ sessions.
     */
    public CustomeLinkedList<LoginSession> getAllSessions() {
        return loadAllSessions();
    }
}
