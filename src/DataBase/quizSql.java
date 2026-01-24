package DataBase;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


import Constraints.Question;
import Constraints.adminAnswer;

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
    

    //CREATE TABLES 
    public static void createQuizTables() {

        String quizTable =
                "CREATE TABLE IF NOT EXISTS quiz (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "title VARCHAR(200) NOT NULL)";


        try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL,
                jdbcConnection.USER, jdbcConnection.PASSWORD);
        		    		
             Statement stmt = conn.createStatement()) {
             stmt.executeUpdate(quizTable);
             System.out.println("createQuizTables");
      
        } catch (Exception e) {
            e.printStackTrace();
            error += quizTableError + "\n";
            flag = false;
        }
    }
    
    //  INSERT QUIZ
    public static int  insertQuiz(String title) {

        String sql = "INSERT INTO quiz (title) VALUES (?)";

        try (Connection conn = DriverManager.getConnection(
                jdbcConnection.dbURL,
                jdbcConnection.USER,
                jdbcConnection.PASSWORD);

            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, title);
            ps.executeUpdate();
            System.out.println("insertQuiz");

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


        try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL,
                jdbcConnection.USER, jdbcConnection.PASSWORD);
        		
            Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(questionTable);
            
            System.out.println("createQuestionTables");

        } catch (Exception e) {
            e.printStackTrace();
            error += QuestionTablesError + "\n";
            flag = false;
           
        }
        
    }
    
    
    //  Insert Question
    public static int insertOneQuestion(int quizId, Question q) {
        String sql = "INSERT INTO questions (quiz_id, ques, opt1, opt2, opt3, opt4) "
        		+ "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL,
                jdbcConnection.USER, jdbcConnection.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, quizId);
            ps.setString(2, q.getQues());
            ps.setString(3, q.getOpttext1());
            ps.setString(4, q.getOpttext2());
            ps.setString(5, q.getOpttext3());
            ps.setString(6, q.getOpttext4());

            ps.executeUpdate();
            System.out.println("insertOneQuestion successfully!");

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
	        
        try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL,
                jdbcConnection.USER, jdbcConnection.PASSWORD);
            Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(answerTable);
            System.out.println("createAnswerTables");

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

    try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL,
            jdbcConnection.USER, jdbcConnection.PASSWORD);) {

        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setInt(1, questionId);                 
        pstmt.setBoolean(2, ans.getcorrectopt1());
        pstmt.setBoolean(3, ans.getcorrectopt2());
        pstmt.setBoolean(4, ans.getcorrectopt3());
        pstmt.setBoolean(5, ans.getcorrectopt4());
        pstmt.executeUpdate();
        
        System.out.println("insertAllAnswer successfully!");

    } catch (Exception e) {	
        e.printStackTrace();
        error += AnswerInsertionError + "\n";
        flag = false;
    }
}

}  


