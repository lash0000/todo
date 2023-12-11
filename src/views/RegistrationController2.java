package views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegistrationController2 {

    @FXML
    private TextField emailField;

    private String userEmail;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField confirmEmailField;

    @FXML
    private Button registerButton;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/task_scheduler";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @FXML
    private void registerUser(ActionEvent event) throws IOException{
        // Validate password and confirm password
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
  

        if (password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Please enter both password and confirm password.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Passwords do not match. Please enter the same password in both fields.");
            return;
        }

        // Insert data into the database
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO registration (email, password) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, userEmail); // userEmail is null here
                preparedStatement.setString(2, password);
                preparedStatement.executeUpdate();
            }
            showAlert("User registered successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error registering user. Please try again.");
        }
        

         Parent root = FXMLLoader.load(getClass().getResource("resources/RegSuccess.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registration Result");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // You may have other methods for handling Scene 2 as needed
}
