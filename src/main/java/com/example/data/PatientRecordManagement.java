package com.example.data;

import java.io.File;

import com.example.model.Patient;
import com.example.model.ds.CustomeLinkedList;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

// In PatientRecordManagement.java

public class PatientRecordManagement {
    private static final String FILE = "patient.json";
    private final ObjectMapper mapper = new ObjectMapper();
    private CustomeLinkedList<Patient> allPatients;

    private static final PatientRecordManagement INSTANCE = new PatientRecordManagement();

    private PatientRecordManagement() {
        loadAll();  // this will call mapper.readValue(...) on CustomeLinkedList<Patient>
    }

    public static PatientRecordManagement getInstance() {
        return INSTANCE;
    }

    private void loadAll() {
        File file = new File(FILE);
        if (!file.exists()) {
            allPatients = new CustomeLinkedList<>();
            return;
        }

        try {
            // Because of @JsonCreator/@JsonValue, Jackson knows how to (de)serialize
            allPatients = mapper.readValue(
                new File(FILE),
                new TypeReference<CustomeLinkedList<Patient>>() {}
            );
        } catch (Exception e) {
            e.printStackTrace();
            allPatients = new CustomeLinkedList<>();
        }
    }

    public void saveAll() {
        try {
            mapper.writerWithDefaultPrettyPrinter()
                  .writeValue(new File(FILE), allPatients);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addPatient(Patient patient) {
        allPatients.add(patient);
        saveAll();
    }

    public CustomeLinkedList<Patient> getAllPatients() {
        return allPatients;
    }

    public Patient findPatientById(String id) {
        for (Patient p : allPatients) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        throw new IllegalStateException("Patient not found with ID: " + id);
    }
}
