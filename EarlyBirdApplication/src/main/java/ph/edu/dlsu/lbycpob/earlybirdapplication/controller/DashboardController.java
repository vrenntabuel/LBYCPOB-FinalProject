package ph.edu.dlsu.lbycpob.earlybirdapplication.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import ph.edu.dlsu.lbycpob.earlybirdapplication.model.Assignment;
import ph.edu.dlsu.lbycpob.earlybirdapplication.service.RiskCalculator;
import ph.edu.dlsu.lbycpob.earlybirdapplication.service.TaskManager;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DashboardController {

    private static final String BASE_PATH =
            "/ph/edu/dlsu/lbycpob/earlybirdapplication/";

    @FXML
    private Label totalAssignmentsLabel;

    @FXML
    private Label completedAssignmentsLabel;

    @FXML
    private Label dueSoonLabel;

    @FXML
    private Label highRiskLabel;

    @FXML
    private Label progressLabel;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private VBox upcomingContainer;

    @FXML
    private VBox riskContainer;

    @FXML
    public void initialize() {
        refreshDashboard();
    }

    @FXML
    private void handleRefresh() {
        refreshDashboard();
    }

    @FXML
    private void handleAssignments(ActionEvent event) throws IOException {
        openView(event, "assignments-view.fxml", "EarlyBird - Assignments");
    }

    @FXML
    private void handleCalendar(ActionEvent event) throws IOException {
        openView(event, "calendar-view.fxml", "EarlyBird - Calendar");
    }

    @FXML
    private void handleFocus(ActionEvent event) throws IOException {
        openView(event, "focus-view.fxml", "EarlyBird - Focus Timer");
    }

    private void refreshDashboard() {
        List<Assignment> assignments = new ArrayList<>(
                TaskManager.getAssignments()
        );

        updateSummary(assignments);
        updateProgress(assignments);
        updateUpcoming(assignments);
        updateRisks(assignments);
    }

    private void updateSummary(List<Assignment> assignments) {
        int total = assignments.size();

        int completed = (int) assignments.stream()
                .filter(Assignment::isCompleted)
                .count();

        int dueSoon = (int) assignments.stream()
                .filter(assignment -> !assignment.isCompleted())
                .filter(this::isDueSoon)
                .count();

        int highRisk = (int) assignments.stream()
                .filter(assignment -> !assignment.isCompleted())
                .filter(assignment -> "HIGH".equals(
                        RiskCalculator.calculateRisk(assignment)))
                .count();

        totalAssignmentsLabel.setText(String.valueOf(total));
        completedAssignmentsLabel.setText(String.valueOf(completed));
        dueSoonLabel.setText(String.valueOf(dueSoon));
        highRiskLabel.setText(String.valueOf(highRisk));
    }

    private void updateProgress(List<Assignment> assignments) {
        int total = assignments.size();
        int completed = (int) assignments.stream()
                .filter(Assignment::isCompleted)
                .count();

        double progress = total == 0
                ? 0.0
                : (double) completed / total;

        progressBar.setProgress(progress);
        progressLabel.setText(
                completed + " of " + total + " assignments completed"
        );
    }

    private void updateUpcoming(List<Assignment> assignments) {
        upcomingContainer.getChildren().clear();

        List<Assignment> upcoming = assignments.stream()
                .filter(assignment -> !assignment.isCompleted())
                .sorted(Comparator.comparing(
                        Assignment::getDueDate,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .limit(5)
                .toList();

        if (upcoming.isEmpty()) {
            upcomingContainer.getChildren().add(
                    createEmptyLabel("No upcoming assignments.")
            );
            return;
        }

        for (Assignment assignment : upcoming) {
            upcomingContainer.getChildren().add(
                    createUpcomingCard(assignment)
            );
        }
    }

    private void updateRisks(List<Assignment> assignments) {
        riskContainer.getChildren().clear();

        List<Assignment> risks = assignments.stream()
                .filter(assignment -> !assignment.isCompleted())
                .filter(assignment -> {
                    String risk = RiskCalculator.calculateRisk(assignment);
                    return "HIGH".equals(risk) || "MEDIUM".equals(risk);
                })
                .sorted(Comparator.comparingLong(
                        RiskCalculator::getDaysRemaining))
                .limit(5)
                .toList();

        if (risks.isEmpty()) {
            riskContainer.getChildren().add(
                    createEmptyLabel("No immediate deadline risks.")
            );
            return;
        }

        for (Assignment assignment : risks) {
            riskContainer.getChildren().add(
                    createRiskCard(assignment)
            );
        }
    }

    private VBox createUpcomingCard(Assignment assignment) {
        VBox card = new VBox(5);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle(
                "-fx-background-color: #f7f7f7;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 12;" +
                        "-fx-border-color: #e0e0e0;" +
                        "-fx-border-radius: 8;"
        );

        Label title = new Label(assignment.getTitle());
        title.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
        title.setWrapText(true);

        String dueText = assignment.getDueDate() == null
                ? "No deadline"
                : "Due " + assignment.getDueDate()
                           .format(DateTimeFormatter.ofPattern("MMM d, yyyy"));

        Label details = new Label(
                assignment.getSubject() + "  •  " + dueText
        );
        details.setStyle("-fx-font-size: 12px; -fx-text-fill: #555555;");
        details.setWrapText(true);

        card.getChildren().addAll(title, details);
        return card;
    }

    private HBox createRiskCard(Assignment assignment) {
        HBox card = new HBox(15);
        card.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle(
                "-fx-background-color: #f7f7f7;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 12;" +
                        "-fx-border-color: #e0e0e0;" +
                        "-fx-border-radius: 8;"
        );

        Label title = new Label(assignment.getTitle());
        title.setStyle("-fx-font-weight: bold;");
        title.setWrapText(true);
        title.setMaxWidth(300);

        long days = RiskCalculator.getDaysRemaining(assignment);
        String dueText;

        if (days < 0) {
            dueText = "Overdue by " + Math.abs(days) + " day(s)";
        } else if (days == 0) {
            dueText = "Due today";
        } else {
            dueText = "Due in " + days + " day(s)";
        }

        Label due = new Label(dueText);
        due.setStyle("-fx-text-fill: #555555;");

        Label risk = new Label(
                RiskCalculator.calculateRisk(assignment) + " RISK"
        );
        risk.setStyle("-fx-font-weight: bold;");

        card.getChildren().addAll(title, due, risk);
        return card;
    }

    private Label createEmptyLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: #777777; -fx-font-size: 13px;");
        return label;
    }

    private boolean isDueSoon(Assignment assignment) {
        if (assignment.getDueDate() == null) {
            return false;
        }

        long days = RiskCalculator.getDaysRemaining(assignment);
        return days >= 0 && days <= 3;
    }

    private void openView(
            ActionEvent event,
            String view,
            String title) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(BASE_PATH + view)
        );

        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        stage.setScene(new Scene(root, 1000, 700));
        stage.setTitle(title);
        stage.show();
    }
}