package UserController;

import DataBase.ResponseSql;
import DataBase.quizSql;
import Constraints.Response;
import Constraints.Question;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {

    @FXML private Label Question;
    @FXML private RadioButton resOpt1, resOpt2, resOpt3, resOpt4;
    @FXML private Button nextBtn, prevBtn, submitBtn;
    @FXML private Label questionNumber;
    private List<Question> questions;
    private int currentIndex = 0;
    private int quizId;
    private int userId;

    private ToggleGroup optionsGroup;
    private boolean testSubmitted = false;

    private Map<Integer, Integer> userResponses = new HashMap<>();
    
    public void setQuizId(int quizId) {
        this.quizId = quizId;
        loadQuestions();
        showQuestion(0);
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setQuizUserController(quizUser controller) {
    }


    private void loadQuestions() {
        questions = quizSql.getQuestionsByQuizId(quizId);

        if (questions.isEmpty()) {
            Question.setText("No questions found for this quiz.");
            nextBtn.setDisable(true);
            prevBtn.setDisable(true);
            submitBtn.setDisable(true);
        }
    }

    // ==================== SHOW QUESTION ====================
    private void showQuestion(int index) {
        if (questions.isEmpty()) return;
        questionNumber.setText("Question " + (index + 1) + " / " + questions.size());
        Question q = questions.get(index);
        Question.setText(q.getQues());
        resOpt1.setText(q.getOpttext1());
        resOpt2.setText(q.getOpttext2());
        resOpt3.setText(q.getOpttext3());
        resOpt4.setText(q.getOpttext4());

        if (optionsGroup == null) {
            optionsGroup = new ToggleGroup();
            resOpt1.setToggleGroup(optionsGroup);
            resOpt2.setToggleGroup(optionsGroup);
            resOpt3.setToggleGroup(optionsGroup);
            resOpt4.setToggleGroup(optionsGroup);
        } else {
            optionsGroup.selectToggle(null); // reset selection
        }

        // Restore previous selection if exists
        int questionId = q.getQuestionId();
        Integer savedOption = userResponses.get(questionId);
        if (savedOption != null) {
            switch (savedOption) {
                case 1 -> resOpt1.setSelected(true);
                case 2 -> resOpt2.setSelected(true);
                case 3 -> resOpt3.setSelected(true);
                case 4 -> resOpt4.setSelected(true);
            }
        }

        prevBtn.setDisable(index == 0);
        nextBtn.setDisable(index == questions.size() - 1);
    }

   
    private void saveCurrentResponse() {
        Toggle selectedToggle = optionsGroup.getSelectedToggle();
        if (selectedToggle == null) return;

        int selectedOption = -1;
        if (selectedToggle == resOpt1) selectedOption = 1;
        else if (selectedToggle == resOpt2) selectedOption = 2;
        else if (selectedToggle == resOpt3) selectedOption = 3;
        else if (selectedToggle == resOpt4) selectedOption = 4;

        int questionId = questions.get(currentIndex).getQuestionId();
        userResponses.put(questionId, selectedOption);
    }


    @FXML
    private void nextBtnFun() {
        saveCurrentResponse();
        if (currentIndex < questions.size() - 1) {
            currentIndex++;
            showQuestion(currentIndex);
        }
    }

    @FXML
    private void prevBtnFun() {
        saveCurrentResponse();
        if (currentIndex > 0) {
            currentIndex--;
            showQuestion(currentIndex);
        }
    }

    
    
    
    @FXML
    private void submitBtnFun() {
        Stage stage = (Stage) submitBtn.getScene().getWindow();
        submitTest(stage);
    }

    public void submitTest(Stage stage) {
        if (testSubmitted) return; 
        testSubmitted = true;

        saveCurrentResponse();

        for (Map.Entry<Integer, Integer> entry : userResponses.entrySet()) {
            Response r = new Response(userId, quizId, entry.getKey(), entry.getValue());
            ResponseSql.insertStudentAnswer(r);
        }
        ResponseSql.markQuizAttempted(userId, quizId);
        Platform.runLater(stage::close);
        showAlert(Alert.AlertType.INFORMATION, "Submitted", "Quiz submitted successfully.");
    }
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
