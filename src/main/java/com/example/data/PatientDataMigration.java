package com.example.data;

import com.example.model.Patient;
import com.example.model.Diagnosis;
import com.example.model.ds.CustomeLinkedList;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to migrate patient data from old format (String[] illnessHistory)
 * to new format (Diagnosis[] illnessHistory).
 */
public class PatientDataMigration {
    private static final String PATIENTS_FILE = "patients.json";
    private final ObjectMapper objectMapper;
    
    public PatientDataMigration() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
    
    /**
     * Migrates patient data from old format to new format if needed.
     * This should be called once during application startup.
     */
    public void migratePatientDataIfNeeded() {
        try {
            File file = new File(PATIENTS_FILE);
            if (!file.exists()) {
                System.out.println("No patient data file found. Skipping migration.");
                return;
            }
            
            // Read the JSON as a tree to check format
            JsonNode root = objectMapper.readTree(file);
            
            if (!root.isArray()) {
                System.out.println("Invalid patient data format. Skipping migration.");
                return;
            }
            
            boolean needsMigration = false;
            List<Patient> migratedPatients = new ArrayList<>();
            
            for (JsonNode patientNode : root) {
                JsonNode illnessHistoryNode = patientNode.get("illnessHistory");
                
                if (illnessHistoryNode != null && illnessHistoryNode.isArray() && 
                    illnessHistoryNode.size() > 0 && illnessHistoryNode.get(0).isTextual()) {
                    // Old format detected - array of strings
                    needsMigration = true;
                    Patient migratedPatient = migratePatient(patientNode);
                    migratedPatients.add(migratedPatient);
                } else {
                    // New format or empty history - parse normally
                    Patient patient = objectMapper.treeToValue(patientNode, Patient.class);
                    migratedPatients.add(patient);
                }
            }
            
            if (needsMigration) {
                System.out.println("Migrating patient data from old format to new format...");
                
                // Create backup
                File backup = new File(PATIENTS_FILE + ".backup." + System.currentTimeMillis());
                file.renameTo(backup);
                System.out.println("Backup created: " + backup.getName());
                
                // Save migrated data
                objectMapper.writerWithDefaultPrettyPrinter()
                          .writeValue(new File(PATIENTS_FILE), migratedPatients);
                System.out.println("Patient data migration completed successfully.");
            } else {
                System.out.println("Patient data is already in new format. No migration needed.");
            }
            
        } catch (IOException e) {
            System.err.println("Error during patient data migration: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Migrates a single patient from old format to new format.
     */
    private Patient migratePatient(JsonNode patientNode) throws IOException {
        // Parse basic patient fields
        Patient patient = new Patient();
        patient.setId(patientNode.get("id").asInt());
        patient.setUsername(patientNode.get("username").asText());
        patient.setPassword(patientNode.get("password").asText());
        patient.setFullname(patientNode.get("fullname").asText());
        patient.setEmail(patientNode.get("email").asText());
        patient.setAge(patientNode.get("age").asInt());
        patient.setAddress(patientNode.get("address").asText());
        patient.setPhoneNumber(patientNode.get("phoneNumber").asText());
        
        // Migrate illness history from strings to diagnosis objects
        JsonNode illnessHistoryNode = patientNode.get("illnessHistory");
        if (illnessHistoryNode != null && illnessHistoryNode.isArray()) {
            CustomeLinkedList<Diagnosis> newIllnessHistory = new CustomeLinkedList<>();
            
            for (JsonNode illnessNode : illnessHistoryNode) {
                if (illnessNode.isTextual()) {
                    String oldIllness = illnessNode.asText();
                    
                    // Create a placeholder diagnosis for the old illness string
                    Diagnosis placeholderDiagnosis = new Diagnosis(
                        0, // Will be set by DiagnosisDAO when saved
                        0, // Unknown doctor - placeholder
                        patient.getId(),
                        oldIllness, // Use old illness as patient complaint
                        "Migrated from old system", // Generic diagnosis
                        "N/A", // No medicine info available
                        LocalDateTime.now(), // Migration timestamp
                        0 // No associated appointment
                    );
                    
                    newIllnessHistory.add(placeholderDiagnosis);
                }
            }
            
            patient.setIllnessHistory(newIllnessHistory);
        }
        
        return patient;
    }
}
