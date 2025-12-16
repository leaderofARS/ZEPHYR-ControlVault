package system;

import users.*;
import exceptions.AccessDeniedException;
import exceptions.VisitorExpiredException;

public class AccessManager {

    public AccessManager() {}

    // Core RBAC check
    public boolean hasAccess(User user, AccessZone zone)
            throws VisitorExpiredException, AccessDeniedException {

        switch (user.getRole()) {

            case "ADMIN":
                return true;

            case "EMPLOYEE":
                return checkEmployeeAccess(zone);

            case "VISITOR":
                return checkVisitorAccess((Visitor) user, zone);

            default:
                throw new AccessDeniedException("Unknown user role: " + user.getRole());
        }
    }

    // Employee rules
    private boolean checkEmployeeAccess(AccessZone zone)
            throws AccessDeniedException {

        switch (zone) {
            case LAB:
            case OFFICE_FLOOR:
            case LOBBY:
                return true;

            case SERVER_ROOM:
                throw new AccessDeniedException("Employee cannot access Server Room");

            default:
                throw new AccessDeniedException("Invalid access zone");
        }
    }

    // Visitor rules + badge handling
    private boolean checkVisitorAccess(Visitor visitor, AccessZone zone)
            throws VisitorExpiredException, AccessDeniedException {

        if (visitor.isBadgeExpired()) {
            throw new VisitorExpiredException("Visitor badge expired");
        }

        if (zone == AccessZone.LOBBY) {
            visitor.decrementBadgeValidity();
            return true;
        }

        throw new AccessDeniedException("Visitor cannot access zone: " + zone);
    }

    // Access execution
    public void processAccess(User user, AccessZone zone)
            throws VisitorExpiredException, AccessDeniedException {

        if (hasAccess(user, zone)) {
            user.accessArea(zone);
        }
    }
}
