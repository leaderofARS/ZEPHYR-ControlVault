package users;

public class Visitor extends User {

    // Visitor-specific fields
    private int badgeValidity; 
    // This represents how many times the visitor can access zones
    // Actual decrement and expiry logic comes in later

    // Constructor
    public Visitor(String userId, String username, String password, int badgeValidity) {
        super(userId, username, password, "VISITOR");
        this.badgeValidity = badgeValidity;
    }

    // Getters & Setters
    public int getBadgeValidity() {
        return badgeValidity;
    }

    public void setBadgeValidity(int badgeValidity) {
        this.badgeValidity = badgeValidity;
    }

    // Polymorphic Access Behavior
    // Only placeholder (logic comes in Phase 2)
    @Override
    public void accessArea(String zone) {
        // No actual access control yet
        System.out.println("VISITOR " + username + " is attempting to access: " + zone);
    }

    // Placeholder for future logic
    public void decrementBadgeValidity() {
    }

    public boolean isBadgeExpired() {
        return false;
    }
}
