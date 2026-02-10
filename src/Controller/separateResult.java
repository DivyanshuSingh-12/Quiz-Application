package Controller;

import DataBase.ResponseSql;
import DataBase.StudentDataSql;
import DataBase.quizSql;
import Constraints.quizResult;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class separateResult {

    @FXML private Label quizTitle;
    @FXML private Label totalScore;
    @FXML private TableView<quizResult> resultTable;
    @FXML private TableColumn<quizResult, String> userIdCol;    
    @FXML private TableColumn<quizResult, String> userNameCol; 
    @FXML private TableColumn<quizResult, Integer> scoreCol;
    @FXML private Button refreshBtn;

    private int currentQuizId;  
    
    @FXML
    public void initialize() {
    	 userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));     
         userNameCol.setCellValueFactory(new PropertyValueFactory<>("userName"));   
         scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));
    }

    public void loadQuizResul(int quizId, String title) {
        this.currentQuizId = quizId;
        quizTitle.setText(title);
        loadResults(quizId);
    }

    private void loadResults(int quizId) {
        List<Integer> studentIds = ResponseSql.getStudentIdsAttemptedQuiz(quizId);

        ObservableList<quizResult> data = FXCollections.observableArrayList();


        for (int userId : studentIds) {
            int score = ResponseSql.evaluateQuiz(userId, quizId);

            String username = StudentDataSql.getUsernameById(userId);                
            String fullName = StudentDataSql.getStudentFullNameById(userId);         

            data.add(new quizResult(username, fullName, score));
        }

        resultTable.setItems(data);
        int totalQuizScore = quizSql.getTotalQuestions(quizId);
        totalScore.setText("Total Score : " + totalQuizScore);
    }

    
    @FXML private void refreshBtnClicked() {loadResults(currentQuizId);}
}
