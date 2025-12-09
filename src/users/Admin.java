package users;

public class Admin extends User {

    // Constructor
    public Admin(String userId, String username, String password) {
        super(userId, username, password, "ADMIN");
    }

    // Polymorphic Access Behavior
    // Phase 1: Only structural (no actual logic yet)
    @Override
    public void accessArea(String zone) {

        System.out.println("ADMIN " + username + " is attempting to access: " + zone);
    }

    // Optional: Admin-specific method placeholders
    public void registerNewUser() {
        // Placeholder for Admin-only functionality.
    }

    public void viewSystemLogs() {
        // Placeholder for Admin-only functionality.

    }
}