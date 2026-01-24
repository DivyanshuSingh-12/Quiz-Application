package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class jdbcConnection {

    public static final String URL = "jdbc:mysql://localhost:3306";
    public static final String USER = "root";
    public static final String PASSWORD = "root";
    public static String dbURL;

    public static void createDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

           try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                 Statement stmt = conn.createStatement()) {

                String sql = "CREATE DATABASE IF NOT EXISTS quizdb";
                stmt.executeUpdate(sql);
            }

            dbURL = URL + "/quizdb";
            DriverManager.getConnection(dbURL, USER, PASSWORD);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
