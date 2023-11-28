package views;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Login {
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField keyField;

    @FXML
    private void userLogin(ActionEvent event) throws IOException {
        String email = emailField.getText();
        String pwd = keyField.getText();

        if (email.equals("test@email.com") && pwd.equals("qcu")) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/Dashboard.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/main.css").toExternalForm());
                Stage stage = (Stage) emailField.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                System.out.println("nonneee");
                e.printStackTrace();
            }
        } else {
            System.out.println("Wrong credentials!");
        }
    }
}
