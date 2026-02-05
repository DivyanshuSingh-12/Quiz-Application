package UserController;

import Constraints.Question;
import DataBase.ResponseSql;
import DataBase.StudentLoginSession;
import DataBase.quizSql;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class separateResult {

    @FXML private Label quizIdLabel;

    @FXML private TableView<Question> tableView;
    @FXML private TableColumn<Question, String> questionCol;
    @FXML private TableColumn<Question, String> correctCol;
    @FXML private TableColumn<Question, String> myAnswerCol;

    public void loadQuizResul(int quizId, String quizTitle) {

        quizIdLabel.setText(quizTitle);

        int userId = StudentLoginSession.loggedStudent.getId();

        questionCol.setCellValueFactory(q ->
                new SimpleStringProperty(q.getValue().getQues())
        );

        correctCol.setCellValueFactory(q ->
                new SimpleStringProperty(
                        getOptionText(
                                q.getValue(),
                                ResponseSql.getCorrectOption(q.getValue().getQuestionId()
                                )
                        )
                )
        );

        myAnswerCol.setCellValueFactory(q ->
                new SimpleStringProperty(
                        getOptionText(
                                q.getValue(),
                                ResponseSql.getSelectedOption(userId,quizId,q.getValue().getQuestionId()
                                )
                        )
                )
        );

        tableView.setItems(FXCollections.observableArrayList(quizSql.getQuestionsByQuizId(quizId)));
    }

    private String getOptionText(Question q, int opt) {
        return switch (opt) {
            case 1 -> "1. " + q.getOpttext1();
            case 2 -> "2. " + q.getOpttext2();
            case 3 -> "3. " + q.getOpttext3();
            case 4 -> "4. " + q.getOpttext4();
            default -> "Not Answered";
        };
    }
}
