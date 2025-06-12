package com.example.ui;

import com.example.model.Doctor;

/**
 * Holds the currently‐logged‐in Doctor so that any controller can retrieve it.
 */
public class CurrentDoctorHolder {
    private static Doctor doctor;

    public static void setDoctor(Doctor d) {
        doctor = d;
    }

    public static Doctor getDoctor() {
        return doctor;
    }
}
