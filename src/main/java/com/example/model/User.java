package com.example.model;

import java.util.Objects;

/**
 * Base class for all users in the hospital management system.
 * Contains common fields that all users share: id, username, password, fullname, email.
 */
public abstract class User {
    protected int id;
    protected String username;
    protected String password;
    protected String fullname;
    protected String email;

    // Default constructor
    public User() {}

    // Constructor with all fields
    public User(int id, String username, String password, String fullname, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.email = email;
    }

    // Getters and Setters
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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Abstract method for ID validation - subclasses must implement
    public abstract boolean isValidId(int id);

    // Validate ID when setting it
    public void setValidatedId(int id) {
        if (!isValidId(id)) {
            throw new IllegalArgumentException("Invalid ID format for " + this.getClass().getSimpleName());
        }
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s{id=%d, username='%s', fullname='%s', email='%s'}", 
                this.getClass().getSimpleName(), id, username, fullname, email);
    }
}