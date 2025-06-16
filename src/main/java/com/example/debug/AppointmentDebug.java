package com.example.debug;

import com.example.data.AppointmentDAO;
import com.example.model.Appointment;
import com.example.model.ds.CustomeLinkedList;
import java.time.LocalDateTime;

public class AppointmentDebug {
    public static void main(String[] args) {
        AppointmentDAO dao = new AppointmentDAO();
        
        System.out.println("=== Appointment Debug for Doctor 2147 ===");
        System.out.println("Current time: " + LocalDateTime.now());
        System.out.println();
        
        // Get all appointments
        CustomeLinkedList<Appointment> allAppointments = dao.getAllAppointments();
        System.out.println("Total appointments in system: " + allAppointments.size());
        
        // Filter for doctor 2147
        System.out.println("\nAll appointments for Doctor 2147:");
        for (Appointment a : allAppointments) {
            if (a.getDoctorId() == 2147) {
                System.out.println("  " + a.getAppointmentId() + " | " + a.getTime() + " | " + a.getStatus() + 
                                  " | Valid time: " + dao.isValidAppointmentTime(a.getTime()) +
                                  " | Future: " + a.getTime().isAfter(LocalDateTime.now()));
            }
        }
        
        // Get filtered queue
        CustomeLinkedList<Appointment> queue = dao.getQueueForDoctor(2147);
        System.out.println("\nFiltered queue for Doctor 2147: " + queue.size() + " appointments");
        for (Appointment a : queue) {
            System.out.println("  " + a.getAppointmentId() + " | " + a.getTime() + " | " + a.getPatientIllness());
        }
    }
}
