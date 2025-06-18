import com.example.data.PendingDoctorRegistrationDAO;
import com.example.model.PendingDoctorRegistration;

public class TestDeclineRegistration {
    public static void main(String[] args) {
        PendingDoctorRegistrationDAO dao = new PendingDoctorRegistrationDAO();
        
        System.out.println("=== Testing Decline Registration ===");
        
        // Load current pending registrations
        System.out.println("Loading pending registrations...");
        var pendingRegistrations = dao.getAllPendingRegistrations();
        
        System.out.println("Found " + pendingRegistrations.size() + " pending registrations:");
        for (int i = 0; i < pendingRegistrations.size(); i++) {
            PendingDoctorRegistration reg = pendingRegistrations.get(i);
            System.out.println("Registration " + i + ": Doctor ID=" + reg.getDoctorId() + 
                             ", Name=" + reg.getDoctorName() + 
                             ", Status=" + reg.getStatus());
        }
        
        if (pendingRegistrations.size() > 0) {
            PendingDoctorRegistration firstReg = pendingRegistrations.get(0);
            String doctorId = firstReg.getDoctorId();
            
            System.out.println("\nAttempting to remove registration with Doctor ID: " + doctorId);
            
            // Test removal
            boolean removed = dao.removePendingRegistration(doctorId);
            System.out.println("Removal result: " + removed);
            
            // Reload and check
            System.out.println("\nReloading pending registrations after removal...");
            var afterRemoval = dao.getAllPendingRegistrations();
            System.out.println("Found " + afterRemoval.size() + " pending registrations after removal:");
            for (int i = 0; i < afterRemoval.size(); i++) {
                PendingDoctorRegistration reg = afterRemoval.get(i);
                System.out.println("Registration " + i + ": Doctor ID=" + reg.getDoctorId() + 
                                 ", Name=" + reg.getDoctorName() + 
                                 ", Status=" + reg.getStatus());
            }
        } else {
            System.out.println("No pending registrations found to test with.");
        }
    }
}
