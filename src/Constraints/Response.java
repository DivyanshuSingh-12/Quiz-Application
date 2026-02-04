package Constraints;

public class Response {

    private int userId;
    private int quizId;
    private int questionId;
    private int selectedOption;

   
    public static final int NO_SELECTION = -1;
    public static final int OPTION_1 = 1;
    public static final int OPTION_2 = 2;
    public static final int OPTION_3 = 3;
    public static final int OPTION_4 = 4;

    public Response() {
    }

    public Response(int userId, int quizId, int questionId, int selectedOption) {
        this.userId = userId;
        this.quizId = quizId;
        this.questionId = questionId;
        this.selectedOption = selectedOption;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(int selectedOption) {
        this.selectedOption = selectedOption;
    }

   
    public boolean isAnswered() {
        return selectedOption != NO_SELECTION;
    }

    @Override
    public String toString() {
        return "Response{" +
                "userId=" + userId +
                ", quizId=" + quizId +
                ", questionId=" + questionId +
                ", selectedOption=" + selectedOption +
                '}';
    }
}
