package Controller;
import java.io.IOException;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class AdminPage {
	@FXML private TabPane adminTabPane;
	@FXML private Tab Main;
	@FXML private Tab addquiz;
	@FXML private Tab addstudent;	
	@FXML private Tab Quizzes; 
	@FXML private Tab Result;
	

	@FXML
	public void initialize() {
	    loadTab(addquiz, "/FXML/addQuiz.fxml");
	    loadTab(addstudent, "/FXML/addStudent.fxml");
	    loadTab(Main, "/FXML/adminHome.fxml");
	    loadTab(Quizzes, "/FXML/manageQuizzes.fxml");
	    loadTab(Result, "/FXML/result.fxml");
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
