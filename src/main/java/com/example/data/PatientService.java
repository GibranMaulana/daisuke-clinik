package com.example.data;

import com.example.model.Patient;
import com.example.model.ds.CustomeBST;
import com.example.model.ds.CustomeLinkedList;

public class PatientService {
    private final PatientDAO patientDAO = new PatientDAO();

    // Backward compatibility method
    public Patient registerPatient(String name, int age, String address, String phoneNumber) {
        return registerPatient(name, null, age, address, phoneNumber);
    }

    // New method with email support
    public Patient registerPatient(String name, String email, int age, String address, String phoneNumber) {
        int nextId = generateUniquePatientId();
        Patient newPatient = new Patient(nextId, name, null, name, email, age, address, phoneNumber);
        patientDAO.registerPatient(newPatient);
        return newPatient;
    }

    // Utility method to generate unique patient ID
    public int generateUniquePatientId() {
        // 1) Get all patients in a linked list
        CustomeBST<Patient> bst = patientDAO.getAllPatientsBST();
        CustomeLinkedList<Patient> all = bst.inOrderList();

        // 2) Find max numeric ID
        int maxId = 0;
        for (Patient p : all) {
            if (p.getId() > maxId) {
                maxId = p.getId();
            }
        }

        // 3) Next ID is maxId + 1
        return maxId + 1;  // this is your "7-digit" number if you want leading zeros visually
    }
}
