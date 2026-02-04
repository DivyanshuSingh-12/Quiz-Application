package UserController;

import Constraints.Student;
import DataBase.StudentDataSql;
import DataBase.StudentLoginSession;
import DataBase.jdbcConnection;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class studentProfile implements Initializable {
	@FXML private Label updatenotification;   

    @FXML private TextField firstName;
    @FXML private TextField lastName;
    @FXML private TextField contact;
    @FXML private TextField userNmae;   
    @FXML private TextField password;


    @FXML private RadioButton maleOpt;
    @FXML private RadioButton femaleOpt;
    @FXML private RadioButton OtherOpt; 

    @FXML private ToggleGroup StudentGender;


    @FXML private Button updateBtn;
    @FXML private Button deleteBtn;
    @FXML private Button logoutbtn;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        updatenotification.setVisible(false);

        if (StudentGender != null) {
            maleOpt.setToggleGroup(StudentGender);
            femaleOpt.setToggleGroup(StudentGender);
            OtherOpt.setToggleGroup(StudentGender);
        }

        loadStudentProfile();
    }


    private void loadStudentProfile() {
        Student student = StudentLoginSession.loggedStudent;
        if (student == null) return;

        firstName.setText(student.getFirstName());
        lastName.setText(student.getLastName());
        contact.setText(student.getContact());
        userNmae.setText(student.getUsername());
        password.setText(student.getPassword());

        switch (student.getGender()) {
            case "Male" -> maleOpt.setSelected(true);
            case "Female" -> femaleOpt.setSelected(true);
            default -> OtherOpt.setSelected(true);
        }
    }

    @FXML
    private void updatebtnFun() {

        String fName = firstName.getText();
        String lName = lastName.getText();
        String phone = contact.getText();
        String username = userNmae.getText();
        String pass = password.getText();
        String gender = getSelectedGender();

        Student updatedStudent = new Student(StudentLoginSession.loggedStudent.getId(),username,pass,
                fName,lName,phone,gender);

        boolean updated = StudentDataSql.updateStudent(updatedStudent);

        if (updated) {
            StudentLoginSession.loggedStudent = updatedStudent;
            showUpdateNotification("Profile updated successfully!");
            loadStudentProfile(); 
        } else showUpdateNotification("Update failed!");
    }

 
    @FXML
    private void deleteBtnFun() {
        String username = userNmae.getText();
        boolean deleted = StudentDataSql.deleteStudentByUsername(username);

        if (deleted) {
            showUpdateNotification("Account deleted successfully!");
            PauseTransition pause = new PauseTransition(Duration.seconds(1)); 
            pause.setOnFinished(e -> LogoutBtnFun()); // logout after pause
            pause.play();
        } else {
            showUpdateNotification("Deletion failed!");
        }
    }


    @FXML
    private void LogoutBtnFun() {
        try {
            FXMLLoader loader = new FXMLLoader( getClass().getResource("/FXML/Login.fxml") );

            Parent root = loader.load();
            Stage stage = (Stage) logoutbtn.getScene().getWindow();
            Scene scene = new Scene(root, 1366, 768);
            scene.getStylesheets().add(
                   getClass().getResource("/CSS/Login.css").toExternalForm()
            );

            stage.setTitle("Quiz Application");
            stage.setScene(scene);
            stage.show();

            StudentLoginSession.loggedStudent = null;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String getSelectedGender() {
        Toggle selected = StudentGender.getSelectedToggle();
        if (selected == maleOpt) return "Male";
        if (selected == femaleOpt) return "Female";
        if (selected == OtherOpt) return "Other";
        return null;
    }
    
    private void showUpdateNotification(String message) {
        updatenotification.setText(message);
        updatenotification.setVisible(true);

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> updatenotification.setVisible(false));
        pause.play();
    }


}
