package views;

import java.time.LocalDate;

public class Task2 {
    private String name;
     private LocalDate startDate;
    private LocalDate endDate;
    private String priorityLevel;
    private boolean done;

    // Constructors, getters, and setters

    public Task2(String taskName, LocalDate startDate, LocalDate endDate, String priorityLevel, boolean done) {
        this.name = taskName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.priorityLevel = priorityLevel;
        this.done = done;
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(String priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
