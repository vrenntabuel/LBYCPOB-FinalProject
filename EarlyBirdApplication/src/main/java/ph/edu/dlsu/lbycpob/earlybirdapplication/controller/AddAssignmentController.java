package ph.edu.dlsu.lbycpob.earlybirdapplication.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AddAssignmentController {

    @FXML
    private TextField titleField;

    @FXML
    private TextField subjectField;

    @FXML
    private DatePicker dueDatePicker;

    @FXML
    private TextField durationField;

    @FXML
    private ComboBox<String> priorityComboBox;

    @FXML
    private TextArea descriptionField;

    @FXML
    public void initialize() {

        priorityComboBox.getItems().addAll(
                "Low",
                "Medium",
                "High"
        );
    }

    @FXML
    private void handleAddAssignment(ActionEvent event) {

        /*
         * KEEP YOUR EXISTING ASSIGNMENT CREATION CODE HERE.
         *
         * After successfully adding the assignment,
         * return to the Assignment page.
         */

        goToAssignments(event);
    }

    @FXML
    private void handleCancel(ActionEvent event) {

        goToAssignments(event);
    }

    private void goToAssignments(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/ph/edu/dlsu/lbycpob/earlybirdapplication/"
                                    + "assignments-view.fxml"
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

            stage.setTitle(
                    "EarlyBird - Assignments"
            );

            stage.show();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}