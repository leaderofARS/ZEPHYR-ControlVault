package users;

public class Employee extends User {

    // Constructor
    public Employee(String userId, String username, String password) {
        super(userId, username, password, "EMPLOYEE");
    }

    // Polymorphic Access Behavior
    // structural (no real logic yet)
    @Override
    public void accessArea(String zone) {
        // placeholder.
        System.out.println("EMPLOYEE " + username + " is attempting to access: " + zone);
    }

    // Employee-specific placeholders
    public void checkIn() {
        // Placeholder for attendance
    }

    public void checkOut() {
        // Placeholder for attendance
    }
}
