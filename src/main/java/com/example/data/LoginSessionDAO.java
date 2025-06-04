package com.example.data;

import com.example.model.Doctor;
import com.example.model.ds.CustomeLinkedList;

/**
 * An in‐memory list of currently logged‐in doctors.
 * We will not persist this to disk (it resets on app restart).
 */
public class LoginSessionDAO {
    private final CustomeLinkedList<Doctor> loggedIn;

    public LoginSessionDAO() {
        loggedIn = new CustomeLinkedList<>();
    }

    public void loginDoctor(Doctor doc) {
        loggedIn.add(doc);
    }

    public void logoutDoctor(int doctorId) {
        Doctor toRemove = null;
        for (Doctor d : loggedIn) {
            if (d.getId() == doctorId) {
                toRemove = d;
                break;
            }
        }
        if (toRemove != null) {
            loggedIn.remove(toRemove);
        }
    }

    public CustomeLinkedList<Doctor> getAllLoggedIn() {
        return loggedIn;
    }
}
