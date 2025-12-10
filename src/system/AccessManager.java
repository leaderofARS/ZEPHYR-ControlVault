package system;

import users.*;
import exceptions.AccessDeniedException;
import exceptions.VisitorExpiredException;

public class AccessManager {

    public AccessManager() {}


    public boolean hasAccess(User user, AccessZone zone)
            throws VisitorExpiredException, AccessDeniedException {

        String role = user.getRole();

        switch (role) {

            case "ADMIN":
                // Admin → full access
                return true;

            case "EMPLOYEE":
                return checkEmployeeAccess(zone);

            case "VISITOR":
                return checkVisitorAccess((Visitor) user, zone);

            default:
                throw new AccessDeniedException("Unknown user role: " + role);
        }
    }

    // EMPLOYEE ACCESS RULES
    private boolean checkEmployeeAccess(AccessZone zone) throws AccessDeniedException {

        switch (zone) {
            case LAB:
            case OFFICE_FLOOR:
            case LOBBY:
                return true;

            case SERVER_ROOM:
                throw new AccessDeniedException("Employee cannot access the Server Room.");

            default:
                throw new AccessDeniedException("Unknown zone.");
        }
    }

    // VISITOR ACCESS RULES + BADGE EXPIRY LOGIC
    private boolean checkVisitorAccess(Visitor visitor, AccessZone zone)
            throws VisitorExpiredException, AccessDeniedException {

        // Check badge first
        if (visitor.getBadgeValidity() <= 0) {
            throw new VisitorExpiredException("Visitor badge expired.");
        }

        // Visitors can ONLY access lobby
        if (zone == AccessZone.LOBBY) {
            // Decrement badge validity on successful access
            visitor.setBadgeValidity(visitor.getBadgeValidity() - 1);
            return true;
        }

        throw new AccessDeniedException("Visitor cannot access zone: " + zone);
    }

    // PROCESS ACCESS (connects RBAC + polymorphism)
    public void processAccess(User user, AccessZone zone) {

        try {
            boolean allowed = hasAccess(user, zone);

            if (allowed) {
                user.accessArea(zone.toString());  // Polymorphism in action
            }

        } catch (VisitorExpiredException e) {
            System.out.println("ACCESS BLOCKED (Visitor Expired): " + e.getMessage());

        } catch (AccessDeniedException e) {
            System.out.println("ACCESS DENIED: " + e.getMessage());
        }
    }
}
