package DataBase;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Constraints.Question;
import Constraints.adminAnswer;
import Constraints.quizSelection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class quizSql {
	public String Successful = "";  
    private static String QuestionTablesError = "Question Table Error";
    private static  String quizTableError = "QuizTable Error";
    private static String quizTitleError = "QuizTitel Insertion Error";
    private static String QuestionInsertionError = " Question Insertion Error";
    private static String AnswerTablesError = "Answer Tables Error";
    private static String AnswerInsertionError = "Answer Insertion Error";
    public static  String error = ""; 
    public static Boolean flag = true; 
    


    public static void createQuizTables() {
    	String quizTable =
    		    "CREATE TABLE IF NOT EXISTS quiz (" +
    		    "id INT AUTO_INCREMENT PRIMARY KEY, " +
    		    "title VARCHAR(200) NOT NULL, " +
    		    "admin_id VARCHAR(100) NOT NULL" +
    		    ")";


        try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL, jdbcConnection.USER, jdbcConnection.PASSWORD);
        		    		
             Statement stmt = conn.createStatement()) {
             stmt.executeUpdate(quizTable);
      
        } catch (Exception e) { 
            e.printStackTrace();
            error += quizTableError + "\n";
            flag = false;
        }
    }
    

    public static int  insertQuiz(String title,String string) {

        String sql = "INSERT INTO quiz (title, admin_id) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL, jdbcConnection.USER, jdbcConnection.PASSWORD);

            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, title);
            ps.setString(2, string);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            
            if (rs.next()) {
                return rs.getInt(1); // quiz id
            } else {                  
            error += QuestionInsertionError + "\n";
            flag = false;
            return -1;  
            } 
            
          
            

        } catch (Exception e) {
            e.printStackTrace();
            error += quizTitleError + "\n";
            flag = false;
            return -1;
        }
    }

    
         
    


    public static void createQuestionTables() {

    	String questionTable = "CREATE TABLE IF NOT EXISTS questions ("
    	        + "question_id INT AUTO_INCREMENT PRIMARY KEY, "
    	        + "quiz_id INT, "
    	        + "ques VARCHAR(255), "
    	        + "opt1 VARCHAR(100), "
    	        + "opt2 VARCHAR(100), "
    	        + "opt3 VARCHAR(100), "
    	        + "opt4 VARCHAR(100), "
    	        + "FOREIGN KEY (quiz_id) REFERENCES quiz(id) ON DELETE CASCADE"
    	        + ");";


    	  try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL, jdbcConnection.USER, jdbcConnection.PASSWORD);
        		
            Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(questionTable);
            
        } catch (Exception e) {
            e.printStackTrace();
            error += QuestionTablesError + "\n";
            flag = false;
           
        }
        
    }
    
    

    public static int insertOneQuestion(int quizId, Question q) {
        String sql = "INSERT INTO questions (quiz_id, ques, opt1, opt2, opt3, opt4) "
        		+ "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL, jdbcConnection.USER, jdbcConnection.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, quizId);
            ps.setString(2, q.getQues());
            ps.setString(3, q.getOpttext1());
            ps.setString(4, q.getOpttext2());
            ps.setString(5, q.getOpttext3());
            ps.setString(6, q.getOpttext4());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); 
            } else {
                error += QuestionInsertionError + "\n";
                flag = false;
                return -1;
            }

        } catch (Exception e) {
            e.printStackTrace();
            error += QuestionInsertionError + "\n";
            flag = false;
            return -1;
        }
    }

    
    public static void createAnswerTables() {

    	String answerTable =
    		    "CREATE TABLE IF NOT EXISTS admin_answer (" +
    		    "answer_id INT AUTO_INCREMENT PRIMARY KEY, " +
    		    "question_id INT NOT NULL, " +
    		    "opt1 BOOLEAN NOT NULL, " +
    		    "opt2 BOOLEAN NOT NULL, " +
    		    "opt3 BOOLEAN NOT NULL, " +
    		    "opt4 BOOLEAN NOT NULL, " +   
    		    "FOREIGN KEY (question_id) "
    		    + "REFERENCES questions(question_id) ON DELETE CASCADE" +
    		    ");";
	        
    	  try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL, jdbcConnection.USER, jdbcConnection.PASSWORD);
            Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(answerTable);
        } catch (Exception e) {
            e.printStackTrace();
            error += AnswerTablesError + "\n";
            flag = false;
        }
    }
    
    
    
public static void insertAllAnswer(int questionId ,adminAnswer ans) {
    String sql = "INSERT INTO admin_answer  "
    		+ "(question_id, opt1, opt2, opt3, opt4) "
    		+ "VALUES (?, ?, ?, ?, ?)";

    try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL, jdbcConnection.USER, jdbcConnection.PASSWORD);) {

        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setInt(1, questionId);                 
        pstmt.setBoolean(2, ans.getcorrectopt1());
        pstmt.setBoolean(3, ans.getcorrectopt2());
        pstmt.setBoolean(4, ans.getcorrectopt3());
        pstmt.setBoolean(5, ans.getcorrectopt4());
        pstmt.executeUpdate();
        
    } catch (Exception e) {	
        e.printStackTrace();
        error += AnswerInsertionError + "\n";
        flag = false;
    }
}

public static ObservableList<String> getQuizzesByAdmin(String adminId) {
    ObservableList<String> quizzes = FXCollections.observableArrayList();

    String sql = "SELECT title FROM quiz WHERE admin_id = ?";

    try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL, jdbcConnection.USER, jdbcConnection.PASSWORD);
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, adminId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            quizzes.add(rs.getString("title"));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return quizzes;
}


public static ObservableList<quizSelection> getAllQuizzesForUser() {
    ObservableList<quizSelection> list = FXCollections.observableArrayList();
    String sql = "SELECT id, title, admin_id FROM quiz";  

    try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL,jdbcConnection.USER,jdbcConnection.PASSWORD);
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            list.add(
                new quizSelection(
                    rs.getInt("id"),            
                    rs.getString("admin_id"),
                    rs.getString("title"),
                    "NOT ATTEMPTED"
                )
            );
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;   
}

public static boolean deleteQuiz(int quizId) {
    String sql = "DELETE FROM quiz WHERE id = ?";

    try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL,jdbcConnection.USER,jdbcConnection.PASSWORD);
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, quizId);
        int affectedRows = ps.executeUpdate();
        return affectedRows > 0;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

public static List<Question> getQuestionsByQuizId(int quizId) {
    List<Question> list = new ArrayList<>();
    String query = "SELECT * FROM questions WHERE quiz_id = ?";

    try (Connection con = DriverManager.getConnection(jdbcConnection.dbURL, jdbcConnection.USER, jdbcConnection.PASSWORD);
         PreparedStatement pst = con.prepareStatement(query)) {

        pst.setInt(1, quizId);
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            list.add(new Question(
                rs.getString("ques"),
                rs.getString("opt1"),
                rs.getString("opt2"),
                rs.getString("opt3"),
                rs.getString("opt4"),
                0 // default correct option, to be updated later
            ));
        }

    } catch (Exception e) {
        e.printStackTrace();
        error += "Error fetching questions for quiz ID: " + quizId + "\n";
    }

    return list;
}



}  


