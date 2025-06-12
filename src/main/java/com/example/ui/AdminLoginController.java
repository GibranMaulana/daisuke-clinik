package com.example.ui;

import com.example.data.AdminService;
import com.example.model.Admin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminLoginController {
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Label statusLabel;
    
    private AdminService adminService = new AdminService();

    @FXML
    private void onLoginClicked(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        
        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            showStatus("Please enter both username and password", true);
            return;
        }
        
        try {
            // Authenticate admin
            Admin admin = adminService.authenticate(username, password);
            
            if (admin != null) {
                // Authentication successful - navigate to admin dashboard
                showStatus("Login successful! Loading dashboard...", false);
                
                // Load admin dashboard
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ui/admin_dashboard.fxml"));
                Parent root = loader.load();
                
                // Get the controller and set the current admin
                AdminController adminController = loader.getController();
                adminController.setCurrentAdmin(admin);
                
                // Switch to dashboard
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.getScene().setRoot(root);
                
            } else {
                showStatus("Invalid username or password", true);
                passwordField.clear();
            }
            
        } catch (Exception e) {
            showStatus("Login failed: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    @FXML
    private void onBackClicked(ActionEvent event) {
        try {
            // Go back to main login screen
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/ui/login_view.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            showStatus("Error loading main screen", true);
            e.printStackTrace();
        }
    }
    
    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setVisible(true);
        
        if (isError) {
            statusLabel.setStyle("-fx-text-fill: #DC3545; -fx-font-size: 12px; -fx-font-family: 'Roboto', 'Arial', 'Helvetica', sans-serif;");
        } else {
            statusLabel.setStyle("-fx-text-fill: #28A745; -fx-font-size: 12px; -fx-font-family: 'Roboto', 'Arial', 'Helvetica', sans-serif;");
        }
    }
}
