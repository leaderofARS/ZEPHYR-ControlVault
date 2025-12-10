package users;

public class Visitor extends User {

    private int badgeValidity; // remaining allowed accesses

    public Visitor(String userId, String username, String password, int badgeValidity) {
        super(userId, username, password, "VISITOR");
        this.badgeValidity = badgeValidity;
    }

    // Getter & Setter
    public int getBadgeValidity() {
        return badgeValidity;
    }

    public void setBadgeValidity(int badgeValidity) {
        this.badgeValidity = badgeValidity;
    }


    public void decrementBadgeValidity() {
        if (badgeValidity > 0) {
            badgeValidity--;
        }
    }

    public boolean isBadgeExpired() {
        return badgeValidity <= 0;
    }

    // now production-ready)

    @Override
    public void accessArea(String zone) {
        System.out.println("VISITOR " + username + " accessed: " + zone +
                " | Remaining badge validity: " + badgeValidity);
    }

    @Override
    public String toString() {
        return "[VISITOR] " + username +
                " (ID: " + userId + ", Badge: " + badgeValidity + ")";
    }
}
