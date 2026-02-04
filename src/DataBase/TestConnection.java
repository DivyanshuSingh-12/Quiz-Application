//package DataBase;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//
//public class TestConnection {
//    public static void main(String[] args) {
//        jdbcConnection.createDatabase();
//
//        try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL, jdbcConnection.USER, jdbcConnection.PASSWORD)) {
//            System.out.println("Connected successfully to quizdb!");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
