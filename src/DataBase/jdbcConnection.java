//
////////////////////////////////ONLINE DATABASE////////////////////////////////
//package DataBase;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.Statement;
//
//public class jdbcConnection {
//
//    public static final String HOST = "sql12.freesqldatabase.com";
//    public static final String DATABASE = "sql12816554";
//    public static final String USER = "sql12816554";
//    public static final String PASSWORD = "m8w6hrCDaj";
//    public static final String PORT = "3306";
//
//    public static String dbURL;
//
//    public static void createDatabase() {
//        try {
//
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            String serverURL = "jdbc:mysql://" + HOST + ":" + PORT + "/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
//
//            try (Connection conn = DriverManager.getConnection(serverURL, USER, PASSWORD);
//                 
//            	Statement stmt = conn.createStatement()) {
//                String sql = "CREATE DATABASE IF NOT EXISTS " + DATABASE;
//                stmt.executeUpdate(sql);
//            }
//
//           
//            dbURL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
//
//            createTables();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//private static void createTables() {
//    quizSql.createQuizTables();
//    quizSql.createQuestionTables();
//    quizSql.createAnswerTables();
//    ResponseSql.createStudentResponseTable();
//    ResponseSql.createQuizAttemptTable();
//    StudentDataSql.createStudentTable();
//    StudentDataSql.createUserQuizHideTable();
//} 
//}
//

package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class jdbcConnection {

   // public static final String URL = "jdbc:mysql://localhost:3306";
	public static final String URL = "jdbc:mysql://localhost:3306?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    
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

           // dbURL = URL + "/quizdb";
            dbURL = "jdbc:mysql://localhost:3306/quizdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            createTables();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void createTables() {
        quizSql.createQuizTables();
        quizSql.createQuestionTables();
        quizSql.createAnswerTables();
        ResponseSql.createStudentResponseTable();
        ResponseSql.createQuizAttemptTable();
        StudentDataSql.createStudentTable();
        StudentDataSql.createUserQuizHideTable();
    }   
}

