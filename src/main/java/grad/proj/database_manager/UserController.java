package grad.proj.database_manager;


import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by mohammad on 22/06/17.
 */
public class UserController {
    private static String currentUser;

    public static String getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(String currentUser) {
        UserController.currentUser = currentUser;
    }

    // Returns null if not existing.
    private static String getPassword(String username) throws SQLException {
        ResultSet resultSet = DatabaseManager.executeQuery("SELECT * FROM " + DatabaseManager
                .USERS_TABLE + " WHERE username = '" + username + "'");
        if (!resultSet.next()) return null;
        return resultSet.getString("password");
    }

    private static boolean checkCredentials(String username, String password) throws SQLException {
        return password.equals(getPassword(username));
    }

    // Returns true on success, false otherwise.
    public static boolean login(String username, String password) throws SQLException {
        System.out.println("Logging in " + username + "," + password + "...");
        if (!checkCredentials(username, password)) return false;
        currentUser = username;
        return true;
    }

    // Returns true on success, false otherwise.
    public static boolean signup(String username, String password) throws SQLException {
        System.out.println("Signing up " + username + "," + password + "...");
        if (getPassword(username) != null) return false;


        DatabaseManager.executeInsert(DatabaseManager.USERS_TABLE, new String[]{username,
                password});
        login(username, password);
        return true;
    }

    public static boolean createEnvironment(String environment_name, String[] items) {
        return true;
    }

    public static boolean removeEnvironment(String environment_name) {
        return true;
    }

    public static boolean removeItemFromEnvironment(String environment_name, String item) {
        return true;
    }

    public static boolean addItemFromEnvironment(String environment_name, String item) {
        return true;
    }


}
