package Controller;
import java.io.IOException;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;



public class AdminPage {

	@FXML
	private TabPane adminTabPane;
	@FXML
	private Tab Main;
	@FXML
	private Tab addquiz;
	@FXML
	private Tab addstudent;
	
	@FXML private Tab Quizzes;  
	
	@FXML
	public void initialize() {
        try {
            Parent addQuizView  = FXMLLoader.load(getClass().getResource("/FXML/addQuiz.fxml"));
            addquiz.setContent(addQuizView );
            
            Parent addStudentView = FXMLLoader.load(getClass().getResource("/FXML/addStudent.fxml"));
            addstudent.setContent(addStudentView);
            
            Parent addMainView = FXMLLoader.load(getClass().getResource("/FXML/adminHome.fxml"));
            Main.setContent(addMainView);
            
            Parent quizzesView = FXMLLoader.load(getClass().getResource("/FXML/manageQuizzes.fxml"));
            Quizzes.setContent(quizzesView);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	

}
