package Controller;

import Constraints.quizSelection;
import DataBase.quizSql;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class manageQuizzes {

    @FXML private TextField searchInput;
    @FXML private Button searchBtn;
    @FXML private ListView<quizSelection> listresult;
    @FXML private Button updateBtn;
    @FXML private Button deleteBtn;
    @FXML private Button refreshBtn;


    private ObservableList<quizSelection> quizList;

    @FXML
    public void initialize() {
    	 loadAllQuizzes();
        // Double click to open quiz for update
        listresult.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                quizSelection selectedQuiz = listresult.getSelectionModel().getSelectedItem();
                if (selectedQuiz != null) {
                    openUpdateQuizWindow(selectedQuiz);
                }
            }
        });
    }

    @FXML
    private void searchBtnClicked() {
        searchQuizzes();
    }

    @FXML
    private void searchInputfun() {
        searchQuizzes();
    }

    private void searchQuizzes() {
        String keyword = searchInput.getText().trim().toLowerCase();

        if (keyword.isEmpty()) {
            // Show all if input is empty
            listresult.setItems(quizList);
            return;
        }

        ObservableList<quizSelection> filteredList = FXCollections.observableArrayList();

        for (quizSelection q : quizList) {
            if (q.getTitle().toLowerCase().contains(keyword)) {
                filteredList.add(q);
            }
        }

        if (filteredList.isEmpty()) {
            showAlert("No Results", "No quizzes found matching \"" + searchInput.getText().trim() + "\". Showing all quizzes.");
            listresult.setItems(quizList);  // Reset to show all
        } else {
            listresult.setItems(filteredList); // Show filtered results
        }
    }

    @FXML
    private void updatebtnClicked() {
        quizSelection selectedQuiz = listresult.getSelectionModel().getSelectedItem();
        if (selectedQuiz == null) {
            showAlert("Error", "Select a quiz to update.");
            return;
        }
        openUpdateQuizWindow(selectedQuiz);
    }

    @FXML
    private void deleteBtnClicked() {
        quizSelection selectedQuiz = listresult.getSelectionModel().getSelectedItem();
        if (selectedQuiz == null) {
            showAlert("Error", "Select a quiz to delete.");
            return;
        }

        boolean confirmed = showConfirmDialog("Delete Quiz", "Are you sure you want to delete this quiz?");
        if (!confirmed) return;

        boolean deleted = quizSql.deleteQuiz(selectedQuiz.getId());
        if (deleted) {
            quizList.remove(selectedQuiz);
            showAlert("Success", "Quiz deleted successfully.");
        } else {
            showAlert("Error", "Failed to delete quiz.");
        }
    }

    @FXML
    private void refreshbtnClicked() {
        loadAllQuizzes();
    }

    
    private void openUpdateQuizWindow(quizSelection quiz) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/updateQuiz.fxml"));
            Parent root = loader.load();

            Controller.updateQuizController controller = loader.getController();
            controller.setQuizData(quiz);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Update Quiz");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open update quiz window.");
        }
    }
    
    private void loadAllQuizzes() {
        quizList = FXCollections.observableArrayList(
                quizSql.getAllQuizzesForUser()
        );
        listresult.setItems(quizList);
    }


    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private boolean showConfirmDialog(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);

        return alert.showAndWait().filter(response -> response == javafx.scene.control.ButtonType.OK).isPresent();
    }
}
