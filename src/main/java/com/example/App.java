package com.example;

import com.example.data.PatientDataMigration;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import java.io.InputStream;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Run patient data migration at application startup
        try {
            PatientDataMigration migration = new PatientDataMigration();
            migration.migratePatientDataIfNeeded();
        } catch (Exception e) {
            System.err.println("Warning: Patient data migration failed: " + e.getMessage());
            e.printStackTrace();
        }

        // Try to load Roboto fonts if available
        try {
            loadFonts(
                "/fonts/static/Roboto-Regular.ttf",
                "/fonts/static/Roboto-Medium.ttf",
                "/fonts/static/Roboto-Black.ttf",
                "/fonts/static/Roboto_Condensed-Regular.ttf",
                "/fonts/static/Roboto_Condensed-Bold.ttf"
            );
        } catch (Exception e) {
            System.out.println("Custom fonts not available, using system fonts");
        }

        Parent root = FXMLLoader.load(getClass().getResource("ui/login_view.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Hospital Management System");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.show();
    }

    private void loadFonts(String... fontPaths) {
        for (String fontPath : fontPaths) {
            try (InputStream is = getClass().getResourceAsStream(fontPath)) {
                if (is != null) {
                    Font.loadFont(is, 12);
                } else {
                    System.err.println("Could not load font: " + fontPath);
                }
            } catch (Exception e) {
                System.err.println("Error loading font " + fontPath + ": " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
