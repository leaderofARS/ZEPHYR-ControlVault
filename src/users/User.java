package users;

public abstract class User {

    // Fields
    protected String userId;
    protected String username;
    protected String password;   // Will be validated by AuthManager
    protected String role;       // ADMIN, EMPLOYEE, VISITOR

    // Constructor
    public User(String userId, String username, String password, String role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // -----------------------------
    // Getters & Setters
    // -----------------------------
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Abstract Method
    // Must be overridden by Admin, Employee, Visitor
    public abstract void accessArea(String zone);

    //toString() — helps with logs/debugging
    @Override
    public String toString() {
        return "[" + role + "] " + username + " (ID: " + userId + ")";
    }
}
