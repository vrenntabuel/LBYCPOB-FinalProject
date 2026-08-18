package ph.edu.dlsu.lbycpob.earlybirdapplication.util;

import ph.edu.dlsu.lbycpob.earlybirdapplication.model.Assignment;

import java.util.ArrayList;
import java.util.List;

public class AppData {

    private static final List<Assignment> assignments = new ArrayList<>();

    private AppData() {
    }

    public static List<Assignment> getAssignments() {
        return assignments;
    }

    public static void addAssignment(Assignment assignment) {
        assignments.add(assignment);
    }

    public static void removeAssignment(Assignment assignment) {
        assignments.remove(assignment);
    }

    public static void clearAssignments() {
        assignments.clear();
    }
}