# Hospital Management System

A comprehensive JavaFX-based hospital management system implementing custom data structures for efficient patient, doctor, and appointment management.

## 🏥 Overview

This Hospital Management System is a desktop application built with JavaFX that provides comprehensive management capabilities for hospital operations. The system implements custom data structures including linked lists, binary search trees, and queues to demonstrate data structure concepts while providing real-world functionality.

## ✨ Key Features

### 👥 Multi-User System
- **Patients**: Registration, appointment booking, medical records access
- **Doctors**: Dashboard, appointment processing, patient directory
- **Administrators**: System-wide management, user oversight, pending registrations

### 🏗️ Custom Data Structures
- **CustomeLinkedList**: Dynamic list implementation for data storage
- **CustomeBST**: Binary Search Tree for efficient patient searches
- **CustomeQueue**: Queue implementation for pending doctor registrations

### 💾 Data Persistence
- JSON-based data storage for all entities
- Automatic session management for doctors
- Real-time data synchronization

### 🎨 Modern UI
- Material Design inspired interface
- FontAwesome and Material Design icons
- Responsive layouts with custom styling
- Professional color schemes and typography

## 📋 System Requirements

- **Java**: JDK 11 or higher
- **JavaFX**: 17.0.2
- **Maven**: 3.6 or higher
- **Operating System**: Windows, macOS, or Linux

## 🚀 Quick Start

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd hospital
   ```

2. **Build the project**
   ```bash
   mvn clean compile
   ```

3. **Run the application**
   ```bash
   mvn javafx:run
   ```

### Default Login Credentials

**Admin Access:**
- Username: `admin`
- Password: `admin123`

**Test Doctor:**
- ID: `9814`
- Password: `password123`

**Test Patient:**
- Username: `gibran_azmi`
- Password: `password123`

## 🏗️ Architecture

```
hospital/
├── src/main/java/
│   ├── com/example/
│   │   ├── App.java                    # Main application entry
│   │   ├── data/                       # Data access layer
│   │   ├── model/                      # Data models
│   │   │   └── ds/                     # Custom data structures
│   │   └── ui/                         # User interface controllers
│   └── module-info.java               # Java module configuration
├── src/main/resources/
│   ├── com/example/ui/                 # FXML view files
│   └── fonts/                          # Custom fonts
├── JSON Data Files/
│   ├── patients.json                   # Patient records
│   ├── doctors.json                    # Doctor information
│   ├── allAppointments.json           # Appointment data
│   ├── loginSessions.json             # Active doctor sessions
│   ├── admins.json                     # Administrator accounts
│   └── pendingDoctorRegistrations.json # Pending registrations
└── pom.xml                            # Maven configuration
```

## 🔧 Core Components

### Data Models
- **User**: Base class for all user types
- **Patient**: Extends User with medical information
- **Doctor**: Extends User with specialty and login tracking
- **Admin**: System administrator with elevated privileges
- **Appointment**: Appointment scheduling and management
- **PendingDoctorRegistration**: Queue-based registration system

### Custom Data Structures
- **CustomeLinkedList<T>**: Generic linked list with JSON serialization
- **CustomeBST<T>**: Binary search tree for efficient searching
- **CustomeQueue<T>**: Queue implementation for FIFO operations

### User Interfaces
- **Login System**: Role-based authentication
- **Patient Portal**: Appointment booking, medical records
- **Doctor Dashboard**: Appointment processing, patient management
- **Admin Panel**: System oversight, user management

## 📊 Data Management

### JSON Persistence
All data is stored in JSON format for easy maintenance and debugging:

```json
// patients.json example
[
  {
    "id": 4101360,
    "username": "gibran_azmi",
    "fullname": "gibran maulana azmi",
    "email": "gibran@gmail.com",
    "age": 21,
    "address": "Jakarta",
    "phoneNumber": "555-1234",
    "illnessHistory": ["flu", "headache"]
  }
]
```

### Data Structures Usage
- **Patient Search**: BST for O(log n) patient lookups by ID
- **Appointment Queue**: LinkedList for doctor-specific appointment queues
- **Registration Queue**: Queue for pending doctor registrations
- **Session Management**: LinkedList for active doctor sessions

## 🎯 User Workflows

### Patient Journey
1. **Registration**: Create account with personal information
2. **Login**: Access patient portal
3. **Book Appointment**: Select available doctor and time slot
4. **View Records**: Access medical history and appointments

### Doctor Journey
1. **Login**: Access doctor dashboard
2. **View Queue**: See scheduled appointments
3. **Process Appointments**: Handle patient consultations
4. **Patient Directory**: Search and view patient information

### Admin Journey
1. **System Overview**: Monitor all system activities
2. **User Management**: Manage patients and doctors
3. **Registration Approval**: Process pending doctor registrations
4. **System Monitoring**: View statistics and logs

## 🛠️ Technical Features

### Session Management
- Automatic session cleanup for doctors
- 24-hour session expiration
- Real-time login status tracking

### Data Validation
- ID format validation (7-digit IDs)
- Required field validation
- Email format checking

### Search Capabilities
- BST-based patient search by ID
- Prefix-based patient name search
- Real-time search filtering

### Error Handling
- Comprehensive exception handling
- User-friendly error messages
- Graceful degradation

## 🎨 UI/UX Features

### Design System
- **Colors**: Professional blue/gray color palette
- **Typography**: Roboto font family
- **Icons**: FontAwesome and Material Design icons
- **Layout**: Responsive grid-based layouts

### Interactive Elements
- Hover effects on buttons
- Status indicators
- Progress feedback
- Modal dialogs for confirmations

### Accessibility
- Keyboard navigation support
- Clear visual hierarchy
- Consistent interaction patterns
- Error state indicators

## 📈 Performance

### Optimization Features
- Lazy loading of data
- Efficient BST operations
- Minimal UI updates
- Smart data caching

### Memory Management
- Proper resource cleanup
- Efficient data structures
- Minimal object creation

## 🧪 Testing

The project includes comprehensive testing for:
- Data structure operations
- User authentication
- Appointment scheduling
- Admin operations

Run tests with:
```bash
mvn test
```

## 📚 Documentation

Additional documentation available:
- [API Documentation](API.md) - Detailed API reference
- [User Guide](USER_GUIDE.md) - Complete user manual
- [Developer Guide](DEVELOPER_GUIDE.md) - Development setup and guidelines
- [Data Structures](DATA_STRUCTURES.md) - Custom implementations guide

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👨‍💻 Authors

- **Development Team** - Initial work and implementation
- **Contributors** - See CONTRIBUTORS.md for the list of contributors

## 🙏 Acknowledgments

- JavaFX community for excellent documentation
- Jackson library for JSON processing
- Ikonli project for icon support
- BootstrapFX for styling components

## 📞 Support

For support and questions:
- Check the documentation first
- Open an issue on GitHub
- Contact the development team

---

**Hospital Management System** - Efficient healthcare management through modern software engineering principles.
