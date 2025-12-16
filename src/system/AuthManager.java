package system;

import system.FileHandler;
import users.*;
import exceptions.InvalidCredentialsException;

import java.util.HashMap;
import java.util.List;

public class AuthManager {

    private final FileHandler fileHandler;
    private final HashMap<String, User> userMap;
    private final String userFilePath;

    public AuthManager(String userFilePath) {
        this.fileHandler = new FileHandler();
        this.userMap = new HashMap<>();
        this.userFilePath = userFilePath;
    }
    // Load users using FileHandler

    public void loadUsersFromFile() {
        List<String> lines = fileHandler.readFile(userFilePath);

        for (String line : lines) {
            String[] parts = line.split(",");

            String userId = parts[0].trim();
            String username = parts[1].trim();
            String password = parts[2].trim();
            String role = parts[3].trim();

            // FORMAT:
            // ADMIN:    ID,username,password,ADMIN
            // EMPLOYEE: ID,username,password,EMPLOYEE
            // VISITOR:  ID,username,password,VISITOR,badge

            switch (role.toUpperCase()) {
                case "ADMIN":
                    userMap.put(username, new Admin(userId, username, password));
                    break;

                case "EMPLOYEE":
                    userMap.put(username, new Employee(userId, username, password));
                    break;

                case "VISITOR":
                    int badge = Integer.parseInt(parts[4].trim());
                    userMap.put(username, new Visitor(userId, username, password, badge));
                    break;

                default:
                    System.out.println("Invalid role in users file: " + role);
            }
        }
    }

    // Authentication
    public User login(String username, String password)
            throws InvalidCredentialsException {

        if (!userMap.containsKey(username)) {
            throw new InvalidCredentialsException("User not found");
        }

        User user = userMap.get(username);

        if (!user.getPassword().equals(password)) {
            throw new InvalidCredentialsException("Incorrect password");
        }

        return user;
    }

    // Admin-only registration
    public void registerUser(User user) {
        userMap.put(user.getUsername(), user);
        fileHandler.appendToFile(userFilePath, serializeUser(user));
    }

    // Helper
    public User getUser(String username) {
        return userMap.get(username);
    }

    private String serializeUser(User user) {
        if (user instanceof Visitor) {
            Visitor v = (Visitor) user;
            return String.join(",",
                    v.getUserId(),
                    v.getUsername(),
                    v.getPassword(),
                    "VISITOR",
                    String.valueOf(v.getBadgeValidity())
            );
        }

        return String.join(",",
                user.getUserId(),
                user.getUsername(),
                user.getPassword(),
                user.getRole()
        );
    }
}
