package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class jdbcConnection {

    private static final String SERVER_URL ="jdbc:mysql://localhost:3306?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/quizdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private static Connection persistentConnection;

    public static Connection getConnection() throws SQLException {
        if (persistentConnection == null || persistentConnection.isClosed()) {
            persistentConnection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        }
        return persistentConnection;
    }

    public static void createDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(SERVER_URL, USER, PASSWORD);
                 Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS quizdb");
            }

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

    public static void closeConnection() {
        if (persistentConnection != null) {
            try {
                if (!persistentConnection.isClosed()) {
                    persistentConnection.close();
                    System.out.println("MySQL connection closed");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}


//
//package DataBase;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.Statement;
//import java.sql.SQLException;
//
//public class jdbcConnection {
//
//  public static final String URL = "jdbc:mysql://sql12.freesqldatabase.com:3306?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
//  public static final String USER = "sql12816563";
//  public static final String PASSWORD = "g2zVfL6pE8";
//  public static String dbURL;
//
//  private static Connection persistentConnection;
//
//  public static Connection getConnection() throws SQLException {
//      if (persistentConnection == null || persistentConnection.isClosed()) {
//          persistentConnection = DriverManager.getConnection(dbURL, USER, PASSWORD);
//      }
//      return persistentConnection;
//  }
//
//  public static void closeConnection() {
//      if (persistentConnection != null) {
//          try {
//              if (!persistentConnection.isClosed()) {
//                  persistentConnection.close();
//                  System.out.println("Online MySQL connection closed");
//              }
//          } catch (SQLException e) {
//              e.printStackTrace();
//          }
//      }
//  }
//
//  public static void createDatabase() {
//      try {
//          Class.forName("com.mysql.cj.jdbc.Driver");
//          try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
//               Statement stmt = conn.createStatement()) {
//              String sql = "CREATE DATABASE IF NOT EXISTS sql12816563";
//              stmt.executeUpdate(sql);
//          }
//
//          dbURL = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12816563?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
//
//          createTables();
//
//      } catch (Exception e) {
//          e.printStackTrace();
//      }
//  }
//
//  private static void createTables() {
//      quizSql.createQuizTables();
//      quizSql.createQuestionTables();
//      quizSql.createAnswerTables();
//      ResponseSql.createStudentResponseTable();
//      ResponseSql.createQuizAttemptTable();
//      StudentDataSql.createStudentTable();
//      StudentDataSql.createUserQuizHideTable();
//  }
//}











