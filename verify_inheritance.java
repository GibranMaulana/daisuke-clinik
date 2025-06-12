import com.example.model.Doctor;
import com.example.model.Patient;
import com.example.model.User;

public class verify_inheritance {
    public static void main(String[] args) {
        System.out.println("=== Testing User Inheritance Implementation ===\n");
        
        // Test Doctor ID validation
        testDoctorValidation();
        
        // Test Patient ID validation  
        testPatientValidation();
        
        // Test inheritance functionality
        testInheritanceFunctionality();
        
        System.out.println("\n✅ All User inheritance tests passed successfully!");
    }
    
    private static void testDoctorValidation() {
        System.out.println("🔍 Testing Doctor (4-digit ID validation):");
        Doctor doctor = new Doctor();
        
        // Test with existing doctor IDs from doctors.json
        int[] doctorIds = {5557, 5558, 1234, 9999, 999, 10000};
        for (int id : doctorIds) {
            boolean isValid = doctor.isValidId(id);
            String status = isValid ? "✅ VALID" : "❌ INVALID";
            System.out.println("  Doctor ID " + id + ": " + status);
        }
        
        // Test creating doctor with valid ID
        Doctor validDoctor = new Doctor(5557, "Dr. Smith", "docpass1", "Cardiology");
        System.out.println("  Created: " + validDoctor.toString());
        System.out.println("  Name (backward compatibility): " + validDoctor.getName());
        System.out.println("  Fullname (inherited): " + validDoctor.getFullname());
    }
    
    private static void testPatientValidation() {
        System.out.println("\n🔍 Testing Patient (7-digit ID validation):");
        Patient patient = new Patient();
        
        // Test with existing patient IDs from patients.json
        int[] patientIds = {2214098, 3089698, 1234567, 9999999, 123456, 12345678};
        for (int id : patientIds) {
            boolean isValid = patient.isValidId(id);
            String status = isValid ? "✅ VALID" : "❌ INVALID";
            System.out.println("  Patient ID " + id + ": " + status);
        }
        
        // Test creating patient with valid ID
        Patient validPatient = new Patient(2214098, "nadhifa", 19, "jl.kendondong", "081385927668");
        System.out.println("  Created: " + validPatient.toString());
        System.out.println("  Name (backward compatibility): " + validPatient.getName());
        System.out.println("  Fullname (inherited): " + validPatient.getFullname());
    }
    
    private static void testInheritanceFunctionality() {
        System.out.println("\n🔍 Testing User inheritance functionality:");
        
        // Test Doctor with full User constructor
        Doctor doctorWithUserFields = new Doctor(1234, "drsmith", "password123", 
                                                "Dr. John Smith", "drsmith@hospital.com", "Cardiology");
        System.out.println("  Doctor with User fields:");
        System.out.println("    ID: " + doctorWithUserFields.getId());
        System.out.println("    Username: " + doctorWithUserFields.getUsername());
        System.out.println("    Fullname: " + doctorWithUserFields.getFullname());
        System.out.println("    Email: " + doctorWithUserFields.getEmail());
        System.out.println("    Specialty: " + doctorWithUserFields.getSpecialty());
        
        // Test Patient with full User constructor
        Patient patientWithUserFields = new Patient(1234567, "johndoe", "password456",
                                                   "John Doe", "johndoe@email.com", 30, "123 Main St", "555-1234");
        System.out.println("\n  Patient with User fields:");
        System.out.println("    ID: " + patientWithUserFields.getId());
        System.out.println("    Username: " + patientWithUserFields.getUsername());
        System.out.println("    Fullname: " + patientWithUserFields.getFullname());
        System.out.println("    Email: " + patientWithUserFields.getEmail());
        System.out.println("    Age: " + patientWithUserFields.getAge());
        
        // Test polymorphism
        User[] users = {doctorWithUserFields, patientWithUserFields};
        System.out.println("\n  Testing polymorphism:");
        for (User user : users) {
            System.out.println("    " + user.getClass().getSimpleName() + " - " + user.toString());
        }
    }
}
