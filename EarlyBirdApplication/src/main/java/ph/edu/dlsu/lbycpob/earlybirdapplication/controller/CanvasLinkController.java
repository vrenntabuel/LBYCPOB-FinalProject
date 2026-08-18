package ph.edu.dlsu.lbycpob.earlybirdapplication.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class CanvasLinkController {

    @FXML
    private TextField canvasLinkField;

    @FXML
    private Label statusLabel;

    @FXML
    public void handleConnectCanvas(ActionEvent event) {

        String canvasLink = canvasLinkField.getText().trim();

        if (canvasLink.isEmpty()) {
            statusLabel.setText("Please enter your Canvas calendar link.");
            statusLabel.setStyle("-fx-text-fill: #d32f2f;");

            return;
        }

        if (!canvasLink.startsWith("http://")
                && !canvasLink.startsWith("https://")) {

            statusLabel.setText("Please enter a valid Canvas calendar URL.");
            statusLabel.setStyle("-fx-text-fill: #d32f2f;");

            return;
        }

        /*
         * Canvas link accepted.
         *
         * The link can later be used by FileManager/CalendarService
         * to retrieve actual Canvas calendar events.
         */

        statusLabel.setText(
                "Canvas calendar link connected successfully."
        );

        statusLabel.setStyle("-fx-text-fill: #2e7d32;");

        goToCalendar(event);
    }

    @FXML
    public void handleContinueWithoutCanvas(ActionEvent event) {

        goToCalendar(event);
    }

    private void goToCalendar(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/ph/edu/dlsu/lbycpob/earlybirdapplication/calendar-view.fxml"
                    )
            );

            Scene scene = new Scene(loader.load());

            Stage stage =
                    (Stage) ((Node) event.getSource())
                            .getScene()
                            .getWindow();

            stage.setScene(scene);
            stage.setTitle("EarlyBird - Calendar");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}