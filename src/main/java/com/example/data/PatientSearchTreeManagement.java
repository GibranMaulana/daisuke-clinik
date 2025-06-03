package com.example.data;

import com.example.model.Patient;
import com.example.model.ds.CustomeBST;
import com.example.model.ds.CustomeLinkedList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * A singleton‐style manager that builds a CustomeBST<Patient> at startup,
 * and provides insert/search/in‐order methods.
 *
 * It also keeps the JSON‐backed linked list (PatientRecordManagement) in sync
 * whenever a new patient is added.  Searching and in‐order display only use the BST.
 */
public class PatientSearchTreeManagement {

    private final CustomeBST<Patient> patientTree;
    private final PatientRecordManagement recordMgr;

    // Singleton instance
    private static final PatientSearchTreeManagement INSTANCE = new PatientSearchTreeManagement();

    /** Private constructor: load all patients from PatientRecordManagement into the BST */
    private PatientSearchTreeManagement() {
        // 1) Get the singleton that already loaded everything into a CustomeLinkedList<Patient>
        recordMgr = PatientRecordManagement.getInstance();

        // 2) Build an empty BST
        patientTree = new CustomeBST<>();

        // 3) Insert every patient from the linked list into the BST
        CustomeLinkedList<Patient> allList = recordMgr.getAllPatients();
        for (Patient p : allList) {
            // Insert into BST (uses Patient.compareTo => compares by ID)
            patientTree.insert(p);
        }
    }

    /** Get the singleton instance */
    public static PatientSearchTreeManagement getInstance() {
        return INSTANCE;
    }

    /**
     * Insert a brand‐new patient into BOTH the JSON‐backed linked list AND the BST.
     * If you want to guard against duplicate IDs, you can first call searchPatient(p.getId())
     * and throw or return false if found.  For simplicity, this just inserts unconditionally.
     */
    public void insertPatient(Patient p) {
        // 1) Add to the JSON‐backed linked list (so it’s saved on disk)
        recordMgr.addPatient(p);       // this calls saveAll() internally

        // 2) Also insert into the BST for fast future searching
        patientTree.insert(p);
    }

    /**
     * Search for a patient by ID.  Returns the Patient object if found, or null otherwise.
     * Internally we construct a “dummy” Patient with only its ID set, since Patient.compareTo
     * compares by ID.  CustomeBST.search(...) will compare using compareTo.
     */
    public Patient searchPatient(String id) {
        // Create a “key” object that only has its ID set.
        Patient key = new Patient(id, null, 0, null, null);
        return patientTree.search(key);
    }

    /**
     * Traverse the BST in order (sorted by patient ID) and return a List<Patient> in ascending order.
     * If you simply want to print them instead, you could replace this with an inOrderTraversal that
     * prints to console.  Here we collect into a List and return.
     */
    public List<Patient> inOrderDisplay() {
        List<Patient> sorted = new ArrayList<>();
        // Assume CustomeBST has a method:
        //    void inOrderTraversal(Consumer<T> visitor)
        // which calls visitor.accept(node.value) for each node in sorted order.
        patientTree.inOrderTraversal(new Consumer<Patient>() {
            @Override
            public void accept(Patient p) {
                sorted.add(p);
            }
        });
        return sorted;
    }
}
