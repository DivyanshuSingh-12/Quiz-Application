package Start;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Quizmain extends Application {

	
  @Override
  public void start(Stage stage) throws Exception {

      Parent root = FXMLLoader.load(getClass().getResource("/FXML/Login.fxml"));
      
      Scene scene = new Scene(root, 1366 , 768);
      scene.getStylesheets().addAll(
      		getClass().getResource("/CSS/Login.css").toExternalForm()       		
      		);
      stage.setTitle("Quiz Application");
      stage.setScene(scene);
      stage.show();
      
      stage.setOnCloseRequest(event -> shutdown());
  }
  
  @Override
  public void stop() { shutdown();}

  private void shutdown() {
      try {
          DataBase.jdbcConnection.closeConnection();
      } catch (Exception e) {
          e.printStackTrace();
      } finally {
          System.exit(0);
      }
  }

    public static void main(String[] args) {
        launch(args);
    }
}

	
	
	
	
	
	
	
	
	
	
	
	






