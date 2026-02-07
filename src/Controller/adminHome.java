package Controller;

import Constraints.Admin;
import Constraints.quizSelection;
import DataBase.LoginSession;
import DataBase.ResponseSql;
import DataBase.AdminDataSql;
import DataBase.quizSql;
import javafx.animation.PauseTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.text.Text;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class adminHome implements Initializable {
	@FXML private Label updatenotification;
	@FXML private Button updatebtn;
	@FXML private Button deletebtn;
	@FXML private Button logoutbtn;
    @FXML private ImageView profileImage;
    @FXML private TextField firstName;
    @FXML private TextField contact; 
    @FXML private TextField lastName;
    @FXML private TextField userId;
    @FXML private TextField password;
    @FXML private RadioButton male;
    @FXML private RadioButton female;
    @FXML private RadioButton Other;
    @FXML private ListView<String> QuizList;
    @FXML private BarChart<String, Number> barChart;
    @FXML private PieChart pieChart;
    @FXML private Button refreshBtn;
    


    @FXML private Text totalAttemptLabel;
    @FXML private Text totalStudentLabel;
    @FXML private Text avrgScoreLabel;
    @FXML private Text totalQuizLabel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setupGenderToggle();
        loadAdminProfile();     
        loadQuizList();
        loadBarChart();
        loadPieChart();
        loadDashboardStats(); 
       updatenotification.setVisible(false);
       
       QuizList.setOnMouseClicked(event -> {
    	    if (event.getClickCount() != 2) return;

    	    String selectedTitle = QuizList.getSelectionModel().getSelectedItem();
    	    if (selectedTitle == null) return; 

    	    quizSelection quiz = quizSql.getQuizByTitle(selectedTitle);
    	    if (quiz == null) return;
    	    openUpdateQuizWindow(quiz); 
    	});

    }

    private void setupGenderToggle() {
        ToggleGroup genderGroup = new ToggleGroup();
        male.setToggleGroup(genderGroup);
        female.setToggleGroup(genderGroup);
        Other.setToggleGroup(genderGroup);
    }



    private void loadAdminProfile() {

        Admin admin = LoginSession.loggedAdmin;

        if (admin == null) return;

        firstName.setText(admin.getFirstName());
        lastName.setText(admin.getLastName());
        userId.setText(admin.getUserId());
        password.setText(admin.getPassword());
        contact.setText(admin.getContact());

        switch (admin.getgender()) {
            case "Male" -> male.setSelected(true);
            case "Female" -> female.setSelected(true);
            default -> Other.setSelected(true);
        }

        if (admin.getimagePath() != null && !admin.getimagePath().isEmpty()) {
            File file = new File(admin.getimagePath());
            if (file.exists()) {
                profileImage.setImage(new Image(file.toURI().toString()));
            }
        }
    }
    
    
    @FXML
    private void updateBtnFun(ActionEvent event) {

        Admin admin = LoginSession.loggedAdmin;
        if (admin == null) return;

        admin.setFirstName(firstName.getText());
        admin.setLastName(lastName.getText());
        admin.setUserId(userId.getText());
        admin.setPassword(password.getText());
        admin.setContact(contact.getText());

        if (male.isSelected()) admin.setgender("Male");
        else if (female.isSelected()) admin.setgender("Female");
        else admin.setgender("Other");

        boolean updated = AdminDataSql.updateAdmin(admin);

        if (updated) {
            showUpdateNotification("Profile updated!");
            loadAdminProfile();
        } else {
            showUpdateNotification("Update failed!");
        }
    }


    @FXML
    private void deleteBtnFun(ActionEvent event) {
         Admin admin = LoginSession.loggedAdmin;
         if (admin == null) return;
           boolean deleted =  AdminDataSql.deleteAdmin(admin.getUserId());

           if (deleted) {
               showUpdateNotification("Account deleted!");
               PauseTransition pause = new PauseTransition(Duration.seconds(1.3)); 
               pause.setOnFinished(e -> logoutBtnFun()); 
               pause.play();
           } else {
               showUpdateNotification("Deletion failed!");
           }
         
    }

    
    
    
    @FXML
    private void logoutBtnFun() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) logoutbtn.getScene().getWindow();

            Scene scene = new Scene(root, 1366, 768);
            scene.getStylesheets().add(getClass().getResource("/CSS/Login.css").toExternalForm());
            stage.setTitle("Quiz Application");
            stage.setScene(scene);
            stage.show();

            LoginSession.loggedAdmin = null;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    @FXML
    private void refreshBtnFun(ActionEvent event) {
        loadAdminProfile();     
        loadQuizList();
        loadBarChart();
        loadPieChart();
        loadDashboardStats(); 
    }    
    
    private void loadQuizList() {
        Admin admin = LoginSession.loggedAdmin;
        ObservableList<String> quizzes = quizSql.getQuizzesByAdmin(admin.getUserId());
        QuizList.setItems(quizzes);
    }





    private void loadBarChart() {
        if (LoginSession.loggedAdmin == null) return;

        barChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Quiz Attempts");

        String adminId = LoginSession.loggedAdmin.getUserId();
        List<Integer> quizIds = quizSql.getAllQuizIdsByAdmin(adminId);

        for (int quizId : quizIds) {
            String quizTitle = quizSql.getQuizTitleById(quizId);
            List<Integer> studentIds = ResponseSql.getStudentIdsAttemptedQuiz(quizId);
            int attempts = studentIds.size(); // number of students who attempted this quiz

            series.getData().add(new XYChart.Data<>(quizTitle, attempts));
        }

        barChart.getData().add(series);

        barChart.getStylesheets().add(
            getClass().getResource("/CSS/chartStyle.css").toExternalForm()
        );
    }



    private void loadPieChart() {
        if (LoginSession.loggedAdmin == null) return;

        String adminId = LoginSession.loggedAdmin.getUserId();
        List<Integer> quizIds = quizSql.getAllQuizIdsByAdmin(adminId);

        int passCount = 0;
        int failCount = 0;

        for (int quizId : quizIds) {
            int totalQuestions = quizSql.getTotalQuestions(quizId);
            List<Integer> studentIds = ResponseSql.getStudentIdsAttemptedQuiz(quizId);

            for (int studentId : studentIds) {
                int score = ResponseSql.evaluateQuiz(studentId, quizId);
                double percentage = (score * 100.0) / totalQuestions;

                if (percentage >= 33) passCount++;
                else failCount++;
            }
        }

        PieChart.Data passSlice = new PieChart.Data("Pass", passCount);
        PieChart.Data failSlice = new PieChart.Data("Fail", failCount);

        pieChart.getData().clear();
        pieChart.getData().addAll(passSlice, failSlice);

        passSlice.getNode().setStyle("-fx-pie-color: #00B7B5;");
        failSlice.getNode().setStyle("-fx-pie-color: #018790;");


        pieChart.getStylesheets().add(
            getClass().getResource("/css/chartStyle.css").toExternalForm()
        );
    

        pieChart.applyCss();
        pieChart.layout();
    
    }

    public static int getTotalQuizzes(String adminId) {
        return quizSql.executeIntQuery(
            "SELECT COUNT(*) FROM quiz WHERE admin_id = ?",
            adminId
        );
    }

    public static int getTotalAttempts(String adminId) {
        return quizSql.executeIntQuery(
            """
            SELECT COUNT(DISTINCT sr.user_id, sr.quiz_id)
            FROM student_response sr
            JOIN quiz q ON sr.quiz_id = q.id
            WHERE q.admin_id = ?
            """,
            adminId
        );
    }


    public static int getTotalStudents() {
        return quizSql.executeIntQuery(
            "SELECT COUNT(*) FROM Studentdata"
        );
    }


    public static double getAverageScore(String adminId) {
        return quizSql.executeDoubleQuery(
            """
            SELECT AVG(score) FROM (
                SELECT sr.user_id, sr.quiz_id, COUNT(*) AS score
                FROM student_response sr
                JOIN admin_answer aa ON sr.question_id = aa.question_id
                JOIN quiz q ON sr.quiz_id = q.id
                WHERE q.admin_id = ?
                  AND sr.opt1 = aa.opt1
                  AND sr.opt2 = aa.opt2
                  AND sr.opt3 = aa.opt3
                  AND sr.opt4 = aa.opt4
                GROUP BY sr.user_id, sr.quiz_id
            ) t
            """,
            adminId
        );
    }



    private void loadDashboardStats() {
        if (LoginSession.loggedAdmin == null) return;
        String adminId = LoginSession.loggedAdmin.getUserId();
        totalQuizLabel.setText(String.valueOf(getTotalQuizzes(adminId)));
        totalAttemptLabel.setText(String.valueOf(getTotalAttempts(adminId)));
        totalStudentLabel.setText(String.valueOf(getTotalStudents()));
        avrgScoreLabel.setText(String.format("%.2f", getAverageScore(adminId)));
    }

    private void openUpdateQuizWindow(quizSelection quiz) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/updateQuiz.fxml"));
            Parent root = loader.load();

            updateQuizController controller = loader.getController();
            controller.setQuizData(quiz); 

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Update Quiz");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   


    
    private void showUpdateNotification(String message) {
        updatenotification.setText(message);
        updatenotification.setVisible(true);

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> updatenotification.setVisible(false));
        pause.play();
    }

}
