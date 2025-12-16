package users;

import system.AccessZone;

public class Visitor extends User {

    private int badgeValidity;

    public Visitor(String userId, String username, String password, int badgeValidity) {
        super(userId, username, password, "VISITOR");
        this.badgeValidity = badgeValidity;
    }

    public int getBadgeValidity() {
        return badgeValidity;
    }

    public void decrementBadgeValidity() {
        if (badgeValidity > 0) badgeValidity--;
    }

    public boolean isBadgeExpired() {
        return badgeValidity <= 0;
    }

    @Override
    public void accessArea(AccessZone zone) {
        // No printing here
    }

    @Override
    public String serialize() {
        return String.join(",",
                userId,
                username,
                password,
                role,
                String.valueOf(badgeValidity)
        );
    }

    @Override
    public String toString() {
        return "[VISITOR] " + username +
                " (ID: " + userId + ", Badge: " + badgeValidity + ")";
    }
}
