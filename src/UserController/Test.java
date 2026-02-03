package UserController;

import DataBase.quizSql;
import Constraints.Question;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class Test {

    @FXML  private Label Question;
    @FXML  private RadioButton resOpt1, resOpt2, resOpt3, resOpt4;
    @FXML  private Button nextBtn, prevBtn, submitBtn;
    private List<Question> questions;
    private int currentIndex = 0;
    private int quizId;

    private ToggleGroup optionsGroup;

    private boolean testSubmitted = false;
    
    

    private quizUser quizUserController;

    public void setQuizId(int quizId) {
        this.quizId = quizId;
        loadQuestions();
        showQuestion(0);
    }

    public void setQuizUserController(quizUser controller) {
        this.quizUserController = controller;
    }
    
    
    
    

    private void loadQuestions() {
        questions = quizSql.getQuestionsByQuizId(quizId);
        if (questions.isEmpty()) {
            Question.setText("No questions found for this quiz.");
            nextBtn.setDisable(true);
            prevBtn.setDisable(true);
        }
    }

    private void showQuestion(int index) {
        if (questions.isEmpty()) return;

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

        prevBtn.setDisable(index == 0);
        nextBtn.setDisable(index == questions.size() - 1);
    }

    @FXML
    private void nextBtnFun() {
        if (currentIndex < questions.size() - 1) {
            currentIndex++;
            showQuestion(currentIndex);
        }
    }

    @FXML
    private void prevBtnFun() {
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

        if (quizUserController != null) {
            quizUserController.onTestSubmitted(quizId);
        } 

       Platform.runLater(stage::close);
    }

   
}

