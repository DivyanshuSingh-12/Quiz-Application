package UserController;

import Constraints.Student;
import DataBase.StudentDataSql;
import DataBase.StudentLoginSession;
import DataBase.quizSql;
import DataBase.ResponseSql;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.net.URL;
import java.util.List;
import java.util.Optional;
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
    @FXML private Button refreshBtn;


    @FXML private PieChart performancePie;
    @FXML private LineChart<String, Number> performanceLine;
    @FXML private CategoryAxis quizNamesAxis;
    @FXML private NumberAxis scoreAxis;




    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	 
    	
        updatenotification.setVisible(false);

        if (StudentGender != null) {
            maleOpt.setToggleGroup(StudentGender);
            femaleOpt.setToggleGroup(StudentGender);
            OtherOpt.setToggleGroup(StudentGender);
        }

        loadStudentProfile();
        loadPerformanceChart(); 
        loadPerformanceLineChart();
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
        boolean confirmDelete = showDeleteConfirmation();

        if (!confirmDelete) return;
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



    private void loadPerformanceLineChart() {
        if (StudentLoginSession.loggedStudent == null) return;

        int studentId = StudentLoginSession.loggedStudent.getId();
        List<Integer> quizzes = quizSql.getAllQuizIds();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Scores");

        int maxScore = 0;

        for (int quizId : quizzes) {
            String quizName = quizSql.getQuizTitleById(quizId);
            int score = ResponseSql.evaluateQuiz(studentId, quizId);
            series.getData().add(new XYChart.Data<>(quizName, score));

            if (score > maxScore) maxScore = score;
        }

        performanceLine.getData().clear();
        performanceLine.getData().add(series);

        scoreAxis.setAutoRanging(false);
        scoreAxis.setLowerBound(0);
        scoreAxis.setUpperBound(maxScore + 1);
        scoreAxis.setTickUnit(Math.max(1, (maxScore + 1) / 5.0));

        URL barCss = getClass().getResource("/CSS/chartStyle.css");
        if (barCss != null) performanceLine.getStylesheets().add(barCss.toExternalForm());
        else System.out.println("chartStyle.css not found for lineChart!");
    }

    
    private void loadPerformanceChart() {
        if (StudentLoginSession.loggedStudent == null) return;

        int studentId = StudentLoginSession.loggedStudent.getId();
        List<Integer> quizzes = quizSql.getAllQuizIds(); 

        int passCount = 0;
        int failCount = 0;

        for (int quizId : quizzes) {
            int score = ResponseSql.evaluateQuiz(studentId, quizId);
            int totalQuestions = quizSql.getTotalQuestions(quizId);
            double percentage = (score * 100.0) / totalQuestions;

            if (percentage >= 33) {
                passCount++;
            } else {
                failCount++;
            }
        }

        PieChart.Data passSlice = new PieChart.Data("Pass", passCount);
        PieChart.Data failSlice = new PieChart.Data("Fail", failCount);
        
        performancePie.getData().clear();   
        performancePie.getData().addAll(passSlice, failSlice);
        
        passSlice.getNode().setStyle("-fx-pie-color: #00B7B5;");
        failSlice.getNode().setStyle("-fx-pie-color: #018790;");
       
        URL pieCss = getClass().getResource("/CSS/chartStyle.css");
        if (pieCss != null) performancePie.getStylesheets().add(pieCss.toExternalForm());
        else System.out.println("chartStyle.css not found for PieChart!");
       

        performancePie.applyCss();
        performancePie.layout();
        
    }

    @FXML
    private void refreshBtnFun() {

        if (StudentLoginSession.loggedStudent == null) return;

        loadStudentProfile();
        loadPerformanceLineChart();
        loadPerformanceChart();

        updatenotification.setText("Data refreshed successfully");
    }



    private boolean showDeleteConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Account");
        alert.setHeaderText("Are you sure you want to delete your account?");
        alert.setContentText("This action cannot be undone.");

        ButtonType yesBtn = new ButtonType("Yes, Delete");
        ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yesBtn, cancelBtn);

        Optional<ButtonType> result = alert.showAndWait();

        return result.isPresent() && result.get() == yesBtn;
    }


}
