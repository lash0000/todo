package views;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {
    @FXML
    private Button registerButton;

    @FXML
    private PasswordField keyField;

    @FXML
    private TextField usernameField;

    private String email;  // Assuming you have an email field

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = keyField.getText();

    
        if (authenticateUser(username, password)) {
            showAlert("Login Successful!");
    
             // Set the email in the UserData class
            // Load the dashboard scene
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/dashboard.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);

                // Get the stage from the action event
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error loading dashboard.");
            }
        } else {
            showAlert("Invalid username or password. Please try again.");
        }
    }
    
    //two tables are being provided during authentication

    private boolean authenticateUser(String email, String password) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/task_scheduler", "root", "")) {
            String query = "SELECT * FROM registration WHERE email = ? AND password = ? UNION SELECT * FROM login WHERE username = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, email);
                preparedStatement.setString(4, password);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next(); 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error authenticating user.");
            return false;
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Login Result");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public void goToRegistration(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("resources/RegPt1.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void goToLogin(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("../Login.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
