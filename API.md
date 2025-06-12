# API Documentation

## Overview

This document provides detailed API documentation for the Hospital Management System's core classes and methods.

## Data Access Objects (DAOs)

### PatientDAO

Handles patient data persistence and retrieval using a Binary Search Tree for efficient operations.

```java
public class PatientDAO {
    private static final String FILE = "patients.json";
    private final ObjectMapper mapper;
    private CustomeBST<Patient> patientsBST;
}
```

#### Methods

| Method | Description | Return Type | Parameters |
|--------|-------------|-------------|------------|
| `registerPatient(Patient p)` | Registers a new patient | `void` | `Patient p` |
| `findById(int id)` | Finds patient by ID | `Patient` | `int id` |
| `updatePatient(Patient p)` | Updates existing patient | `void` | `Patient p` |
| `deletePatient(int id)` | Deletes patient by ID | `boolean` | `int id` |
| `getAllPatients()` | Returns all patients | `CustomeLinkedList<Patient>` | None |
| `getAllPatientsBST()` | Returns patient BST | `CustomeBST<Patient>` | None |

#### Usage Example

```java
PatientDAO patientDAO = new PatientDAO();

// Register new patient
Patient newPatient = new Patient(1234567, "john_doe", "password", 
                                "John Doe", "john@email.com", 
                                30, "123 Main St", "555-1234");
patientDAO.registerPatient(newPatient);

// Find patient by ID
Patient found = patientDAO.findById(1234567);

// Update patient
found.setAge(31);
patientDAO.updatePatient(found);
```

### DoctorDAO

Manages doctor information and login sessions.

```java
public class DoctorDAO {
    private static final String FILE = "doctors.json";
    private final ObjectMapper mapper;
    private CustomeLinkedList<Doctor> doctorList;
}
```

#### Methods

| Method | Description | Return Type | Parameters |
|--------|-------------|-------------|------------|
| `registerDoctor(Doctor d)` | Registers a new doctor | `void` | `Doctor d` |
| `findById(int id)` | Finds doctor by ID | `Doctor` | `int id` |
| `updateDoctor(Doctor d)` | Updates doctor information | `void` | `Doctor d` |
| `deleteDoctor(int id)` | Deletes doctor by ID | `boolean` | `int id` |
| `getAllDoctors()` | Returns all doctors | `CustomeLinkedList<Doctor>` | None |

#### Usage Example

```java
DoctorDAO doctorDAO = new DoctorDAO();

// Register new doctor
Doctor newDoctor = new Doctor(1234, "dr_smith", "password", 
                             "Dr. Smith", "Cardiology");
doctorDAO.registerDoctor(newDoctor);

// Find and update doctor
Doctor doctor = doctorDAO.findById(1234);
doctor.setCurrentLoginTime(LocalDateTime.now());
doctorDAO.updateDoctor(doctor);
```

### AppointmentDAO

Handles appointment scheduling and management.

```java
public class AppointmentDAO {
    private static final String FILE = "allAppointments.json";
    private final ObjectMapper mapper;
}
```

#### Methods

| Method | Description | Return Type | Parameters |
|--------|-------------|-------------|------------|
| `scheduleAppointment(Appointment a)` | Schedules new appointment | `void` | `Appointment a` |
| `getQueueForDoctor(int doctorId)` | Gets doctor's appointment queue | `CustomeLinkedList<Appointment>` | `int doctorId` |
| `processNextAppointment(int doctorId)` | Processes next appointment | `Appointment` | `int doctorId` |
| `peekNextAppointment(int doctorId)` | Views next appointment | `Appointment` | `int doctorId` |
| `getAllAppointments()` | Returns all appointments | `CustomeLinkedList<Appointment>` | None |
| `hasAppointment(int patientId, int doctorId)` | Checks if appointment exists | `boolean` | `int patientId, int doctorId` |

#### Usage Example

```java
AppointmentDAO appointmentDAO = new AppointmentDAO();

// Schedule appointment
Appointment appointment = new Appointment(5001234, 1234567, 9814, 
                                        "Cardiology", 
                                        LocalDateTime.now().plusDays(1), 
                                        "Chest pain");
appointmentDAO.scheduleAppointment(appointment);

// Get doctor's queue
CustomeLinkedList<Appointment> queue = appointmentDAO.getQueueForDoctor(9814);

// Process next appointment
Appointment next = appointmentDAO.processNextAppointment(9814);
```

### LoginSessionDAO

Manages active doctor login sessions with automatic cleanup.

```java
public class LoginSessionDAO {
    private static final String FILE = "loginSessions.json";
    private static final long EXPIRATION_HOURS = 24;
    private final ObjectMapper mapper;
}
```

#### Methods

| Method | Description | Return Type | Parameters |
|--------|-------------|-------------|------------|
| `addSession(LoginSession session)` | Adds new login session | `void` | `LoginSession session` |
| `loadAllSessions()` | Loads all active sessions | `CustomeLinkedList<LoginSession>` | None |
| `getAllSessions()` | Gets all sessions with cleanup | `CustomeLinkedList<LoginSession>` | None |

## Data Models

### Patient

Represents a patient in the system.

```java
public class Patient extends User implements Comparable<Patient> {
    private int age;
    private String address;
    private String phoneNumber;
    private CustomeLinkedList<String> illnessHistory;
}
```

#### Constructor

```java
// Full constructor
public Patient(int id, String username, String password, 
               String fullname, String email, int age, 
               String address, String phoneNumber)

// Backward compatibility constructor
public Patient(int id, String fullname, int age, 
               String address, String phoneNumber)
```

#### Key Methods

| Method | Description | Return Type |
|--------|-------------|-------------|
| `getName()` | Gets patient name | `String` |
| `getAge()` | Gets patient age | `int` |
| `getAddress()` | Gets patient address | `String` |
| `getPhoneNumber()` | Gets phone number | `String` |
| `getIllnessHistory()` | Gets illness history | `CustomeLinkedList<String>` |
| `addIllness(String illness)` | Adds illness to history | `void` |
| `isValidId(int id)` | Validates 7-digit ID | `boolean` |

### Doctor

Represents a doctor in the system.

```java
public class Doctor extends User implements Comparable<Doctor> {
    private String specialty;
    private LocalDateTime currentLoginTime;
    private CustomeLinkedList<LocalDateTime> loginHistory;
}
```

#### Constructor

```java
public Doctor(int id, String username, String password, 
              String fullname, String specialty)
```

#### Key Methods

| Method | Description | Return Type |
|--------|-------------|-------------|
| `getSpecialty()` | Gets doctor's specialty | `String` |
| `getCurrentLoginTime()` | Gets current login time | `LocalDateTime` |
| `getLoginHistory()` | Gets login history | `CustomeLinkedList<LocalDateTime>` |
| `login()` | Records login time | `void` |
| `logout()` | Clears current session | `void` |

### Appointment

Represents an appointment between patient and doctor.

```java
public class Appointment {
    private int appointmentId;
    private int patientId;
    private int doctorId;
    private String doctorSpecialty;
    private LocalDateTime time;
    private String patientIllness;
    private String status;
}
```

#### Constructor

```java
public Appointment(int appointmentId, int patientId, int doctorId,
                   String doctorSpecialty, LocalDateTime time, 
                   String patientIllness)
```

#### Key Methods

| Method | Description | Return Type |
|--------|-------------|-------------|
| `getAppointmentId()` | Gets appointment ID | `int` |
| `getPatientId()` | Gets patient ID | `int` |
| `getDoctorId()` | Gets doctor ID | `int` |
| `getTime()` | Gets appointment time | `LocalDateTime` |
| `getPatientIllness()` | Gets illness description | `String` |
| `getStatus()` | Gets appointment status | `String` |

## Custom Data Structures

### CustomeLinkedList<T>

Generic linked list implementation with JSON serialization support.

```java
public class CustomeLinkedList<T> implements Iterable<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;
}
```

#### Key Methods

| Method | Description | Return Type | Time Complexity |
|--------|-------------|-------------|-----------------|
| `add(T data)` | Adds element to end | `void` | O(1) |
| `remove(T data)` | Removes first occurrence | `boolean` | O(n) |
| `get(int index)` | Gets element at index | `T` | O(n) |
| `contains(T data)` | Checks if element exists | `boolean` | O(n) |
| `size()` | Returns list size | `int` | O(1) |
| `isEmpty()` | Checks if list is empty | `boolean` | O(1) |
| `clear()` | Removes all elements | `void` | O(1) |

#### Usage Example

```java
CustomeLinkedList<String> list = new CustomeLinkedList<>();
list.add("First");
list.add("Second");
list.add("Third");

for (String item : list) {
    System.out.println(item);
}

boolean exists = list.contains("Second"); // true
String first = list.get(0); // "First"
```

### CustomeBST<T>

Generic Binary Search Tree implementation for efficient searching.

```java
public class CustomeBST<T extends Comparable<T>> {
    private Node<T> root;
    private int size;
}
```

#### Key Methods

| Method | Description | Return Type | Time Complexity |
|--------|-------------|-------------|-----------------|
| `insert(T value)` | Inserts value into tree | `void` | O(log n) average |
| `search(T key)` | Searches for value | `T` | O(log n) average |
| `delete(T value)` | Deletes value from tree | `void` | O(log n) average |
| `contains(T value)` | Checks if value exists | `boolean` | O(log n) average |
| `findMin()` | Finds minimum value | `T` | O(log n) |
| `findMax()` | Finds maximum value | `T` | O(log n) |
| `inOrderTraversal(Consumer<T>)` | Traverses in order | `void` | O(n) |
| `inOrderList()` | Returns sorted list | `CustomeLinkedList<T>` | O(n) |
| `size()` | Returns tree size | `int` | O(1) |
| `height()` | Returns tree height | `int` | O(n) |

#### Usage Example

```java
CustomeBST<Patient> patientTree = new CustomeBST<>();

// Insert patients
patientTree.insert(patient1);
patientTree.insert(patient2);
patientTree.insert(patient3);

// Search for patient
Patient found = patientTree.search(searchKey);

// Get all patients in sorted order
CustomeLinkedList<Patient> sortedPatients = patientTree.inOrderList();

// Traverse tree
patientTree.inOrderTraversal(patient -> {
    System.out.println(patient.getName());
});
```

### CustomeQueue<T>

Generic queue implementation using linked list.

```java
public class CustomeQueue<T> {
    private CustomeLinkedList<T> list;
}
```

#### Key Methods

| Method | Description | Return Type | Time Complexity |
|--------|-------------|-------------|-----------------|
| `enqueue(T value)` | Adds element to rear | `void` | O(1) |
| `dequeue()` | Removes element from front | `T` | O(1) |
| `peek()` | Views front element | `T` | O(1) |
| `isEmpty()` | Checks if queue is empty | `boolean` | O(1) |
| `size()` | Returns queue size | `int` | O(1) |

#### Usage Example

```java
CustomeQueue<PendingDoctorRegistration> pendingQueue = new CustomeQueue<>();

// Add registration to queue
pendingQueue.enqueue(registration);

// Process next registration
PendingDoctorRegistration next = pendingQueue.dequeue();

// Check what's next without removing
PendingDoctorRegistration peek = pendingQueue.peek();
```

## Service Classes

### AdminService

Provides business logic for admin operations.

```java
public class AdminService {
    private final AdminDAO adminDAO;
    private final DoctorDAO doctorDAO;
    private final PatientDAO patientDAO;
    private final AppointmentDAO appointmentDAO;
    private final LoginSessionDAO sessionDAO;
}
```

#### Key Methods

| Method | Description | Return Type |
|--------|-------------|-------------|
| `authenticate(String username, String password)` | Authenticates admin | `Admin` |
| `getAllDoctors()` | Gets all doctors | `CustomeLinkedList<Doctor>` |
| `getCurrentlyLoggedInDoctors()` | Gets logged-in doctors | `CustomeLinkedList<Doctor>` |
| `getAllPatients()` | Gets all patients | `CustomeLinkedList<Patient>` |
| `getAllAppointments()` | Gets all appointments | `CustomeLinkedList<Appointment>` |
| `getSystemStats()` | Gets system statistics | `SystemStats` |
| `removeDoctorById(int id)` | Removes doctor | `boolean` |
| `removePatientById(int id)` | Removes patient | `boolean` |

### PatientService

Provides business logic for patient operations.

```java
public class PatientService {
    private final PatientDAO patientDAO;
}
```

#### Key Methods

| Method | Description | Return Type |
|--------|-------------|-------------|
| `registerPatient(String name, String email, int age, String address, String phone)` | Registers patient | `Patient` |
| `generateUniquePatientId()` | Generates unique ID | `int` |

## UI Controllers

### PatientDashboardController

Controls patient dashboard interface.

#### Key Methods

| Method | Description | Trigger |
|--------|-------------|---------|
| `initialize()` | Initializes dashboard | Automatic |
| `loadPatientInfo()` | Loads patient information | Internal |
| `loadRecentActivity()` | Loads recent appointments | Internal |
| `onBookAppointmentClicked()` | Navigates to booking | Button click |
| `onMedicalRecordsClicked()` | Navigates to records | Button click |
| `onLogoutClicked()` | Logs out patient | Button click |

### DoctorDashboardController

Controls doctor dashboard interface.

#### Key Methods

| Method | Description | Trigger |
|--------|-------------|---------|
| `setLoggedInDoctor(Doctor d)` | Sets current doctor | External call |
| `refreshDashboard()` | Refreshes all data | Internal |
| `onProcessNextClicked()` | Processes next appointment | Button click |
| `onViewPatientsClicked()` | Opens patient directory | Button click |

### AdminController

Controls admin panel interface.

#### Key Methods

| Method | Description | Trigger |
|--------|-------------|---------|
| `setCurrentAdmin(Admin admin)` | Sets current admin | External call |
| `loadDashboardData()` | Loads all admin data | Internal |
| `approvePendingRegistration()` | Approves doctor registration | Button click |
| `removeDoctor()` | Removes doctor from system | Button click |
| `removePatient()` | Removes patient from system | Button click |

## Session Management

### CurrentPatientHolder

Manages current patient session.

```java
public class CurrentPatientHolder {
    private static int patientId;
    private static Patient currentPatient;
}
```

#### Key Methods

| Method | Description | Return Type |
|--------|-------------|-------------|
| `setCurrentPatient(Patient patient)` | Sets current patient | `void` |
| `getCurrentPatient()` | Gets current patient | `Patient` |
| `isLoggedIn()` | Checks login status | `boolean` |
| `clearSession()` | Clears session | `void` |
| `getPatientId()` | Gets patient ID | `int` |

### CurrentDoctorHolder

Manages current doctor session.

```java
public class CurrentDoctorHolder {
    private static Doctor doctor;
}
```

#### Key Methods

| Method | Description | Return Type |
|--------|-------------|-------------|
| `setDoctor(Doctor d)` | Sets current doctor | `void` |
| `getDoctor()` | Gets current doctor | `Doctor` |

## Error Handling

### Common Exceptions

| Exception | Cause | Resolution |
|-----------|-------|------------|
| `IllegalStateException` | Patient not found | Check patient ID validity |
| `IndexOutOfBoundsException` | Invalid list index | Validate index range |
| `NumberFormatException` | Invalid number format | Validate input format |
| `IOException` | File access error | Check file permissions |
| `JsonProcessingException` | JSON parsing error | Validate JSON structure |

### Best Practices

1. **Always validate input data** before processing
2. **Use try-catch blocks** for file operations
3. **Check for null values** before method calls
4. **Provide meaningful error messages** to users
5. **Log exceptions** for debugging purposes

## Performance Considerations

### Optimization Guidelines

1. **Use BST for patient searches** - O(log n) vs O(n) linear search
2. **Limit appointment queries** - Filter by doctor ID first
3. **Cache frequently accessed data** - Keep current sessions in memory
4. **Minimize JSON file writes** - Batch operations when possible
5. **Use efficient data structures** - LinkedList for sequential access, BST for searches

### Memory Management

1. **Clear unused references** - Set objects to null when done
2. **Avoid memory leaks** - Properly cleanup listeners and events
3. **Use lazy loading** - Load data only when needed
4. **Limit collection sizes** - Paginate large datasets

## Testing Guidelines

### Unit Testing

```java
@Test
public void testPatientRegistration() {
    PatientDAO dao = new PatientDAO();
    Patient patient = new Patient(1234567, "test", 25, "Address", "Phone");
    
    dao.registerPatient(patient);
    Patient found = dao.findById(1234567);
    
    assertEquals(patient.getId(), found.getId());
    assertEquals(patient.getName(), found.getName());
}
```

### Integration Testing

Test complete workflows:
1. Patient registration → Login → Appointment booking
2. Doctor login → Appointment processing → Patient update
3. Admin login → User management → System statistics

### Data Validation Testing

Test edge cases:
1. Invalid ID formats
2. Empty required fields
3. Duplicate registrations
4. Invalid date ranges
5. SQL injection attempts (though using JSON)

This completes the comprehensive API documentation for the Hospital Management System.
