package com.example.model;

import com.example.model.ds.CustomeLinkedList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Patient implements Comparable<Patient> {
    private int id;       // 7‐digit numeric string
    private String name;
    private int age;
    private String address;
    private String phoneNumber;

    // ─────────────────────────────────────────
    // 1) Keep the raw linked list, but ignore it when Jackson serializes
    // ─────────────────────────────────────────
    @JsonIgnore
    private CustomeLinkedList<String> illnessHistory;

    public Patient() {
        this.illnessHistory = new CustomeLinkedList<>();
    }

    public Patient(int id, String name, int age, String address, String phoneNumber) {
        this();
        this.id = id;
        this.name = name;
        this.age = age;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    // ─────────────────────────────────────────
    // 2) Usual getters & setters for basic fields
    // ─────────────────────────────────────────
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
    // 3) Getter for illnessHistory as a String[] (for Jackson)
    // ─────────────────────────────────────────
    @JsonProperty("illnessHistory")
    public String[] getIllnessHistoryArray() {
        // Convert the CustomeLinkedList<String> into a String[]:
        int n = illnessHistory.size();
        String[] arr = new String[n];
        int idx = 0;
        for (String s : illnessHistory) {
            arr[idx++] = s;
        }
        return arr;
    }

    // ─────────────────────────────────────────
    // 4) Setter for illnessHistory from a String[] (for Jackson)
    // ─────────────────────────────────────────
    @JsonProperty("illnessHistory")
    public void setIllnessHistoryArray(String[] arr) {
        this.illnessHistory = new CustomeLinkedList<>();
        if (arr != null) {
            for (String s : arr) {
                this.illnessHistory.add(s);
            }
        }
    }

    // ─────────────────────────────────────────
    // 5) Allow other code to add directly to the linked list
    // ─────────────────────────────────────────
    public CustomeLinkedList<String> getIllnessHistory() {
        return illnessHistory;
    }

    public void setIllnessHistory(CustomeLinkedList<String> illnessHistory) {
        this.illnessHistory = illnessHistory;
    }

    public void addIllness(String illness) {
        illnessHistory.add(illness);
    }

    // ─────────────────────────────────────────
    // 6) toString(), equals(), hashCode(), compareTo
    // ─────────────────────────────────────────
    @Override
    public String toString() {
        return String.format("%s | %s | %d | %s | %s",
                id, name, age, address, phoneNumber);
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
