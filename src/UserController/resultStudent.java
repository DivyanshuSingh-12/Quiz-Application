package UserController;

import java.io.IOException;
import java.util.stream.Collectors;

import Constraints.studentResult;
import DataBase.ResponseSql;
import DataBase.StudentLoginSession;
import DataBase.quizSql;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class resultStudent {

    @FXML private TableView<studentResult> studentResult;
    @FXML private TableColumn<studentResult, String> quizTitleCol;
    @FXML private TableColumn<studentResult, Integer> myPointsCol;
    @FXML private TableColumn<studentResult, Integer> totalPointsCol;
    @FXML private Button exploreBtn;
    @FXML private Button refreshBtn;
    @FXML private TextField searchBar;
    @FXML private Button searchBtn;

    private final ObservableList<studentResult> list = FXCollections.observableArrayList();

  
    @FXML
    public void initialize() {

        quizTitleCol.setCellValueFactory(new PropertyValueFactory<>("quizTitle"));
        myPointsCol.setCellValueFactory(new PropertyValueFactory<>("myPoints"));
        totalPointsCol.setCellValueFactory(new PropertyValueFactory<>("totalPoints"));

        studentResult.setItems(list);
        loadData();
    }


    private void loadData() {

        list.clear();
        int userId = StudentLoginSession.loggedStudent.getId();

        quizSql.getAttemptedQuizzes(userId).forEach(q -> {

            int quizId = q.getId();
            String title = q.getTitle();

            int myScore = ResponseSql.evaluateQuiz(userId, quizId);
            int totalPoints = quizSql.getTotalQuestions(quizId);

            list.add(new studentResult(quizId,title,myScore,totalPoints));       
           });
    }
    

    @FXML
    private void exploreBtnFun() {
        studentResult selected = studentResult.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        int quizId = selected.getQuizId();
        String quizTitle = selected.getQuizTitle();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserFXML/separateResult.fxml"));
            Parent root = loader.load();

            separateResult controller = loader.getController();
            controller.loadQuizResul(quizId, quizTitle);

            Stage stage = new Stage();
            stage.setTitle("Quiz Review: " + quizTitle);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void searchBtnClicked() {
        String query = searchBar.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            loadData();
            return;
        }

        ObservableList<studentResult> filtered = list.stream()
                .filter(q -> q.getQuizTitle().toLowerCase().contains(query))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        if (filtered.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No Results", "No quizzes found matching your search.");
            loadData();
        } else {
        	studentResult.setItems(filtered);
        	studentResult.refresh();
        }
    }

    @FXML
    private void refreshBtnFun() {
        loadData();
    }
    
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
