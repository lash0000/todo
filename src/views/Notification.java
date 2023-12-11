package views;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class Notification extends HBox {
    public Notification(String message) {
        Label label = new Label(message);
        label.setTextFill(Color.WHITE);
        setStyle("-fx-background-color: #4CAF50;");
        setAlignment(Pos.CENTER);
        getChildren().add(label);
    }
}
