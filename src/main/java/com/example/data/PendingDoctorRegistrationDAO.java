package com.example.data;

import com.example.model.PendingDoctorRegistration;
import com.example.model.ds.CustomeQueue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import com.example.model.ds.CustomeLinkedList;

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
        CustomeLinkedList<PendingDoctorRegistration> allPending = getAllPendingRegistrations();
        for (PendingDoctorRegistration registration : allPending) {
            if (registration.getDoctorId() == doctorId) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get pending registration by doctor ID
     */
    public PendingDoctorRegistration getPendingByDoctorId(int doctorId) {
        CustomeLinkedList<PendingDoctorRegistration> allPending = getAllPendingRegistrations();
        for (PendingDoctorRegistration registration : allPending) {
            if (registration.getDoctorId() == doctorId) {
                return registration;
            }
        }
        return null;
    }

    /**
     * Get all pending registrations as a list (for admin view)
     */
    public CustomeLinkedList<PendingDoctorRegistration> getAllPendingRegistrations() {
        CustomeLinkedList<PendingDoctorRegistration> list = new CustomeLinkedList<>();
        
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
            PendingDoctorRegistration[] registrationArray = objectMapper.readValue(
                file, PendingDoctorRegistration[].class
            );
            
            // Clear the queue and reload from file
            this.pendingQueue = new CustomeQueue<>();
            for (PendingDoctorRegistration registration : registrationArray) {
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
            CustomeLinkedList<PendingDoctorRegistration> registrationList = getAllPendingRegistrations();
            
            // Convert CustomeLinkedList to array for JSON serialization
            // Handle empty list case to avoid ClassCastException
            PendingDoctorRegistration[] registrationArray;
            if (registrationList.size() == 0) {
                // Create empty array with correct type
                registrationArray = new PendingDoctorRegistration[0];
            } else {
                registrationArray = registrationList.toJsonArray();
            }
            
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(PENDING_FILE), registrationArray);
        } catch (IOException e) {
            System.err.println("Error saving pending registrations: " + e.getMessage());
        }
    }
}
