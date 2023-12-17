package views;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class settingsController {
        // for personal information setting
    public TextField retrieveName;
    public PasswordField retrievePWD;
    //provide for todo tab

    private Stage stage;
    private Scene scene;
    private Parent root;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/task_scheduler";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    

    @FXML
    private void retrieveUser() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT email, password FROM registration WHERE id = 1";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String name = resultSet.getString("email");
                        String password = resultSet.getString("password");
                        // Check if the text fields are not null before setting text
                        if (retrieveName != null && retrievePWD != null) {
                            retrieveName.setText(name);
                            retrievePWD.setText(password);
                        } else {
                            System.out.println("Text fields are null.");
                        }
                    } else {
                        System.out.println("Email not found in the database.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void submitNewDataUser(ActionEvent event) throws IOException {
        String newName = retrieveName.getText();
        String newPassword = retrievePWD.getText();
    
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "UPDATE registration SET email = ?, password = ? WHERE id = ?";
    
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                int userIdToUpdate = 1; // Replace with the actual user ID to update
    
                preparedStatement.setString(1, newName);
                preparedStatement.setString(2, newPassword);
                preparedStatement.setInt(3, userIdToUpdate);
    
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    showSuccessDialog("User data updated successfully!");
    
                    System.out.println("User data updated successfully!");
                } else {
                    System.out.println("Failed to update user data.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

     private void showSuccessDialog(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


     
    
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
