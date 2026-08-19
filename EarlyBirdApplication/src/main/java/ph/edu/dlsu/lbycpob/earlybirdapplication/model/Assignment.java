package ph.edu.dlsu.lbycpob.earlybirdapplication.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Assignment extends Task {

    private String subject;
    private double estimatedDuration;
    private String priority;
    private String description;
    private List<Subtask> subtasks;

    public Assignment(
            String title,
            String subject,
            LocalDate dueDate,
            double estimatedDuration,
            String priority,
            String description) {

        super(title, dueDate);

        this.subject = subject;
        this.estimatedDuration = estimatedDuration;
        this.priority = priority;
        this.description = description;
        this.subtasks = new ArrayList<>();
    }

    @Override
    public String getTaskType() {
        return "Assignment";
    }

    public String getSubject() {
        return subject;
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