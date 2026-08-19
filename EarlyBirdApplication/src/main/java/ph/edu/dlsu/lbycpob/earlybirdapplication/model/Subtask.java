package ph.edu.dlsu.lbycpob.earlybirdapplication.model;

import java.time.LocalDate;

public class Subtask extends Task {

    public Subtask(String title, LocalDate dueDate) {
        super(title, dueDate);
    }

    @Override
    public String getTaskType() {
        return "Subtask";
    }
}