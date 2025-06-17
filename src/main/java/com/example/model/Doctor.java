package com.example.model;

import com.example.model.ds.CustomeLinkedList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Objects;

public class Doctor extends User implements Comparable<Doctor> {
    private String specialty;
    @JsonIgnore
    private CustomeLinkedList<LocalDateTime> loginHistory;

    public Doctor() {
        super();
        loginHistory = new CustomeLinkedList<>();
    }

    // Constructor for backward compatibility (maps name to fullname)
    public Doctor(int id, String name, String password, String specialty) {
        super(id, name, password, name, null); // username=name, fullname=name, email=null
        this.specialty = specialty;
        this.loginHistory = new CustomeLinkedList<>();
    }

    // Full constructor with User fields
    public Doctor(int id, String username, String password, String fullname, String email, String specialty) {
        super(id, username, password, fullname, email);
        this.specialty = specialty;
        this.loginHistory = new CustomeLinkedList<>();
    }

    // ID validation for 4-digit IDs
    @Override
    public boolean isValidId(int id) {
        return id >= 1000 && id <= 9999;
    }

    // Backward compatibility: getName() maps to getFullname()
    public String getName() {
        return getFullname();
    }

    public void setName(String name) {
        setFullname(name);
    }

    private LocalDateTime currentLoginTime;

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public LocalDateTime getCurrentLoginTime() { return currentLoginTime; }
    public void setCurrentLoginTime(LocalDateTime loginTime) {
        this.currentLoginTime = loginTime;
        if (loginTime != null) {
            // Only add to history if no login exists for today
            boolean hasLoginToday = false;
            java.time.LocalDate today = loginTime.toLocalDate();
            
            for (LocalDateTime historyTime : loginHistory) {
                if (historyTime.toLocalDate().equals(today)) {
                    hasLoginToday = true;
                    break;
                }
            }
            
            // Only add if this is the first login of the day
            if (!hasLoginToday) {
                loginHistory.add(loginTime);
            }
        }
    }

    // ─────────────────────────────────────────
    // Expose loginHistory as a LocalDateTime[] for Jackson
    // ─────────────────────────────────────────
    @JsonProperty("loginHistory")
    public LocalDateTime[] getLoginHistoryArray() {
        int n = loginHistory.size();
        LocalDateTime[] arr = new LocalDateTime[n];
        int idx = 0;
        for (LocalDateTime dt : loginHistory) {
            arr[idx++] = dt;
        }
        return arr;
    }

    @JsonProperty("loginHistory")
    public void setLoginHistoryArray(LocalDateTime[] arr) {
        this.loginHistory = new CustomeLinkedList<>();
        if (arr != null) {
            for (LocalDateTime dt : arr) {
                this.loginHistory.add(dt);
            }
        }
    }

    public CustomeLinkedList<LocalDateTime> getLoginHistory() {
        return loginHistory;
    }

    public void setLoginHistory(CustomeLinkedList<LocalDateTime> loginHistory) {
        this.loginHistory = loginHistory;
    }

    // ─────────────────────────────────────────
    // toString, equals, hashCode, compareTo
    // ─────────────────────────────────────────
    @Override
    public String toString() {
        return String.format("%s | %s | %s", id, getFullname(), specialty);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doctor)) return false;
        Doctor other = (Doctor) o;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Doctor other) {
        return Integer.compare(this.id, other.id);
    }
}
