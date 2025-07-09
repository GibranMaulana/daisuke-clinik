# 🏥 Sistem Manajemen Rumah Sakit

<div align="center">

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-007396?style=for-the-badge&logo=java&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![JSON](https://img.shields.io/badge/JSON-000000?style=for-the-badge&logo=json&logoColor=white)
![Custom DSA](https://img.shields.io/badge/Custom_DSA-FF6B6B?style=for-the-badge&logo=data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjQiIGhlaWdodD0iMjQiIHZpZXdCb3g9IjAgMCAyNCAyNCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHBhdGggZD0iTTIgMTJIMjJNMTIgMlYyMk0yIDJMMjIgMjJNMjIgMkwyIDIyIiBzdHJva2U9IndoaXRlIiBzdHJva2Utd2lkdGg9IjIiIHN0cm9rZS1saW5lY2FwPSJyb3VuZCIvPgo8L3N2Zz4K&logoColor=white)

**Sistem Manajemen Rumah Sakit Modern dengan Struktur Data Kustom**

*Aplikasi desktop untuk mengelola operasional rumah sakit dengan antarmuka yang intuitif dan efisien*

</div>

---

## 📋 Daftar Isi

- [Tech Stack](#-tech-stack)
- [Konsep Inti](#-konsep-inti)
- [Fitur Utama](#-fitur-utama)
- [Arsitektur Sistem](#️-arsitektur-sistem)
- [Struktur Data Kustom](#-struktur-data-kustom)
- [Instalasi](#-instalasi)
- [Penggunaan](#-penggunaan)
- [Model Data](#-model-data)
- [Screenshots](#-screenshots)

---

## 🛠 Tech Stack

### **Core Technologies**
- **Java 11+** - Bahasa pemrograman utama
- **JavaFX 17.0.2** - Framework UI desktop modern
- **Maven** - Build tool dan dependency management

### **UI & Design**
- **FXML** - Deklarative UI layout
- **CSS** - Styling dan theming
- **Ikonli** - Icon library (FontAwesome, Material Design)
- **BootstrapFX** - Modern UI components

### **Data Management**
- **Jackson** - JSON serialization/deserialization
- **Custom Data Structures** - Implementasi struktur data sendiri
- **File-based Storage** - Persistensi data menggunakan JSON files

### **Testing & Quality**
- **JUnit 5** - Unit testing framework
- **Custom Debug Tools** - Debugging utilities

---

## 🧠 Konsep Inti

### **1. Implementasi Struktur Data Kustom**
Sistem ini **tidak menggunakan** Java Collections Framework standar (`ArrayList`, `LinkedList`, `Queue`, dll). Sebagai gantinya, mengimplementasikan struktur data sendiri:

- **`CustomeLinkedList<T>`** - Linked list generik dengan iterasi
- **`CustomeQueue<T>`** - Queue implementation menggunakan linked list
- **`CustomeBST<T>`** - Binary Search Tree untuk pencarian efisien

### **2. Arsitektur Multi-Role**
Sistem mendukung tiga jenis pengguna dengan hak akses berbeda:
- **Admin** - Mengelola sistem, dokter, dan data pasien
- **Dokter** - Menangani appointment dan diagnosis
- **Pasien** - Booking appointment dan melihat rekam medis

### **3. Session Management**
Implementasi sistem tracking login yang robust:
- **Doctor Session Tracking** - Memantau dokter yang sedang online
- **Login History** - Riwayat login semua pengguna
- **Auto Session Cleanup** - Pembersihan session yang kadaluarsa

### **4. Real-time Data Management**
- **Appointment Queue System** - Antrian appointment real-time
- **Expired Appointment Cleanup** - Auto-remove appointment kadaluarsa
- **Time Conflict Detection** - Deteksi konflik jadwal otomatis

---

## ✨ Fitur Utama

### 👨‍💼 **Fitur Admin**
```
🔐 Dashboard Admin Terpusat
├── 📊 Monitoring Status Sistem
├── 👥 Manajemen Dokter
│   ├── Approve/Reject Registrasi Dokter
│   ├── View Dokter Online/Offline
│   └── Remove Dokter (Super Admin)
├── 🏥 Manajemen Pasien
│   ├── View Semua Pasien
│   ├── Search Pasien by ID
│   └── Remove Pasien
├── 📅 Manajemen Appointment
│   ├── View Semua Appointment
│   ├── Filter by Status
│   ├── Auto-cleanup Expired
│   └── Manual Remove (Super Admin)
└── 📈 Login History & Analytics
```

### 👨‍⚕️ **Fitur Dokter**
```
🏥 Dashboard Dokter Interaktif
├── 📋 Queue Management
│   ├── View Appointment Queue
│   ├── Process Next Appointment
│   └── Priority-based Sorting
├── 👤 Patient Processing
│   ├── View Patient Details
│   ├── Medical History Review
│   └── Diagnosis & Treatment
├── 📝 Medical Records
│   ├── Create Diagnosis
│   ├── Prescription Management
│   └── Patient History Update
└── 📊 Statistics & Analytics
    ├── Total Queue Count
    ├── Next Appointment Info
    └── Recent Activity
```

### 🏥 **Fitur Pasien**
```
👤 Patient Portal Lengkap
├── 🗓️ Appointment Booking
│   ├── Select Doctor & Specialty
│   ├── Choose Date & Time
│   ├── Conflict Detection
│   └── Real-time Availability
├── 📋 Medical Records
│   ├── Diagnosis History
│   ├── Treatment Records
│   └── Medicine Prescriptions
├── 📊 Dashboard Overview
│   ├── Recent Appointments
│   ├── Personal Information
│   └── Quick Actions
└── 🔄 Real-time Updates
```

---

## 🏗️ Arsitektur Sistem

### **Design Pattern: MVC + DAO**
```
📁 com.example
├── 🎮 ui/                    # Controllers & FXML (View & Controller)
│   ├── AdminController
│   ├── DoctorDashboardController
│   ├── PatientDashboardController
│   └── LoginController (Unified)
├── 🗃️ data/                 # Data Access Layer (DAO)
│   ├── AdminDAO
│   ├── DoctorDAO
│   ├── PatientDAO
│   ├── AppointmentDAO
│   └── DiagnosisDAO
├── 🏗️ model/               # Business Logic (Model)
│   ├── Admin, Doctor, Patient
│   ├── Appointment, Diagnosis
│   └── ds/ (Custom Data Structures)
└── 🔧 services/            # Business Services
    ├── AdminService
    ├── DoctorSessionService
    └── LoginSessionDAO
```

### **Data Flow Architecture**
```
🖥️ UI Layer (JavaFX)
    ↕️
🎯 Controller Layer
    ↕️
⚙️ Service Layer
    ↕️
🗄️ DAO Layer
    ↕️
📄 JSON Storage
```

---

## 🔗 Struktur Data Kustom

### **`CustomeLinkedList<T>`**
```java
// Implementasi linked list generik dengan fitur lengkap
✅ Generic type support
✅ Iterable interface
✅ JSON serialization
✅ Memory efficient
✅ Dynamic sizing

// Operasi yang didukung:
- add(T data)
- remove(T data)
- get(int index)
- contains(T data)
- size() / isEmpty()
- Iterator support
```

### **`CustomeQueue<T>`**
```java
// Queue implementation menggunakan CustomeLinkedList
✅ FIFO operations
✅ Generic support
✅ JSON compatible

// Operasi yang didukung:
- enqueue(T value)
- dequeue()
- peek()
- isEmpty() / size()
```

### **`CustomeBST<T>`**
```java
// Binary Search Tree untuk pencarian efisien
✅ Balanced operations
✅ In-order traversal
✅ Generic comparable support

// Operasi yang didukung:
- insert(T data)
- search(T key)
- inOrderList()
- inOrderTraversal(Consumer<T>)
```

---

## 🚀 Instalasi

### **Prerequisites**
- Java 11 atau lebih tinggi
- Maven 3.6+
- IDE yang mendukung JavaFX (IntelliJ IDEA/Eclipse)

### **Step-by-Step Installation**

1. **Clone Repository**
```bash
git clone <repository-url>
cd hospital
```

2. **Install Dependencies**
```bash
mvn clean install
```

3. **Compile Project**
```bash
mvn compile
```

4. **Run Application**
```bash
mvn exec:java -Dexec.mainClass="com.example.App"
```

### **Alternative Run Methods**
```bash
# Using JavaFX Maven Plugin
mvn javafx:run

# Direct Java execution (after compile)
java -cp target/classes com.example.App
```

---

## 📖 Penggunaan

### **Login Pertama Kali**

1. **Admin Login**
   - Username: `admin`
   - Password: `admin123`
   - Role: Admin/Super Admin

2. **Registrasi Dokter**
   - Dokter mendaftar melalui sistem registrasi
   - Admin approve/reject registrasi
   - Setelah approved, dokter bisa login

3. **Registrasi Pasien**
   - Self-registration tersedia
   - Langsung bisa login setelah registrasi

### **Workflow Umum**

```
👤 Pasien
  ├── 📝 Registrasi Account
  ├── 🗓️ Book Appointment dengan Dokter
  ├── ⏰ Tunggu waktu appointment
  └── 📋 Lihat hasil diagnosis

👨‍⚕️ Dokter  
  ├── 📝 Registrasi → 👨‍💼 Admin Approval
  ├── 🏥 Login ke Dashboard
  ├── 📋 Lihat Queue Appointment
  ├── 🔍 Process Patient
  └── 💊 Input Diagnosis & Medicine

👨‍💼 Admin
  ├── 🔐 Login Dashboard
  ├── ✅ Approve Dokter Baru
  ├── 👥 Monitor System Status
  └── 🗑️ Cleanup Expired Data
```

---

## 📊 Model Data

### **Core Entities**

```java
🏥 Appointment
├── appointmentId: int
├── patientId: int  
├── doctorId: int
├── doctorSpecialty: String
├── time: LocalDateTime
├── patientIllness: String
└── status: String

👤 Patient
├── id: int
├── username: String
├── password: String
├── fullname: String
├── email: String
├── age: int
├── address: String
├── phoneNumber: String
└── illnessHistory: CustomeLinkedList<Diagnosis>

👨‍⚕️ Doctor
├── id: int
├── username: String  
├── password: String
├── fullname: String
├── specialty: String
└── isApproved: boolean

💊 Diagnosis
├── id: int
├── doctorId: int
├── patientId: int
├── patientComplaint: String
├── doctorDiagnosis: String
├── recommendedMedicine: String
├── diagnosisDate: LocalDateTime
└── appointmentId: int
```

### **Data Storage Structure**
```
📁 Hospital System Root
├── 📄 allAppointments.json         # Semua appointment
├── 📄 patients.json                # Data pasien
├── 📄 doctors.json                 # Data dokter  
├── 📄 admins.json                  # Data admin
├── 📄 diagnoses.json               # Riwayat diagnosis
├── 📄 activeDoctorSessions.json    # Session dokter aktif
├── 📄 doctorSessionHistory.json    # History login dokter
└── 📄 pendingDoctorRegistrations.json # Registrasi pending
```

---

## 🎯 Key Features Highlights

### **🔄 Real-time Session Tracking**
- Monitoring dokter online/offline secara real-time
- Auto-cleanup session yang expired
- History login lengkap dengan timestamp

### **⚡ Smart Appointment System**
- Deteksi konflik waktu otomatis
- Priority-based appointment queue
- Auto-remove appointment yang expired (past date + 2 hours)

### **🛡️ Role-based Access Control**
- Admin: Full system access + super admin privileges
- Dokter: Appointment & patient management
- Pasien: Self-service appointment & medical records

### **📱 Modern UI/UX**
- Responsive JavaFX interface
- Icon-based navigation
- Color-coded priority system
- Interactive dashboard cards

### **🔧 Custom Data Structure Benefits**
- Memory efficient operations
- Educational value (no standard collections)
- Full control over data operations
- JSON serialization compatible

---

## 🏆 Technical Achievements

- ✅ **Zero Java Collections** - Semua menggunakan struktur data kustom
- ✅ **Modern JavaFX UI** - Interface yang user-friendly
- ✅ **JSON Persistence** - Data storage yang reliable
- ✅ **Session Management** - Real-time user tracking
- ✅ **Multi-role System** - Comprehensive access control
- ✅ **Time Management** - Smart appointment scheduling
- ✅ **Medical Records** - Complete patient history
- ✅ **Admin Dashboard** - Centralized system management

---

## 📞 Support & Documentation

Untuk bantuan teknis atau pertanyaan tentang sistem:

1. **Code Documentation** - Setiap class memiliki JavaDoc lengkap
2. **Debug Tools** - Built-in debugging utilities
3. **Error Handling** - Comprehensive exception management
4. **Logging** - Console output untuk monitoring

---

## 🛠 Implementasi Fitur yang Telah Dibangun

### **✅ Fitur yang Sudah Diimplementasi**

Berikut adalah daftar lengkap fitur yang telah berhasil diimplementasikan dalam sistem:

#### **👥 Patient Management**
```
1. ✅ Add New Patient
   📍 Location: PatientRegistrationController
   📝 Implementation: Form registrasi lengkap dengan validasi
   🔧 Tech: CustomeLinkedList untuk storage, JSON persistence

2. ✅ Remove Patient by ID  
   📍 Location: AdminController.removePatientById()
   📝 Implementation: Admin dapat menghapus pasien dari sistem
   🔧 Tech: PatientDAO.deletePatient() dengan file update

3. ✅ Search Patient by Name
   📍 Location: AdminController patient search functionality
   📝 Implementation: Search real-time dengan text field
   🔧 Tech: Iterasi CustomeLinkedList dengan string matching

4. ✅ Display All Patients
   📍 Location: AdminController patients table
   📝 Implementation: TableView dengan data dari CustomeLinkedList
   🔧 Tech: ObservableList adapter untuk JavaFX compatibility
```

#### **👨‍⚕️ Doctor Management** 
```
5. ✅ Doctor Login
   📍 Location: LoginController (Unified login system)
   📝 Implementation: Multi-role authentication dengan session tracking
   🔧 Tech: DoctorSessionService untuk session management

6. ✅ Doctor Logout  
   📍 Location: DoctorDashboardController.logout()
   📝 Implementation: Session cleanup dan return ke login
   🔧 Tech: Session termination dengan timestamp logging

7. ✅ View Last Logged-in Doctor
   📍 Location: AdminController login history table
   📝 Implementation: Real-time tracking dokter yang sedang online
   🔧 Tech: DoctorSessionService.getCurrentlyLoggedInDoctors()
```

#### **📅 Appointment System**
```
8. ✅ Schedule Appointment
   📍 Location: PatientAppointmentController
   📝 Implementation: Booking sistem dengan conflict detection
   🔧 Tech: 
   - Doctor selection dengan ListView
   - DatePicker dengan validation (no past dates)
   - Time slot ComboBox dengan available times
   - Real-time conflict checking
   - CustomeLinkedList untuk appointment storage

9. ✅ Process Appointment
   📍 Location: ProcessAppointmentController
   📝 Implementation: Dokter dapat memproses appointment dan diagnosis
   🔧 Tech:
   - Patient detail display
   - Medical history review (CustomeLinkedList<Diagnosis>)
   - Diagnosis creation dengan prescribed medicine
   - Appointment queue management
   - Auto-remove processed appointments

10. ✅ Display Upcoming Appointments
    📍 Location: DoctorDashboardController.refreshAppointmentList()
    📝 Implementation: Priority-based appointment cards dengan time sorting
    🔧 Tech:
    - Custom sorting algorithm (no Java Collections)
    - Time-based priority coloring (URGENT/SOON/SCHEDULED)
    - Real-time queue updates
    - Filter by doctor ID dan valid time slots
```

#### **🔍 Advanced Search & Data Structures**
```
11. ✅ Search Patient by ID (BST)
    📍 Location: PatientSearchTreeManagement.searchPatient()
    📝 Implementation: Binary Search Tree untuk pencarian O(log n)
    🔧 Tech:
    - CustomeBST<Patient> implementation
    - Patient.compareTo() berdasarkan ID
    - Efficient search operations
    - Integration dengan PatientDAO

12. ✅ Display All Patients (BST Inorder)
    📍 Location: PatientSearchTreeManagement.inOrderDisplay()
    📝 Implementation: In-order traversal untuk sorted patient list
    🔧 Tech:
    - CustomeBST.inOrderTraversal() dengan Consumer pattern
    - Sorted by patient ID ascending
    - CustomeLinkedList result collection
    - No Java Collections used
```

### **🏗️ Technical Implementation Details**

#### **Data Structure Implementations**
```java
// CustomeLinkedList<T> - Core storage structure
✅ Generic linked list dengan node-based storage
✅ Implements Iterable<T> untuk for-each loops  
✅ JSON serialization via @JsonValue/@JsonCreator
✅ Operations: add(), remove(), get(), contains(), size()
✅ Memory efficient - no ArrayList/LinkedList dependencies

// CustomeQueue<T> - FIFO operations  
✅ Built on top of CustomeLinkedList
✅ Operations: enqueue(), dequeue(), peek(), isEmpty()
✅ Used for pending doctor registrations
✅ JSON compatible serialization

// CustomeBST<T> - Efficient searching
✅ Binary Search Tree dengan generic support
✅ In-order traversal dengan Consumer callbacks
✅ O(log n) search operations
✅ Used for patient search optimization
```

#### **Session Management System**
```java
// DoctorSessionService - Real-time tracking
✅ Track active doctor sessions
✅ Login/logout timestamp recording  
✅ Auto-cleanup expired sessions
✅ getCurrentlyLoggedInDoctors() untuk admin dashboard
✅ Complete session history logging
```

#### **Appointment Queue System** 
```java
// AppointmentDAO - Smart scheduling
✅ Time conflict detection (doctor & patient)
✅ Valid appointment time checking (business hours)
✅ Auto-cleanup expired appointments (past date + 2 hours)
✅ Priority-based sorting (upcoming appointments first)
✅ processNextAppointment() untuk doctor workflow
```

#### **Multi-Role Authentication**
```java
// LoginController - Unified authentication
✅ Admin/Doctor/Patient dalam satu interface
✅ Role-based redirection setelah login
✅ Session initialization untuk setiap role
✅ Secure password handling
✅ Login attempt logging
```

### **🎨 UI/UX Implementation Highlights**

#### **Modern JavaFX Interface**
```
✅ Custom CSS styling dengan Roboto font family
✅ Icon-based navigation (FontAwesome, Material Design)
✅ Responsive layout dengan GridPane/VBox/HBox
✅ Color-coded priority system (Red/Blue/Green)
✅ Interactive cards dengan hover effects
✅ Real-time status updates
```

#### **Admin Dashboard Features**
```
✅ Tabbed interface untuk different functions
✅ TableView untuk data display (Doctors, Patients, Appointments)
✅ Real-time doctor online status monitoring
✅ Login history table dengan timestamp
✅ Super admin restrictions untuk sensitive operations
✅ Appointment cleanup tools
```

#### **Doctor Dashboard Features**  
```
✅ Appointment queue cards dengan priority indicators
✅ Statistics display (total queue, next appointment)
✅ One-click appointment processing
✅ Patient medical history integration
✅ Diagnosis creation workflow
```

#### **Patient Portal Features**
```
✅ Doctor selection dengan specialty display
✅ Calendar-based appointment booking
✅ Time slot selection dengan availability checking
✅ Medical records history view
✅ Recent appointment tracking
```

### **🔄 Data Flow Implementation**

```
📱 User Action (JavaFX UI)
    ↓
🎮 Controller Layer (Event Handling)
    ↓
⚙️ Service Layer (Business Logic)
    ↓  
🗄️ DAO Layer (Data Access)
    ↓
🔗 Custom Data Structures (Storage)
    ↓
📄 JSON Files (Persistence)
```

**Example Flow: Schedule Appointment**
1. `PatientAppointmentController.onScheduleClicked()`
2. `AppointmentDAO.hasTimeConflict()` - conflict checking
3. `AppointmentDAO.scheduleAppointment()` - add to CustomeLinkedList
4. JSON file update via Jackson serialization
5. UI feedback dengan success message

### **📊 Performance Characteristics**

```
🔍 Search Operations:
├── Patient by ID (BST): O(log n)
├── Linear search dalam CustomeLinkedList: O(n)  
├── Appointment conflict detection: O(n)
└── Doctor session lookup: O(n)

💾 Memory Usage:
├── CustomeLinkedList: O(n) - node-based storage
├── CustomeQueue: O(n) - wrapper around linked list
├── CustomeBST: O(n) - tree node storage
└── No overhead dari Java Collections

⚡ Real-time Updates:
├── Session tracking: Event-driven updates
├── Appointment queue: Automatic refresh
├── UI notifications: Immediate feedback
└── Data persistence: Immediate JSON write
```

---

## 🐛 Bug Fixes & Improvements

### **Critical Bug Fixes**

#### **1. Pending Doctor Registration Decline Issue**
- **Problem**: Menolak pendaftaran dokter tidak menghapus entri dari `pendingDoctorRegistrations.json`
- **Root Cause**: `ClassCastException` saat menyimpan list kosong (Object[] vs PendingDoctorRegistration[])
- **Solution**: Implementasi null-safe handling di `PendingDoctorRegistrationDAO.savePendingRegistrations()`
- **Status**: ✅ **FIXED**

#### **2. NullPointerException in Process Appointment**
- **Problem**: Error `Cannot invoke "Patient.getName()" because "p" is null`
- **Root Cause**: Appointment mereferensikan patient yang tidak ada (data inconsistency)
- **Solution**: 
  - Implementasi null-checking di `ProcessAppointmentController.setAppointment()`
  - Tambah missing patient record untuk menjaga data integrity
  - Graceful handling saat patient tidak ditemukan
- **Status**: ✅ **FIXED**

### **New Features & Improvements**

#### **Data Integrity Validation**
```java
// New utility class for data validation
DataIntegrityValidator validator = new DataIntegrityValidator();
validator.runFullIntegrityCheck(); // Validates all data relationships
```

**Features:**
- ✅ Validasi appointment-patient references
- ✅ Deteksi orphaned records
- ✅ Automatic data consistency reporting
- ✅ Easy integration untuk development/debugging

#### **Enhanced Error Handling**
- **Null-safe operations** di semua DAO classes
- **Graceful degradation** saat data tidak ditemukan
- **User-friendly error messages** di UI
- **Console logging** untuk debugging

#### **Robustness Improvements**
- **Empty list handling** di custom data structures
- **Type safety** untuk JSON serialization
- **Memory leak prevention** di UI controllers
- **Session management** yang lebih robust

---

### **Development Notes**

#### **Testing Strategy**
```bash
# Run application tests
mvn clean compile javafx:run

# Test specific functionality
mvn compile exec:java -Dexec.mainClass="com.example.util.DataIntegrityValidator"
```

#### **Known Limitations**
- File-based storage (untuk skalabilitas besar perlu database)
- Single-user concurrent access (tidak ada locking mechanism)
- UI responsiveness bergantung pada ukuran data

#### **Future Enhancements**
- [ ] Database integration (PostgreSQL/MySQL)
- [ ] Multi-user session management
- [ ] Advanced reporting dan analytics
- [ ] Mobile app integration
- [ ] Email notifications
- [ ] Backup dan restore functionality

---

## 👥 Tim Pengembang

<div align="center">

### **Contributors**

| Nama | NIM |
|------|-----|
| **Trisa Dwi Rahmawati** | L0124079 | 
| **Gibran Maulana Azmi** | L0124100 | 
| **Muhammad Azzam Alfarozy** | L0124106 | 
| **Muhammad Raditya Boy W.** | L0124109 |

</div>

---
### **Our team Contribution**

**Trisa Dwi Rahmawati**
- patient management service
- admin model init,
- doctor pending registration logic
- admin level logic
- user abstrak

**Gibran Maulan Azmi** 
- setup maven project
- Data services logic dan DAO, UI fxml dan UI controller

**Muhammad Azzam Alfarozy**
- appointment model init
- custom Queue data structure
- diagnosis model
- login session model
- patient model

**Muhammad Raditya Boy**
- Custom LinkedList for data structure
- doctor model init 
- doctor session model
- pending doctor registration model

---

### **Acknowledgments**

Tim ini berkolaborasi untuk mengembangkan sistem manajemen rumah sakit yang komprehensif dengan implementasi struktur data kustom. Setiap anggota tim memberikan kontribusi unik dalam berbagai aspek pengembangan, mulai dari desain UI yang intuitif hingga implementasi algoritma yang efisien.

**Special Thanks:**
- Dosen pembimbing untuk guidance dan feedback
- Komunitas JavaFX untuk resources dan dokumentasi
- Open source libraries yang mendukung pengembangan

<div align="center">

**🏥 Sistem Manajemen Rumah Sakit** 

*Dikembangkan dengan ❤️ menggunakan Java & JavaFX*

---

*© 2025 - Hospital Management System. Built with Custom Data Structures.*

</div>