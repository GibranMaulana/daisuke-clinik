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
}
