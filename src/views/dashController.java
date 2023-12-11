package views;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.TableCell;

public class dashController {

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
    private ComboBox<String> priorityComboBox;

    @FXML
    private VBox VBox1;

    @FXML
    private TextField usernameField;

    @FXML
    private Button label2;

    @FXML
    private Label welcome;

    @FXML
    private TextField taskPriorityField;

    //for Todo tab
    @FXML
    private TableView<Task2> tableViewList2;

    //provide for todo tab

    private Stage stage;
    private Scene scene;
    private Parent root;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/task_scheduler";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @FXML
    private void initialize() {
        if (tableViewList != null && columnName != null && columnDescription != null &&
                columnPriority != null && columnEdit != null && columnDelete != null) {

            // Initialize other columns (name, description, priority)
            columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
            columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            columnPriority.setCellValueFactory(new PropertyValueFactory<>("priority"));

            columnEdit.setCellFactory(param -> new TableCell<Task, Void>() {
                private final Button editButton = new Button("Edit");

                {
                    editButton.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
                    editButton.setMaxWidth(Double.MAX_VALUE);

                    // Set the click event for the edit button
                    editButton.setOnAction(event -> {
                        Task task = getTableView().getItems().get(getIndex());
                        // Add your edit logic here
                        System.out.println("Edit clicked for task: " + task.getName());
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(editButton);
                    }
                }
            });

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

    // Other methods...

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
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT task_name, task_description, task_priority FROM tasks";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                    ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    String name = resultSet.getString("task_name");
                    String description = resultSet.getString("task_description");
                    String priority = resultSet.getString("task_priority");

                    // Create a Task object and add it to the list
                    taskList.add(new Task(name, description, priority));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the SQL exception, show an error message, log, etc.
        }
    }

    @FXML
    private void addTask(ActionEvent event) throws IOException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO tasks (start_date, end_date, task_name, task_description, task_priority, task_time) VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query,
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setDate(1, java.sql.Date.valueOf(startDateField.getValue()));
                preparedStatement.setDate(2, java.sql.Date.valueOf(endDateField.getValue()));
                preparedStatement.setString(3, taskNameField.getText());
                preparedStatement.setString(4, taskDescriptionField.getText());
                preparedStatement.setString(5, taskPriorityField.getText());
                preparedStatement.setString(6, timeField.getText());

                int affectedRows = preparedStatement.executeUpdate();

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

}
