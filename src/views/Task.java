package views;

import javafx.beans.property.SimpleStringProperty;

public class Task {
  
    private SimpleStringProperty name;
    private SimpleStringProperty description;
    private SimpleStringProperty priority;

    public Task(String name, String description, String priority) {
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.priority = new SimpleStringProperty(priority);
    }

    public String getName() {
        return name.get();
    }

    public String getDescription() {
        return description.get();
    }

    public String getPriority() {
        return priority.get();
    }
}

