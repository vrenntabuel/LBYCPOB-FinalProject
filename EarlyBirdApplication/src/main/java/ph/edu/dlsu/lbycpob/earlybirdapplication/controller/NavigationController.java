package ph.edu.dlsu.lbycpob.earlybirdapplication.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class NavigationController {

    private void changeScene(String fxmlFile, ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/ph/edu/dlsu/lbycpob/earlybirdapplication/"
                                    + fxmlFile
                    )
            );

            Parent root = loader.load();

            Stage stage = (Stage)
                    ((Node) event.getSource())
                            .getScene()
                            .getWindow();

            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {

            e.printStackTrace();

        }
    }

    @FXML
    private void handleHome(ActionEvent event) {

        changeScene("home-view.fxml", event);
    }

    @FXML
    private void handleAssignments(ActionEvent event) {

        changeScene("assignments-view.fxml", event);
    }

    @FXML
    private void handleCalendar(ActionEvent event) {

        changeScene("calendar-view.fxml", event);
    }

    @FXML
    private void handleFocusTimer(ActionEvent event) {

        changeScene("focus-view.fxml", event);
    }

    @FXML
    private void handleDashboard(ActionEvent event) {

        changeScene("dashboard-view.fxml", event);
    }
}