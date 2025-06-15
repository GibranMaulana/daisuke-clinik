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
        CustomeLinkedList<Appointment> result = new CustomeLinkedList<>();
        for (Appointment a : all) {
            if (a.getDoctorId() == doctorId) {
                result.add(a);
            }
        }
        return result;
    }

    /** Dequeue (first) appointment for doctorId, then save. */
    public Appointment processNextAppointment(int doctorId) {
        CustomeLinkedList<Appointment> all = loadAllAppointments();
        Appointment toProcess = null;
        for (Appointment a : all) {
            if (a.getDoctorId() == doctorId) {
                toProcess = a;
                break;
            }
        }
        if (toProcess != null) {
            all.remove(toProcess);
            saveAllAppointments(all);
        }
        return toProcess;
    }

    /** Return (without dequeuing) first appointment for doctorId */
    public Appointment peekNextAppointment(int doctorId) {
        CustomeLinkedList<Appointment> all = loadAllAppointments();
        for (Appointment a : all) {
            if (a.getDoctorId() == doctorId) {
                return a;
            }
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
}
