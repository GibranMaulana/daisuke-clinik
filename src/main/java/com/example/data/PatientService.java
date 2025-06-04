package com.example.data;

import com.example.model.Patient;
import com.example.model.ds.CustomeBST;
import com.example.model.ds.CustomeLinkedList;

public class PatientService {
    private final PatientDAO patientDAO = new PatientDAO();

    public Patient registerPatient(String name, int age, String address, String phoneNumber) {
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
        int nextId = maxId + 1;  // this is your “7-digit” number if you want leading zeros visually
        //    e.g. if nextId=1, you can store it as int 1. If you ever need to print as 7 digits:
        //    String formatted = String.format("%07d", nextId);

        Patient newPatient = new Patient(nextId, name, age, address, phoneNumber);
        patientDAO.registerPatient(newPatient);
        return newPatient;
    }
}
