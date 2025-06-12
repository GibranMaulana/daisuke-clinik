package com.example.model;

import com.example.model.ds.CustomeQueue;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Admin implements Comparable<Admin> {
    private int id;
    private String username;
    private String password;
    private AdminLevel adminLevel;
    
    @JsonIgnore
    private CustomeQueue<PendingDoctorRegistration> pendingDoctorRegistrations;

    public Admin() {
        this.pendingDoctorRegistrations = new CustomeQueue<>();
        this.adminLevel = AdminLevel.REGULAR; // Default to regular admin
    }

    public Admin(int id, String username, String password) {
        this();
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Admin(int id, String username, String password, AdminLevel adminLevel) {
        this();
        this.id = id;
        this.username = username;
        this.password = password;
        this.adminLevel = adminLevel;
    }

    // ─────────────────────────────────────────
    // Basic getters & setters
    // ─────────────────────────────────────────
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AdminLevel getAdminLevel() {
        return adminLevel;
    }

    public void setAdminLevel(AdminLevel adminLevel) {
        this.adminLevel = adminLevel;
    }

    // ─────────────────────────────────────────
    // Admin Level Helper Methods
    // ─────────────────────────────────────────
    @JsonIgnore
    public boolean isSuperAdmin() {
        return adminLevel == AdminLevel.SUPER;
    }

    @JsonIgnore
    public boolean isRegularAdmin() {
        return adminLevel == AdminLevel.REGULAR;
    }

    @JsonIgnore
    public boolean canRemoveUsers() {
        return isSuperAdmin();
    }

    @JsonIgnore
    public boolean canProcessRegistrations() {
        return isSuperAdmin();
    }

    @JsonIgnore
    public boolean canViewOnly() {
        return isRegularAdmin();
    }

    // ─────────────────────────────────────────
    // Pending Doctor Registration Queue Management
    // ─────────────────────────────────────────
    public CustomeQueue<PendingDoctorRegistration> getPendingDoctorRegistrations() {
        return pendingDoctorRegistrations;
    }

    public void addPendingDoctorRegistration(PendingDoctorRegistration registration) {
        pendingDoctorRegistrations.enqueue(registration);
    }

    @JsonIgnore
    public PendingDoctorRegistration getNextPendingRegistration() {
        return pendingDoctorRegistrations.peek();
    }

    @JsonIgnore
    public PendingDoctorRegistration processNextPendingRegistration() {
        return pendingDoctorRegistrations.dequeue();
    }

    public boolean hasPendingRegistrations() {
        return !pendingDoctorRegistrations.isEmpty();
    }

    @JsonIgnore
    public int getPendingRegistrationsCount() {
        return pendingDoctorRegistrations.size();
    }

    @JsonProperty("pendingRegistrationsArray")
    public Object[] getPendingRegistrationsArray() {
        return pendingDoctorRegistrations.toArray();
    }

    @JsonProperty("pendingRegistrationsArray")
    public void setPendingRegistrationsArray(Object[] array) {
        pendingDoctorRegistrations = new CustomeQueue<>();
        if (array != null) {
            for (Object obj : array) {
                if (obj instanceof PendingDoctorRegistration) {
                    pendingDoctorRegistrations.enqueue((PendingDoctorRegistration) obj);
                }
            }
        }
    }

    // ─────────────────────────────────────────
    // Object methods
    // ─────────────────────────────────────────
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Admin admin = (Admin) obj;
        return id == admin.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Admin other) {
        return Integer.compare(this.id, other.id);
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", adminLevel=" + adminLevel +
                ", pendingRegistrations=" + getPendingRegistrationsCount() +
                '}';
    }
}
