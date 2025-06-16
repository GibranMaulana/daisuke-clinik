package com.example.data;

import com.example.model.Appointment;
import com.example.model.ds.CustomeLinkedList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;

/**
 * Now reads/writes an Appointment[] (with int IDs).
 */
public class AppointmentDAO {
    private static final String FILE = "allAppointments.json";
    private final ObjectMapper mapper;

    public AppointmentDAO() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /** JSON → Appointment[] → CustomeLinkedList */
    public CustomeLinkedList<Appointment> loadAllAppointments() {
        try {
            File f = new File(FILE);
            if (!f.exists() || f.length() == 0) {
                return new CustomeLinkedList<>();
            }
            Appointment[] arr = mapper.readValue(f, Appointment[].class);
            CustomeLinkedList<Appointment> list = new CustomeLinkedList<>();
            for (Appointment a : arr) {
                list.add(a);
            }
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new CustomeLinkedList<>();
        }
    }

    /** CustomeLinkedList → Appointment[] → JSON */
    private void saveAllAppointments(CustomeLinkedList<Appointment> all) {
        try {
            int n = all.size();
            Appointment[] arr = new Appointment[n];
            int idx = 0;
            for (Appointment a : all) {
                arr[idx++] = a;
            }
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE), arr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /** Enqueue + save */
    public void scheduleAppointment(Appointment a) {
        CustomeLinkedList<Appointment> all = loadAllAppointments();
        all.add(a);
        saveAllAppointments(all);
    }

    /** Return queue for a given doctorId */
    public CustomeLinkedList<Appointment> getQueueForDoctor(int doctorId) {
        CustomeLinkedList<Appointment> all = loadAllAppointments();
        CustomeLinkedList<Appointment> doctorAppointments = new CustomeLinkedList<>();
        
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        
        // Filter appointments for this doctor and collect only scheduled ones that are in the future
        for (Appointment a : all) {
            if (a.getDoctorId() == doctorId && 
                "scheduled".equals(a.getStatus()) &&
                a.getTime().isAfter(now) &&
                isValidAppointmentTime(a.getTime())) {
                doctorAppointments.add(a);
            }
        }
        
        // Sort the appointments by time using insertion sort to maintain custom data structure
        return sortAppointmentsByTime(doctorAppointments);
    }
    
    /**
     * Helper method to sort appointments by time using insertion sort
     * This maintains our custom data structure approach without using ArrayList
     */
    private CustomeLinkedList<Appointment> sortAppointmentsByTime(CustomeLinkedList<Appointment> appointments) {
        if (appointments.size() <= 1) {
            return appointments;
        }
        
        CustomeLinkedList<Appointment> sorted = new CustomeLinkedList<>();
        
        for (Appointment current : appointments) {
            // Find the correct position to insert current appointment
            boolean inserted = false;
            CustomeLinkedList<Appointment> temp = new CustomeLinkedList<>();
            
            for (Appointment existing : sorted) {
                if (!inserted && current.getTime().isBefore(existing.getTime())) {
                    temp.add(current);
                    inserted = true;
                }
                temp.add(existing);
            }
            
            // If not inserted yet, add at the end
            if (!inserted) {
                temp.add(current);
            }
            
            sorted = temp;
        }
        
        return sorted;
    }

    /** Dequeue (first) appointment for doctorId, then save. */
    public Appointment processNextAppointment(int doctorId) {
        // Get the sorted queue for this doctor
        CustomeLinkedList<Appointment> sortedQueue = getQueueForDoctor(doctorId);
        
        if (sortedQueue.isEmpty()) {
            return null;
        }
        
        // Get the earliest appointment (first in sorted queue)
        Appointment toProcess = sortedQueue.get(0);
        
        // Remove the appointment from the main list and save
        CustomeLinkedList<Appointment> all = loadAllAppointments();
        all.remove(toProcess);
        saveAllAppointments(all);
        
        return toProcess;
    }

    /** Return (without dequeuing) first appointment for doctorId */
    public Appointment peekNextAppointment(int doctorId) {
        CustomeLinkedList<Appointment> queue = getQueueForDoctor(doctorId);
        if (queue.size() > 0) {
            return queue.get(0); // Get first appointment from sorted queue
        }
        return null;
    }

    public boolean hasAppointment(int patientId, int doctorId) {
        try {
            File f = new File(FILE);
            if (!f.exists() || f.length() == 0) {
                return false;
            }
            Appointment[] arr = mapper.readValue(f, Appointment[].class);
            for (Appointment a : arr) {
                if (a.getPatientId() == patientId && a.getDoctorId() == doctorId) {
                    return true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /** Get all appointments as a list - used by admin functions */
    public CustomeLinkedList<Appointment> getAllAppointments() {
        return loadAllAppointments();
    }

    /**
     * Check if there's a time conflict for a doctor at the specified time
     */
    public boolean hasTimeConflict(int doctorId, java.time.LocalDateTime appointmentTime) {
        CustomeLinkedList<Appointment> all = loadAllAppointments();
        for (Appointment a : all) {
            if (a.getDoctorId() == doctorId && a.getTime().equals(appointmentTime)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if a patient already has an appointment at the specified time
     */
    public boolean patientHasTimeConflict(int patientId, java.time.LocalDateTime appointmentTime) {
        CustomeLinkedList<Appointment> all = loadAllAppointments();
        for (Appointment a : all) {
            if (a.getPatientId() == patientId && a.getTime().equals(appointmentTime)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the appointment time is valid (between 08:00-11:00 or 13:00-21:00)
     */
    public boolean isValidAppointmentTime(java.time.LocalDateTime appointmentTime) {
        java.time.LocalTime time = appointmentTime.toLocalTime();
        java.time.LocalTime morning_start = java.time.LocalTime.of(8, 0);
        java.time.LocalTime morning_end = java.time.LocalTime.of(11, 0);
        java.time.LocalTime afternoon_start = java.time.LocalTime.of(13, 0);
        java.time.LocalTime afternoon_end = java.time.LocalTime.of(21, 0);
        
        // Must be exactly on the hour
        if (time.getMinute() != 0 || time.getSecond() != 0) {
            return false;
        }
        
        // Check if time is in valid ranges
        return (time.compareTo(morning_start) >= 0 && time.compareTo(morning_end) <= 0) ||
               (time.compareTo(afternoon_start) >= 0 && time.compareTo(afternoon_end) <= 0);
    }

    /**
     * Get available time slots for a specific date
     */
    public java.util.List<java.time.LocalTime> getAvailableTimeSlots() {
        java.util.List<java.time.LocalTime> slots = new java.util.ArrayList<>();
        
        // Morning slots: 08:00 - 11:00
        for (int hour = 8; hour <= 11; hour++) {
            slots.add(java.time.LocalTime.of(hour, 0));
        }
        
        // Afternoon slots: 13:00 - 21:00
        for (int hour = 13; hour <= 21; hour++) {
            slots.add(java.time.LocalTime.of(hour, 0));
        }
        
        return slots;
    }

    /**
     * Remove appointments that have passed their scheduled time + 2 hours
     * This method should be called periodically to clean up old appointments
     */
    public int removeExpiredAppointments() {
        CustomeLinkedList<Appointment> all = loadAllAppointments();
        CustomeLinkedList<Appointment> validAppointments = new CustomeLinkedList<>();
        java.time.LocalDateTime cutoffTime = java.time.LocalDateTime.now().minusHours(2);
        
        int removedCount = 0;
        
        for (Appointment appointment : all) {
            // Keep appointments that are still within the 2-hour grace period
            if (appointment.getTime().isAfter(cutoffTime)) {
                validAppointments.add(appointment);
            } else {
                removedCount++;
                System.out.println("Removing expired appointment: " + appointment.getAppointmentId() + 
                                 " scheduled for " + appointment.getTime());
            }
        }
        
        // Save the filtered list back to file
        saveAllAppointments(validAppointments);
        
        return removedCount;
    }
    
    /**
     * Remove a specific appointment by ID (for admin use)
     */
    public boolean removeAppointment(int appointmentId) {
        CustomeLinkedList<Appointment> all = loadAllAppointments();
        
        for (Appointment appointment : all) {
            if (appointment.getAppointmentId() == appointmentId) {
                all.remove(appointment);
                saveAllAppointments(all);
                return true;
            }
        }
        
        return false; // Appointment not found
    }
}
