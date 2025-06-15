package com.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a pending doctor registration request in the admin's queue
 */
public class PendingDoctorRegistration implements Comparable<PendingDoctorRegistration> {
    private int requestId;
    private String doctorName;
    private String doctorUsername;
    private String doctorPassword;
    private String doctorEmail;
    private String specialty;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime requestTime;
    private String status; // "PENDING", "APPROVED", "REJECTED"

    public PendingDoctorRegistration() {
        this.requestTime = LocalDateTime.now();
        this.status = "PENDING";
    }

    public PendingDoctorRegistration(int requestId, String doctorName, String doctorPassword, String specialty) {
        this();
        this.requestId = requestId;
        this.doctorName = doctorName;
        this.doctorUsername = doctorName; // fallback for backward compatibility
        this.doctorPassword = doctorPassword;
        this.doctorEmail = null; // no email in old constructor
        this.specialty = specialty;
    }

    /**
     * Constructor that takes a Doctor object for pending registration
     */
    public PendingDoctorRegistration(Doctor doctor) {
        this();
        this.requestId = doctor.getId(); // Use doctor ID as request ID
        this.doctorName = doctor.getName();
        this.doctorUsername = doctor.getUsername();
        this.doctorPassword = doctor.getPassword();
        this.doctorEmail = doctor.getEmail();
        this.specialty = doctor.getSpecialty();
    }

    // ─────────────────────────────────────────
    // Getters & Setters
    // ─────────────────────────────────────────
    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorPassword() {
        return doctorPassword;
    }

    public void setDoctorPassword(String doctorPassword) {
        this.doctorPassword = doctorPassword;
    }

    public String getDoctorUsername() {
        return doctorUsername;
    }

    public void setDoctorUsername(String doctorUsername) {
        this.doctorUsername = doctorUsername;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public void setDoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Get the doctor ID (same as request ID in this implementation)
     */
    @JsonIgnore
    public int getDoctorId() {
        return requestId;
    }

    /**
     * Convert this pending registration back to a Doctor object
     */
    @JsonIgnore
    public Doctor toDoctor() {
        return new Doctor(requestId, doctorUsername, doctorPassword, doctorName, doctorEmail, specialty);
    }

    // ─────────────────────────────────────────
    // Business Methods
    // ─────────────────────────────────────────
    public void approve() {
        this.status = "APPROVED";
    }

    public void reject() {
        this.status = "REJECTED";
    }

    @JsonIgnore
    public boolean isPending() {
        return "PENDING".equals(this.status);
    }

    @JsonIgnore
    public boolean isApproved() {
        return "APPROVED".equals(this.status);
    }

    @JsonIgnore
    public boolean isRejected() {
        return "REJECTED".equals(this.status);
    }

    // ─────────────────────────────────────────
    // Object methods
    // ─────────────────────────────────────────
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PendingDoctorRegistration that = (PendingDoctorRegistration) obj;
        return requestId == that.requestId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId);
    }

    @Override
    public int compareTo(PendingDoctorRegistration other) {
        // Sort by request time (oldest first)
        return this.requestTime.compareTo(other.requestTime);
    }

    @Override
    public String toString() {
        return "PendingDoctorRegistration{" +
                "requestId=" + requestId +
                ", doctorName='" + doctorName + '\'' +
                ", specialty='" + specialty + '\'' +
                ", requestTime=" + requestTime +
                ", status='" + status + '\'' +
                '}';
    }
}
