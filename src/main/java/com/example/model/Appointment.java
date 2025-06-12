package com.example.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Appointment now uses int for all IDs.
 */
public class Appointment {
    private int appointmentId;      // was String, now int (auto‐generated)
    private int patientId;          // int instead of String
    private int doctorId;           // int instead of String
    private String doctorSpecialty;
    private LocalDateTime time;
    private String patientIllness;
    private String status = "scheduled"; // Default status

    public Appointment() {
        // For Jackson
    }

    public Appointment(int appointmentId,
                       int patientId,
                       int doctorId,
                       String doctorSpecialty,
                       LocalDateTime time,
                       String patientIllness) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.doctorSpecialty = doctorSpecialty;
        this.time = time;
        this.patientIllness = patientIllness;
        this.status = "scheduled";
    }

    // ─────────────── Getters & Setters ───────────────

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorSpecialty() {
        return doctorSpecialty;
    }

    public void setDoctorSpecialty(String doctorSpecialty) {
        this.doctorSpecialty = doctorSpecialty;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getPatientIllness() {
        return patientIllness;
    }

    public void setPatientIllness(String patientIllness) {
        this.patientIllness = patientIllness;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format(
            "%d | P:%d | D:%d | %s | %s | %s",
            appointmentId, patientId, doctorId, time, patientIllness, status
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Appointment)) return false;
        Appointment other = (Appointment) o;
        return this.appointmentId == other.appointmentId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(appointmentId);
    }
}
