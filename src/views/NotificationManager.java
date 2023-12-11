package views;

import javafx.animation.PauseTransition;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox; // Add this import statement
import javafx.util.Duration;

public class NotificationManager {
    private static final Duration NOTIFICATION_DISPLAY_TIME = Duration.seconds(5);

    public static void showNotification(HBox notification, VBox container) {
        container.getChildren().add(notification);

        PauseTransition pause = new PauseTransition(NOTIFICATION_DISPLAY_TIME);
        pause.setOnFinished(event -> container.getChildren().remove(notification));
        pause.play();
    }
}
