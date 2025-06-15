# FXML Loading Error Fix

## Issue Description
The application was encountering a `javafx.fxml.LoadException` with a `NumberFormatException` for the input string "USE_COMPUTED_SIZE" at line 145 of the patient_medical_records.fxml file.

## Root Cause
The error was caused by using JavaFX constant values that are only valid in Java code, not in FXML files:
- `USE_COMPUTED_SIZE` - Invalid in FXML, only valid in JavaFX code
- `Infinity` - Invalid as a numeric value in FXML
- Invalid attribute combinations that couldn't be parsed by the FXML loader

## Error Details
```
javafx.fxml.LoadException: 
/home/bammm/coding/tubes-sda1/hospital/target/classes/com/example/ui/patient_medical_records.fxml:145
Caused by: java.lang.NumberFormatException: For input string: "USE_COMPUTED_SIZE"
```

## Solution Applied

### 1. Removed Invalid Attributes from Main VBox
**Before:**
```xml
<VBox style="-fx-padding: 25; -fx-spacing: 20; -fx-background-color: #F8F9FA;"
      fillWidth="true" minHeight="0"
      prefWidth="USE_COMPUTED_SIZE" maxWidth="Infinity">
```

**After:**
```xml
<VBox style="-fx-padding: 25; -fx-spacing: 20; -fx-background-color: #F8F9FA;"
      fillWidth="true">
```

### 2. Fixed Medical History Container
**Before:**
```xml
<HBox fx:id="illnessHistoryContainer" spacing="8" style="-fx-padding: 5;"
      fillHeight="true" minWidth="0"
      prefHeight="USE_COMPUTED_SIZE" maxHeight="Infinity">
```

**After:**
```xml
<HBox fx:id="illnessHistoryContainer" spacing="8" style="-fx-padding: 5;"
      fillHeight="true">
```

### 3. Fixed Appointment History Container
**Before:**
```xml
<HBox fx:id="appointmentHistoryContainer" spacing="12" style="-fx-padding: 5;"
      fillHeight="true" minWidth="0"
      prefHeight="USE_COMPUTED_SIZE" maxHeight="Infinity">
```

**After:**
```xml
<HBox fx:id="appointmentHistoryContainer" spacing="12" style="-fx-padding: 5;"
      fillHeight="true">
```

## FXML vs JavaFX Code Guidelines

### ❌ Invalid in FXML
- `USE_COMPUTED_SIZE` - Use in JavaFX code only
- `Infinity` - Not a valid numeric value
- Constants from JavaFX classes (e.g., `Region.USE_COMPUTED_SIZE`)

### ✅ Valid in FXML
- Numeric values: `"400"`, `"500"`, etc.
- Percentages: `"50%"`
- Keywords: `"ALWAYS"`, `"AS_NEEDED"`, `"NEVER"`
- Boolean values: `"true"`, `"false"`

## Key Lessons

1. **FXML Limitations**: FXML cannot directly reference JavaFX constants
2. **Attribute Validation**: FXML parser strictly validates attribute values
3. **Simplicity**: Keep FXML simple and move complex logic to controllers
4. **Testing**: Always test FXML changes immediately after modification

## Alternative Approaches

If you need dynamic sizing behavior:
1. **Set in Controller**: Use `node.setPrefWidth(Region.USE_COMPUTED_SIZE)` in Java code
2. **CSS Styling**: Use CSS properties in style attributes
3. **Layout Constraints**: Use layout pane properties instead of node properties

## Verification

### ✅ Compilation Status
- Clean compilation successful
- No FXML loading errors
- Application starts normally

### ✅ Functionality Preserved
- Main content scrolling works correctly
- Medical history horizontal scrolling functional
- Appointment history horizontal scrolling functional
- Patient information displays properly
- Navigation sidebar works as expected

## Files Modified
- `/src/main/resources/com/example/ui/patient_medical_records.fxml`

## Testing Results
- ✅ Application starts without errors
- ✅ FXML loads successfully
- ✅ All UI components render correctly
- ✅ Scrolling functionality preserved
- ✅ Layout responsiveness maintained

---
**Resolution Status:** ✅ **RESOLVED**  
**Date Fixed:** June 15, 2025  
**Impact:** Critical error blocking application startup - now resolved
