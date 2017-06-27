package grad.proj.database_manager;

import com.sun.deploy.util.StringUtils;

import java.sql.*;
import java.util.Arrays;

/**
 * Created by mohammad on 21/06/17.
 */
public class DatabaseManager {
    public static final String DB_NAME = "smartcornea";
    public static final String USERS_TABLE = "Users";
    public static final String ITEMS_TABLE = "Items";
    public static final String ENVIRONMENTS_TABLE = "Circles";
    public static final String ENVIRONMENTS_ITEMS_TABLE = "CirclesItems";

    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PASSWORD = "root";

    private static Connection connection;
    private static Statement statement;


    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DB_NAME,
                    DATABASE_USER, DATABASE_PASSWORD);
            statement = connection.createStatement();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ResultSet executeQuery(String query) throws SQLException {
        System.out.println("Executing query: " + query);
        return statement.executeQuery(query);
    }

    public static int executeUpdate(String query) throws SQLException {
        System.out.println("Executing update: " + query);
        int affected_rows = statement.executeUpdate(query);
        return affected_rows;
    }

    public static int executeInsert(String table_name, String[] values) throws SQLException {
        String list = "'" + StringUtils.join(Arrays.asList(values), "','") + "'";

        String query = "INSERT INTO " + table_name + " VALUES " + "(" + list + ")";
        return executeUpdate(query);
    }

    public static int deleteRows(String table_name, String where_clause)
            throws SQLException {
        String query = "DELETE FROM " + table_name + " WHERE " + where_clause;
        return executeUpdate(query);
    }

    public static int deleteAllRows(String table_name) throws SQLException {
        String query = "DELETE FROM " + table_name;
        return executeUpdate(query);
    }

}


