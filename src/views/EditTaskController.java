package views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditTaskController {

    @FXML
    private TextField taskNameField; 

    private Task taskToEdit;
    private Stage dialogStage;

    public void setTaskToEdit(Task task) {
        this.taskToEdit = task;
        taskNameField.setText(task.getName());
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void handleSave() {
        dialogStage.close();
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}
