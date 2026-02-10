package UserController;



import java.io.IOException;

import Constraints.quizSelection;
import DataBase.ResponseSql;
import DataBase.StudentDataSql;
import DataBase.StudentLoginSession;
import DataBase.quizSql;
import javafx.collections.FXCollections;
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
    @FXML private TextField searchbar; 
    @FXML private Button searchBtn;


    private boolean testSubmitted = false;

    @FXML
    public void initialize() {
        // Setup table columns
        createdByCol.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        quizTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadQuizzes();

        // Double-click row to open test
        quizTable.setRowFactory(tv -> {
            TableRow<quizSelection> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    quizSelection quiz = row.getItem();

                    if ("ATTEMPTED".equals(quiz.getStatus())) {
                        showAlert(Alert.AlertType.INFORMATION, "Already Attempted", 
                                  "You have already submitted this quiz.");
                        return;
                    }

                    openTestWindow(quiz);
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
        
        if ("ATTEMPTED".equals(selectedQuiz.getStatus())) {
            showAlert(Alert.AlertType.INFORMATION, "Already Attempted", 
                      "You have already submitted this quiz.");
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
            testController.setQuizId(quiz.getId()); 
            testController.setQuizUserController(this); 
            currentTestController = testController;
            
            testController.setUserId(StudentLoginSession.loggedStudent.getId());
            
                        
                        
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Test - " + quiz.getTitle());
            stage.setFullScreen(true);
            stage.setResizable(false);
            stage.setFullScreenExitHint("");
            stage.setFullScreenExitKeyCombination(null);

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

        stage.iconifiedProperty().addListener((obs, oldVal, isMinimized) -> { if (isMinimized) submitTest(stage); });
        stage.focusedProperty().addListener((obs, oldVal, isFocused) -> { if (!isFocused) submitTest(stage); });
        stage.fullScreenProperty().addListener((obs, oldVal, isFull) -> {if (!isFull) submitTest(stage); });
    }


    private Test currentTestController;

    private void submitTest(Stage stage) {
        if (currentTestController != null) {
            currentTestController.submitTest(stage); 
            loadQuizzes();
        }
    }

    

    private void loadQuizzes() {

        ObservableList<quizSelection> data = FXCollections.observableArrayList();
        int userId = StudentLoginSession.loggedStudent.getId();

        for (quizSelection qs : quizSql.getAllQuizzesForUser()) {
            int quizId = qs.getId();

            String quizStatus = quizSql.getQuizStatus(quizId);
            if ("CLOSED".equalsIgnoreCase(quizStatus))  continue;
            if (StudentDataSql.isQuizHiddenForUser(quizId, userId)) continue;

            boolean attempted = ResponseSql.isQuizAttempted(quizId, userId);
            qs.setStatus(attempted ? "ATTEMPTED" : "NOT ATTEMPTED");
            data.add(qs);
        }

        quizTable.setItems(data);
        quizTable.refresh();
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

        int userId = StudentLoginSession.loggedStudent.getId();
        boolean hidden = StudentDataSql.hideQuizForUser(userId, selectedQuiz.getId());

        if (hidden) {
            showAlert(Alert.AlertType.INFORMATION, "Deleted", "Quiz deleted successfully.");
            loadQuizzes(); 
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to hide quiz.");
        }
    }

    @FXML
    private void searchBtnClicked() {
        String query = searchbar.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            loadQuizzes();
            return;
        }

        ObservableList<quizSelection> filteredData = FXCollections.observableArrayList();
        int userId = StudentLoginSession.loggedStudent.getId();

        for (quizSelection qs : quizSql.getAllQuizzesForUser()) {
            if (StudentDataSql.isQuizHiddenForUser(qs.getId(), userId)) continue;

            if (qs.getTitle().toLowerCase().contains(query)) {
                boolean attempted = ResponseSql.isQuizAttempted(qs.getId(), userId);
                qs.setStatus(attempted ? "ATTEMPTED" : "NOT ATTEMPTED");
                filteredData.add(qs);
            }
        }

        if (filteredData.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No Results", "No quizzes found matching your search.");
            loadQuizzes();
        } else {
            quizTable.setItems(filteredData);
            quizTable.refresh();
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
