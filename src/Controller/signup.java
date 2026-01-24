package Controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class signup {   

    @FXML
    private Tab adminRegister;

    @FXML
    private Tab userRegister;

    @FXML
    private TabPane signupTabpane;

    @FXML
    public void initialize() {
        try {
            Parent adminPane = FXMLLoader.load(getClass().getResource("/FXML/AdminRegister.fxml"));
            adminRegister.setContent(adminPane);

            Parent studentPane = FXMLLoader.load(getClass().getResource("/FXML/StudentRegister.fxml")); 
            userRegister.setContent(studentPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
