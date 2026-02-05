package Constraints;

public class QuestionResult {

    private String question;
    private String correctAnswer;
    private String myAnswer;

    public QuestionResult(String question, String correctAnswer, String myAnswer) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.myAnswer = myAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getMyAnswer() {
        return myAnswer;
    }
}
