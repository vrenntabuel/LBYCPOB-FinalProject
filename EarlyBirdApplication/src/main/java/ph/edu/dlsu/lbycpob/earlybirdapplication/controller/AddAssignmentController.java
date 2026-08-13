package ph.edu.dlsu.lbycpob.earlybirdapplication.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ph.edu.dlsu.lbycpob.earlybirdapplication.model.Assignment;
import ph.edu.dlsu.lbycpob.earlybirdapplication.service.TaskManager;

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
    private ComboBox<String> priorityBox;

    @FXML
    private TextArea descriptionField;

    @FXML
    public void initialize() {
        priorityBox.getItems().addAll("Low", "Medium", "High");
    }

    @FXML
    private void handleAddAssignment(ActionEvent event) throws IOException {

        String title = titleField.getText().trim();
        String subject = subjectField.getText().trim();
        String durationText = durationField.getText().trim();

        if (title.isEmpty()
                || subject.isEmpty()
                || dueDatePicker.getValue() == null
                || durationText.isEmpty()
                || priorityBox.getValue() == null) {

            showAlert("Please complete all required fields.");
            return;
        }

        double duration;

        try {
            duration = Double.parseDouble(durationText);

            if (duration <= 0) {
                showAlert("Duration must be greater than 0.");
                return;
            }

        } catch (NumberFormatException e) {
            showAlert("Estimated duration must be a number.");
            return;
        }

        Assignment assignment = new Assignment(
                title,
                subject,
                dueDatePicker.getValue(),
                duration,
                priorityBox.getValue(),
                descriptionField.getText().trim()
        );

        TaskManager.addAssignment(assignment);

        System.out.println("Assignment saved: " + assignment.getTitle());

        goToAssignments(event);
    }

    private void goToAssignments(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/ph/edu/dlsu/lbycpob/earlybirdapplication/assignments-view.fxml"
                )
        );

        Scene scene = new Scene(loader.load(), 1000, 700);

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        stage.setScene(scene);
        stage.setTitle("EarlyBird - Assignments");
        stage.show();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Assignment");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}