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
        return userId + "," + username + ",CHECK_IN";
    }

    public String checkOut() {
        return userId + "," + username + ",CHECK_OUT";
    }

}
