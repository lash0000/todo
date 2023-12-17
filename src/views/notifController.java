package views;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;


 public class notifController{

    private Stage stage;
    private Scene scene;
    private Parent root;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/task_scheduler";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    
    public void switchToDashboard(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("resources/dashboard.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToNotif(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("resources/notifications.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToManage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("resources/manage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToTodo(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("resources/to-do.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToAddTask(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("resources/AddTask.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchSetting(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("resources/Settings.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchAppSupport(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("resources/AppSupport.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchAppInfo(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("resources/AppInfo.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


  
}
