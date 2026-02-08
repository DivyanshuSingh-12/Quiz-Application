//package DataBase;
//
//import DataBase.jdbcConnection;
//import java.sql.Connection;
//import java.sql.Statement;
//
//public class TestConnection {
//    public static void main(String[] args) {
//        jdbcConnection.createDatabase();
//
//        boolean flag = true;
//        String testQuery = "SELECT 1"; 
//
//        try (Connection conn = java.sql.DriverManager.getConnection(jdbcConnection.dbURL,
//                jdbcConnection.USER, jdbcConnection.PASSWORD);
//             Statement stmt = conn.createStatement()) {
//
//            stmt.executeQuery(testQuery);
//            System.out.println("Connection to online MySQL database successful!");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            flag = false;
//        }
//
//        if (!flag) {
//            System.out.println("Connection failed.");
//        }
//    }
//}
