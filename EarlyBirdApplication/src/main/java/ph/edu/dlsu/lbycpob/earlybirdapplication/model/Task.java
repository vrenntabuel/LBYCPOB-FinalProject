package ph.edu.dlsu.lbycpob.earlybirdapplication.model;

import java.time.LocalDate;

public abstract class Task {

    private String title;
    private LocalDate dueDate;
    private boolean completed;

    protected Task(String title, LocalDate dueDate) {
        this.title = title;
        this.dueDate = dueDate;
        this.completed = false;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public abstract String getTaskType();
}