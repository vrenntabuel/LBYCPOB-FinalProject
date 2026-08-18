package ph.edu.dlsu.lbycpob.earlybirdapplication.service;

import ph.edu.dlsu.lbycpob.earlybirdapplication.model.Assignment;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    // =========================================================
    // SHARED ASSIGNMENT LIST
    // =========================================================

    private static final List<Assignment> assignments =
            new ArrayList<>();


    // =========================================================
    // ADD ONE ASSIGNMENT
    // =========================================================

    public static void addAssignment(
            Assignment assignment
    ) {

        if (assignment == null) {
            return;
        }

        assignments.add(assignment);
    }


    // =========================================================
    // ADD MULTIPLE ASSIGNMENTS
    // =========================================================

    public static void addAssignments(
            List<Assignment> newAssignments
    ) {

        if (newAssignments == null) {
            return;
        }


        for (Assignment assignment : newAssignments) {

            addAssignment(assignment);
        }
    }


    // =========================================================
    // GET ALL ASSIGNMENTS
    // =========================================================

    public static List<Assignment> getAssignments() {

        return assignments;
    }
}