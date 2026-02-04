package DataBase;

import Constraints.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class StudentDataSql {

    public static Boolean flag = true;

    
    public static void createStudentTable() {

        String sql = "CREATE TABLE IF NOT EXISTS Studentdata (" +
                     "id INT AUTO_INCREMENT PRIMARY KEY, " +
                     "username VARCHAR(200) NOT NULL UNIQUE, " +
                     "password VARCHAR(200) NOT NULL, " +
                     "firstName VARCHAR(200) NOT NULL, " +
                     "lastName VARCHAR(200) NOT NULL, " +
                     "contact VARCHAR(200) NOT NULL, " +
                     "gender VARCHAR(50) NOT NULL" +
                     ")";

        try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL,
                jdbcConnection.USER,
                jdbcConnection.PASSWORD);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);

        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
    }


    public static boolean insertStudent(Student st) {
    	createStudentTable() ;

        String checkSQL = "SELECT COUNT(*) FROM Studentdata WHERE username = ?";
        String insertSQL =
                "INSERT INTO Students (username, password, firstName, lastName, contact, gender) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL,jdbcConnection.USER,jdbcConnection.PASSWORD)) {

       
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSQL)) {
                checkStmt.setString(1, st.getUsername());
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    return false; 
                }
            }

            // insert student
            try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
                ps.setString(1, st.getUsername());
                ps.setString(2, st.getPassword()); 
                ps.setString(3, st.getFirstName());
                ps.setString(4, st.getLastName());
                ps.setString(5, st.getContact());
                ps.setString(6, st.getGender());
                ps.executeUpdate();
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
            return false;
        }
    }


    public static Student getStudentByLogin(String username, String password) {

        String sql = "SELECT * FROM Studentdata WHERE username = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL,jdbcConnection.USER,jdbcConnection.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Student(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("contact"),
                        rs.getString("gender")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static boolean deleteStudent(String username) {

        String sql = "DELETE FROM Studentdata WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL,jdbcConnection.USER,jdbcConnection.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
            return false;
        }
    }


    public static ObservableList<Student> getAllStudents() {

        ObservableList<Student> students = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Studentdata";

        try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL,jdbcConnection.USER,jdbcConnection.PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("contact"),
                        rs.getString("gender")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return students;
    }

public static boolean updateStudent(Student st) {
    String sql = "UPDATE Studentdata SET password = ?, firstName = ?, lastName = ?, contact = ?, gender = ? WHERE username = ?";

    try (Connection conn = DriverManager.getConnection( jdbcConnection.dbURL,jdbcConnection.USER, jdbcConnection.PASSWORD);
    		
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, st.getPassword());
        ps.setString(2, st.getFirstName());
        ps.setString(3, st.getLastName());
        ps.setString(4, st.getContact());
        ps.setString(5, st.getGender());
        ps.setString(6, st.getUsername());  

        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0; 

    } catch (Exception e) {
        e.printStackTrace();
        flag = false;
        return false;
    }
 }


public static boolean deleteStudentByUsername(String username) {
    String sql = "DELETE FROM Studentdata WHERE username = ?";

    try (Connection conn = DriverManager.getConnection( jdbcConnection.dbURL,jdbcConnection.USER,jdbcConnection.PASSWORD);
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, username);

        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0; 

    } catch (Exception e) {
        e.printStackTrace();
        flag = false;
        return false;
    }
    
   }


public static void createUserQuizHideTable() {
    String sql = "CREATE TABLE IF NOT EXISTS user_quiz_hide (" +
                 "id INT AUTO_INCREMENT PRIMARY KEY, " +
                 "user_id INT NOT NULL, " +
                 "quiz_id INT NOT NULL, " +
                 "UNIQUE(user_id, quiz_id)" +  
                 ")";

    try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL,jdbcConnection.USER,jdbcConnection.PASSWORD);
         Statement stmt = conn.createStatement()) {

        stmt.executeUpdate(sql);

    } catch (Exception e) {
        e.printStackTrace();
        flag = false;
    }
}


	public static boolean isQuizHiddenForUser(int quizId, int userId) {
		createUserQuizHideTable();
	 String sql = "SELECT id FROM user_quiz_hide WHERE user_id = ? AND quiz_id = ?";
	 try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL, jdbcConnection.USER, jdbcConnection.PASSWORD);
	      PreparedStatement ps = conn.prepareStatement(sql)) {
	     ps.setInt(1, userId);
	     ps.setInt(2, quizId);
	     try (ResultSet rs = ps.executeQuery()) {
	         return rs.next();
	     }
	 } catch (Exception e) {
	     e.printStackTrace();
	 }
	 return false;
	}
	

	public static boolean hideQuizForUser(int userId, int quizId) {
		createUserQuizHideTable();
	 String sql = "INSERT IGNORE INTO user_quiz_hide (user_id, quiz_id) VALUES (?, ?)";
	 try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL, jdbcConnection.USER, jdbcConnection.PASSWORD);
	      PreparedStatement ps = conn.prepareStatement(sql)) {
	     ps.setInt(1, userId);
	     ps.setInt(2, quizId);
	     ps.executeUpdate();
	     return true;
	 } catch (Exception e) {
	     e.printStackTrace();
	     return false;
	 }
	}

}














