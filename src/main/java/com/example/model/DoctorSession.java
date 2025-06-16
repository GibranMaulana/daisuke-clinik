package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.Objects;

/**
 * Represents a complete doctor session with both login and logout times.
 * This class tracks the full lifecycle of a doctor's session.
 */
public class DoctorSession {
    private int doctorId;
    private String doctorName;
    private LocalDateTime loginTime;
    private LocalDateTime logoutTime;
    private boolean isActive; // true if doctor is currently logged in

    public DoctorSession() { }

    /**
     * Constructor for a new login session (logout time is null, isActive = true)
     */
    public DoctorSession(int doctorId, String doctorName, LocalDateTime loginTime) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.loginTime = loginTime;
        this.logoutTime = null;
        this.isActive = true;
    }

    /**
     * Constructor for a complete session with both login and logout times
     */
    public DoctorSession(int doctorId, String doctorName, LocalDateTime loginTime, LocalDateTime logoutTime) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.loginTime = loginTime;
        this.logoutTime = logoutTime;
        this.isActive = false;
    }

    // Getters and Setters
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

    public LocalDateTime getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(LocalDateTime logoutTime) {
        this.logoutTime = logoutTime;
        this.isActive = (logoutTime == null);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    /**
     * Marks this session as logged out with the current timestamp
     */
    public void logout() {
        this.logoutTime = LocalDateTime.now();
        this.isActive = false;
    }

    /**
     * Marks this session as logged out with a specific timestamp
     */
    public void logout(LocalDateTime logoutTime) {
        this.logoutTime = logoutTime;
        this.isActive = false;
    }

    /**
     * Calculate the duration of this session
     * @return Duration of the session, or null if session is still active
     */
    @JsonIgnore
    public Duration getSessionDuration() {
        if (loginTime == null) return null;
        if (logoutTime == null) {
            // Session is still active, return duration from login to now
            return Duration.between(loginTime, LocalDateTime.now());
        }
        return Duration.between(loginTime, logoutTime);
    }

    /**
     * Check if this session has expired based on end of login day (23:59)
     */
    @JsonIgnore
    public boolean isExpired() {
        if (loginTime == null) return true;
        if (!isActive) return false; // Already logged out, not expired
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endOfLoginDay = loginTime.toLocalDate().atTime(23, 59, 59);
        return now.isAfter(endOfLoginDay);
    }

    /**
     * Auto-logout if session has expired
     */
    public void checkAndAutoLogout() {
        if (isActive && isExpired()) {
            // Auto-logout at end of day
            LocalDateTime endOfLoginDay = loginTime.toLocalDate().atTime(23, 59, 59);
            logout(endOfLoginDay);
        }
    }

    @Override
    public String toString() {
        String status = isActive ? "ACTIVE" : "COMPLETED";
        if (logoutTime != null) {
            Duration duration = getSessionDuration();
            long hours = duration.toHours();
            long minutes = duration.toMinutesPart();
            return String.format("%d | %s | Login: %s | Logout: %s | Duration: %dh %dm | %s",
                    doctorId, doctorName, loginTime, logoutTime, hours, minutes, status);
        } else {
            return String.format("%d | %s | Login: %s | Logout: N/A | Status: %s",
                    doctorId, doctorName, loginTime, status);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoctorSession that = (DoctorSession) o;
        return doctorId == that.doctorId &&
                Objects.equals(loginTime, that.loginTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(doctorId, loginTime);
    }
}
