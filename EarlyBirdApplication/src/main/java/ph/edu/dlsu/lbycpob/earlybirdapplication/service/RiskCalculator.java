package ph.edu.dlsu.lbycpob.earlybirdapplication.service;

import ph.edu.dlsu.lbycpob.earlybirdapplication.model.Assignment;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class RiskCalculator {

    public static String calculateRisk(Assignment assignment) {

        if (assignment == null || assignment.getDueDate() == null) {
            return "Unknown";
        }

        LocalDate today = LocalDate.now();

        long daysRemaining = ChronoUnit.DAYS.between(
                today,
                assignment.getDueDate()
        );

        if (daysRemaining <= 1) {
            return "HIGH";
        } else if (daysRemaining <= 3) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }

    public static long getDaysRemaining(Assignment assignment) {

        if (assignment == null || assignment.getDueDate() == null) {
            return 0;
        }

        return ChronoUnit.DAYS.between(
                LocalDate.now(),
                assignment.getDueDate()
        );
    }
}
