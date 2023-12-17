package views;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.TableCell;


class PriorityComparator implements Comparator<Task2> {
    @Override
    public int compare(Task2 task1, Task2 task2) {
        String priorityLevel1 = task1.getPriorityLevel().toLowerCase();
        String priorityLevel2 = task2.getPriorityLevel().toLowerCase();

        // Custom order based on priority levels: High > Medium > Low
        return comparePriorityLevels(priorityLevel1, priorityLevel2);
    }

    private int comparePriorityLevels(String priorityLevel1, String priorityLevel2) {
        int order1 = getPriorityOrder(priorityLevel1);
        int order2 = getPriorityOrder(priorityLevel2);

        return Integer.compare(order1, order2);
    }

    private int getPriorityOrder(String priorityLevel) {
        switch (priorityLevel) {
            case "high":
                return 0;
            case "medium":
                return 1;
            case "low":
                return 2;
            default:
                return 3; // For any other priority levels, consider them equal
        }
    }
}




public class todoController {
    
    @FXML
    private RadioButton highRad;

    @FXML
    private RadioButton lowRad;

    @FXML
    private RadioButton mediumRad;

    @FXML
    private ToggleGroup priority;
    
    @FXML
    private Button taskDoneBtn;

    @FXML
    private Button taskOverduedBtn;

    @FXML
    private Button taskPendingBtn;

 

    @FXML
    private TableColumn<Task2, Boolean> columnDone;

    @FXML
    private TableColumn<Task2, String> columnEndDate;

    @FXML
    private TableColumn<Task2, String> columnPriorityLevel;

    @FXML
    private TableColumn<Task2, String> columnStartDate;

    @FXML
    private TableColumn<Task2, String> column_Name;

    @FXML
    private TableView<Task2> tableViewList2;

    private PriorityQueue<Task2> taskQueue = new PriorityQueue<>(new PriorityComparator());
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    private Connection connection;

    
    private Stage stage;
    private Scene scene;
    private Parent root;

    
    @FXML
private void initialize() {
    try {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/task_scheduler", "root", "");
    } catch (SQLException e) {
        e.printStackTrace();
    }

    // Select the "High" radio button by default
    highRad.setSelected(true);

    initializeTableData();

    // Add event handlers for radio buttons
    highRad.setOnAction(this::handleRadioButton);
    mediumRad.setOnAction(this::handleRadioButton);
    lowRad.setOnAction(this::handleRadioButton);
}

   
@FXML
private void initializeTableData() {
    taskQueue.clear();

    ObservableList<Task2> taskList = FXCollections.observableArrayList();

    // Connect to the database
    try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/task_scheduler", "root", "")) {
        String selectedPriority = getSelectedPriority();
        // Execute SQL query with a WHERE clause to filter by the selected priority level
        String sql = "SELECT task_name, start_date, end_date, task_priority FROM tasks WHERE task_priority = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, selectedPriority);
            try (ResultSet resultSet = statement.executeQuery()) {
                // Populate the taskList
                while (resultSet.next()) {
                    String name = resultSet.getString("task_name");
                    String startDateString = resultSet.getString("start_date");
                    LocalDate startDate = startDateString != null ? LocalDate.parse(startDateString, DATE_FORMATTER) : null;
                    String endDateString = resultSet.getString("end_date");
                    LocalDate endDate = endDateString != null ? LocalDate.parse(endDateString, DATE_FORMATTER) : null;
                                        String priorityLevel = resultSet.getString("task_priority");
                    Task2 task = new Task2(name, startDate, endDate, priorityLevel, false);
                    taskList.add(task);
                }
                // Set the data in the TableView
                tableViewList2.setItems(taskList);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    // Set cell value factories for each column
    column_Name.setCellValueFactory(new PropertyValueFactory<>("name"));
    columnStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
    columnEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
    columnPriorityLevel.setCellValueFactory(new PropertyValueFactory<>("priorityLevel"));
    columnDone.setCellValueFactory(new PropertyValueFactory<>("done"));

    // Create a button cell for the Done column
    columnDone.setCellFactory(tc -> new TableCell<Task2, Boolean>() {
        final Button btn = new Button("Done");

        {
            btn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
            btn.setMaxWidth(Double.MAX_VALUE);

           // Inside your "Done" button handler
btn.setOnAction(event -> {
    Task2 task = getTableView().getItems().get(getIndex());

    // Remove the item from the table
    taskList.remove(task);

    // Remove the item from the database
    deleteTaskFromDatabase(task);

    // Check priority and update the table only if it's medium or low
    String selectedPriority = getSelectedPriority();
    if ("high".equals(selectedPriority) || "medium".equals(selectedPriority) || "low".equals(selectedPriority)) {
        initializeTableData();
    }
});

        }

        @Override
        protected void updateItem(Boolean item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                setGraphic(btn);
            }
        }
    });
}

   

@FXML
private void handlePendingButton(ActionEvent event) {
    ObservableList<Task2> pendingTaskList = FXCollections.observableArrayList();

    // Connect to the database
    try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/task_scheduler", "root", "")) {
        // Execute SQL query to retrieve pending tasks (tasks with a future end_date)
        String sql = "SELECT task_name, start_date, end_date, task_priority FROM tasks WHERE end_date > NOW()";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                // Populate the pendingTaskList
                while (resultSet.next()) {
                    String name = resultSet.getString("task_name");
                    LocalDate startDate = LocalDate.parse(resultSet.getString("start_date"));
                    LocalDate endDate = resultSet.getString("end_date") != null
                            ? LocalDate.parse(resultSet.getString("end_date"))
                            : null;
                    String priorityLevel = resultSet.getString("task_priority");

                    Task2 task = new Task2(name, startDate, endDate, priorityLevel, false);
                    pendingTaskList.add(task);
                }

                // Set the data in the TableView
                tableViewList2.setItems(pendingTaskList);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    

@FXML
private void handleOverduedButton(ActionEvent event) {
    ObservableList<Task2> overduedTaskList = FXCollections.observableArrayList();

    // Connect to the database
    try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/task_scheduler", "root", "")) {
        // Execute SQL query to retrieve overdue tasks (tasks with an end_date less than the current date)
        String sql = "SELECT task_name, start_date, end_date, task_priority FROM tasks WHERE end_date < NOW()";
        System.out.println("SQL Query: " + sql); // Debug: Print SQL query

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                // Populate the overduedTaskList
                while (resultSet.next()) {
                    String name = resultSet.getString("task_name");
                    LocalDate startDate = LocalDate.parse(resultSet.getString("start_date"));
                    LocalDate endDate = resultSet.getString("end_date") != null
                            ? LocalDate.parse(resultSet.getString("end_date"))
                            : null;
                    String priorityLevel = resultSet.getString("task_priority");

                    Task2 task = new Task2(name, startDate, endDate, priorityLevel, false);
                    overduedTaskList.add(task);
                }

                // Set the data in the TableView
                tableViewList2.setItems(overduedTaskList);

                // Refresh the TableView to reflect the changes
                tableViewList2.refresh();
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


private LocalDate parseDate(String dateString) {
    return dateString != null ? LocalDate.parse(dateString, DATE_FORMATTER) : null;
}


@FXML
private void handleRadioButton(ActionEvent event) {
    String selectedPriority = getSelectedPriority();

    ObservableList<Task2> taskList = FXCollections.observableArrayList();

    // Reuse the existing connection
    try {
        // Execute SQL query with a WHERE clause to filter by the selected priority level
        String sql = "SELECT task_name, start_date, end_date, task_priority FROM tasks WHERE task_priority = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, selectedPriority);
            try (ResultSet resultSet = statement.executeQuery()) {
                // Populate the taskList
                while (resultSet.next()) {
                    String name = resultSet.getString("task_name");
                    LocalDate startDate = LocalDate.parse(resultSet.getString("start_date"));
                    LocalDate endDate = resultSet.getString("end_date") != null
                            ? LocalDate.parse(resultSet.getString("end_date"))
                            : null;
                    String priorityLevel = resultSet.getString("task_priority");

                    Task2 task = new Task2(name, startDate, endDate, priorityLevel, false);
                    taskList.add(task);
                }

                // Set the data in the TableView
                Platform.runLater(() -> tableViewList2.setItems(taskList));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


private void deleteTaskFromDatabase(Task2 task) {
    try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/task_scheduler", "root", "")) {
        // Insert the completed task into the "done" table (if needed)
        // You need to adjust this SQL query based on your database schema
        String insertDoneTaskSQL = "INSERT INTO done_tasks (task_name, start_date, end_date, task_priority) VALUES (?, ?, ?, ?)";
        try (PreparedStatement insertStatement = connection.prepareStatement(insertDoneTaskSQL)) {
            insertStatement.setString(1, task.getName());
            insertStatement.setString(2, task.getStartDate().format(DATE_FORMATTER));
            insertStatement.setString(3, task.getEndDate() != null ? task.getEndDate().format(DATE_FORMATTER) : null);
            insertStatement.setString(4, task.getPriorityLevel());
            insertStatement.executeUpdate();
        }

        // Remove the completed task from the tasks table
        String deleteTaskSQL = "DELETE FROM tasks WHERE task_name=?";
        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteTaskSQL)) {
            deleteStatement.setString(1, task.getName());
            deleteStatement.executeUpdate();
        }

        // Schedule the UI update on the JavaFX Application Thread
        Platform.runLater(() -> tableViewList2.refresh());
    } catch (SQLException e) {
        e.printStackTrace();
    }
}




private String getSelectedPriority() {
    RadioButton selectedRadioButton = (RadioButton) priority.getSelectedToggle();

    if (selectedRadioButton != null) {
        return selectedRadioButton.getText().toLowerCase();
    } else {
        // Handle the case where no radio button is selected
        return ""; // You may want to return a default priority or handle it according to your needs
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

