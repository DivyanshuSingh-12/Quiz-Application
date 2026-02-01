package Controller;

import javafx.fxml.FXML;
import DataBase.LoginSession;
import DataBase.StudentDataSql;
import DataBase.StudentLoginSession;
import DataBase.UserDataSql;
import DataBase.jdbcConnection;
import Constraints.Admin;
import Constraints.Student;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.event.ActionEvent;

public class QuizLogin {

	@FXML
	private Label LoginMsg;
	@FXML
	private Label signUp;
    // ---------- ADMIN ----------
    @FXML
    private VBox AdminVbox;
    @FXML
    private TextField adminUser;
    @FXML
    private TextField adminPassword;
    @FXML
    private Button adminLoginBtn;

    // ---------- STUDENT ----------
    @FXML
    private VBox studentVbox;
    @FXML
    private TextField studentUser;
    @FXML
    private TextField studentPassword;
    @FXML
    private Button stdLoginBtn;

   
    
    // ---------- Admin and student Vbox color ----------
    @FXML
    private void initialize() {
    	jdbcConnection.createDatabase();       
        UserDataSql.CreateAdminDataTable();
        AdminVbox.getStyleClass().add("admin-default");
        studentVbox.getStyleClass().add("student-default");
    }
    
    @FXML
    private void Adminselected(MouseEvent event) {
        selectAdmin();
    }

    @FXML
    private void studentSelected(MouseEvent event) {
        selectStudent();
    }
    
    @FXML
    private void Adminuserfieldclicked(MouseEvent event) {
    	 selectAdmin();
    }
    @FXML
    private void adminPassclicked(MouseEvent event) {
    	selectAdmin();
    }
    @FXML
    private void adminLoginClicked(MouseEvent event) {
    	selectAdmin();
    }

    @FXML
    private void studentUserclicked(MouseEvent event) {
    	selectStudent();
    }
    
    @FXML
    private void studentPasswordclicked(MouseEvent event) {
    	selectStudent();
    }
    
    @FXML
    private void studentLoginClicked(MouseEvent event) {
    	selectStudent();
    }  
    
    
    //Color change algorithm
	@FXML
	private void selectAdmin() {
	    // Select Admin
	    if (!AdminVbox.getStyleClass().contains("selected")) {
	        AdminVbox.getStyleClass().add("selected");
	        AdminVbox.getStyleClass().remove("admin-default");
	    }
	
	    // Deselect Student
	    studentVbox.getStyleClass().remove("selected");
	    if (!studentVbox.getStyleClass().contains("student-default")) {
	        studentVbox.getStyleClass().add("student-default");
	    }
	}
	
	@FXML
	private void selectStudent() {
	    // Select Student
	    if (!studentVbox.getStyleClass().contains("selected")) {
	        studentVbox.getStyleClass().add("selected");
	        studentVbox.getStyleClass().remove("student-default");
	    }
	
	    // Deselect Admin
	    AdminVbox.getStyleClass().remove("selected");
	    if (!AdminVbox.getStyleClass().contains("admin-default")) {
	        AdminVbox.getStyleClass().add("admin-default");
	    }
	}
    
 

	@FXML
	private void adminLogin(ActionEvent event) {


	    String user = adminUser.getText().trim();
	    String password = adminPassword.getText().trim();

	    if (user.isEmpty() || password.isEmpty()) {
	        LoginMsg.setText("Enter credentials");
	        LoginMsg.setVisible(true);
	        return;
	    }
	    jdbcConnection.createDatabase();
	    UserDataSql.CreateAdminDataTable();


	    Admin admin = UserDataSql.getAdminByLogin(user, password);

	    if (admin == null) {
	        LoginMsg.setText("Login Failed");
	        LoginMsg.setVisible(true);
	        return;
	    }
	    LoginSession.loggedAdmin = admin;
	    LoginMsg.setVisible(false);

	    try {
	        Parent root = FXMLLoader.load(getClass().getResource("/FXML/adminPage.fxml"));
	        Scene scene = new Scene(root, 1366, 768);
	        Stage stage = (Stage) adminLoginBtn.getScene().getWindow();
	        stage.setScene(scene);
	        stage.show();

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}


  

 

	  @FXML
	    private void studentLogin(ActionEvent event) {
	        String user = studentUser.getText().trim();
	        String password = studentPassword.getText().trim();

	        if (user.isEmpty() || password.isEmpty()) {
	            LoginMsg.setText("Enter credentials");
	            LoginMsg.setVisible(true);
	            return;
	        }

	        jdbcConnection.createDatabase();
             // Fetch student from DB
	        Student student = StudentDataSql.getStudentByLogin(user, password);

	        if (student == null) {
	            LoginMsg.setText("Login Failed");
	            LoginMsg.setVisible(true);
	            return;
	        }

	        // Store logged-in student in session
	        StudentLoginSession.loggedStudent = student;

	        // Load student page
	        try {
	            Parent root = FXMLLoader.load(getClass().getResource("/UserFXML/studentPage.fxml"));
	            Scene scene = new Scene(root, 1366, 768);
	            Stage stage = (Stage) stdLoginBtn.getScene().getWindow();
	            stage.setScene(scene);
	            stage.show();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
    
    
    @FXML
    private void signUp(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/Adminregister.fxml"));
            Stage stage = (Stage) signUp.getScene().getWindow();
            Scene scene = new Scene(root, 1366 , 768);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }  


}














