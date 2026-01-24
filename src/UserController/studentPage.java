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
	   try {
	      Parent addHomeView  = FXMLLoader.load(getClass().getResource("/UserFXML/studentHome.fxml"));
	      StudentProfile.setContent(addHomeView );
	            
	      Parent addResultView = FXMLLoader.load(getClass().getResource("/UserFXML/resultStudent.fxml"));
	      Studentresult.setContent(addResultView);
	            
	      Parent addStudetnQuizView = FXMLLoader.load(getClass().getResource("/UserFXML/quizUser.fxml"));
	      StudentQuiz.setContent(addStudetnQuizView);
	            

	    } catch (IOException e) {
	            e.printStackTrace();
	        }
		}
}


