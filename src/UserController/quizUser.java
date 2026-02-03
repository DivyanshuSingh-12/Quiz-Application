package UserController;



import java.io.IOException;

import Constraints.quizSelection;
import DataBase.quizSql;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class quizUser {

    @FXML private TableView<quizSelection> quizTable;
    @FXML private TableColumn<quizSelection, String> createdByCol;
    @FXML private TableColumn<quizSelection, String> quizTitleCol;
    @FXML private TableColumn<quizSelection, String> statusCol;
    @FXML private Button refreshBtn;
    @FXML private Button deleteBtn;
    @FXML private Button openBtn;

    private boolean testSubmitted = false;

    @FXML
    public void initialize() {
        // Setup table columns
        createdByCol.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        quizTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Load data
        loadQuizzes();

        // Double-click row to open test
        quizTable.setRowFactory(tv -> {
            TableRow<quizSelection> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    openTestWindow(row.getItem());
                }
            });
            return row;
        });
    }

    @FXML
    public void openBtnFun() {
        quizSelection selectedQuiz = quizTable.getSelectionModel().getSelectedItem();
        if (selectedQuiz == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a quiz to open.");
            return;
        }
        openTestWindow(selectedQuiz);
    }
    


    private void openTestWindow(quizSelection quiz) {
        try {
        	testSubmitted = false; // 🔴 RESET HERE
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserFXML/Test.fxml"));
            AnchorPane root = loader.load();
            

            Test testController = loader.getController();
            testController.setQuizId(quiz.getId()); //  quizId passed here
            testController.setQuizUserController(this);           
                        
                        
                        
                        
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Test - " + quiz.getTitle());
            stage.setFullScreen(true);
            stage.setResizable(false);
            stage.setFullScreenExitHint("");
            stage.setFullScreenExitKeyCombination(null);

            // Unified auto-submit listeners
            AutoSubmitLogic(stage, testController);

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the test window.");
        }
    }

    private void AutoSubmitLogic(Stage stage,Test testController) {

        stage.setOnCloseRequest(e -> {
            e.consume();
            submitTest(stage);
        });

        stage.iconifiedProperty().addListener((obs, oldVal, isMinimized) -> { if (isMinimized) submitTest(stage);});
        stage.focusedProperty().addListener((obs, oldVal, isFocused) -> { if (!isFocused) submitTest(stage);});
        stage.fullScreenProperty().addListener((obs, oldVal, isFull) -> {if (!isFull) submitTest(stage); });
    }

    public void onTestSubmitted(int quizId) {
             for (quizSelection qs : quizTable.getItems()) {
                 if (qs.getId() == quizId) {
                     qs.setStatus("ATTEMPTED");
                     quizTable.refresh();
                     break;
                 }
             }
         }

    private void submitTest(Stage stage) {
        if (testSubmitted) return; // avoid multiple submissions
        testSubmitted = true;
        showAlert(Alert.AlertType.INFORMATION, "Test Submitted", "Test submitted!");
        stage.close();
    }

    private void loadQuizzes() {
        ObservableList<quizSelection> data = quizSql.getAllQuizzesForUser();
        quizTable.setItems(data);
    }

    @FXML
    public void refreshBtnFun() {
        loadQuizzes();
    }

    @FXML
    public void deleteBtnFun() {
        quizSelection selectedQuiz = quizTable.getSelectionModel().getSelectedItem();
        if (selectedQuiz == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a quiz to delete.");
            return;
        }

        boolean deleted = quizSql.deleteQuiz(selectedQuiz.getId());
        if (deleted) {
            showAlert(Alert.AlertType.INFORMATION, "Deleted", "Quiz deleted successfully.");
            loadQuizzes();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete quiz.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
