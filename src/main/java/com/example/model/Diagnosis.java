package com.example.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a medical diagnosis made by a doctor for a patient
 */
public class Diagnosis {
    private int id;
    private int doctorId;
    private int patientId;
    private String patientComplaint;      // keluhan patient
    private String doctorDiagnosis;       // diagnosis doctor
    private String recommendedMedicine;   // medicine recommended from doctor
    private LocalDateTime diagnosisDate;
    private int appointmentId;            // Link to original appointment
    
    public Diagnosis() {
        // Default constructor for Jackson
    }
    
    public Diagnosis(int id, int doctorId, int patientId, String patientComplaint, 
                    String doctorDiagnosis, String recommendedMedicine, 
                    LocalDateTime diagnosisDate, int appointmentId) {
        this.id = id;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.patientComplaint = patientComplaint;
        this.doctorDiagnosis = doctorDiagnosis;
        this.recommendedMedicine = recommendedMedicine;
        this.diagnosisDate = diagnosisDate;
        this.appointmentId = appointmentId;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getPatientComplaint() {
        return patientComplaint;
    }

    public void setPatientComplaint(String patientComplaint) {
        this.patientComplaint = patientComplaint;
    }

    public String getDoctorDiagnosis() {
        return doctorDiagnosis;
    }

    public void setDoctorDiagnosis(String doctorDiagnosis) {
        this.doctorDiagnosis = doctorDiagnosis;
    }

    public String getRecommendedMedicine() {
        return recommendedMedicine;
    }

    public void setRecommendedMedicine(String recommendedMedicine) {
        this.recommendedMedicine = recommendedMedicine;
    }

    public LocalDateTime getDiagnosisDate() {
        return diagnosisDate;
    }

    public void setDiagnosisDate(LocalDateTime diagnosisDate) {
        this.diagnosisDate = diagnosisDate;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    @Override
    public String toString() {
        return String.format("Diagnosis{id=%d, doctorId=%d, patientId=%d, complaint='%s', diagnosis='%s', medicine='%s', date=%s}", 
                id, doctorId, patientId, patientComplaint, doctorDiagnosis, recommendedMedicine, diagnosisDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Diagnosis)) return false;
        Diagnosis diagnosis = (Diagnosis) o;
        return id == diagnosis.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
