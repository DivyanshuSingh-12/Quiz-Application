package Controller;

import Constraints.Student;
import DataBase.StudentDataSql;
import DataBase.jdbcConnection;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class addStudent implements Initializable {

    @FXML private TextField username;
    @FXML private TextField firstname;
    @FXML private TextField lastname;
    @FXML private TextField contact;

    @FXML private RadioButton male;
    @FXML private RadioButton female;
    @FXML private RadioButton other;
    @FXML private ToggleGroup Gender;

    @FXML private Button saveBtn;
    @FXML private Button removeBtn;

    @FXML private TableView<Student> tableView;
    @FXML private TableColumn<Student, String> colUsername;
    @FXML private TableColumn<Student, String> colFirstName;
    @FXML private TableColumn<Student, String> colLastName;
    @FXML private TableColumn<Student, String> colContact;
    @FXML private TableColumn<Student, String> colGender;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        jdbcConnection.createDatabase();
        StudentDataSql.createStudentTable();


        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));

        loadTable();

        saveBtn.setOnAction(e -> saveStudent());
        removeBtn.setOnAction(e -> removeStudent());
    }


    private void saveStudent() {
        String user = username.getText().trim();
        String first = firstname.getText().trim();
        String last = lastname.getText().trim();
        String cont = contact.getText().trim();
        RadioButton selectedGender = (RadioButton) Gender.getSelectedToggle();

        if (user.isEmpty() || first.isEmpty() || last.isEmpty() || cont.isEmpty() || selectedGender == null) {
            showAlert("Validation Error", "Please fill all fields!");
            return;
        }


        Student student = new Student(0, user, user, first, last, cont, selectedGender.getText());

        boolean inserted = StudentDataSql.insertStudent(student);

        if (inserted) {
            showAlert("Success", "Student inserted successfully!\nPassword is same as username by default.");
            clearForm();
            loadTable();
        } else {
            if (StudentDataSql.flag == false) {
                showSqlError();
            } else {
                showAlert("Insert Failed", "Username '" + user + "' already exists.");
            }
        }
    }


    private void removeStudent() {
        String usernameInput = username.getText().trim();
        Student selected = tableView.getSelectionModel().getSelectedItem();

        if (!usernameInput.isEmpty()) {
            boolean deleted = StudentDataSql.deleteStudent(usernameInput);
            if (deleted) {
                showAlert("Success", "Student with username '" + usernameInput + "' removed successfully!");
                loadTable();
                clearForm();
            } else {
                showAlert("Error", "No student found with username '" + usernameInput + "'.");
            }
        } else if (selected != null) {
            boolean deleted = StudentDataSql.deleteStudent(selected.getUsername());
            if (deleted) {
                showAlert("Success", "Student removed successfully!");
                loadTable();
            } else {
                showSqlError();
            }
        } else {
            showAlert("Selection Error", "Please select a student in the table or enter a username!");
        }
    }


    private void loadTable() {
        ObservableList<Student> students = StudentDataSql.getAllStudents();
        tableView.setItems(students);
    }


    
    
    
    private void clearForm() {
        username.clear();
        firstname.clear();
        lastname.clear();
        contact.clear();
        if (Gender.getSelectedToggle() != null) {
            Gender.getSelectedToggle().setSelected(false);
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSqlError() {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Database Error");
        alert.setHeaderText(null);
        alert.setContentText("Something went wrong with the database!");
        alert.showAndWait();
    }
}
