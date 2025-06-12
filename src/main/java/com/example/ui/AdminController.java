package com.example.ui;

import com.example.data.AdminService;
import com.example.data.PendingDoctorRegistrationDAO;
import com.example.data.DoctorDAO;
import com.example.data.PatientDAO;
import com.example.model.Admin;
import com.example.model.Doctor;
import com.example.model.Patient;
import com.example.model.Appointment;
import com.example.model.PendingDoctorRegistration;
import com.example.model.ds.CustomeLinkedList;
import com.example.model.ds.CustomeBST;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller for Admin dashboard and operations
 */
public class AdminController {
    
    // Authentication fields
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label statusLabel;
    
    // Admin dashboard components
    @FXML private TabPane adminTabPane;
    @FXML private Label welcomeLabel;
    
    // System overview tab
    @FXML private Label totalDoctorsLabel;
    @FXML private Label totalPatientsLabel;
    @FXML private Label totalAppointmentsLabel;
    @FXML private Label loggedInDoctorsLabel;
    
    // Doctor management tables
    @FXML private TableView<Doctor> allDoctorsTable;
    @FXML private TableColumn<Doctor, String> doctorIdColumn;
    @FXML private TableColumn<Doctor, String> doctorUsernameColumn;
    @FXML private TableColumn<Doctor, String> doctorNameColumn;
    @FXML private TableColumn<Doctor, String> doctorEmailColumn;
    @FXML private TableColumn<Doctor, String> doctorSpecialtyColumn;
    @FXML private TableColumn<Doctor, String> doctorStatusColumn;
    
    @FXML private TableView<Doctor> loggedInDoctorsTable;
    @FXML private TableColumn<Doctor, String> activeDoctorIdColumn;
    @FXML private TableColumn<Doctor, String> activeDoctorUsernameColumn;
    @FXML private TableColumn<Doctor, String> activeDoctorNameColumn;
    @FXML private TableColumn<Doctor, String> activeDoctorEmailColumn;
    @FXML private TableColumn<Doctor, String> activeDoctorSpecialtyColumn;
    @FXML private TableColumn<Doctor, String> activeDoctorLoginTimeColumn;
    
    // Patient management table
    @FXML private TableView<Patient> allPatientsTable;
    @FXML private TableColumn<Patient, String> patientIdColumn;
    @FXML private TableColumn<Patient, String> patientUsernameColumn;
    @FXML private TableColumn<Patient, String> patientNameColumn;
    @FXML private TableColumn<Patient, String> patientEmailColumn;
    @FXML private TableColumn<Patient, Integer> patientAgeColumn;
    @FXML private TableColumn<Patient, String> patientAddressColumn;
    @FXML private TableColumn<Patient, String> patientPhoneColumn;
    @FXML private TableColumn<Patient, String> patientIllnessColumn;

    // Doctor search and removal fields
    @FXML private TextField doctorIdField;
    @FXML private Button removeDoctorButton;
    
    // Appointment management table
    @FXML private TableView<Appointment> allAppointmentsTable;
    @FXML private TableColumn<Appointment, Integer> appointmentIdColumn;
    @FXML private TableColumn<Appointment, Integer> appointmentPatientIdColumn;
    @FXML private TableColumn<Appointment, String> appointmentDoctorIdColumn;
    @FXML private TableColumn<Appointment, String> appointmentTimeColumn;
    @FXML private TableColumn<Appointment, String> appointmentIllnessColumn;
    @FXML private TableColumn<Appointment, String> appointmentStatusColumn;
    
    // Pending registrations table
    @FXML private TableView<PendingDoctorRegistration> pendingRegistrationsTable;
    @FXML private TableColumn<PendingDoctorRegistration, Integer> regRequestIdColumn;
    @FXML private TableColumn<PendingDoctorRegistration, String> regDoctorNameColumn;
    @FXML private TableColumn<PendingDoctorRegistration, String> regSpecialtyColumn;
    @FXML private TableColumn<PendingDoctorRegistration, String> regRequestTimeColumn;
    @FXML private TableColumn<PendingDoctorRegistration, String> regStatusColumn;
    
    // Keep legacy TextArea for backward compatibility (can be removed if not used)
    @FXML private TextArea pendingRegistrationsArea;
    @FXML private TextField patientIdField;
    @FXML private TextField patientNameField;
    @FXML private Label patientSearchStatus;

    // Navigation bar buttons
    @FXML private Button navDashboard;
    @FXML private Button navDoctors;
    @FXML private Button navPatients;
    @FXML private Button navAppointments;
    @FXML private Button navRegistrations;
    
    // Action buttons
    @FXML private Button refreshStatsButton;
    @FXML private Button viewAllDoctorsButton;
    @FXML private Button viewLoggedInDoctorsButton;
    @FXML private Button viewDoctorLoginHistoryButton;
    @FXML private Button clearSearchButton;
    @FXML private Button removePatientButton;
    @FXML private Button viewAllAppointmentsButton;
    @FXML private Button acceptButton;
    @FXML private Button declineButton;
    @FXML private Button logoutButton;
    
    // View containers
    @FXML private VBox dashboardView;
    @FXML private VBox doctorsView;
    @FXML private VBox patientsView;
    @FXML private VBox appointmentsView;
    @FXML private VBox registrationsView;
    
    // Services
    private AdminService adminService;
    private PendingDoctorRegistrationDAO pendingDAO;
    private DoctorDAO doctorDAO;
    private Admin currentAdmin;
    
    public AdminController() {
        this.adminService = new AdminService();
        this.pendingDAO = new PendingDoctorRegistrationDAO();
        this.doctorDAO = new DoctorDAO();
    }
    
    /**
     * Called from AdminLoginController when an admin has successfully logged in.
     * Sets the current admin and updates the welcome label.
     */
    public void setCurrentAdmin(Admin admin) {
        this.currentAdmin = admin;
        if (welcomeLabel != null) {
            // Show admin level in welcome message
            String adminLevelText = admin.isSuperAdmin() ? " (Super Admin)" : " (Regular Admin)";
            welcomeLabel.setText("Welcome, " + admin.getUsername() + adminLevelText);
        }
        showAdminDashboard();
        loadDashboardData(); // Load all dashboard data when admin logs in
        
        // Apply admin level restrictions after UI is loaded
        applyAdminLevelRestrictions();
    }
    
    /**
     * Get the current logged-in admin
     */
    public Admin getCurrentAdmin() {
        return this.currentAdmin;
    }
    
    @FXML
    private void initialize() {
        setupTableColumns();
        setupEventHandlers();
        hideAdminDashboard();
        // Initialize navbar state - dashboard is active by default
        updateNavbarState("dashboard");
    }
    
    private void setupTableColumns() {
        // Setup Doctor tables
        if (doctorIdColumn != null) {
            doctorIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            doctorUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
            doctorNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            doctorEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
            doctorSpecialtyColumn.setCellValueFactory(new PropertyValueFactory<>("specialty"));
            doctorStatusColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                    cellData.getValue().getCurrentLoginTime() != null ? "Online" : "Offline"));
        }
        
        if (activeDoctorIdColumn != null) {
            activeDoctorIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            activeDoctorUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
            activeDoctorNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            activeDoctorEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
            activeDoctorSpecialtyColumn.setCellValueFactory(new PropertyValueFactory<>("specialty"));
            activeDoctorLoginTimeColumn.setCellValueFactory(cellData -> {
                if (cellData.getValue().getCurrentLoginTime() != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    return new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getCurrentLoginTime().format(formatter));
                }
                return new javafx.beans.property.SimpleStringProperty("N/A");
            });
        }
        
        // Setup Patient table
        if (patientIdColumn != null) {
            patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            patientUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
            patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            patientEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
            patientAgeColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
            patientAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
            patientPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
            patientIllnessColumn.setCellValueFactory(cellData -> {
                // Get the most recent illness from history or return "None"
                if (cellData.getValue().getIllnessHistory().size() > 0) {
                    String lastIllness = null;
                    for (String illness : cellData.getValue().getIllnessHistory()) {
                        lastIllness = illness; // Gets the last one added
                    }
                    return new javafx.beans.property.SimpleStringProperty(lastIllness != null ? lastIllness : "None");
                }
                return new javafx.beans.property.SimpleStringProperty("None");
            });
        }
        
        // Setup Appointment table
        if (appointmentIdColumn != null) {
            appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
            appointmentPatientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patientId"));
            appointmentDoctorIdColumn.setCellValueFactory(new PropertyValueFactory<>("doctorId"));
            appointmentTimeColumn.setCellValueFactory(cellData -> {
                if (cellData.getValue().getTime() != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    return new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getTime().format(formatter));
                }
                return new javafx.beans.property.SimpleStringProperty("N/A");
            });
            appointmentIllnessColumn.setCellValueFactory(new PropertyValueFactory<>("patientIllness"));
            appointmentStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        }
        
        // Setup Pending Registrations table
        if (regRequestIdColumn != null) {
            regRequestIdColumn.setCellValueFactory(new PropertyValueFactory<>("requestId"));
            regDoctorNameColumn.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
            regSpecialtyColumn.setCellValueFactory(new PropertyValueFactory<>("specialty"));
            regRequestTimeColumn.setCellValueFactory(cellData -> {
                if (cellData.getValue().getRequestTime() != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    return new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getRequestTime().format(formatter));
                }
                return new javafx.beans.property.SimpleStringProperty("N/A");
            });
            regStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        }
    }
    
    private void setupEventHandlers() {
        // Event handlers are now set up automatically via FXML onAction attributes
        // No manual button handler setup needed
        
        // Setup live patient search functionality
        setupPatientSearchListeners();
        
        // Setup enhanced remove patient button behavior
        setupRemovePatientButton();
    }
    
    /**
     * Sets up live search functionality for patient search fields
     */
    private void setupPatientSearchListeners() {
        if (patientIdField != null && patientNameField != null) {
            // Add listeners for live search as user types
            patientIdField.textProperty().addListener((observable, oldValue, newValue) -> {
                applyPatientFilters();
            });
            
            patientNameField.textProperty().addListener((observable, oldValue, newValue) -> {
                applyPatientFilters();
            });
        }
    }
    
    /**
     * Sets up enhanced behavior for the remove patient button
     */
    private void setupRemovePatientButton() {
        if (removePatientButton != null && allPatientsTable != null) {
            // Initially disable the button if no patient is selected
            removePatientButton.setDisable(true);
            
            // Enable/disable button based on table selection
            allPatientsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
                boolean hasSelection = newSelection != null;
                removePatientButton.setDisable(!hasSelection);
                
                // Update button text based on selection
                if (hasSelection) {
                    removePatientButton.setText("Remove " + newSelection.getName());
                } else {
                    removePatientButton.setText("Remove Selected");
                }
            });
            
            // Allow multiple selection for potential future bulk operations
            allPatientsTable.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.SINGLE);
        }
    }
    
    private void showAdminDashboard() {
        if (adminTabPane != null) {
            adminTabPane.setVisible(true);
        }
        if (welcomeLabel != null && currentAdmin != null) {
            welcomeLabel.setText("Welcome, " + currentAdmin.getUsername());
        }
    }
    
    private void hideAdminDashboard() {
        if (adminTabPane != null) {
            adminTabPane.setVisible(false);
        }
    }
    
    private void loadDashboardData() {
        // Use background thread to prevent UI blocking
        Thread loadingThread = new Thread(() -> {
            try {
                // Show loading indicator
                Platform.runLater(() -> {
                    showStatus("Loading dashboard data...", true);
                });
                
                // Load all data optimized
                loadDashboardDataOptimized();
                
                // Update UI on JavaFX Application Thread
                Platform.runLater(() -> {
                    showStatus("Dashboard data loaded successfully", true);
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    showStatus("Error loading dashboard data: " + e.getMessage(), false);
                });
            }
        });
        loadingThread.setDaemon(true);
        loadingThread.start();
    }
    
    private void loadDashboardDataOptimized() {
        // Load data using individual methods
        AdminService.SystemStats systemStats = adminService.getSystemStats();
        CustomeLinkedList<Doctor> allDoctors = adminService.getAllDoctors();
        CustomeLinkedList<Doctor> loggedInDoctors = adminService.getCurrentlyLoggedInDoctors();
        CustomeLinkedList<Patient> allPatients = adminService.getAllPatients();
        CustomeLinkedList<Appointment> allAppointments = adminService.getAllAppointments();
        
        // Update UI with all data at once
        Platform.runLater(() -> {
            updateSystemStatsUI(systemStats);
            updateDoctorsTableUI(allDoctors);
            updateLoggedInDoctorsTableUI(loggedInDoctors);
            updatePatientsTableUI(allPatients);
            updateAppointmentsTableUI(allAppointments);
        });
        
        // Load pending registrations separately as it depends on current admin
        Platform.runLater(() -> {
            loadPendingRegistrations();
        });
    }
    
    private void updateSystemStatsUI(AdminService.SystemStats stats) {
        if (totalDoctorsLabel != null) {
            totalDoctorsLabel.setText(String.valueOf(stats.getTotalDoctors()));
        }
        if (totalPatientsLabel != null) {
            totalPatientsLabel.setText(String.valueOf(stats.getTotalPatients()));
        }
        if (totalAppointmentsLabel != null) {
            totalAppointmentsLabel.setText(String.valueOf(stats.getTotalAppointments()));
        }
        if (loggedInDoctorsLabel != null) {
            loggedInDoctorsLabel.setText(String.valueOf(stats.getLoggedInDoctors()));
        }
    }
    
    private void updateDoctorsTableUI(CustomeLinkedList<Doctor> doctors) {
        if (allDoctorsTable != null) {
            ObservableList<Doctor> doctorsList = FXCollections.observableArrayList();
            for (Doctor doctor : doctors) {
                doctorsList.add(doctor);
            }
            allDoctorsTable.setItems(doctorsList);
        }
    }
    
    private void updateLoggedInDoctorsTableUI(CustomeLinkedList<Doctor> loggedInDoctors) {
        if (loggedInDoctorsTable != null) {
            ObservableList<Doctor> doctorsList = FXCollections.observableArrayList();
            for (Doctor doctor : loggedInDoctors) {
                doctorsList.add(doctor);
            }
            loggedInDoctorsTable.setItems(doctorsList);
        }
    }
    
    private void updatePatientsTableUI(CustomeLinkedList<Patient> patients) {
        if (allPatientsTable != null) {
            ObservableList<Patient> patientsList = FXCollections.observableArrayList();
            for (Patient patient : patients) {
                patientsList.add(patient);
            }
            allPatientsTable.setItems(patientsList);
            
            // Update search status to show total count
            if (patientSearchStatus != null) {
                patientSearchStatus.setText("Showing all patients (" + patientsList.size() + ")");
            }
        }
    }
    
    private void updateAppointmentsTableUI(CustomeLinkedList<Appointment> appointments) {
        if (allAppointmentsTable != null) {
            ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();
            for (Appointment appointment : appointments) {
                appointmentsList.add(appointment);
            }
            allAppointmentsTable.setItems(appointmentsList);
        }
    }
    
    private void loadSystemStats() {
        try {
            AdminService.SystemStats stats = adminService.getSystemStats();
            
            if (totalDoctorsLabel != null) {
                totalDoctorsLabel.setText(String.valueOf(stats.getTotalDoctors()));
            }
            if (totalPatientsLabel != null) {
                totalPatientsLabel.setText(String.valueOf(stats.getTotalPatients()));
            }
            if (totalAppointmentsLabel != null) {
                totalAppointmentsLabel.setText(String.valueOf(stats.getTotalAppointments()));
            }
            if (loggedInDoctorsLabel != null) {
                loggedInDoctorsLabel.setText(String.valueOf(stats.getLoggedInDoctors()));
            }
        } catch (Exception e) {
            showStatus("Error loading system stats: " + e.getMessage(), false);
        }
    }
    
    private void loadAllDoctors() {
        if (allDoctorsTable == null) return;
        
        try {
            CustomeLinkedList<Doctor> doctors = adminService.getAllDoctors();
            ObservableList<Doctor> doctorsList = FXCollections.observableArrayList();
            
            for (Doctor doctor : doctors) {
                doctorsList.add(doctor);
            }
            
            allDoctorsTable.setItems(doctorsList);
        } catch (Exception e) {
            showStatus("Error loading doctors: " + e.getMessage(), false);
        }
    }
    
    private void loadLoggedInDoctors() {
        if (loggedInDoctorsTable == null) return;
        
        try {
            CustomeLinkedList<Doctor> loggedInDoctors = adminService.getCurrentlyLoggedInDoctors();
            ObservableList<Doctor> doctorsList = FXCollections.observableArrayList();
            
            for (Doctor doctor : loggedInDoctors) {
                doctorsList.add(doctor);
            }
            
            loggedInDoctorsTable.setItems(doctorsList);
        } catch (Exception e) {
            showStatus("Error loading logged in doctors: " + e.getMessage(), false);
        }
    }
    
    private void loadAllPatients() {
        if (allPatientsTable == null) return;
        
        try {
            CustomeLinkedList<Patient> patients = adminService.getAllPatients();
            ObservableList<Patient> patientsList = FXCollections.observableArrayList();
            
            for (Patient patient : patients) {
                patientsList.add(patient);
            }
            
            allPatientsTable.setItems(patientsList);
        } catch (Exception e) {
            showStatus("Error loading patients: " + e.getMessage(), false);
        }
    }
    
    private void loadAllAppointments() {
        if (allAppointmentsTable == null) return;
        
        try {
            CustomeLinkedList<Appointment> appointments = adminService.getAllAppointments();
            ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();
            
            for (Appointment appointment : appointments) {
                appointmentsList.add(appointment);
            }
            
            allAppointmentsTable.setItems(appointmentsList);
        } catch (Exception e) {
            showStatus("Error loading appointments: " + e.getMessage(), false);
        }
    }
    
    private void loadPendingRegistrations() {
        if (pendingRegistrationsTable == null) return;
        
        try {
            if (currentAdmin == null) {
                pendingRegistrationsTable.setItems(FXCollections.observableArrayList());
                showStatus("Error: No admin logged in", false);
                return;
            }
            
            // Use the new PendingDoctorRegistrationDAO instead of AdminService
            List<PendingDoctorRegistration> allPending = pendingDAO.getAllPendingRegistrations();
            ObservableList<PendingDoctorRegistration> tableData = FXCollections.observableArrayList();
            
            // Filter to show only pending registrations
            for (PendingDoctorRegistration reg : allPending) {
                if (reg.isPending()) {
                    tableData.add(reg);
                }
            }
            
            pendingRegistrationsTable.setItems(tableData);
            
            // Update legacy TextArea if it exists (for backward compatibility)
            if (pendingRegistrationsArea != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("=== PENDING DOCTOR REGISTRATIONS ===\n\n");
                
                if (tableData.isEmpty()) {
                    sb.append("No pending registrations.\n");
                } else {
                    sb.append("Total pending: ").append(tableData.size()).append("\n\n");
                    
                    for (PendingDoctorRegistration reg : tableData) {
                        sb.append("Request ID: ").append(reg.getRequestId()).append("\n");
                        sb.append("Name: ").append(reg.getDoctorName()).append("\n");
                        sb.append("Specialty: ").append(reg.getSpecialty()).append("\n");
                        sb.append("Request Time: ").append(reg.getRequestTime()).append("\n");
                        sb.append("Status: ").append(reg.getStatus()).append("\n");
                        sb.append("─────────────────────────────────────\n");
                    }
                }
                
                pendingRegistrationsArea.setText(sb.toString());
            }
            
        } catch (Exception e) {
            showStatus("Error loading pending registrations: " + e.getMessage(), false);
        }
    }
    
    private void showStatus(String message, boolean isSuccess) {
        if (statusLabel == null) return;
        
        statusLabel.setText(message);
        statusLabel.setStyle(isSuccess ? 
            "-fx-text-fill: green; -fx-font-weight: bold;" : 
            "-fx-text-fill: red; -fx-font-weight: bold;");
    }
    
    // ═══════════════════════════════════════════════════════════════════════════════════
    // FXML Event Handler Methods (Referenced in admin_dashboard.fxml)
    // ═══════════════════════════════════════════════════════════════════════════════════
    
    @FXML
    private void refreshStatistics() {
        if (currentAdmin != null) {
            loadSystemStats();
            showStatus("Statistics refreshed", true);
        }
    }
    
    @FXML
    private void viewPendingRegistrations() {
        loadPendingRegistrations();
    }
    
    @FXML
    private void acceptDoctorRegistration() {
        if (!currentAdmin.isSuperAdmin()) {
            showStatus("Access denied: Only super admins can accept registrations.", false);
            return;
        }
        
        PendingDoctorRegistration selected = pendingRegistrationsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showStatus("Please select a pending registration to accept.", false);
            return;
        }
        
        try {
            // Convert pending registration to doctor and add to doctors.json
            Doctor newDoctor = selected.toDoctor();
            doctorDAO.registerDoctor(newDoctor);
            
            // Remove from pending queue
            pendingDAO.removePendingRegistration(selected.getDoctorId());
            
            // Mark as approved (optional, since we're removing it)
            selected.approve();
            
            // Refresh the tables
            loadPendingRegistrations();
            loadAllDoctors();
            refreshStatistics();
            
            showStatus("Doctor registration accepted successfully! Doctor ID: " + newDoctor.getId(), true);
            
        } catch (Exception e) {
            showStatus("Error accepting registration: " + e.getMessage(), false);
        }
    }
    
    @FXML
    private void declineDoctorRegistration() {
        if (!currentAdmin.isSuperAdmin()) {
            showStatus("Access denied: Only super admins can decline registrations.", false);
            return;
        }
        
        PendingDoctorRegistration selected = pendingRegistrationsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showStatus("Please select a pending registration to decline.", false);
            return;
        }
        
        try {
            // Mark as rejected and remove from pending queue
            selected.reject();
            pendingDAO.removePendingRegistration(selected.getDoctorId());
            
            // Refresh the table
            loadPendingRegistrations();
            
            showStatus("Doctor registration declined.", true);
            
        } catch (Exception e) {
            showStatus("Error declining registration: " + e.getMessage(), false);
        }
    }
    
    @FXML
    private void approvePendingRegistration() {
        if (!currentAdmin.isSuperAdmin()) {
            showStatus("Access denied: Only super admins can approve registrations.", false);
            return;
        }
        
        PendingDoctorRegistration selected = pendingRegistrationsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showStatus("Please select a pending registration to approve.", false);
            return;
        }
        
        try {
            // Convert pending registration to doctor and add to doctors.json
            Doctor newDoctor = selected.toDoctor();
            doctorDAO.registerDoctor(newDoctor);
            
            // Remove from pending queue
            pendingDAO.removePendingRegistration(selected.getDoctorId());
            
            // Mark as approved (optional, since we're removing it)
            selected.approve();
            
            // Refresh the table
            loadPendingRegistrations();
            
            showStatus("Doctor registration approved successfully! Doctor ID: " + newDoctor.getId(), true);
            
        } catch (Exception e) {
            showStatus("Error approving registration: " + e.getMessage(), false);
        }
    }
    
    @FXML
    private void rejectPendingRegistration() {
        if (!currentAdmin.isSuperAdmin()) {
            showStatus("Access denied: Only super admins can reject registrations.", false);
            return;
        }
        
        PendingDoctorRegistration selected = pendingRegistrationsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showStatus("Please select a pending registration to reject.", false);
            return;
        }
        
        try {
            // Mark as rejected and remove from pending queue
            selected.reject();
            pendingDAO.removePendingRegistration(selected.getDoctorId());
            
            // Refresh the table
            loadPendingRegistrations();
            
            showStatus("Doctor registration rejected.", true);
            
        } catch (Exception e) {
            showStatus("Error rejecting registration: " + e.getMessage(), false);
        }
    }
    
    @FXML
    private void viewAllDoctors() {
        loadAllDoctors();
    }
    
    @FXML
    private void viewLoggedInDoctors() {
        loadLoggedInDoctors();
    }
    
    @FXML
    private void viewDoctorLoginHistory() {
        // Display login history information in status
        try {
            CustomeLinkedList<Doctor> doctors = adminService.getAllDoctors();
            int totalLogins = 0;
            for (Doctor doctor : doctors) {
                totalLogins += doctor.getLoginHistory().size();
            }
            showStatus("Total login sessions recorded: " + totalLogins, true);
        } catch (Exception e) {
            showStatus("Error loading doctor login history: " + e.getMessage(), false);
        }
    }
    
    @FXML
    private void searchPatient() {
        String patientIdStr = patientIdField.getText().trim();
        if (patientIdStr.isEmpty()) {
            showStatus("Please enter a patient ID", false);
            return;
        }
        
        try {
            int patientId = Integer.parseInt(patientIdStr);
            Patient patient = adminService.searchPatientById(patientId);
            
            if (patient != null) {
                // Filter the patient table to show only this patient
                ObservableList<Patient> filteredList = FXCollections.observableArrayList();
                filteredList.add(patient);
                allPatientsTable.setItems(filteredList);
                showStatus("Patient found: " + patient.getName(), true);
            } else {
                // Clear table and show message
                allPatientsTable.setItems(FXCollections.observableArrayList());
                showStatus("Patient not found with ID: " + patientId, false);
            }
        } catch (NumberFormatException e) {
            showStatus("Invalid patient ID format", false);
        } catch (Exception e) {
            showStatus("Search error: " + e.getMessage(), false);
        }
    }
    
    @FXML
    private void viewAllPatients() {
        loadAllPatients();
    }
    
    @FXML
    private void removePatient() {
        if (!currentAdmin.isSuperAdmin()) {
            showStatus("Access denied: Only super admins can remove patients.", false);
            return;
        }
        
        // Try to get selected patient from table first
        Patient selectedPatient = allPatientsTable.getSelectionModel().getSelectedItem();
        
        if (selectedPatient != null) {
            // Remove selected patient from table
            int patientId = selectedPatient.getId();
            
            // Show enhanced confirmation dialog with patient details
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Patient Removal");
            alert.setHeaderText("Remove Patient: " + selectedPatient.getName());
            alert.setContentText(String.format(
                "Are you sure you want to permanently remove this patient?\n\n" +
                "Patient Details:\n" +
                "• ID: %d\n" +
                "• Name: %s\n" +
                "• Age: %d\n" +
                "• Phone: %s\n\n" +
                "This action cannot be undone!",
                patientId, 
                selectedPatient.getName(), 
                selectedPatient.getAge(),
                selectedPatient.getPhoneNumber()
            ));
            
            if (alert.showAndWait().get() == ButtonType.OK) {
                try {
                    boolean removed = adminService.removePatientById(patientId);
                    if (removed) {
                        showStatus("✓ Patient removed successfully: " + selectedPatient.getName(), true);
                        applyPatientFilters(); // Refresh the filtered view
                        refreshStatistics(); // Update patient count
                        
                        // Clear selection after removal
                        allPatientsTable.getSelectionModel().clearSelection();
                    } else {
                        showStatus("✗ Patient could not be removed", false);
                    }
                } catch (Exception e) {
                    showStatus("✗ Removal error: " + e.getMessage(), false);
                }
            }
            return;
        }
        
        // Fallback to manual ID entry if no selection
        String patientIdStr = patientIdField.getText().trim();
        if (patientIdStr.isEmpty()) {
            showStatus("Please select a patient from the table or enter a patient ID to remove", false);
            return;
        }
        
        try {
            int patientId = Integer.parseInt(patientIdStr);
            
            // First try to find the patient to get details for confirmation
            Patient patientToRemove = adminService.searchPatientById(patientId);
            
            // Show enhanced confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Patient Removal");
            
            if (patientToRemove != null) {
                alert.setHeaderText("Remove Patient: " + patientToRemove.getName());
                alert.setContentText(String.format(
                    "Are you sure you want to permanently remove this patient?\n\n" +
                    "Patient Details:\n" +
                    "• ID: %d\n" +
                    "• Name: %s\n" +
                    "• Age: %d\n" +
                    "• Phone: %s\n\n" +
                    "This action cannot be undone!",
                    patientId, 
                    patientToRemove.getName(), 
                    patientToRemove.getAge(),
                    patientToRemove.getPhoneNumber()
                ));
            } else {
                alert.setHeaderText("Remove Patient ID: " + patientId);
                alert.setContentText("Are you sure you want to remove patient with ID: " + patientId + "?\n\n" +
                                   "Warning: Patient details could not be retrieved.\n" +
                                   "This action cannot be undone!");
            }
            
            if (alert.showAndWait().get() == ButtonType.OK) {
                boolean removed = adminService.removePatientById(patientId);
                if (removed) {
                    String patientName = patientToRemove != null ? patientToRemove.getName() : "ID: " + patientId;
                    showStatus("✓ Patient removed successfully: " + patientName, true);
                    patientIdField.clear();
                    applyPatientFilters(); // Refresh the filtered view
                    refreshStatistics(); // Update patient count
                } else {
                    showStatus("✗ Patient not found or could not be removed", false);
                }
            }
        } catch (NumberFormatException e) {
            showStatus("Invalid patient ID format", false);
        } catch (Exception e) {
            showStatus("Removal error: " + e.getMessage(), false);
        }
    }

    @FXML
    private void removeDoctor() {
        if (!currentAdmin.isSuperAdmin()) {
            showStatus("Access denied: Only super admins can remove doctors.", false);
            return;
        }
        
        // Try to get selected doctor from table first
        Doctor selectedDoctor = allDoctorsTable.getSelectionModel().getSelectedItem();
        
        if (selectedDoctor != null) {
            // Remove selected doctor from table
            int doctorId = selectedDoctor.getId();
            
            // Show enhanced confirmation dialog with doctor details
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Doctor Removal");
            alert.setHeaderText("Remove Doctor: " + selectedDoctor.getName());
            alert.setContentText(String.format(
                "Are you sure you want to permanently remove this doctor?\n\n" +
                "Doctor Details:\n" +
                "• ID: %d\n" +
                "• Name: %s\n" +
                "• Specialty: %s\n\n" +
                "This action cannot be undone!",
                doctorId, 
                selectedDoctor.getName(), 
                selectedDoctor.getSpecialty()
            ));
            
            if (alert.showAndWait().get() == ButtonType.OK) {
                try {
                    boolean removed = adminService.removeDoctorById(doctorId);
                    if (removed) {
                        showStatus("✓ Doctor removed successfully: " + selectedDoctor.getName(), true);
                        loadAllDoctors(); // Refresh the doctor table
                        refreshStatistics(); // Update doctor count
                        
                        // Clear selection after removal
                        allDoctorsTable.getSelectionModel().clearSelection();
                    } else {
                        showStatus("✗ Doctor could not be removed", false);
                    }
                } catch (Exception e) {
                    showStatus("✗ Removal error: " + e.getMessage(), false);
                }
            }
            return;
        }
        
        // Fallback to manual ID entry if no selection
        String doctorIdStr = doctorIdField.getText().trim();
        if (doctorIdStr.isEmpty()) {
            showStatus("Please select a doctor from the table or enter a doctor ID to remove", false);
            return;
        }
        
        try {
            int doctorId = Integer.parseInt(doctorIdStr);
            
            // First try to find the doctor to get details for confirmation
            Doctor doctorToRemove = adminService.searchDoctorById(doctorId);
            
            // Show enhanced confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Doctor Removal");
            
            if (doctorToRemove != null) {
                alert.setHeaderText("Remove Doctor: " + doctorToRemove.getName());
                alert.setContentText(String.format(
                    "Are you sure you want to permanently remove this doctor?\n\n" +
                    "Doctor Details:\n" +
                    "• ID: %d\n" +
                    "• Name: %s\n" +
                    "• Specialty: %s\n\n" +
                    "This action cannot be undone!",
                    doctorId, 
                    doctorToRemove.getName(), 
                    doctorToRemove.getSpecialty()
                ));
            } else {
                alert.setHeaderText("Remove Doctor ID: " + doctorId);
                alert.setContentText("Are you sure you want to remove doctor with ID: " + doctorId + "?\n\n" +
                                   "Warning: Doctor details could not be retrieved.\n" +
                                   "This action cannot be undone!");
            }
            
            if (alert.showAndWait().get() == ButtonType.OK) {
                boolean removed = adminService.removeDoctorById(doctorId);
                if (removed) {
                    String doctorName = doctorToRemove != null ? doctorToRemove.getName() : "ID: " + doctorId;
                    showStatus("✓ Doctor removed successfully: " + doctorName, true);
                    doctorIdField.clear();
                    loadAllDoctors(); // Refresh the doctor table
                    refreshStatistics(); // Update doctor count
                } else {
                    showStatus("✗ Doctor not found or could not be removed", false);
                }
            }
        } catch (NumberFormatException e) {
            showStatus("Invalid doctor ID format", false);
        } catch (Exception e) {
            showStatus("Removal error: " + e.getMessage(), false);
        }
    }
    
    /**
     * Enhanced patient search - applies filters based on ID prefix and name
     * This method is called live as user types in search fields
     */
    private void applyPatientFilters() {
        if (allPatientsTable == null) return;
        
        String idPrefix = patientIdField != null ? patientIdField.getText().trim() : "";
        String namePart = patientNameField != null ? patientNameField.getText().trim().toLowerCase() : "";
        
        try {
            // Create a fresh PatientDAO instance to load latest data from JSON file
            // This ensures we get the most up-to-date patient list after any removals
            PatientDAO freshPatientDAO = new PatientDAO();
            CustomeBST<Patient> patientsBST = freshPatientDAO.getAllPatientsBST();
            ObservableList<Patient> filteredPatients = FXCollections.observableArrayList();
            
            // Apply filters using BST traversal
            patientsBST.inOrderTraversal(patient -> {
                String idStr = Integer.toString(patient.getId());
                String nameLower = patient.getName().toLowerCase();
                
                boolean idMatches = idPrefix.isEmpty() || idStr.startsWith(idPrefix);
                boolean nameMatches = namePart.isEmpty() || nameLower.contains(namePart);
                
                if (idMatches && nameMatches) {
                    filteredPatients.add(patient);
                }
            });
            
            allPatientsTable.setItems(filteredPatients);
            
            // Update search status
            updatePatientSearchStatus(filteredPatients.size(), idPrefix, namePart);
            
        } catch (Exception e) {
            showStatus("Error filtering patients: " + e.getMessage(), false);
        }
    }
    
    /**
     * Updates the patient search status label
     */
    private void updatePatientSearchStatus(int count, String idPrefix, String namePart) {
        if (patientSearchStatus == null) return;
        
        StringBuilder status = new StringBuilder();
        
        if (idPrefix.isEmpty() && namePart.isEmpty()) {
            status.append("Showing all patients (").append(count).append(")");
        } else {
            status.append("Found ").append(count).append(" patient(s)");
            if (!idPrefix.isEmpty()) {
                status.append(" with ID starting with '").append(idPrefix).append("'");
            }
            if (!namePart.isEmpty()) {
                if (!idPrefix.isEmpty()) status.append(" and");
                status.append(" name containing '").append(namePart).append("'");
            }
        }
        
        patientSearchStatus.setText(status.toString());
    }
    
    /**
     * Clears all patient search filters and shows all patients
     */
    @FXML
    private void clearPatientSearch() {
        if (patientIdField != null) {
            patientIdField.clear();
        }
        if (patientNameField != null) {
            patientNameField.clear();
        }
        
        // Load all patients to reset the view
        loadAllPatients();
        
        if (patientSearchStatus != null) {
            patientSearchStatus.setText("Showing all patients");
        }
        
        showStatus("Search filters cleared", true);
    }
    
    @FXML
    private void viewAllAppointments() {
        loadAllAppointments();
    }
    
    // ─────────────────────────────────────────────────────────────────────────
    // Navigation Bar Methods
    // ─────────────────────────────────────────────────────────────────────────
    
    @FXML
    private void showDashboardView() {
        switchToView("dashboard");
        updateNavbarState("dashboard");
        loadDashboardData();
    }
    
    @FXML
    private void showDoctorsView() {
        switchToView("doctors");
        updateNavbarState("doctors");
        loadAllDoctors();
        loadLoggedInDoctors();
    }
    
    @FXML
    private void showPatientsView() {
        switchToView("patients");
        updateNavbarState("patients");
        loadAllPatients();
    }
    
    @FXML
    private void showAppointmentsView() {
        switchToView("appointments");
        updateNavbarState("appointments");
        loadAllAppointments();
    }
    
    @FXML
    private void showRegistrationsView() {
        switchToView("registrations");
        updateNavbarState("registrations");
        loadPendingRegistrations();
    }
    
    /**
     * Switches between different views in the admin dashboard
     * Only the selected view will be visible, others will be hidden
     */
    private void switchToView(String viewName) {
        // Hide all views first
        if (dashboardView != null) dashboardView.setVisible(false);
        if (doctorsView != null) doctorsView.setVisible(false);
        if (patientsView != null) patientsView.setVisible(false);
        if (appointmentsView != null) appointmentsView.setVisible(false);
        if (registrationsView != null) registrationsView.setVisible(false);
        
        // Show the selected view
        switch (viewName.toLowerCase()) {
            case "dashboard":
                if (dashboardView != null) dashboardView.setVisible(true);
                break;
            case "doctors":
                if (doctorsView != null) doctorsView.setVisible(true);
                break;
            case "patients":
                if (patientsView != null) patientsView.setVisible(true);
                break;
            case "appointments":
                if (appointmentsView != null) appointmentsView.setVisible(true);
                break;
            case "registrations":
                if (registrationsView != null) registrationsView.setVisible(true);
                break;
            default:
                // Default to dashboard if unknown view
                if (dashboardView != null) dashboardView.setVisible(true);
                break;
        }
    }
    
    /**
     * Updates the navbar visual state to show which section is active
     */
    private void updateNavbarState(String activeSection) {
        // Reset all navbar buttons to inactive state
        resetNavbarButtons();
        
        // Activate the selected section
        switch (activeSection.toLowerCase()) {
            case "dashboard":
                setActiveNavButton(navDashboard, "fas-tachometer-alt");
                updateStatus("Dashboard view");
                break;
            case "doctors":
                setActiveNavButton(navDoctors, "fas-user-md");
                updateStatus("Doctor management view");
                break;
            case "patients":
                setActiveNavButton(navPatients, "fas-users");
                updateStatus("Patient management view");
                break;
            case "appointments":
                setActiveNavButton(navAppointments, "fas-calendar-alt");
                updateStatus("Appointment management view");
                break;
            case "registrations":
                setActiveNavButton(navRegistrations, "fas-user-plus");
                updateStatus("Doctor registrations view");
                break;
        }
    }
    
    /**
     * Sets a navbar button as active with proper styling and icon color
     */
    private void setActiveNavButton(Button button, String iconLiteral) {
        if (button != null) {
            button.setStyle("-fx-background-color: transparent; -fx-text-fill: #FFFFFF; -fx-font-family: 'Roboto Medium', 'Arial', 'Helvetica', sans-serif; -fx-font-size: 14px; -fx-padding: 12 20; -fx-background-radius: 6; -fx-cursor: hand; -fx-graphic-text-gap: 8; -fx-border-color: transparent; -fx-border-width: 0;");
            
            // Update icon color to white for active state
            if (button.getGraphic() instanceof org.kordamp.ikonli.javafx.FontIcon) {
                org.kordamp.ikonli.javafx.FontIcon icon = (org.kordamp.ikonli.javafx.FontIcon) button.getGraphic();
                icon.setIconColor(javafx.scene.paint.Color.WHITE);
            }
        }
    }
    
    /**
     * Resets all navbar buttons to inactive state
     */
    private void resetNavbarButtons() {
        String inactiveStyle = "-fx-background-color: transparent; -fx-text-fill: #ADB5BD; -fx-font-family: 'Roboto Medium', 'Arial', 'Helvetica', sans-serif; -fx-font-size: 14px; -fx-padding: 12 20; -fx-background-radius: 6; -fx-cursor: hand; -fx-graphic-text-gap: 8; -fx-border-color: transparent; -fx-border-width: 0;";
        
        setInactiveNavButton(navDashboard, inactiveStyle);
        setInactiveNavButton(navDoctors, inactiveStyle);
        setInactiveNavButton(navPatients, inactiveStyle);
        setInactiveNavButton(navAppointments, inactiveStyle);
        setInactiveNavButton(navRegistrations, inactiveStyle);
    }
    
    /**
     * Sets a navbar button as inactive with proper styling and icon color
     */
    private void setInactiveNavButton(Button button, String style) {
        if (button != null) {
            button.setStyle(style);
            
            // Update icon color to gray for inactive state
            if (button.getGraphic() instanceof org.kordamp.ikonli.javafx.FontIcon) {
                org.kordamp.ikonli.javafx.FontIcon icon = (org.kordamp.ikonli.javafx.FontIcon) button.getGraphic();
                icon.setIconColor(javafx.scene.paint.Color.web("#ADB5BD"));
            }
        }
    }
    
    /**
     * Updates the status label with current operation
     */
    private void updateStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }

    @FXML
    private void logout() {
        try {
            // Navigate back to login screen
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/ui/login_view.fxml"));
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            showStatus("Error during logout: " + e.getMessage(), false);
        }
    }
    
    /**
     * Applies admin level restrictions to UI elements based on current admin privileges.
     * Super admins have full access, regular admins can only view data.
     */
    private void applyAdminLevelRestrictions() {
        if (currentAdmin == null) return;
        
        boolean isSuperAdmin = currentAdmin.isSuperAdmin();
        
        // Disable/enable patient removal functionality
        if (removePatientButton != null) {
            removePatientButton.setDisable(!isSuperAdmin);
            if (!isSuperAdmin) {
                removePatientButton.setText("View Only (Super Admin Required)");
                removePatientButton.setStyle("-fx-background-color: #6C757D; -fx-text-fill: white; -fx-font-family: 'Roboto Medium', 'Arial', 'Helvetica', sans-serif; -fx-font-size: 14px; -fx-padding: 12 25; -fx-background-radius: 8; -fx-cursor: not-allowed; -fx-graphic-text-gap: 10;");
            } else {
                removePatientButton.setText("Remove Selected");
                removePatientButton.setStyle("-fx-background-color: #DC3545; -fx-text-fill: white; -fx-font-family: 'Roboto Medium', 'Arial', 'Helvetica', sans-serif; -fx-font-size: 14px; -fx-padding: 12 25; -fx-background-radius: 8; -fx-cursor: hand; -fx-graphic-text-gap: 10;");
            }
        }

        // Disable/enable doctor removal functionality
        if (removeDoctorButton != null) {
            removeDoctorButton.setDisable(!isSuperAdmin);
            if (!isSuperAdmin) {
                removeDoctorButton.setText("View Only (Super Admin Required)");
                removeDoctorButton.setStyle("-fx-background-color: #6C757D; -fx-text-fill: white; -fx-font-family: 'Roboto Medium', 'Arial', 'Helvetica', sans-serif; -fx-font-size: 14px; -fx-padding: 12 25; -fx-background-radius: 8; -fx-cursor: not-allowed; -fx-graphic-text-gap: 10;");
            } else {
                removeDoctorButton.setText("Remove Selected");
                removeDoctorButton.setStyle("-fx-background-color: #DC3545; -fx-text-fill: white; -fx-font-family: 'Roboto Medium', 'Arial', 'Helvetica', sans-serif; -fx-font-size: 14px; -fx-padding: 12 25; -fx-background-radius: 8; -fx-cursor: hand; -fx-graphic-text-gap: 10;");
            }
        }
        
        // Disable/enable doctor registration processing buttons
        if (acceptButton != null) {
            acceptButton.setDisable(!isSuperAdmin);
            if (!isSuperAdmin) {
                acceptButton.setText("View Only");
                acceptButton.setStyle("-fx-background-color: #6C757D; -fx-text-fill: white; -fx-font-family: 'Roboto Medium', 'Arial', 'Helvetica', sans-serif; -fx-font-size: 14px; -fx-padding: 12 25; -fx-background-radius: 8; -fx-cursor: not-allowed; -fx-graphic-text-gap: 10;");
            } else {
                acceptButton.setText("Accept");
                acceptButton.setStyle("-fx-background-color: #28A745; -fx-text-fill: white; -fx-font-family: 'Roboto Medium', 'Arial', 'Helvetica', sans-serif; -fx-font-size: 14px; -fx-padding: 12 25; -fx-background-radius: 8; -fx-cursor: hand; -fx-graphic-text-gap: 10;");
            }
        }
        
        if (declineButton != null) {
            declineButton.setDisable(!isSuperAdmin);
            if (!isSuperAdmin) {
                declineButton.setText("View Only");
                declineButton.setStyle("-fx-background-color: #6C757D; -fx-text-fill: white; -fx-font-family: 'Roboto Medium', 'Arial', 'Helvetica', sans-serif; -fx-font-size: 14px; -fx-padding: 12 25; -fx-background-radius: 8; -fx-cursor: not-allowed; -fx-graphic-text-gap: 10;");
            } else {
                declineButton.setText("Decline");
                declineButton.setStyle("-fx-background-color: #DC3545; -fx-text-fill: white; -fx-font-family: 'Roboto Medium', 'Arial', 'Helvetica', sans-serif; -fx-font-size: 14px; -fx-padding: 12 25; -fx-background-radius: 8; -fx-cursor: hand; -fx-graphic-text-gap: 10;");
            }
        }
        
        // Add visual indicators for permission level
        updatePermissionIndicators(isSuperAdmin);
    }
    
    /**
     * Updates visual indicators to show current admin permission level
     */
    private void updatePermissionIndicators(boolean isSuperAdmin) {
        String permissionText = isSuperAdmin ? 
            "✓ Super Admin: Full administrative privileges" : 
            "ℹ Regular Admin: View-only access";
        
        String permissionStyle = isSuperAdmin ?
            "-fx-text-fill: #28A745; -fx-font-weight: bold;" :
            "-fx-text-fill: #FFA500; -fx-font-weight: bold;";
            
        if (statusLabel != null) {
            statusLabel.setText(permissionText);
            statusLabel.setStyle(permissionStyle);
        }
    }
}
