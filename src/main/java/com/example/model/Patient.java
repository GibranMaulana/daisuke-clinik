package com.example.model;

import com.example.model.ds.CustomeLinkedList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;

import java.util.Objects;

public class Patient extends User implements Comparable<Patient> {
    private int age;
    private String address;
    private String phoneNumber;

    // ─────────────────────────────────────────
    // 1) Keep the raw linked list, but ignore it when Jackson serializes
    // ─────────────────────────────────────────
    @JsonIgnore
    private CustomeLinkedList<Diagnosis> illnessHistory;

    public Patient() {
        super();
        this.illnessHistory = new CustomeLinkedList<>();
    }

    // Constructor for backward compatibility (fullname as name)
    public Patient(int id, String fullname, int age, String address, String phoneNumber) {
        super(id, fullname, null, fullname, null); // username=fullname, password=null, email=null
        this.age = age;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.illnessHistory = new CustomeLinkedList<>();
    }

    // Full constructor with User fields
    public Patient(int id, String username, String password, String fullname, String email, 
                   int age, String address, String phoneNumber) {
        super(id, username, password, fullname, email);
        this.age = age;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.illnessHistory = new CustomeLinkedList<>();
    }

    // ID validation for 7-digit IDs
    @Override
    public boolean isValidId(int id) {
        return id >= 1000000 && id <= 9999999;
    }

    // Backward compatibility: getName() maps to getFullname()
    public String getName() {
        return getFullname();
    }

    public void setName(String name) {
        setFullname(name);
    }

    // ─────────────────────────────────────────
    // 2) Usual getters & setters for basic fields
    // ─────────────────────────────────────────
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // ─────────────────────────────────────────
    // 3) Getter for illnessHistory as a Diagnosis[] (for Jackson)
    // ─────────────────────────────────────────
    @JsonProperty("illnessHistory")
    public Diagnosis[] getIllnessHistoryArray() {
        // Convert the CustomeLinkedList<Diagnosis> into a Diagnosis[]:
        int n = illnessHistory.size();
        Diagnosis[] arr = new Diagnosis[n];
        int idx = 0;
        for (Diagnosis d : illnessHistory) {
            arr[idx++] = d;
        }
        return arr;
    }

    // ─────────────────────────────────────────
    // 4) Setter for illnessHistory from a Diagnosis[] (for Jackson)
    // ─────────────────────────────────────────
    @JsonProperty("illnessHistory")
    public void setIllnessHistoryArray(Diagnosis[] arr) {
        this.illnessHistory = new CustomeLinkedList<>();
        if (arr != null) {
            for (Diagnosis d : arr) {
                this.illnessHistory.add(d);
            }
        }
    }

    // ─────────────────────────────────────────
    // 5) Allow other code to add directly to the linked list
    // ─────────────────────────────────────────
    public CustomeLinkedList<Diagnosis> getIllnessHistory() {
        return illnessHistory;
    }

    public void setIllnessHistory(CustomeLinkedList<Diagnosis> illnessHistory) {
        this.illnessHistory = illnessHistory;
    }

    public void addDiagnosis(Diagnosis diagnosis) {
        illnessHistory.add(diagnosis);
    }

    // Backward compatibility method for adding simple illness string
    public void addIllness(String illness) {
        // This method is kept for backward compatibility but should be avoided
        // Instead, use addDiagnosis() with proper Diagnosis object
        System.out.println("Warning: addIllness(String) is deprecated. Use addDiagnosis(Diagnosis) instead.");
    }

    // ─────────────────────────────────────────
    // 6) toString(), equals(), hashCode(), compareTo
    // ─────────────────────────────────────────
    @Override
    public String toString() {
        return String.format("%s | %s | %d | %s | %s",
                id, getFullname(), age, address, phoneNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient)) return false;
        Patient other = (Patient) o;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

     @Override
    public int compareTo(Patient other) {
        return Integer.compare(this.id, other.id);
    }
}
