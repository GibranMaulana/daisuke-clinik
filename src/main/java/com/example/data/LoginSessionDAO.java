package com.example.data;

import com.example.model.LoginSession;
import com.example.model.ds.CustomeLinkedList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Manages the list of currently logged‐in doctors, but
 * automatically removes any session older than EXPIRATION_HOURS.
 */
public class LoginSessionDAO {
    private static final String FILE = "loginSessions.json";
    private static final long EXPIRATION_HOURS = 24; // sessions older than this are “stale”

    private final ObjectMapper mapper;

    public LoginSessionDAO() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * Load all sessions from JSON, remove any that are older than EXPIRATION_HOURS,
     * save the cleaned list back to disk, and return it.
     */
    public CustomeLinkedList<LoginSession> loadAllSessions() {
        try {
            File f = new File(FILE);
            if (!f.exists() || f.length() == 0) {
                return new CustomeLinkedList<>();
            }

            // 1) Read the raw array from JSON
            LoginSession[] arr = mapper.readValue(f, LoginSession[].class);

            // 2) Filter out stale sessions
            LocalDateTime now = LocalDateTime.now();
            CustomeLinkedList<LoginSession> freshList = new CustomeLinkedList<>();
            for (LoginSession s : arr) {
                if (s.getLoginTime() != null) {
                    Duration age = Duration.between(s.getLoginTime(), now);
                    if (age.toHours() < EXPIRATION_HOURS) {
                        // Still valid
                        freshList.add(s);
                    }
                }
            }

            // 3) Overwrite the file with only the fresh sessions
            saveAllSessions(freshList);

            return freshList;
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
     * Add a new session (if doctor not already logged‐in and not stale).
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
     * Remove a session by doctorId (on explicit logout).
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
     * Return a list of all currently logged‐in (fresh) doctors’ sessions.
     */
    public CustomeLinkedList<LoginSession> getAllSessions() {
        return loadAllSessions();
    }
}
