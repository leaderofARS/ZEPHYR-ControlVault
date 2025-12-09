package system;

import users.User;
import exceptions.InvalidCredentialsException;
import java.util.HashMap;

public class AuthManager {

    // User store
    private HashMap<String, User> userMap;

    public AuthManager() {
        this.userMap = new HashMap<>();
    }


    // Loads all users from users.txt
    public void loadUsersFromFile(String filePath) {
        // TODO: implementation
    }

    // Validates login credentials & returns the User object
    public User login(String username, String password) throws InvalidCredentialsException {
        // TODO: implementation
        return null;
    }

    // Creates and saves a new user
    public void registerUser(User user) {
        // TODO: implementation
    }

    // Helper to fetch a user by username
    public User getUser(String username) {
        // TODO: implementation
        return null;
    }
}
