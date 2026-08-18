package ph.edu.dlsu.lbycpob.earlybirdapplication.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import ph.edu.dlsu.lbycpob.earlybirdapplication.model.Assignment;
import ph.edu.dlsu.lbycpob.earlybirdapplication.service.TaskManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CanvasLinkController {

    @FXML
    private TextField canvasLinkField;

    @FXML
    private Label statusLabel;


    // =========================================================
    // CONNECT CANVAS
    // =========================================================

    @FXML
    public void handleConnectCanvas(ActionEvent event) {

        String canvasLink = canvasLinkField.getText().trim();

        // Check if empty
        if (canvasLink.isEmpty()) {

            statusLabel.setText(
                    "Please enter your Canvas calendar link."
            );

            statusLabel.setStyle(
                    "-fx-text-fill: #d32f2f;"
            );

            return;
        }


        // Check URL format
        if (!canvasLink.startsWith("http://")
                && !canvasLink.startsWith("https://")) {

            statusLabel.setText(
                    "Please enter a valid Canvas calendar URL."
            );

            statusLabel.setStyle(
                    "-fx-text-fill: #d32f2f;"
            );

            return;
        }


        try {

            // -------------------------------------------------
            // 1. DOWNLOAD CANVAS CALENDAR
            // -------------------------------------------------

            statusLabel.setText(
                    "Connecting to Canvas..."
            );

            statusLabel.setStyle(
                    "-fx-text-fill: #555555;"
            );

            String icsData =
                    downloadCanvasCalendar(canvasLink);


            // -------------------------------------------------
            // 2. PARSE CANVAS EVENTS
            // -------------------------------------------------

            List<Assignment> importedAssignments =
                    parseCanvasCalendar(icsData);


            // -------------------------------------------------
            // 3. CHECK IF ASSIGNMENTS WERE FOUND
            // -------------------------------------------------

            if (importedAssignments.isEmpty()) {

                statusLabel.setText(
                        "Canvas connected, but no assignments were found."
                );

                statusLabel.setStyle(
                        "-fx-text-fill: #d32f2f;"
                );

                return;
            }


            // -------------------------------------------------
            // 4. SAVE ASSIGNMENTS TO TASK MANAGER
            // -------------------------------------------------

            TaskManager.addAssignments(
                    importedAssignments
            );


            // -------------------------------------------------
            // 5. SHOW SUCCESS MESSAGE
            // -------------------------------------------------

            statusLabel.setText(
                    "Canvas connected successfully. "
                            + importedAssignments.size()
                            + " assignments imported."
            );

            statusLabel.setStyle(
                    "-fx-text-fill: #2e7d32;"
            );


            // -------------------------------------------------
            // 6. GO TO ASSIGNMENTS
            // -------------------------------------------------

            goToAssignments(event);


        } catch (Exception e) {

            statusLabel.setText(
                    "Unable to connect to Canvas calendar."
            );

            statusLabel.setStyle(
                    "-fx-text-fill: #d32f2f;"
            );

            e.printStackTrace();
        }
    }


    // =========================================================
    // CONTINUE WITHOUT CANVAS
    // =========================================================

    @FXML
    public void handleContinueWithoutCanvas(ActionEvent event) {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/ph/edu/dlsu/lbycpob/earlybirdapplication/add-assignment-view.fxml"
                            )
                    );

            Parent root = loader.load();

            Stage stage =
                    (Stage) ((Node) event.getSource())
                            .getScene()
                            .getWindow();

            stage.setScene(
                    new Scene(root)
            );

            stage.setTitle(
                    "EarlyBird - Add Assignment"
            );

            stage.show();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }


    // =========================================================
    // GO TO ASSIGNMENTS
    // =========================================================

    private void goToAssignments(ActionEvent event)
            throws IOException {

        FXMLLoader loader =
                new FXMLLoader(
                        getClass().getResource(
                                "/ph/edu/dlsu/lbycpob/earlybirdapplication/assignments-view.fxml"
                        )
                );

        Parent root = loader.load();

        Stage stage =
                (Stage) ((Node) event.getSource())
                        .getScene()
                        .getWindow();

        stage.setScene(
                new Scene(root)
        );

        stage.setTitle(
                "EarlyBird - Assignments"
        );

        stage.show();
    }


    // =========================================================
    // DOWNLOAD CANVAS ICS CALENDAR
    // =========================================================

    private String downloadCanvasCalendar(
            String calendarUrl
    ) throws Exception {

        URL url =
                URI.create(calendarUrl).toURL();

        HttpURLConnection connection =
                (HttpURLConnection)
                        url.openConnection();

        connection.setRequestMethod("GET");

        connection.setConnectTimeout(
                10000
        );

        connection.setReadTimeout(
                10000
        );


        int responseCode =
                connection.getResponseCode();


        if (responseCode != HttpURLConnection.HTTP_OK) {

            throw new Exception(
                    "Canvas returned HTTP "
                            + responseCode
            );
        }


        StringBuilder result =
                new StringBuilder();


        try (
                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        connection.getInputStream(),
                                        StandardCharsets.UTF_8
                                )
                        )
        ) {

            String line;

            while (
                    (line = reader.readLine())
                            != null
            ) {

                result.append(line)
                        .append("\n");
            }
        }


        connection.disconnect();


        return result.toString();
    }


    // =========================================================
    // PARSE CANVAS ICS DATA
    // =========================================================

    private List<Assignment> parseCanvasCalendar(
            String icsData
    ) {

        List<Assignment> assignments =
                new ArrayList<>();


        String[] events =
                icsData.split(
                        "BEGIN:VEVENT"
                );


        for (String event : events) {

            if (!event.contains(
                    "END:VEVENT"
            )) {
                continue;
            }


            String summary =
                    extractICSValue(
                            event,
                            "SUMMARY"
                    );


            String description =
                    extractICSValue(
                            event,
                            "DESCRIPTION"
                    );


            String startDate =
                    extractICSValue(
                            event,
                            "DTSTART"
                    );


            if (summary == null
                    || summary.isBlank()) {

                continue;
            }


            if (startDate == null
                    || startDate.isBlank()) {

                continue;
            }


            LocalDate dueDate =
                    parseICSDate(
                            startDate
                    );


            if (dueDate == null) {
                continue;
            }


            Assignment assignment =
                    new Assignment(
                            summary,
                            "Canvas",
                            dueDate,
                            1,
                            "Medium",
                            description == null
                                    ? ""
                                    : description
                    );


            assignments.add(
                    assignment
            );
        }


        return assignments;
    }


    // =========================================================
    // EXTRACT ICS VALUE
    // =========================================================

    private String extractICSValue(
            String event,
            String key
    ) {

        String[] lines =
                event.split("\n");


        for (String line : lines) {

            line = line.trim();


            // Example:
            // SUMMARY:Final Project

            if (line.startsWith(
                    key + ":"
            )) {

                return line.substring(
                        (key + ":").length()
                ).trim();
            }


            // Example:
            // DTSTART;VALUE=DATE:20260818

            if (line.startsWith(
                    key + ";"
            )) {

                int colonIndex =
                        line.indexOf(':');


                if (colonIndex >= 0) {

                    return line.substring(
                            colonIndex + 1
                    ).trim();
                }
            }
        }


        return null;
    }


    // =========================================================
    // PARSE ICS DATE
    // =========================================================

    private LocalDate parseICSDate(
            String value
    ) {

        try {

            /*
             * Canvas normally gives:
             *
             * 20260818
             *
             * or:
             *
             * 20260818T120000Z
             *
             * We only need the first 8 digits.
             */

            if (value.length() < 8) {
                return null;
            }


            String date =
                    value.substring(
                            0,
                            8
                    );


            return LocalDate.of(

                    Integer.parseInt(
                            date.substring(
                                    0,
                                    4
                            )
                    ),

                    Integer.parseInt(
                            date.substring(
                                    4,
                                    6
                            )
                    ),

                    Integer.parseInt(
                            date.substring(
                                    6,
                                    8
                            )
                    )
            );


        } catch (Exception e) {

            return null;
        }
    }
}