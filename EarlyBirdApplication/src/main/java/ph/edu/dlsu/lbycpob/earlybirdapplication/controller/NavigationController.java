package ph.edu.dlsu.lbycpob.earlybirdapplication.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class NavigationController {

    @FXML
    private void goHome(ActionEvent event) {
        navigate(event, "home-view.fxml", "EarlyBird - Home");
    }

    @FXML
    private void goAssignments(ActionEvent event) {
        navigate(event, "assignments-view.fxml", "EarlyBird - Assignments");
    }

    @FXML
    private void goCalendar(ActionEvent event) {
        navigate(event, "calendar-view.fxml", "EarlyBird - Calendar");
    }

    @FXML
    private void goFocus(ActionEvent event) {
        navigate(event, "focus-view.fxml", "EarlyBird - Focus Timer");
    }

    @FXML
    private void goDashboard(ActionEvent event) {
        navigate(event, "dashboard-view.fxml", "EarlyBird - Dashboard");
    }

    private void navigate(
            ActionEvent event,
            String fxmlFile,
            String title) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/ph/edu/dlsu/lbycpob/earlybirdapplication/"
                                    + fxmlFile
                    )
            );

            Scene scene = new Scene(loader.load());

            Stage stage =
                    (Stage) ((Node) event.getSource())
                            .getScene()
                            .getWindow();

            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}