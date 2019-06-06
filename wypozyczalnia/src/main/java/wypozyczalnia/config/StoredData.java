package wypozyczalnia.config;


public class StoredData {
    private static long loggedUserId;

    public static long getLoggedUserId() {
        return loggedUserId;
    }

    public static void setLoggedUserId(long loggedUserId) {
        StoredData.loggedUserId = loggedUserId;
    }
}
