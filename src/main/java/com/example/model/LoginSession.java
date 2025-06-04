package com.example.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a single “doctor logged in” session.
 */
public class LoginSession {
    private int doctorId;
    private String doctorName;
    private LocalDateTime loginTime;

    public LoginSession() { }

    public LoginSession(int doctorId, String doctorName, LocalDateTime loginTime) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.loginTime = loginTime;
    }

    public int getDoctorId() {
        return doctorId;
    }
    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }
    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    @Override
    public String toString() {
        // e.g. “1234 | Dr. Bob | 2025-06-05T14:22”
        return String.format("%d | %s | %s",
                doctorId, doctorName, loginTime.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoginSession)) return false;
        LoginSession other = (LoginSession) o;
        return this.doctorId == other.doctorId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(doctorId);
    }
}
