package com.example.data;

import com.example.model.PendingDoctorRegistration;
import com.example.model.ds.CustomeQueue;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PendingDoctorRegistrationDAO {
    private static final String PENDING_FILE = "pendingDoctorRegistrations.json";
    private final ObjectMapper objectMapper;
    private CustomeQueue<PendingDoctorRegistration> pendingQueue;

    public PendingDoctorRegistrationDAO() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.pendingQueue = new CustomeQueue<>();
        loadPendingRegistrations();
    }

    /**
     * Add a new pending doctor registration to the queue
     */
    public void addPendingRegistration(PendingDoctorRegistration registration) {
        pendingQueue.enqueue(registration);
        savePendingRegistrations();
    }

    /**
     * Get the next pending registration from the queue (for admin processing)
     */
    public PendingDoctorRegistration getNextPendingRegistration() {
        PendingDoctorRegistration registration = pendingQueue.dequeue();
        if (registration != null) {
            savePendingRegistrations();
        }
        return registration;
    }

    /**
     * Check if a doctor ID is in the pending queue
     */
    public boolean isDoctorPending(int doctorId) {
        List<PendingDoctorRegistration> allPending = getAllPendingRegistrations();
        return allPending.stream()
                .anyMatch(registration -> registration.getDoctorId() == doctorId);
    }

    /**
     * Get pending registration by doctor ID
     */
    public PendingDoctorRegistration getPendingByDoctorId(int doctorId) {
        List<PendingDoctorRegistration> allPending = getAllPendingRegistrations();
        return allPending.stream()
                .filter(registration -> registration.getDoctorId() == doctorId)
                .findFirst()
                .orElse(null);
    }

    /**
     * Get all pending registrations as a list (for admin view)
     */
    public List<PendingDoctorRegistration> getAllPendingRegistrations() {
        List<PendingDoctorRegistration> list = new ArrayList<>();
        
        // Temporarily dequeue all items, add to list, then re-enqueue
        CustomeQueue<PendingDoctorRegistration> tempQueue = new CustomeQueue<>();
        
        while (!pendingQueue.isEmpty()) {
            PendingDoctorRegistration registration = pendingQueue.dequeue();
            list.add(registration);
            tempQueue.enqueue(registration);
        }
        
        // Restore the original queue
        while (!tempQueue.isEmpty()) {
            pendingQueue.enqueue(tempQueue.dequeue());
        }
        
        return list;
    }

    /**
     * Remove a specific pending registration (used when approved/rejected)
     */
    public boolean removePendingRegistration(int doctorId) {
        CustomeQueue<PendingDoctorRegistration> newQueue = new CustomeQueue<>();
        boolean found = false;
        
        while (!pendingQueue.isEmpty()) {
            PendingDoctorRegistration registration = pendingQueue.dequeue();
            if (registration.getDoctorId() != doctorId) {
                newQueue.enqueue(registration);
            } else {
                found = true;
            }
        }
        
        this.pendingQueue = newQueue;
        if (found) {
            savePendingRegistrations();
        }
        return found;
    }

    /**
     * Check if the pending queue is empty
     */
    public boolean isEmpty() {
        return pendingQueue.isEmpty();
    }

    /**
     * Get the size of the pending queue
     */
    public int size() {
        return pendingQueue.size();
    }

    /**
     * Load pending registrations from JSON file into the queue
     */
    private void loadPendingRegistrations() {
        File file = new File(PENDING_FILE);
        if (!file.exists()) {
            return; // No pending registrations file exists yet
        }

        try {
            List<PendingDoctorRegistration> registrationList = objectMapper.readValue(
                file, new TypeReference<List<PendingDoctorRegistration>>() {}
            );
            
            // Clear the queue and reload from file
            this.pendingQueue = new CustomeQueue<>();
            for (PendingDoctorRegistration registration : registrationList) {
                pendingQueue.enqueue(registration);
            }
        } catch (IOException e) {
            System.err.println("Error loading pending registrations: " + e.getMessage());
            this.pendingQueue = new CustomeQueue<>();
        }
    }

    /**
     * Save pending registrations from queue to JSON file
     */
    private void savePendingRegistrations() {
        try {
            List<PendingDoctorRegistration> registrationList = getAllPendingRegistrations();
            
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(PENDING_FILE), registrationList);
        } catch (IOException e) {
            System.err.println("Error saving pending registrations: " + e.getMessage());
        }
    }
}
