package views;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import org.controlsfx.control.Notifications;


 

public class manageController {

    @FXML
    private Label compTask;

    @FXML
    private Button addTaskBtn;

    @FXML
    private DatePicker endDateField;

    @FXML
    private DatePicker startDateField;

    @FXML
    private TextField taskDescriptionField;

    @FXML
    private TextField taskNameField;

    @FXML
    private TextField timeField;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TableView<Task> tableViewList;

    @FXML
    private TableColumn<Task, String> columnName;

    @FXML
    private TableColumn<Task, String> columnDescription;

    @FXML
    private TableColumn<Task, String> columnPriority;

    @FXML
    private ObservableList<Task2> taskList2;

    @FXML
    private TableColumn<Task, Void> columnEdit;
    @FXML
    private TableColumn<Task, Void> columnDelete;

    private ObservableList<Task> taskList;

    @FXML
    private VBox VBox1;

    @FXML
    private TextField usernameField;

    @FXML
    private Button label2;

    @FXML
    private Label welcome;


    @FXML
    private RadioButton highRad;

    @FXML
    private RadioButton mediumRad;

    @FXML
    private RadioButton lowRad;

    @FXML
    private ToggleGroup priorityToggleGroup; 

    @FXML
    private TextField taskPriorityField;

    //for Todo tab
    @FXML
    private TableView<Task2> tableViewList2;

    @FXML
    private Label nextWeekLbl;

    @FXML
    private Label noDueLbl;

    @FXML
    private Label ongoingLbl;

    @FXML
    private Label thisWeekLbl;

    @FXML
    private Label compTaskLbl;

    @FXML
private TextField priorityTextField;
    
      private Stage stage;
    private Scene scene;
    private Parent root;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/task_scheduler";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    
    @FXML
public void initialize() {
    if (tableViewList != null && columnName != null && columnDescription != null &&
            columnPriority != null && columnDelete != null) {

        // Initialize other columns (name, description, priority)
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        columnPriority.setCellValueFactory(new PropertyValueFactory<>("priority"));

        // Initialize delete column
        columnDelete.setCellFactory(param -> new TableCell<Task, Void>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
                deleteButton.setMaxWidth(Double.MAX_VALUE);

                // Set the click event for the delete button
                deleteButton.setOnAction(event -> {
                    Task task = getTableView().getItems().get(getIndex());
                    // Add your delete logic here
                    deleteTask(task);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        // Initialize your observable list
        taskList = FXCollections.observableArrayList();

        // Set the items to your TableView
        tableViewList.setItems(taskList);

        // Retrieve data from the database and add it to the list
        retrieveDataFromDatabase();
    } else {
        System.err.println("Error: TableView or its columns not injected properly.");
    }
}




      private void deleteTask(Task task) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "DELETE FROM tasks WHERE task_name = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, task.getName());

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows > 0) {
                    // If the deletion was successful, remove the task from the list and update the
                    // TableView
                    taskList.remove(task);
                    tableViewList.refresh(); // Refresh the TableView to reflect the changes
                } else {
                    // Handle the case where the deletion was not successful
                    System.out.println("Error deleting task from the database.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the SQL exception, show an error message, log, etc.
        }
    }

    

    private void retrieveDataFromDatabase() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT task_name, task_description, task_priority FROM tasks ORDER BY FIELD(task_priority, 'High', 'Medium', 'Low')");
             ResultSet resultSet = preparedStatement.executeQuery()) {
    
            while (resultSet.next()) {
                String name = resultSet.getString("task_name");
                String description = resultSet.getString("task_description");
                String priority = resultSet.getString("task_priority");
    
                // Create a Task object and add it to the list
                taskList.add(new Task(name, description, priority));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the SQL exception, show an error message, log, etc.
        }
    }
    
    

    @FXML
private void addTask(ActionEvent event) throws IOException {
    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
        String query = "INSERT INTO tasks (start_date, end_date, task_name, task_description, task_priority, task_time) VALUES (?, IFNULL(?, NULL), ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setDate(1, java.sql.Date.valueOf(startDateField.getValue()));

            // Check if endDateField is not null before setting the end_date
            if (endDateField.getValue() != null) {
                preparedStatement.setDate(2, java.sql.Date.valueOf(endDateField.getValue()));
            } else {
                preparedStatement.setNull(2, Types.NULL);
            }

            preparedStatement.setString(3, taskNameField.getText());
            preparedStatement.setString(4, taskDescriptionField.getText());
            preparedStatement.setString(5, getSelectedPriority());  // Updated line
            preparedStatement.setString(6, timeField.getText());

            int affectedRows = preparedStatement.executeUpdate();

            // Check if the end_date is within the current week
            if (isWithinCurrentWeek(endDateField.getValue())) {
                displayPopupNotification("Task Reminder", "Task '" + taskNameField.getText() + "' is due this week!");
            }

           // Check if the end_date is tomorrow and show a warning
            if (isTomorrow(endDateField.getValue())) {
                String imagePath = new File("src/views/resources/img/science.png").toURI().toString();
                displayPopupNotification("Task Warning", "The task '" + taskNameField.getText() + "' is due tomorrow.", imagePath);
            }


            // Check if the end_date is 2 weeks away
            if (isTwoWeeksAway(endDateField.getValue())) {
                displayPopupNotification("Task Reminder", "Task '" + taskNameField.getText() + "' is due in 2 weeks!");
            }

            Parent root = FXMLLoader.load(getClass().getResource("resources/manage.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }
    } catch (SQLException e) {
        e.printStackTrace();
        // Handle the SQL exception, show an error message, log, etc.
    }
}

private String getSelectedPriority() {
    RadioButton selectedRadioButton = (RadioButton) priorityToggleGroup.getSelectedToggle();

    if (selectedRadioButton != null) {
        return selectedRadioButton.getText();
    } else {
        // Handle the case where no radio button is selected
        return ""; // You may want to return a default priority or handle it according to your needs
    }
}

private boolean isTwoWeeksAway(LocalDate endDate) {
    if (endDate != null) {
        LocalDate twoWeeksAway = LocalDate.now().plusWeeks(2);
        return endDate.isEqual(twoWeeksAway);
    }
    return false;
}

private boolean isTomorrow(LocalDate endDate) {
    if (endDate != null) {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        return endDate.isEqual(tomorrow);
    }
    return false;
}

private boolean isWithinCurrentWeek(LocalDate endDate) {
    if (endDate != null) {
        LocalDate today = LocalDate.now();
        LocalDate endOfWeek = today.plusDays(6 - today.getDayOfWeek().getValue()); // Assuming the week starts on Monday
        return endDate.isBefore(endOfWeek.plusDays(1)) && endDate.isAfter(today.minusDays(1));
    }
    return false;
}

private void displayPopupNotification(String title, String content, String imagePath) {
    Image image = new Image(imagePath);
    ImageView imageView = new ImageView(image);
    imageView.setFitWidth(50); // Adjust the width as needed
    imageView.setFitHeight(50); // Adjust the height as needed

    Notifications.create()
            .title(title)
            .text(content)
            .graphic(imageView)
            .position(Pos.BOTTOM_RIGHT)
            .showInformation();
}


     private void displayPopupNotification(String title, String content) {
        Notifications.create()
                .title(title)
                .text(content)
                .position(Pos.BOTTOM_RIGHT)
                .showInformation();
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
