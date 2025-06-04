package com.example.data;

import com.example.model.Doctor;
import com.example.model.ds.CustomeLinkedList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;

/**
 * Now reads/writes a Doctor[] (with int IDs).
 */
public class DoctorDAO {
    private static final String FILE = "doctors.json";
    private final ObjectMapper mapper;
    private CustomeLinkedList<Doctor> doctorList;

    public DoctorDAO() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        this.doctorList = loadAllDoctors();
    }

    /** JSON → Doctor[] → CustomeLinkedList */
    private CustomeLinkedList<Doctor> loadAllDoctors() {
        try {
            File f = new File(FILE);
            if (!f.exists() || f.length() == 0) {
                return new CustomeLinkedList<>();
            }
            Doctor[] arr = mapper.readValue(f, Doctor[].class);
            CustomeLinkedList<Doctor> list = new CustomeLinkedList<>();
            for (Doctor d : arr) {
                list.add(d);
            }
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new CustomeLinkedList<>();
        }
    }

    /** CustomeLinkedList → Doctor[] → JSON */
    private void saveAllDoctors() {
        try {
            int n = doctorList.size();
            Doctor[] arr = new Doctor[n];
            int idx = 0;
            for (Doctor d : doctorList) {
                arr[idx++] = d;
            }
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE), arr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void registerDoctor(Doctor d) {
        doctorList.add(d);
        saveAllDoctors();
    }

    public Doctor findById(int id) {
        for (Doctor d : doctorList) {
            if (d.getId() == id) return d;
        }
        return null;
    }

    public void updateDoctor(Doctor d) {
        Doctor old = findById(d.getId());
        if (old != null) {
            doctorList.remove(old);
        }
        doctorList.add(d);
        saveAllDoctors();
    }

    public CustomeLinkedList<Doctor> getAllDoctors() {
        return doctorList;
    }
}
