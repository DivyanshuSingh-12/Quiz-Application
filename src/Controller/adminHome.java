package Controller;

import Constraints.Admin;
import DataBase.LoginSession;
// <-- adjust if your DAO class name differs
import DataBase.UserDataSql;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class adminHome implements Initializable {



    @FXML
    private ImageView profileImage;

    @FXML
    private TextField firstName;

    @FXML
    private TextField contact;
    
    @FXML
    private TextField lastName;

    @FXML
    private TextField userId;

    @FXML
    private TextField password;

    @FXML
    private RadioButton male;

    @FXML
    private RadioButton female;

    @FXML
    private RadioButton Other;



    @FXML
    private ListView<String> QuizList;



    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private PieChart pieChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setupGenderToggle();
        loadAdminProfile();     // <-- CHANGED
        loadQuizList();
        loadBarChart();
        loadPieChart();
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

    private void loadQuizList() {
        ObservableList<String> quizzes = FXCollections.observableArrayList(
                "Java Basics",
                "OOP Concepts",
                "DBMS",
                "Operating Systems",
                "Computer Networks"
        );
        QuizList.setItems(quizzes);
    }

    private void loadBarChart() {

        barChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Quiz Attempts");

        series.getData().add(new XYChart.Data<>("Java", 120));
        series.getData().add(new XYChart.Data<>("DBMS", 95));
        series.getData().add(new XYChart.Data<>("OS", 80));
        series.getData().add(new XYChart.Data<>("CN", 60));

        barChart.getData().add(series);
    }

    private void loadPieChart() {

        pieChart.getData().clear();

        pieChart.setData(FXCollections.observableArrayList(
                new PieChart.Data("Pass", 75),
                new PieChart.Data("Fail", 25)
        ));
    }
}
