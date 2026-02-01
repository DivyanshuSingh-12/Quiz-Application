package UserController;

import Constraints.quizSelection;
import DataBase.quizSql;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class quizUser {

    @FXML private TableView<quizSelection> quizTable;
    

    @FXML private TableColumn<quizSelection, String> createdByCol;

    @FXML private TableColumn<quizSelection, String> quizTitleCol;

    @FXML private TableColumn<quizSelection, String> statusCol;

    @FXML
    public void initialize() {
        createdByCol.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        quizTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        loadQuizzes();
    }

    private void loadQuizzes() {
        ObservableList<quizSelection> data = quizSql.getAllQuizzesForUser();
        quizTable.setItems(data);
    }
}
