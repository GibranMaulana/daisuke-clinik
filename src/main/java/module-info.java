module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    opens com.example.model.ds to 
    com.fasterxml.jackson.databind
    , javafx.fxml
    , javafx.graphics
    , javafx.controls
    , com.fasterxml.jackson.core
    , com.fasterxml.jackson.datatype.jsr310;

    opens com.example.model to 
    com.fasterxml.jackson.databind
    , javafx.fxml
    , javafx.graphics
    , javafx.controls
    , com.fasterxml.jackson.core
    , com.fasterxml.jackson.datatype.jsr310;
    
    opens com.example.ui to 
    com.fasterxml.jackson.databind
    , javafx.fxml
    , javafx.graphics
    , javafx.controls
    , com.fasterxml.jackson.core
    , com.fasterxml.jackson.datatype.jsr310;
    
    opens com.example.data to 
    com.fasterxml.jackson.databind
    , javafx.fxml
    , javafx.graphics
    , javafx.controls
    , com.fasterxml.jackson.core
    , com.fasterxml.jackson.datatype.jsr310;

    opens com.example to javafx.fxml;
    
    exports com.example;
    exports com.example.model;
    exports com.example.model.ds;
    exports com.example.ui;
    exports com.example.data;
}
