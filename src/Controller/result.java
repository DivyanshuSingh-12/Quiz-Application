package Controller;

import Constraints.quizSelection;
import DataBase.AdminLoginSession;
import DataBase.quizSql;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class Result {

    @FXML private ListView<quizSelection> resultList;
    @FXML private RadioButton openRadio;
    @FXML private RadioButton closeRadio;
    @FXML private Button viewBtn;
    @FXML private Button refreshBtn;

    private ToggleGroup toggleGroup;
    private ObservableList<quizSelection> quizList;


    @FXML
    public void initialize() {
        toggleGroup = new ToggleGroup();
        openRadio.setToggleGroup(toggleGroup);
        closeRadio.setToggleGroup(toggleGroup);

        loadQuizzes();

        resultList.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> updateQuizStatusView(newVal)
        );

        resultList.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                viewBtnClicked();
            }
        });
    }


    private void loadQuizzes() {

        quizList = FXCollections.observableArrayList();
        String adminId = AdminLoginSession.loggedAdmin.getUserId();

        List<Integer> quizIds = quizSql.getAllQuizIdsByAdmin(adminId);

        for (int quizId : quizIds) {

            String title = quizSql.getQuizTitleById(quizId);
            String status = quizSql.getQuizStatus(quizId);

            quizList.add(new quizSelection(quizId,adminId,title,status));
        }

        resultList.setItems(quizList);
    }

    
    @FXML private void openRadioFun() {changeQuizStatus("OPEN");}
    @FXML private void closeRadioFun() {changeQuizStatus("CLOSED");}
  
    private void changeQuizStatus(String status) {

        quizSelection quiz = resultList.getSelectionModel().getSelectedItem();
        if (quiz == null) {
            showAlert("Error", "Select a quiz first");
            return;
        }

        boolean success = "OPEN".equals(status)
                ? quizSql.openQuiz(quiz.getId())
                : quizSql.closeQuiz(quiz.getId());

        if (success) {
            quiz.setStatus(status);
            resultList.refresh();
            showAlert("Success", "Quiz status updated");
        } else {
            showAlert("Error", "Failed to update quiz status");
        }
    }


    private void updateQuizStatusView(quizSelection quiz) {
        if (quiz == null) return;
        if ("OPEN".equalsIgnoreCase(quiz.getStatus())) {
            openRadio.setSelected(true);
        } else {
            closeRadio.setSelected(true);
        }
    }


    @FXML
    private void viewBtnClicked() {
        quizSelection quiz = resultList.getSelectionModel().getSelectedItem();
        if (quiz == null) {
            showAlert("Error", "Select a quiz first");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/separateResult.fxml"));
            Parent root = loader.load();
            separateResult controller = loader.getController();
            controller.loadQuizResult(quiz.getId(), quiz.getTitle());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Quiz Results");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void refreshBtnClicked() {
        loadQuizzes();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
