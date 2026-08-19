package ph.edu.dlsu.lbycpob.earlybirdapplication.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import ph.edu.dlsu.lbycpob.earlybirdapplication.model.Assignment;
import ph.edu.dlsu.lbycpob.earlybirdapplication.model.Subtask;

import java.time.LocalDate;
import java.util.Optional;

public class TaskBreakdownController {

    private Assignment assignment;

    @FXML
    private Label assignmentTitleLabel;

    @FXML
    private Label progressLabel;

    @FXML
    private VBox subtaskContainer;

    public void setAssignment(Assignment assignment) {

        this.assignment = assignment;

        assignmentTitleLabel.setText(
                assignment.getTitle()
        );

        loadSubtasks();
    }

    private void loadSubtasks() {

        subtaskContainer.getChildren().clear();

        if (assignment == null) {
            return;
        }

        for (Subtask subtask : assignment.getSubtasks()) {

            CheckBox checkBox =
                    new CheckBox(subtask.getTitle());

            checkBox.setSelected(
                    subtask.isCompleted()
            );

            checkBox.setStyle(
                    "-fx-font-size: 16px;"
            );

            checkBox.setOnAction(event -> {

                subtask.setCompleted(
                        checkBox.isSelected()
                );

                updateProgress();
            });

            subtaskContainer
                    .getChildren()
                    .add(checkBox);
        }

        updateProgress();
    }

    @FXML
    private void handleAddSubtask() {

        if (assignment == null) {
            return;
        }

        TextInputDialog dialog =
                new TextInputDialog();

        dialog.setTitle("Add Subtask");
        dialog.setHeaderText(
                "Add a new subtask"
        );
        dialog.setContentText(
                "Subtask title:"
        );

        Optional<String> result =
                dialog.showAndWait();

        if (result.isEmpty()) {
            return;
        }

        String title =
                result.get().trim();

        if (title.isEmpty()) {
            return;
        }

        Subtask subtask =
                new Subtask(
                        title,
                        assignment.getDueDate()
                );

        assignment.addSubtask(subtask);

        loadSubtasks();
    }

    private void updateProgress() {

        if (assignment == null) {
            return;
        }

        int total =
                assignment.getSubtasks().size();

        int completed = 0;

        for (Subtask subtask :
                assignment.getSubtasks()) {

            if (subtask.isCompleted()) {
                completed++;
            }
        }

        progressLabel.setText(
                "Progress: " +
                        completed +
                        " / " +
                        total
        );
    }

    @FXML
    private void handleBack(ActionEvent event) {

        try {

            javafx.fxml.FXMLLoader loader =
                    new javafx.fxml.FXMLLoader(
                            getClass().getResource(
                                    "/ph/edu/dlsu/lbycpob/earlybirdapplication/"
                                            + "assignment-details-view.fxml"
                            )
                    );

            javafx.scene.Parent root =
                    loader.load();

            AssignmentDetailsController controller =
                    loader.getController();

            controller.setAssignment(
                    assignment
            );

            Stage stage =
                    (Stage) ((Node) event.getSource())
                            .getScene()
                            .getWindow();

            stage.setScene(
                    new Scene(root, 1000, 700)
            );

            stage.setTitle(
                    "EarlyBird - Assignment Details"
            );

            stage.show();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}