package wypozyczalnia.config;


public class StoredData {
    private static long loggedUserId;
    private static boolean leader;
    private static boolean admin;

    public static long getLoggedUserId() {
        return loggedUserId;
    }

    public static void setLoggedUserId(long loggedUserId) {
        StoredData.loggedUserId = loggedUserId;
    }

    public static boolean isLeader() {
        return leader;
    }

    public static void setLeader(boolean leader) {
        StoredData.leader = leader;
    }

    public static boolean isAdmin() {
        return admin;
    }

    public static void setAdmin(boolean admin) {
        StoredData.admin = admin;
    }
}
