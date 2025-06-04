package com.example.data;

import com.example.model.Doctor;
import com.example.model.ds.CustomeLinkedList;

public class DoctorService {
    private final DoctorDAO doctorDAO = new DoctorDAO();

    public Doctor registerDoctor(String name, String password, String specialty) {
        // 1) Get the current list of doctors
        CustomeLinkedList<Doctor> all = doctorDAO.getAllDoctors();

        // 2) Find max numeric ID
        int maxId = 0;
        for (Doctor d : all) {
            if (d.getId() > maxId) {
                maxId = d.getId();
            }
        }

        // 3) Next ID is maxId + 1
        int nextId = maxId + 1;  // e.g. if maxId was 7, new ID = 8

        Doctor newDoctor = new Doctor(nextId, name, password, specialty);
        doctorDAO.registerDoctor(newDoctor);
        return newDoctor;
    }

    public Doctor authenticate(int id, String password) {
        Doctor d = doctorDAO.findById(id);
        if (d != null && d.getPassword().equals(password)) {
            return d;
        }
        return null;
    }
}
