package system;

import users.*;
import exceptions.AccessDeniedException;
import exceptions.VisitorExpiredException;

public class AccessManager {

    private FileHandler fileHandler = new FileHandler();
    private static final String ZONES_FILE = "data/zones.txt";

    public AccessManager() {
    }

    // Helper to check lock status from file
    private boolean isZoneLocked(AccessZone zone) {
        java.util.List<String> lines = fileHandler.readFile(ZONES_FILE);
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 2 && parts[0].equals(zone.name())) {
                return "LOCKED".equals(parts[1].trim());
            }
        }
        return false;
    }

    // Core RBAC check
    public boolean hasAccess(User user, AccessZone zone)
            throws VisitorExpiredException, AccessDeniedException {

        // Rule 1: Admin Master Override (Bypasses Locks)
        if (user.getRole().equals("ADMIN")) {
            return true;
        }

        // Rule 2: Locked Zone Check
        if (isZoneLocked(zone)) {
            throw new AccessDeniedException("Access Denied: Zone " + zone + " is currently LOCKED");
        }

        switch (user.getRole()) {
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
