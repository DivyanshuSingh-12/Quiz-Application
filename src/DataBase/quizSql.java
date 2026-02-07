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
    	createQuizTables();
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
    	createQuestionTables();
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
	createAnswerTables();
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
                rs.getInt("question_id"),  
                rs.getString("ques"),
                rs.getString("opt1"),
                rs.getString("opt2"),
                rs.getString("opt3"),
                rs.getString("opt4"),
                0 
            ));
        }

    } catch (Exception e) {
        e.printStackTrace();
        error += "Error fetching questions for quiz ID: " + quizId + "\n";
    }

    return list;
}

public static ObservableList<quizSelection> getAttemptedQuizzes(int userId) {

    ObservableList<quizSelection> list = FXCollections.observableArrayList();

    String sql = """
        SELECT DISTINCT q.id, q.title, q.admin_id
        FROM quiz q
        JOIN student_response sr ON q.id = sr.quiz_id
        WHERE sr.user_id = ?
    """;

    try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL,jdbcConnection.USER,jdbcConnection.PASSWORD);
         
    	PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            list.add(
                new quizSelection( rs.getInt("id"),rs.getString("admin_id"),rs.getString("title"),"ATTEMPTED"
                )
            );
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}

public static int getTotalQuestions(int quizId) {

    String sql = "SELECT COUNT(*) FROM questions WHERE quiz_id = ?";

    try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL,jdbcConnection.USER,jdbcConnection.PASSWORD);
    		
        PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, quizId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return 0;
}

public static String getQuizTitleById(int quizId) {

    String title = null;
    String query = "SELECT title FROM quiz WHERE id = ?"; 

    try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL,jdbcConnection.USER,jdbcConnection.PASSWORD);
        PreparedStatement ps = conn.prepareStatement(query)
    ) {
        ps.setInt(1, quizId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            title = rs.getString("title");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return title;
}

public static List<Integer> getAllQuizIds() {

    List<Integer> quizIds = new ArrayList<>();
    String sql = "SELECT id FROM quiz";

    try (Connection conn = DriverManager.getConnection(
            jdbcConnection.dbURL,
            jdbcConnection.USER,
            jdbcConnection.PASSWORD);
         Statement st = conn.createStatement();
         ResultSet rs = st.executeQuery(sql)) {

        while (rs.next()) {
            quizIds.add(rs.getInt("id"));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return quizIds;
}


public static List<Integer> getAllQuizIdsByAdmin(String adminId) {
    List<Integer> quizIds = new ArrayList<>();
    String sql = "SELECT id FROM quiz WHERE admin_id = ?";

    try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL, jdbcConnection.USER, jdbcConnection.PASSWORD);
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, adminId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            quizIds.add(rs.getInt("id"));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return quizIds;
}


public static int executeIntQuery(String sql, String param) {

    try (Connection conn = DriverManager.getConnection( jdbcConnection.dbURL,jdbcConnection.USER, jdbcConnection.PASSWORD);
         
        PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, param);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return 0;
}


public static int executeIntQuery(String sql) {

    try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL,jdbcConnection.USER, jdbcConnection.PASSWORD);
         
    		
    	 Statement st = conn.createStatement();
         ResultSet rs = st.executeQuery(sql)) {

        if (rs.next()) {
            return rs.getInt(1);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return 0;
}


public static double executeDoubleQuery(String sql, String param) {

    try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL,jdbcConnection.USER,jdbcConnection.PASSWORD);
        
    	PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, param);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return 0.0;
}



//////////////////////////////////UPDATE//////////////////////////////////////////////////
public static quizSelection getQuizByTitle(String title) {
    quizSelection quiz = null;
    String query = "SELECT * FROM quiz WHERE title = ?";

    try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL, jdbcConnection.USER, jdbcConnection.PASSWORD);
         PreparedStatement ps = conn.prepareStatement(query)) {

        ps.setString(1, title);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int id = rs.getInt("id");
            String adminId = rs.getString("admin_id");
            String status = "NOT ATTEMPTED";
            quiz = new quizSelection(id, adminId, title, status);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return quiz;
}


public static boolean updateQuiz(int quizId, String title, String adminId) {
    String sql = "UPDATE quiz SET title = ?, admin_id = ? WHERE id = ?";

    try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL,jdbcConnection.USER,jdbcConnection.PASSWORD);
        
    	PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, title);
        ps.setString(2, adminId);
        ps.setInt(3, quizId);

        int rows = ps.executeUpdate();
        return rows > 0;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}



public static boolean updateOneQuestion(int questionId, Question q) {
    String sql = "UPDATE questions SET ques = ?, opt1 = ?, opt2 = ?, opt3 = ?, opt4 = ? WHERE question_id = ?";

    try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL,jdbcConnection.USER,jdbcConnection.PASSWORD);
        
    	PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, q.getQues());
        ps.setString(2, q.getOpttext1());
        ps.setString(3, q.getOpttext2());
        ps.setString(4, q.getOpttext3());
        ps.setString(5, q.getOpttext4());
        ps.setInt(6, questionId);

        int rows = ps.executeUpdate();
        return rows > 0;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}


public static adminAnswer getAnswerByQuestionId(int questionId) {
    createAnswerTables();
    String sql = "SELECT * FROM admin_answer WHERE question_id = ?";

    try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL, jdbcConnection.USER,jdbcConnection.PASSWORD);
        
    	PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, questionId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new adminAnswer(
                rs.getBoolean("opt1"),
                rs.getBoolean("opt2"),
                rs.getBoolean("opt3"),
                rs.getBoolean("opt4")
            );
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}


public static void updateAllAnswer(int questionId, adminAnswer ans) {
    createAnswerTables();
    String sql = "UPDATE admin_answer SET opt1 = ?, opt2 = ?, opt3 = ?, opt4 = ? WHERE question_id = ?";

    try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL, jdbcConnection.USER,jdbcConnection.PASSWORD);
        
    	PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setBoolean(1, ans.getcorrectopt1());
        pstmt.setBoolean(2, ans.getcorrectopt2());
        pstmt.setBoolean(3, ans.getcorrectopt3());
        pstmt.setBoolean(4, ans.getcorrectopt4());
        pstmt.setInt(5, questionId);

        int rows = pstmt.executeUpdate();

        if (rows == 0) {
            insertAllAnswer(questionId, ans);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}

public static void deleteQuestionById(int questionId) {
    String sql = "DELETE FROM questions WHERE question_id = ?";
    try ( Connection conn = DriverManager.getConnection(jdbcConnection.dbURL, jdbcConnection.USER,jdbcConnection.PASSWORD);
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, questionId);
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}





}  


