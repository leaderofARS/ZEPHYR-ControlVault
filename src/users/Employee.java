package users;

import system.AccessZone;

public class Employee extends User {

    public Employee(String userId, String username, String password) {
        super(userId, username, password, "EMPLOYEE");
    }

    @Override
    public void accessArea(AccessZone zone) {
        // No printing here
    }

    // Attendance hooks
    public String checkIn() {
        return username + " checked in";
    }

    public String checkOut() {
        return username + " checked out";
    }
}
