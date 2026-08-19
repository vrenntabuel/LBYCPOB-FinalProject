package ph.edu.dlsu.lbycpob.earlybirdapplication.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import ph.edu.dlsu.lbycpob.earlybirdapplication.model.Assignment;

import java.io.IOException;

public class AssignmentDetailsController {

    private Assignment assignment;

    @FXML
    private Label titleLabel;

    @FXML
    private Label subjectLabel;

    @FXML
    private Label dueDateLabel;

    @FXML
    private Label durationLabel;

    @FXML
    private Label priorityLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private CheckBox completedCheckBox;

    public void setAssignment(Assignment assignment) {

        this.assignment = assignment;

        titleLabel.setText(assignment.getTitle());
        subjectLabel.setText(
                "Subject: " + assignment.getSubject()
        );

        dueDateLabel.setText(
                "Due: " + assignment.getDueDate()
        );

        durationLabel.setText(
                "Estimated: " +
                        assignment.getEstimatedDuration() +
                        " hours"
        );

        priorityLabel.setText(
                "Priority: " +
                        assignment.getPriority()
        );

        descriptionLabel.setText(
                assignment.getDescription()
        );

        completedCheckBox.setSelected(
                assignment.isCompleted()
        );
    }

    @FXML
    private void handleCompletionChanged() {

        if (assignment == null) {
            return;
        }

        assignment.setCompleted(
                completedCheckBox.isSelected()
        );
    }

    @FXML
    private void handleTaskBreakdown(ActionEvent event)
            throws IOException {

        if (assignment == null) {
            return;
        }

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/ph/edu/dlsu/lbycpob/earlybirdapplication/"
                                + "task-breakdown-view.fxml"
                )
        );

        Parent root = loader.load();

        TaskBreakdownController controller =
                loader.getController();

        controller.setAssignment(assignment);

        Stage stage = (Stage)
                ((Node) event.getSource())
                        .getScene()
                        .getWindow();

        stage.setScene(
                new Scene(root, 1000, 700)
        );

        stage.setTitle("EarlyBird - Task Breakdown");
        stage.show();
    }

    @FXML
    public void handleBack(ActionEvent event)
            throws IOException {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/ph/edu/dlsu/lbycpob/earlybirdapplication/"
                                + "assignments-view.fxml"
                )
        );

        Scene scene = new Scene(loader.load());

        Stage stage = (Stage)
                ((Node) event.getSource())
                        .getScene()
                        .getWindow();

        stage.setScene(scene);
        stage.setTitle("EarlyBird - Assignments");
        stage.show();
    }
}