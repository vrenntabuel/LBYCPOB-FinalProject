package ph.edu.dlsu.lbycpob.earlybirdapplication.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {

    @FXML
    private void handleGetStarted(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/ph/edu/dlsu/lbycpob/earlybirdapplication/"
                                    + "canvas-link-view.fxml"
                    )
            );

            Parent root = loader.load();

            Stage stage = (Stage)
                    ((Node) event.getSource())
                            .getScene()
                            .getWindow();

            stage.setScene(
                    new Scene(root, 1000, 700)
            );

            stage.setTitle("EarlyBird");

            stage.show();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}