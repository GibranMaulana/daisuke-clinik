package com.example.data;

import com.example.model.Diagnosis;
import com.example.model.ds.CustomeLinkedList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Data Access Object for managing Diagnosis records
 */
public class DiagnosisDAO {
    private static final String FILE = "diagnoses.json";
    private final ObjectMapper mapper;

    public DiagnosisDAO() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * Load all diagnoses from JSON file
     */
    public CustomeLinkedList<Diagnosis> loadAllDiagnoses() {
        try {
            File f = new File(FILE);
            if (!f.exists() || f.length() == 0) {
                return new CustomeLinkedList<>();
            }
            Diagnosis[] arr = mapper.readValue(f, Diagnosis[].class);
            CustomeLinkedList<Diagnosis> list = new CustomeLinkedList<>();
            for (Diagnosis d : arr) {
                list.add(d);
            }
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new CustomeLinkedList<>();
        }
    }

    /**
     * Save all diagnoses to JSON file
     */
    private void saveAllDiagnoses(CustomeLinkedList<Diagnosis> all) {
        try {
            int n = all.size();
            Diagnosis[] arr = new Diagnosis[n];
            int idx = 0;
            for (Diagnosis d : all) {
                arr[idx++] = d;
            }
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE), arr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Add a new diagnosis
     */
    public void addDiagnosis(Diagnosis diagnosis) {
        CustomeLinkedList<Diagnosis> all = loadAllDiagnoses();
        
        // Generate unique ID if not set
        if (diagnosis.getId() == 0) {
            diagnosis.setId(generateUniqueId(all));
        }
        
        all.add(diagnosis);
        saveAllDiagnoses(all);
    }

    /**
     * Get all diagnoses for a specific patient
     */
    public CustomeLinkedList<Diagnosis> getDiagnosesForPatient(int patientId) {
        CustomeLinkedList<Diagnosis> all = loadAllDiagnoses();
        CustomeLinkedList<Diagnosis> result = new CustomeLinkedList<>();
        for (Diagnosis d : all) {
            if (d.getPatientId() == patientId) {
                result.add(d);
            }
        }
        return result;
    }

    /**
     * Get all diagnoses made by a specific doctor
     */
    public CustomeLinkedList<Diagnosis> getDiagnosesForDoctor(int doctorId) {
        CustomeLinkedList<Diagnosis> all = loadAllDiagnoses();
        CustomeLinkedList<Diagnosis> result = new CustomeLinkedList<>();
        for (Diagnosis d : all) {
            if (d.getDoctorId() == doctorId) {
                result.add(d);
            }
        }
        return result;
    }

    /**
     * Find diagnosis by appointment ID
     */
    public Diagnosis getDiagnosisByAppointmentId(int appointmentId) {
        CustomeLinkedList<Diagnosis> all = loadAllDiagnoses();
        for (Diagnosis d : all) {
            if (d.getAppointmentId() == appointmentId) {
                return d;
            }
        }
        return null;
    }

    /**
     * Generate a unique ID for a new diagnosis
     */
    public int generateUniqueId() {
        CustomeLinkedList<Diagnosis> existing = loadAllDiagnoses();
        return generateUniqueId(existing);
    }

    /**
     * Generate a unique ID for a new diagnosis
     */
    private int generateUniqueId(CustomeLinkedList<Diagnosis> existingDiagnoses) {
        int newId;
        boolean isUnique;
        do {
            newId = ThreadLocalRandom.current().nextInt(100000, 999999);
            isUnique = true;
            for (Diagnosis d : existingDiagnoses) {
                if (d.getId() == newId) {
                    isUnique = false;
                    break;
                }
            }
        } while (!isUnique);
        return newId;
    }
}
