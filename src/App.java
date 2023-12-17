import javafx.fxml.FXMLLoader;

import java.io.FileInputStream;
import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import views.dashController;
import javafx.scene.layout.AnchorPane;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);

        
    }
    @Override
    public void start(Stage stage) throws Exception {
        // Specifying to load FXML document
        FXMLLoader loader = new FXMLLoader();
        String fxmlDocPath = "src/Login.fxml";
        try (FileInputStream fxmlStream = new FileInputStream(fxmlDocPath)) {
            AnchorPane root = (AnchorPane) loader.load(fxmlStream);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
            stage.setScene(scene);
            // stage.getIcons().add(new Image("resources/qcu-logo.png"));
            stage.setTitle("GROUP 5 - Task Scheduler");
            stage.setResizable(false); // Window Sizing Prevention
            stage.show();

            
    

            
        } catch (IOException e) {
            System.err.println("Failed to load FXML file: " + e.getMessage());
        }
    }

    
}
