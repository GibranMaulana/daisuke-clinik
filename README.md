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

<div align="center">

**🏥 Sistem Manajemen Rumah Sakit** 

*Dikembangkan dengan ❤️ menggunakan Java & JavaFX*

---

*© 2025 - Hospital Management System. Built with Custom Data Structures.*

</div>
