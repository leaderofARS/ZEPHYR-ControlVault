package users;

import system.AccessZone;

public class Admin extends User {

    public Admin(String userId, String username, String password) {
        super(userId, username, password, "ADMIN");
    }

    @Override
    public void accessArea(AccessZone zone) {
        // No printing here
        // Action description only
    }
}
