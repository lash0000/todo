package views;

public class Task2 {
    private String name;
    private String startDate;
    private String endDate;
    private String priorityLevel;
    private boolean done;

    // Constructors, getters, and setters

    public Task2(String taskName, String startDate, String endDate, String priorityLevel, boolean done) {
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
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
