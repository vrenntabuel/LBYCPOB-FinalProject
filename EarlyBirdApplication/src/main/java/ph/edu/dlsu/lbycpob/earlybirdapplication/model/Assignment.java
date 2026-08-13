package ph.edu.dlsu.lbycpob.earlybirdapplication.model;

import java.time.LocalDate;

public class Assignment {

    private String title;
    private String subject;
    private LocalDate dueDate;
    private double estimatedDuration;
    private String priority;
    private String description;

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
}