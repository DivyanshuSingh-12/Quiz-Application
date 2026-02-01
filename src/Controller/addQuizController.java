package Controller;

import Constraints.Question;
import Constraints.Admin;
import Constraints.adminAnswer;
import DataBase.LoginSession;
import DataBase.jdbcConnection;
import DataBase.quizSql;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class addQuizController {

    // ===== FXML =====

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
    @FXML private Button submitbtn;

    private ToggleGroup correctGroup;


    // ===== LOGIC =====

    private int x = 0;    // current question index

    private Question questionStore = new Question(); // question list holder
    
    
    private void updateQuestionNo() {
        questionNo.setText("Question " + (x + 1));
    }


    @FXML
    public void initialize() {
        questionNo.setText("Question" + (x + 1));
        notificationLabel.setVisible(false);
        
        correctGroup = new ToggleGroup();
        opt1.setToggleGroup(correctGroup);
        Opt2.setToggleGroup(correctGroup);
        opt3.setToggleGroup(correctGroup);
        Opt4.setToggleGroup(correctGroup);
        updateQuestionNo();

    }

    // ================= ADD / NEXT =================

    @FXML
    private void AddBtnClicked(MouseEvent event) {
        if (!validate()) return;
       
        Question q = collectData();
        if (x < questionStore.size()) {
            questionStore.updateQuestion(x, q);
            // edit existing
        } else {
            questionStore.addQuestion(q); // add new
        }

        x++;
        
        if (x < questionStore.size()) {
            loadData(questionStore.getQuestion(x));
        } else {
            clearFrom();
        }
        updateQuestionNo();
        showNotification();
    }

    // ================= PREVIOUS =================

    @FXML
    private void prvBtnClicked(MouseEvent event) {
        if (x == 0) return;
        x--;
        Question q = questionStore.getQuestion(x);
        loadData(q);
        updateQuestionNo();
    }

    // ================= SUBMIT =================

    @FXML
    private void SubmitBtnClicked(MouseEvent event) {

        if (questionStore.size() == 0) {
            alert("No Questions", "Please add at least one question");
            return;
        }
        
        Admin admin = LoginSession.loggedAdmin;
        if (admin == null) {
            alert("Error", "No admin logged in");
            return;
        }
        
        jdbcConnection.createDatabase();
        quizSql.createQuizTables();
        quizSql.createQuestionTables();
        quizSql.createAnswerTables();

        //int quizId = quizSql.insertQuiz(quizTitle.getText().trim());
        int quizId = quizSql.insertQuiz(quizTitle.getText().trim(), admin.getUserId());

        for (Question q : questionStore.getAllQuestions()) {

            int qid = quizSql.insertOneQuestion(quizId, q);

            adminAnswer ans = new adminAnswer(
                    q.getCorrectOpt() == 1,
                    q.getCorrectOpt() == 2,
                    q.getCorrectOpt() == 3,
                    q.getCorrectOpt() == 4
            );

            quizSql.insertAllAnswer(qid, ans);
        }

        clearFrom();
        quizTitle.clear();
        x = 0;
        updateQuestionNo();
        questionStore = new Question();
        alert("Success", "Quiz submitted successfully");
    }



    private Question collectData() {
    	
    	 int correctOpt = 0;
    	    if (opt1.isSelected()) correctOpt = 1;
    	    else if (Opt2.isSelected()) correctOpt = 2;
    	    else if (opt3.isSelected()) correctOpt = 3;
    	    else if (Opt4.isSelected()) correctOpt = 4;
 
        String qText = question.getText().trim();
        String o1 = textopt1.getText().trim();
        String o2 = textOpt2.getText().trim();
        String o3 = textOpt3.getText().trim();
        String o4 = textOpt4.getText().trim();
      

        return new Question(qText, o1, o2, o3, o4,correctOpt);
    }

    private void loadData(Question q) {

        question.setText(q.getQues());
        textopt1.setText(q.getOpttext1());
        textOpt2.setText(q.getOpttext2());
        textOpt3.setText(q.getOpttext3());
        textOpt4.setText(q.getOpttext4());

        // clear first
        opt1.setSelected(false);
        Opt2.setSelected(false);
        opt3.setSelected(false);
        Opt4.setSelected(false);

        // restore selection
        switch (q.getCorrectOpt()) {
            case 1 -> opt1.setSelected(true);
            case 2 -> Opt2.setSelected(true);
            case 3 -> opt3.setSelected(true);
            case 4 -> Opt4.setSelected(true);
        }
    }

    private boolean validate() {

        if (quizTitle.getText().trim().isEmpty() ||
            question.getText().trim().isEmpty() ||
            textopt1.getText().trim().isEmpty() ||
            textOpt2.getText().trim().isEmpty() ||
            textOpt3.getText().trim().isEmpty() ||
            textOpt4.getText().trim().isEmpty()) {

            alert("Input Error", "All fields are required");
            return false;
        }

        if (!opt1.isSelected() && !Opt2.isSelected()
                && !opt3.isSelected() && !Opt4.isSelected()) {

            alert("Input Error", "Select at least one correct option");
            return false;
        }

        return true;
    }



    private void clearFrom() {
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

    private void showNotification() {
        notificationLabel.setText("Question saved");
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

   
}
