package system;

import users.*;
import exceptions.InvalidCredentialsException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class AuthManager {
    private FileHandler fileHandler = new FileHandler();
    private HashMap<String, User> userMap;

    public AuthManager() {
        this.userMap = new HashMap<>();
    }


    public void loadUsersFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                // FORMAT:
                // ADMIN:    ID,username,password,ADMIN
                // EMPLOYEE: ID,username,password,EMPLOYEE
                // VISITOR:  ID,username,password,VISITOR,badge

                String userId = parts[0].trim();
                String username = parts[1].trim();
                String password = parts[2].trim();
                String role = parts[3].trim();

                switch (role.toUpperCase()) {
                    case "ADMIN":
                        userMap.put(username, new Admin(userId, username, password));
                        break;

                    case "EMPLOYEE":
                        userMap.put(username, new Employee(userId, username, password));
                        break;

                    case "VISITOR":
                        int badgeValidity = Integer.parseInt(parts[4].trim());
                        userMap.put(username, new Visitor(userId, username, password, badgeValidity));
                        break;

                    default:
                        System.out.println("Unknown role detected in file: " + role);
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }


    public User login(String username, String password) throws InvalidCredentialsException {

        if (!userMap.containsKey(username)) {
            throw new InvalidCredentialsException("User not found.");
        }

        User user = userMap.get(username);

        if (!user.getPassword().equals(password)) {
            throw new InvalidCredentialsException("Incorrect password.");
        }

        return user;
    }

    // Optional (for admin: create new user)
    public void registerUser(User user) {
        userMap.put(user.getUsername(), user);
        // Member B will handle persistence writing
        fileHandler.appendToFile("data/users.txt", user.serialize());

        // 3. Optional: Confirmation
        System.out.println("User '" + user.getUsername() + "' registered successfully and saved.");
    }
    }

    public User getUser(String username) {
        return userMap.get(username);
    }
}
