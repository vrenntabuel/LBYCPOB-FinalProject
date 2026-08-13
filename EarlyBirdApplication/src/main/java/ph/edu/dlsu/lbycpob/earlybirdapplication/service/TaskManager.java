package ph.edu.dlsu.lbycpob.earlybirdapplication.service;

import ph.edu.dlsu.lbycpob.earlybirdapplication.model.Assignment;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private static final List<Assignment> assignments = new ArrayList<>();

    public static void addAssignment(Assignment assignment) {
        assignments.add(assignment);
    }

    public static List<Assignment> getAssignments() {
        return assignments;
    }
}