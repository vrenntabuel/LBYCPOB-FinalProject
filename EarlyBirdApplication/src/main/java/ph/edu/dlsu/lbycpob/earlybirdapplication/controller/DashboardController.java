package ph.edu.dlsu.lbycpob.earlybirdapplication.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

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
        openView(event, "focus-view.fxml", "EarlyBird - Focus");
    }

    @FXML
    private void handleTaskBreakdown(ActionEvent event) throws IOException {
        openView(event, "task-breakdown-view.fxml", "EarlyBird - Task Breakdown");
    }

    private void openView(ActionEvent event, String view, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/ph/edu/dlsu/lbycpob/earlybirdapplication/" + view
                )
        );

        Scene scene = new Scene(loader.load());

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }
}