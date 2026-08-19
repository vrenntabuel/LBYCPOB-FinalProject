package ph.edu.dlsu.lbycpob.earlybirdapplication.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.util.Duration;

public class FocusController {

    @FXML
    private Label actualTimerLabel;

    @FXML
    private Label durationLabel;

    @FXML
    private Label customSeparator;

    @FXML
    private Spinner<Integer> minutesSpinner;

    @FXML
    private Spinner<Integer> secondsSpinner;

    @FXML
    private Button startPauseButton;

    private Timeline timer;

    private int remainingSeconds = 25 * 60;

    private boolean isRunning = false;

    @FXML
    public void initialize() {

        minutesSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 999, 0)
        );

        secondsSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0)
        );

        updateDisplay();

        hideCustomControls();

        timer = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> updateTimer())
        );

        timer.setCycleCount(Timeline.INDEFINITE);
    }

    private void updateTimer() {

        if (remainingSeconds > 0) {

            remainingSeconds--;

            updateDisplay();

        } else {

            isRunning = false;
            timer.stop();

            startPauseButton.setText("▶ / II");

            actualTimerLabel.setText("00:00");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Focus Timer");
            alert.setHeaderText(null);
            alert.setContentText("Time is up!");
            alert.showAndWait();
        }
    }

    private void updateDisplay() {

        int minutes = remainingSeconds / 60;
        int seconds = remainingSeconds % 60;

        actualTimerLabel.setText(
                String.format("%02d:%02d", minutes, seconds)
        );
    }

    @FXML
    private void handlePomodoro() {

        stopTimer();

        remainingSeconds = 25 * 60;

        durationLabel.setText("25 minutes");
        actualTimerLabel.setText("25:00");

        hideCustomControls();
    }

    @FXML
    private void handleShortBreak() {

        stopTimer();

        remainingSeconds = 10 * 60;

        durationLabel.setText("10 minutes");
        actualTimerLabel.setText("10:00");

        hideCustomControls();
    }

    @FXML
    private void handleCustom() {

        stopTimer();

        durationLabel.setText("Custom Duration");

        showCustomControls();

        updateCustomDuration();
    }

    @FXML
    private void handleMinutesChanged() {

        updateCustomDuration();
    }

    @FXML
    private void handleSecondsChanged() {

        updateCustomDuration();
    }

    private void updateCustomDuration() {

        if (!minutesSpinner.isVisible()) {
            return;
        }

        int minutes = minutesSpinner.getValue();
        int seconds = secondsSpinner.getValue();

        remainingSeconds = (minutes * 60) + seconds;

        updateDisplay();
    }

    @FXML
    private void handleStartPause() {

        if (isRunning) {

            isRunning = false;

            timer.stop();

            startPauseButton.setText("▶ / II");

        } else {

            if (remainingSeconds <= 0) {
                return;
            }

            isRunning = true;

            timer.play();

            startPauseButton.setText("❚❚");
        }
    }

    @FXML
    private void handleReset() {

        stopTimer();

        remainingSeconds = 25 * 60;

        durationLabel.setText("25 minutes");

        actualTimerLabel.setText("25:00");

        startPauseButton.setText("▶ / II");

        hideCustomControls();
    }

    @FXML
    private void handleSkip() {

        stopTimer();

        remainingSeconds = 0;

        actualTimerLabel.setText("00:00");

        startPauseButton.setText("▶ / II");
    }

    private void showCustomControls() {

        minutesSpinner.setVisible(true);
        minutesSpinner.setManaged(true);

        secondsSpinner.setVisible(true);
        secondsSpinner.setManaged(true);

        customSeparator.setVisible(true);
        customSeparator.setManaged(true);
    }

    private void hideCustomControls() {

        minutesSpinner.setVisible(false);
        minutesSpinner.setManaged(false);

        secondsSpinner.setVisible(false);
        secondsSpinner.setManaged(false);

        customSeparator.setVisible(false);
        customSeparator.setManaged(false);
    }

    private void stopTimer() {

        isRunning = false;

        if (timer != null) {
            timer.stop();
        }

        startPauseButton.setText("▶ / II");
    }
}