package views;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;

public class RegistrationController {

    @FXML
    private TextField emailField;

    @FXML
    private TextField confirmEmailField;

    @FXML
    private Button proceedButton;

    private String userEmail;

    private Stage stage;
    private Scene scene;
    private Parent root;

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

    // You may have other methods for handling Scene 1 as needed
}
