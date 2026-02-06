package Controller;

import java.io.File;
import java.util.Optional;

import Constraints.Admin;
import Constraints.Question;
import DataBase.AdminDataSql;
import DataBase.jdbcConnection;
import DataBase.quizSql;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AdminRegisterController {


    @FXML private ImageView adminProfile;
    @FXML  private Button adminprofileBtn;
    private File selectedImageFile;


    @FXML private TextField AdminfirstName;
    @FXML private TextField adminLastName;
    @FXML private TextField adminContact;
    @FXML private TextField AdminuserID;
    @FXML private TextField passwordAdmin;


    @FXML private RadioButton adminMale;
    @FXML private RadioButton adminFemale;
    @FXML private RadioButton adminOther;
    @FXML private ToggleGroup AdminGender;


    @FXML
    private Button AdminRegisterBtn;


    @FXML
    public void initialize() {
    	adminProfile.setImage(new Image(getClass().getResourceAsStream("/defaultprofile.jpg")));
    }


    @FXML
    private void adminprofileBtn() {

        FileChooser fileCh = new FileChooser();
        fileCh.setTitle("Select Profile Image");

        fileCh.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "Image Files", "*.png", "*.jpg", "*.jpeg"
            )
        );

        Stage stage = (Stage) adminProfile.getScene().getWindow();
        File file = fileCh.showOpenDialog(stage);

        if (file != null) {
            adminProfile.setImage(new Image(file.toURI().toString()));
            selectedImageFile = file;
        }
    }



    
    
    
    
    @FXML
    private void AdminRegisterBtn() {

       String firstName = AdminfirstName.getText();
       String lastName = adminLastName.getText();
       String contact = adminContact.getText();
       String  userId = AdminuserID.getText();
       String  password = passwordAdmin.getText();
        RadioButton selectedGender =(RadioButton) AdminGender.getSelectedToggle();


        if (firstName.isEmpty() || lastName.isEmpty() || contact.isEmpty() || userId.isEmpty() ||password.isEmpty() || selectedGender == null) {
            showAlert("Validation Error", "Please fill all fields",false);
            return;
        }else {
        	String  gender = selectedGender.getText();
        	String imagePath = (selectedImageFile != null)
        	        ? selectedImageFile.getAbsolutePath()
        	        : "/defaultprofile.jpg";
        	
        	jdbcConnection.createDatabase();
        	AdminDataSql.CreateAdminDataTable();
            Admin adm = new Admin(firstName,lastName,contact,userId,password,gender,imagePath);
            
            boolean inserted = AdminDataSql.insertAdmin(adm);
            
            if (inserted) {
            	 showAlert("Success", "Admin registered successfully", true);
                clearFrom();
            } else {
                // Check if username already exists
                if (AdminDataSql.flag == false) {
                    alertSql();
                } else {
                    showAlert("Registration Failed", "Username '" + userId + "' already exists. Please choose a different username.",false);
                }
            }
            }
        }

    
    
    private void alertSql() {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
  
   

    private  void clearFrom() {
        AdminfirstName.clear();
        adminLastName.clear();
        adminContact.clear();
        AdminuserID.clear();
        passwordAdmin.clear();
        if (AdminGender.getSelectedToggle() != null) {
            AdminGender.getSelectedToggle().setSelected(false);
        }

        adminProfile.setImage(new Image(getClass().getResourceAsStream("/defaultprofile.jpg")));

        selectedImageFile = null;
   	
    }
    

    private void showAlert(String title, String message, boolean goToLogin) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        if (goToLogin) {
            ButtonType loginBtn = new ButtonType("Go to Login");
            alert.getButtonTypes().setAll(loginBtn);
        }

        alert.showAndWait();

        if (goToLogin) {
            goToLoginPage();
        }
    }

    
    private void goToLoginPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) AdminfirstName.getScene().getWindow();

            Scene scene = new Scene(root, 1366, 768);
            scene.getStylesheets().add(getClass().getResource("/CSS/Login.css").toExternalForm());
            stage.setTitle("Quiz Application");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
