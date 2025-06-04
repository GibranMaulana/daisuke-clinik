package com.example.model;

import com.example.model.ds.CustomeLinkedList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Objects;

public class Doctor implements Comparable<Doctor> {
    private int id;    
    private String name;
    private String password;    
    private String specialty;
    private LocalDateTime currentLoginTime;

    @JsonIgnore
    private CustomeLinkedList<LocalDateTime> loginHistory;

    public Doctor() {
        loginHistory = new CustomeLinkedList<>();
    }

    public Doctor(int id, String name, String password, String specialty) {
        this();
        this.id = id;
        this.name = name;
        this.password = password;
        this.specialty = specialty;
    }

    // ─────────────────────────────────────────
    // Basic getters & setters (id, name, password, specialty, currentLoginTime)
    // ─────────────────────────────────────────
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public LocalDateTime getCurrentLoginTime() { return currentLoginTime; }
    public void setCurrentLoginTime(LocalDateTime loginTime) {
        this.currentLoginTime = loginTime;
        if (loginTime != null) {
            loginHistory.add(loginTime);
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
        return String.format("%s | %s | %s", id, name, specialty);
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
