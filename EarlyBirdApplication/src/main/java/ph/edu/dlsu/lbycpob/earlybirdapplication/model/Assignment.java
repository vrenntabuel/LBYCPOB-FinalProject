package ph.edu.dlsu.lbycpob.earlybirdapplication.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Assignment {

    private String title;
    private String subject;
    private LocalDate dueDate;
    private double estimatedDuration;
    private String priority;
    private String description;
    private List<Subtask> subtasks;


    public Assignment(String title,
                      String subject,
                      LocalDate dueDate,
                      double estimatedDuration,
                      String priority,
                      String description) {

        this.title = title;
        this.subject = subject;
        this.dueDate = dueDate;
        this.estimatedDuration = estimatedDuration;
        this.priority = priority;
        this.description = description;
        this.subtasks = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public String getSubject() {
        return subject;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public double getEstimatedDuration() {
        return estimatedDuration;
    }

    public String getPriority() {
        return priority;
    }

    public String getDescription() {
        return description;
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

}