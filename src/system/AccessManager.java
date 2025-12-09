package system;

import users.User;
import exceptions.AccessDeniedException;
import exceptions.VisitorExpiredException;

public class AccessManager {

    // constructor
    public AccessManager() {}

    // Method Signatures Only

    // Checks if a user can access a specific zone
    public boolean hasAccess(User user, AccessZone zone)
            throws VisitorExpiredException, AccessDeniedException {
        // TODO: implementation
        return false;
    }

    // Calls the user's polymorphic access method
    public void processAccess(User user, AccessZone zone) {
        // TODO: implementation
    }

    // Visitor badge decrement logic
    private void handleVisitorBadge(User user) {
        // TODO: implementation
    }
}
