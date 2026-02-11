package UserController;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;


public class studentPage {

    @FXML private TabPane studentpane;
    @FXML private Tab StudentProfile;
    @FXML private Tab Studentresult;
    @FXML private Tab StudentQuiz;

    @FXML
    public void initialize() {
        loadTab(StudentProfile, "/UserFXML/studentProfile.fxml");
        loadTab(Studentresult, "/UserFXML/resultStudent.fxml");
        loadTab(StudentQuiz, "/UserFXML/quizUser.fxml");
    }


    private void loadTab(Tab tab, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            tab.setContent(view);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }
}
