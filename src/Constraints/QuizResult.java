package Constraints;

public class quizResult {
    private String userId;  
    private String userName; 
    private int score;

    public quizResult(String userId, String userName, int score) {
        this.userId = userId;
        this.userName = userName;
        this.score = score;
    }

    public String getUserId() { return userId; }
    public String getUserName() { return userName; }
    public int getScore() { return score; }

    public void setUserId(String userId) { this.userId = userId; }
    public void setUserName(String userName) { this.userName = userName; }
    public void setScore(int score) { this.score = score; }
}
