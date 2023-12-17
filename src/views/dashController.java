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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.TableCell;

import java.util.Calendar;
import java.util.Date;

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



    // for personal information setting
    public TextField retrieveName;
    public PasswordField retrievePWD;
    //provide for todo tab

    private Stage stage;
    private Scene scene;
    private Parent root;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/task_scheduler";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @FXML
    public void initialize() {
        ongoingSum();
        updateNoDueLabel();
        updateThisWeekLabel();
        updateNextWeekLabel();
        initializeCompletedTasksLabel();
    }

      private void ongoingSum() {
        try {
            // Connect to the database
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    
            // Fetch data from the database
            String query = "SELECT start_date FROM tasks";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            // Get the current date
            java.sql.Date currentDate = new java.sql.Date(new java.util.Date().getTime());
    
            // Initialize sum
            int taskSum = 0;
    
            // Check each task's start date
            while (resultSet.next()) {
                java.sql.Date startDate = resultSet.getDate("start_date");
    
                // Calculate time difference in milliseconds
                long timeDifference = startDate.getTime() - currentDate.getTime();
    
                // Calculate one week in milliseconds
                long oneWeekInMillis = 7 * 24 * 60 * 60 * 1000;
    
                // If the start_date is greater than 1 week from the current date
                if (timeDifference > oneWeekInMillis) {
                    // Increment the task sum
                    taskSum++;
                }
            }
    
            // Set the label text with the sum
            ongoingLbl.setText(Integer.toString(taskSum));
    
            // Close the resources
            resultSet.close();
            preparedStatement.close();
            connection.close();
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Add this method to initialize the completed tasks label
@FXML
public void initializeCompletedTasksLabel() {
    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
        String query = "SELECT COUNT(*) AS completedTasks FROM done_tasks";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                int completedTasks = resultSet.getInt("completedTasks");
                compTaskLbl.setText(Integer.toString(completedTasks));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        // Handle the SQL exception, show an error message, log, etc.
    }
}

    @FXML
public void updateNoDueLabel() {
    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
        String query = "SELECT COUNT(*) AS noDueTasks FROM tasks WHERE end_date IS NULL";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                int noDueTasks = resultSet.getInt("noDueTasks");
                noDueLbl.setText(Integer.toString(noDueTasks));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        // Handle the SQL exception, show an error message, log, etc.
    }
}

@FXML
public void updateThisWeekLabel() {
    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
        // Get the current date
        java.sql.Date currentDate = new java.sql.Date(new java.util.Date().getTime());

        // Calculate the start and end dates of the current week
        java.sql.Date startOfWeek = getStartOfWeek(currentDate);
        java.sql.Date endOfWeek = getEndOfWeek(currentDate);

        // Query to count tasks with end_date within the current week
        String query = "SELECT COUNT(*) AS thisWeekTasks FROM tasks WHERE end_date BETWEEN ? AND ?";
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDate(1, startOfWeek);
            preparedStatement.setDate(2, endOfWeek);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int thisWeekTasks = resultSet.getInt("thisWeekTasks");
                    thisWeekLbl.setText(Integer.toString(thisWeekTasks));
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        // Handle the SQL exception, show an error message, log, etc.
    }
}

private java.sql.Date getStartOfWeek(java.sql.Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);

    // Calculate the start of the week (Sunday)
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);

    // Convert the Calendar to java.sql.Date
    java.sql.Date startOfWeek = new java.sql.Date(calendar.getTimeInMillis());

    return startOfWeek;
}

private java.sql.Date getEndOfWeek(java.sql.Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);

    // Calculate the end of the week (Saturday)
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);
    calendar.set(Calendar.MILLISECOND, 999);

    // Convert the Calendar to java.sql.Date
    java.sql.Date endOfWeek = new java.sql.Date(calendar.getTimeInMillis());

    return endOfWeek;
}

@FXML
public void updateNextWeekLabel() {
    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
        // Get the current date
        java.sql.Date currentDate = new java.sql.Date(new java.util.Date().getTime());

        // Calculate the start and end dates of the next week
        java.sql.Date startOfNextWeek = getStartOfNextWeek(currentDate);
        java.sql.Date endOfNextWeek = getEndOfNextWeek(currentDate);

        // Query to count tasks with end_date within the next week
        String query = "SELECT COUNT(*) AS nextWeekTasks FROM tasks WHERE end_date BETWEEN ? AND ?";
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDate(1, startOfNextWeek);
            preparedStatement.setDate(2, endOfNextWeek);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int nextWeekTasks = resultSet.getInt("nextWeekTasks");
                    nextWeekLbl.setText(Integer.toString(nextWeekTasks));
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        // Handle the SQL exception, show an error message, log, etc.
    }
}

private java.sql.Date getStartOfNextWeek(java.sql.Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);

    // Move to the next week
    calendar.add(Calendar.WEEK_OF_YEAR, 1);

    // Calculate the start of the week (Sunday)
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);

    // Convert the Calendar to java.sql.Date
    java.sql.Date startOfNextWeek = new java.sql.Date(calendar.getTimeInMillis());

    return startOfNextWeek;
}

private java.sql.Date getEndOfNextWeek(java.sql.Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);

    // Move to the next week
    calendar.add(Calendar.WEEK_OF_YEAR, 1);

    // Calculate the end of the week (Saturday)
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);
    calendar.set(Calendar.MILLISECOND, 999);

    // Convert the Calendar to java.sql.Date
    java.sql.Date endOfNextWeek = new java.sql.Date(calendar.getTimeInMillis());

    return endOfNextWeek;
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
