package com.example.ui;

import com.example.model.Patient;

/**
 * Enhanced holder for the "currently logged-in patient."
 * Supports both simple ID tracking and full patient object management.
 */
public class CurrentPatientHolder {
    private static int patientId = -1;
    private static Patient currentPatient = null;

    public static void setPatientId(int id) {
        patientId = id;
    }

    public static int getPatientId() {
        return currentPatient != null ? currentPatient.getId() : patientId;
    }

    /**
     * Set the current logged-in patient
     */
    public static void setCurrentPatient(Patient patient) {
        currentPatient = patient;
        if (patient != null) {
            patientId = patient.getId();
        }
    }

    /**
     * Get the current logged-in patient
     */
    public static Patient getCurrentPatient() {
        return currentPatient;
    }

    /**
     * Check if a patient is currently logged in
     */
    public static boolean isLoggedIn() {
        return currentPatient != null;
    }

    /**
     * Get current patient's full name
     */
    public static String getCurrentPatientName() {
        return currentPatient != null ? currentPatient.getFullname() : "Unknown";
    }

    /**
     * Get current patient's email
     */
    public static String getCurrentPatientEmail() {
        return currentPatient != null ? currentPatient.getEmail() : "";
    }

    /**
     * Clear the current patient session (logout)
     */
    public static void clearSession() {
        currentPatient = null;
        patientId = -1;
    }
}