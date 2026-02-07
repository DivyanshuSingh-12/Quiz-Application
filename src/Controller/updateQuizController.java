package Controller;

import Constraints.Question;
import Constraints.adminAnswer;
import Constraints.quizSelection;
import DataBase.quizSql;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.stage.Stage;

import java.util.List;

public class updateQuizController {

    @FXML private TextField quizTitle;
    @FXML private Text questionNo;
    @FXML private Label notificationLabel;

    @FXML private TextArea question;
    @FXML private TextArea textopt1;
    @FXML private TextArea textOpt2;
    @FXML private TextArea textOpt3;
    @FXML private TextArea textOpt4;

    @FXML private RadioButton opt1;
    @FXML private RadioButton Opt2;
    @FXML private RadioButton opt3;
    @FXML private RadioButton Opt4;

    @FXML private Button addBtn;
    @FXML private Button prvBtn;
    @FXML private Button updateBtn;

    private ToggleGroup correctGroup;
    private int x = 0; 
    private List<Question> questionStore; 
    private quizSelection quiz;

    @FXML
    public void initialize() {
        correctGroup = new ToggleGroup();
        opt1.setToggleGroup(correctGroup);
        Opt2.setToggleGroup(correctGroup);
        opt3.setToggleGroup(correctGroup);
        Opt4.setToggleGroup(correctGroup);

        notificationLabel.setVisible(false);
        questionNo.setText("Question " + (x + 1));
    }

    public void setQuizData(quizSelection quiz) {
        this.quiz = quiz;
        quizTitle.setText(quiz.getTitle());

        x = 0;
        questionStore = quizSql.getQuestionsByQuizId(quiz.getId());
        for (Question q : questionStore) {
            adminAnswer ans = quizSql.getAnswerByQuestionId(q.getQuestionId());
            if (ans != null) {
                if (ans.getcorrectopt1()) q.setCorrectOpt(1);
                else if (ans.getcorrectopt2()) q.setCorrectOpt(2);
                else if (ans.getcorrectopt3()) q.setCorrectOpt(3);
                else if (ans.getcorrectopt4()) q.setCorrectOpt(4);
            }
        }

        if (!questionStore.isEmpty()) {
            loadData(questionStore.get(x));
        } else {
            clearForm();
            questionNo.setText("Question 1");
        }
    }


    private void loadData(Question q) {
        question.setText(q.getQues());
        textopt1.setText(q.getOpttext1());
        textOpt2.setText(q.getOpttext2());
        textOpt3.setText(q.getOpttext3());
        textOpt4.setText(q.getOpttext4());

        correctGroup.selectToggle(null);
        switch (q.getCorrectOpt()) {
            case 1 -> correctGroup.selectToggle(opt1);
            case 2 -> correctGroup.selectToggle(Opt2);
            case 3 -> correctGroup.selectToggle(opt3);
            case 4 -> correctGroup.selectToggle(Opt4);
        }

        questionNo.setText("Question " + (x + 1));
    }

    private int getSelectedOption() {
        if (opt1.isSelected()) return 1;
        if (Opt2.isSelected()) return 2;
        if (opt3.isSelected()) return 3;
        if (Opt4.isSelected()) return 4;
        return 0;
    }

    private void updateCurrentQuestionData() {
        if (x < questionStore.size()) {
            Question q = questionStore.get(x);
            q.setQues(question.getText().trim());
            q.setOpttext1(textopt1.getText().trim());
            q.setOpttext2(textOpt2.getText().trim());
            q.setOpttext3(textOpt3.getText().trim());
            q.setOpttext4(textOpt4.getText().trim());
            q.setCorrectOpt(getSelectedOption());
        }
    }


    @FXML
    private void addBtnClicked() {
        if (!validate()) return;
        if (x < questionStore.size()) {
            updateCurrentQuestionData();
        } else {
            Question q = collectData();
            q.setQuestionId(0); 
            questionStore.add(q);
        }
        x++;
        if (x < questionStore.size()) {
            loadData(questionStore.get(x));
        } else {
            clearForm();
            questionNo.setText("Question " + (x + 1));
        }

        showNotification("Question saved");
    }


    @FXML
    private void prvBtnClicked() {
        if (x == 0) return;

          if (x < questionStore.size()) {
            updateCurrentQuestionData(); 
        } else if (!question.getText().trim().isEmpty()) {
           Question q = collectData();
            q.setQuestionId(0);
            questionStore.add(q);
        }

        x--;
        loadData(questionStore.get(x));
    }

    @FXML
    private void updateBtnClicked() {
        if (questionStore.isEmpty() && question.getText().trim().isEmpty()) {
            alert("Error", "Add at least one question");
            return;
        }

        if (x < questionStore.size()) {
            updateCurrentQuestionData();
        } else if (!question.getText().trim().isEmpty()) {
            Question q = collectData();
            q.setQuestionId(0);
            questionStore.add(q);
        }

        quizSql.updateQuiz(quiz.getId(), quizTitle.getText(), quiz.getCreatedBy());

        for (Question q : questionStore) {
            int questionId = q.getQuestionId();
            if (questionId > 0) {
                quizSql.updateOneQuestion(questionId, q);
            } else {
                questionId = quizSql.insertOneQuestion(quiz.getId(), q);
                q.setQuestionId(questionId);
            }

            adminAnswer ans = new adminAnswer(
                q.getCorrectOpt() == 1,
                q.getCorrectOpt() == 2,
                q.getCorrectOpt() == 3,
                q.getCorrectOpt() == 4
            );
            quizSql.updateAllAnswer(questionId, ans);
        }

        showNotification("Quiz updated successfully");
        PauseTransition delay = new PauseTransition(Duration.seconds(1.3));
        delay.setOnFinished(e -> closeWindow());
        delay.play();
    }


    private Question collectData() {
        int correctOpt = getSelectedOption();
        return new Question(
                question.getText().trim(),
                textopt1.getText().trim(),
                textOpt2.getText().trim(),
                textOpt3.getText().trim(),
                textOpt4.getText().trim(),
                correctOpt
        );
    }

    private boolean validate() {
        if (quizTitle.getText().trim().isEmpty() ||
            question.getText().trim().isEmpty() ||
            textopt1.getText().trim().isEmpty() ||
            textOpt2.getText().trim().isEmpty() ||
            textOpt3.getText().trim().isEmpty() ||
            textOpt4.getText().trim().isEmpty()) {
            alert("Error", "All fields are required");
            return false;
        }

        if (getSelectedOption() == 0) {
            alert("Error", "Select a correct option");
            return false;
        }

        return true;
    }

    private void clearForm() {
        question.clear();
        textopt1.clear();
        textOpt2.clear();
        textOpt3.clear();
        textOpt4.clear();
        opt1.setSelected(false);
        Opt2.setSelected(false);
        opt3.setSelected(false);
        Opt4.setSelected(false);
    }

    private void showNotification(String msg) {
        notificationLabel.setText(msg);
        notificationLabel.setVisible(true);

        PauseTransition p = new PauseTransition(Duration.seconds(2));
        p.setOnFinished(e -> notificationLabel.setVisible(false));
        p.play();
    }

    private void alert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) quizTitle.getScene().getWindow();
        stage.close();
    }
}
