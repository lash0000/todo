package views;

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
import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegistrationController {

   @FXML
    private TextField confirmEmailField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button registerBtn;


    private String userEmail;

    private Stage stage;
    private Scene scene;
    private Parent root;

     // JDBC URL, username, and password of MySQL server
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/task_scheduler";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    @FXML
    void register(ActionEvent event) throws Exception {
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmEmail = confirmEmailField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validate inputs
        if (email.isEmpty() || password.isEmpty() || confirmEmail.isEmpty() || confirmPassword.isEmpty()) {
            // Display an error message or handle validation as needed
            return;
        }

        if (!email.equals(confirmEmail) || !password.equals(confirmPassword)) {
            // Display an error message or handle validation as needed
            return;
        }

        // Perform registration
        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a connection
            try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
                // Create a prepared statement
                String sql = "INSERT INTO registration (email, password) VALUES (?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, email);
                    preparedStatement.setString(2, password);

                    // Execute the statement
                    preparedStatement.executeUpdate();

                    

                    // Redirect to Login.fxml on successful registration
                    Parent root = FXMLLoader.load(getClass().getResource("resources/RegSuccess.fxml"));
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            // Handle exceptions (e.g., display an error message)
            throw new Exception("Error during registration", e);
        }
    }

    @FXML
private void proceedToScene2(ActionEvent event) throws IOException {
    // Validate email and confirm email
    String email = emailField.getText();    
    String confirmEmail = confirmEmailField.getText();

    if (email.isEmpty() || confirmEmail.isEmpty()) {
        showAlert("Please enter both email and confirm email.");
        return;
    }

    if (!email.equals(confirmEmail)) {
        showAlert("Emails do not match. Please enter the same email in both fields.");
        return;
    }

    // Store the email for use in Scene 2
    userEmail = email;

    // Load Scene 2
    Parent root = FXMLLoader.load(getClass().getResource("resources/RegPt2.fxml"));
    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
}


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void switchToLogin(ActionEvent event) throws IOException{
          Parent root = FXMLLoader.load(getClass().getResource("../Login.fxml"));
    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
    }

    // You may have other methods for handling Scene 1 as needed
}
