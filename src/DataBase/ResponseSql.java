package DataBase;

import java.sql.*;
import Constraints.Response;

public class ResponseSql {

    // ---------------- CREATE STUDENT RESPONSE TABLE ----------------
    public static void createStudentResponseTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS student_response (
                response_id INT AUTO_INCREMENT PRIMARY KEY,
                user_id INT NOT NULL,
                quiz_id INT NOT NULL,
                question_id INT NOT NULL,
                opt1 BOOLEAN DEFAULT FALSE,
                opt2 BOOLEAN DEFAULT FALSE,
                opt3 BOOLEAN DEFAULT FALSE,
                opt4 BOOLEAN DEFAULT FALSE,
                UNIQUE KEY uq_user_quiz_question (user_id, quiz_id, question_id),
                FOREIGN KEY (quiz_id) REFERENCES quiz(id) ON DELETE CASCADE,
                FOREIGN KEY (question_id) REFERENCES questions(question_id) ON DELETE CASCADE
            );
        """;

        try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL, jdbcConnection.USER, jdbcConnection.PASSWORD);
             
        	Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Error creating student_response table: " + e.getMessage());
        }
    }

    // ---------------- CREATE QUIZ ATTEMPT TABLE ----------------
    public static void createQuizAttemptTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS quiz_attempt (
                attempt_id INT AUTO_INCREMENT PRIMARY KEY,
                user_id INT NOT NULL,
                quiz_id INT NOT NULL,
                attempted BOOLEAN DEFAULT FALSE,
                UNIQUE KEY uq_user_quiz (user_id, quiz_id),
                FOREIGN KEY (quiz_id) REFERENCES quiz(id) ON DELETE CASCADE
            );
        """;

        try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL, jdbcConnection.USER, jdbcConnection.PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Error creating quiz_attempt table: " + e.getMessage());
        }
    }

    // ---------------- INSERT / UPDATE STUDENT RESPONSE ----------------
    public static void insertStudentAnswer(Response r) {
        createStudentResponseTable();

        String sql = """
            INSERT INTO student_response (user_id, quiz_id, question_id, opt1, opt2, opt3, opt4)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
                opt1 = VALUES(opt1),
                opt2 = VALUES(opt2),
                opt3 = VALUES(opt3),
                opt4 = VALUES(opt4);
        """;

        try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL, jdbcConnection.USER, jdbcConnection.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, r.getUserId());
            ps.setInt(2, r.getQuizId());
            ps.setInt(3, r.getQuestionId());
            ps.setBoolean(4, r.getSelectedOption() == 1);
            ps.setBoolean(5, r.getSelectedOption() == 2);
            ps.setBoolean(6, r.getSelectedOption() == 3);
            ps.setBoolean(7, r.getSelectedOption() == 4);

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserting/updating student answer: " + e.getMessage());
        }
    }

    // ---------------- MARK QUIZ ATTEMPTED ----------------
    public static void markQuizAttempted(int userId, int quizId) {
        createQuizAttemptTable();

        String sql = """
            INSERT INTO quiz_attempt (user_id, quiz_id, attempted)
            VALUES (?, ?, TRUE)
            ON DUPLICATE KEY UPDATE attempted = TRUE;
        """;

        try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL, jdbcConnection.USER, jdbcConnection.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, quizId);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error marking quiz attempted: " + e.getMessage());
        }
    }

    // ---------------- CHECK IF QUIZ ATTEMPTED ----------------
    public static boolean isQuizAttempted(int quizId, int userId) {
        createQuizAttemptTable();

        String sql = "SELECT attempted FROM quiz_attempt WHERE user_id = ? AND quiz_id = ?";

        try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL, jdbcConnection.USER, jdbcConnection.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, quizId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getBoolean("attempted");
            }

        } catch (SQLException e) {
            System.err.println("Error checking quiz attempted: " + e.getMessage());
        }

        return false;
    }

    // ---------------- EVALUATE QUIZ ----------------
    public static int evaluateQuiz(int userId, int quizId) {
        String sql = """
            SELECT sr.opt1, sr.opt2, sr.opt3, sr.opt4,
                   aa.opt1 AS correct1, aa.opt2 AS correct2, aa.opt3 AS correct3, aa.opt4 AS correct4
            FROM student_response sr
            JOIN admin_answer aa ON sr.question_id = aa.question_id
            WHERE sr.user_id = ? AND sr.quiz_id = ?;
        """;

        int score = 0;

        try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL, jdbcConnection.USER, jdbcConnection.PASSWORD);
            
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, quizId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    boolean correct = rs.getBoolean("opt1") == rs.getBoolean("correct1") &&
                                      rs.getBoolean("opt2") == rs.getBoolean("correct2") &&
                                      rs.getBoolean("opt3") == rs.getBoolean("correct3") &&
                                      rs.getBoolean("opt4") == rs.getBoolean("correct4");

                    if (correct) score++;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error evaluating quiz: " + e.getMessage());
        }

        return score;
    }
}
