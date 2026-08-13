package ph.edu.dlsu.lbycpob.earlybirdapplication.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ph.edu.dlsu.lbycpob.earlybirdapplication.model.Assignment;
import ph.edu.dlsu.lbycpob.earlybirdapplication.service.TaskManager;

import java.io.IOException;

public class AssignmentController {

    @FXML
    private VBox assignmentContainer;

    @FXML
    public void initialize() {
        loadAssignments();
    }

    private void loadAssignments() {

        assignmentContainer.getChildren().clear();

        for (Assignment assignment : TaskManager.getAssignments()) {

            VBox card = new VBox(8);
            card.setStyle(
                    "-fx-border-color: #d9d9d9;" +
                            "-fx-border-radius: 8;" +
                            "-fx-background-radius: 8;" +
                            "-fx-padding: 18;"
            );

            Label title = new Label(assignment.getTitle());
            title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

            Label subject = new Label("Subject: " + assignment.getSubject());

            Label dueDate = new Label(
                    "Due: " + assignment.getDueDate()
            );

            Label duration = new Label(
                    "Estimated: " + assignment.getEstimatedDuration() + " hours"
            );

            Label priority = new Label(
                    "Priority: " + assignment.getPriority()
            );

            card.getChildren().addAll(
                    title,
                    subject,
                    dueDate,
                    duration,
                    priority
            );

            assignmentContainer.getChildren().add(card);
        }
    }

    public void handleView(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/ph/edu/dlsu/lbycpob/earlybirdapplication/assignment-details-view.fxml"
                )
        );

        Scene scene = new Scene(loader.load());

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        stage.setScene(scene);
        stage.setTitle("EarlyBird - Assignment Details");
        stage.show();
    }

    public void handleAddAssignment(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/ph/edu/dlsu/lbycpob/earlybirdapplication/add-assignment-view.fxml"
                )
        );

        Scene scene = new Scene(loader.load(), 1000, 700);

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        stage.setScene(scene);
        stage.setTitle("EarlyBird - Add Assignment");
        stage.show();
    }
}