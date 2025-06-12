package com.example.data;

import com.example.model.Patient;
import com.example.model.ds.CustomeBST;
import com.example.model.ds.CustomeLinkedList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;

/**
 * Now works with `Patient[]` and int‐based IDs.
 */
public class PatientDAO {
    private static final String FILE = "patients.json";
    private final ObjectMapper mapper;
    private CustomeBST<Patient> patientsBST;

    public PatientDAO() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        this.patientsBST = loadAllPatients();
    }

    /** JSON → Patient[] → BST */
    private CustomeBST<Patient> loadAllPatients() {
        try {
            File f = new File(FILE);
            if (!f.exists() || f.length() == 0) {
                return new CustomeBST<>();
            }
            Patient[] arr = mapper.readValue(f, Patient[].class);
            CustomeBST<Patient> bst = new CustomeBST<>();
            for (Patient p : arr) {
                bst.insert(p);
            }
            return bst;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new CustomeBST<>();
        }
    }

    /** BST → Patient[] → JSON */
    private void saveAllPatients() {
        try {
            CustomeLinkedList<Patient> all = patientsBST.inOrderList();
            int n = all.size();
            Patient[] arr = new Patient[n];
            int idx = 0;
            for (Patient p : all) {
                arr[idx++] = p;
            }
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE), arr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /** Insert new patient into BST + JSON. */
    public void registerPatient(Patient p) {
        patientsBST.insert(p);
        saveAllPatients();
    }

    /** Find by int ID, either via direct BST search or in‐order scan. */
    public Patient findById(int id) {
        // 1) Try BST.searchExact
        Patient key = new Patient(id, "", 0, "", "");
        Patient found = patientsBST.search(key);
        if (found != null) {
            return found;
        }
        // 2) Fallback to in‐order traversal
        CustomeLinkedList<Patient> all = patientsBST.inOrderList();
        for (Patient p : all) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    /** Delete old and insert updated, then persist. */
    // In PatientDAO.java

    public void updatePatient(Patient p) {
        // 1) Delete any existing entry with the same ID
        Patient existing = findById(p.getId());
        if (existing != null) {
            patientsBST.delete(existing);
        }

        // 2) Insert the updated patient
        patientsBST.insert(p);

        // 3) Persist the entire BST to JSON
        saveAllPatients();
    }

    public CustomeBST<Patient> getAllPatientsBST() {
        return patientsBST;
    }

    /** 
     * Refresh the cached BST data by reloading from the JSON file.
     * This ensures we get the latest data after external changes.
     */
    public void refreshData() {
        this.patientsBST = loadAllPatients();
    }

    /** Get all patients as a list - used by admin functions */
    public CustomeLinkedList<Patient> getAllPatients() {
        return patientsBST.inOrderList();
    }

    /** Delete patient by ID - used by admin functions */
    public boolean deletePatient(int id) {
        Patient toDelete = findById(id);
        if (toDelete != null) {
            patientsBST.delete(toDelete);
            saveAllPatients();
            return true;
        }
        return false;
    }
}
