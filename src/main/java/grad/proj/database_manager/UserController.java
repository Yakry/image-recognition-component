package grad.proj.database_manager;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mohammad on 22/06/17.
 */
public class UserController {
    private static String currentUser;
    private static String currentEnvironment;
    private static Set<String> concernedItems = new HashSet<>();

    public static Set<String> getConcernedItems() {
        return concernedItems;
    }

    public static String getCurrentEnvironment() {
        return currentEnvironment;
    }

    public static void setCurrentEnvironment(String currentEnvironment)
            throws SQLException {
        UserController.currentEnvironment = currentEnvironment;
        updateConcernedItems();
    }

    private static void updateConcernedItems() throws SQLException {
        ResultSet resultSet = DatabaseManager.executeQuery("SELECT item_name " +
                "FROM " + "" + DatabaseManager.ENVIRONMENTS_ITEMS_TABLE + " " +
                "WHERE " +
                "circle_name = '" + currentEnvironment + "' && username = '"
                + currentUser + "'");

        concernedItems.clear();

        while (resultSet.next()) {
            concernedItems.add(resultSet.getString("item_name"));
        }
    }

    public static String getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(String currentUser) {
        UserController.currentUser = currentUser;
    }

    // Returns null if not existing.
    private static String getPassword(String username) throws SQLException {
        ResultSet resultSet = DatabaseManager.executeQuery("SELECT * FROM " +
                DatabaseManager
                        .USERS_TABLE + " WHERE username = '" + username + "'");
        if (!resultSet.next()) return null;
        return resultSet.getString("password");
    }

    private static boolean checkCredentials(String username, String password)
            throws SQLException {
        return password.equals(getPassword(username));
    }

    // Returns true on success, false otherwise.
    public static boolean login(String username, String password) throws
            SQLException {
        System.out.println("Logging in " + username + "," + password + "...");
        if (!checkCredentials(username, password)) return false;
        currentUser = username;
        return true;
    }

    // Returns true on success, false otherwise.
    public static boolean signup(String username, String password) throws
            SQLException {
        System.out.println("Signing up " + username + "," + password + "...");
        if (getPassword(username) != null) return false;


        DatabaseManager.executeInsert(DatabaseManager.USERS_TABLE, new
                String[]{username,
                password});
        login(username, password);
        return true;
    }

    public static boolean createEnvironment(String environment_name, String[]
            items) throws
            SQLException {
        DatabaseManager.executeInsert(DatabaseManager.ENVIRONMENTS_TABLE, new
                String[]
                {environment_name, currentUser});
        for (String item : items) {
            DatabaseManager.executeInsert(DatabaseManager
                    .ENVIRONMENTS_ITEMS_TABLE, new
                    String[]{environment_name, currentUser, item});
        }
        return true;
    }

    public static boolean removeEnvironment(String environment_name) throws
            SQLException {
        DatabaseManager.deleteRows(DatabaseManager.ENVIRONMENTS_TABLE,
                "circle_name = '" + environment_name + "' && username ='" +
                        currentUser + "'");

        return true;
    }

    public static boolean removeItemFromEnvironment(String environment_name,
                                                    String item) throws
            SQLException {
        DatabaseManager.deleteRows(DatabaseManager.ENVIRONMENTS_ITEMS_TABLE,
                "circle_name = '" +
                        environment_name + "' && username ='" + currentUser +
                        "' && " +
                        "item_name ='" + item
                        + "'");

        return true;
    }

    public static boolean addItemToEnvironment(String environment_name,
                                               String item) throws
            SQLException {
        DatabaseManager.executeInsert(DatabaseManager
                .ENVIRONMENTS_ITEMS_TABLE, new String[]{environment_name,
                currentUser, item});
        return true;
    }
}
