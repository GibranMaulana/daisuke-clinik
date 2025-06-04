module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.annotation;
    requires org.kordamp.bootstrapfx.core;

    opens com.example to javafx.fxml, javafx.graphics;
    opens com.example.model to javafx.base, javafx.fxml;
    opens com.example.model.ds to javafx.base;
    opens com.example.ui to javafx.fxml, javafx.graphics, javafx.base;
    
    exports com.example;
    exports com.example.model;
    exports com.example.model.ds;
    exports com.example.ui;
    exports com.example.data;
}
