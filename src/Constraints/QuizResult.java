package Constraints;

public class QuizResult {

    private int quizId;
    private String quizTitle;
    private int myPoints;
    private int totalPoints;

    public QuizResult(int quizId, String quizTitle, int myPoints, int totalPoints) {
        this.quizId = quizId;
        this.quizTitle = quizTitle;
        this.myPoints = myPoints;
        this.totalPoints = totalPoints;
    }

    public int getQuizId() {
        return quizId;
    }

    public String getQuizTitle() {
        return quizTitle;
    }

    public int getMyPoints() {
        return myPoints;
    }

    public int getTotalPoints() {
        return totalPoints;
    }
}
