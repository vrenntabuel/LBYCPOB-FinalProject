package ph.edu.dlsu.lbycpob.earlybirdapplication.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import ph.edu.dlsu.lbycpob.earlybirdapplication.model.Assignment;
import ph.edu.dlsu.lbycpob.earlybirdapplication.service.RiskCalculator;
import ph.edu.dlsu.lbycpob.earlybirdapplication.service.TaskManager;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

public class CalendarController {

    @FXML
    private Label dateRangeLabel;

    @FXML
    private VBox monthView;

    @FXML
    private VBox weekView;

    @FXML
    private VBox riskContainer;

    @FXML
    private GridPane monthGrid;

    @FXML
    private GridPane weekGrid;


    /*
     * Start the calendar on the current month and current week.
     */
    private YearMonth currentMonth = YearMonth.now();

    private LocalDate currentWeek = LocalDate.now();


    @FXML
    public void initialize() {

        showMonth();
        updateRiskIndicator();
    }


    // =========================================================
    // NAVIGATION
    // =========================================================

    @FXML
    private void handleHome(ActionEvent event) throws IOException {
        navigateTo(
                event,
                "home-view.fxml",
                "EarlyBird - Home"
        );
    }

    @FXML
    private void handleAssignments(ActionEvent event) throws IOException {
        navigateTo(
                event,
                "assignments-view.fxml",
                "EarlyBird - Assignments"
        );
    }

    @FXML
    private void handleCalendar(ActionEvent event) {
        // Already on the Calendar page.
    }

    @FXML
    private void handleFocusTimer(ActionEvent event) throws IOException {
        navigateTo(
                event,
                "focus-view.fxml",
                "EarlyBird - Focus Timer"
        );
    }

    @FXML
    private void handleDashboard(ActionEvent event) throws IOException {
        navigateTo(
                event,
                "dashboard-view.fxml",
                "EarlyBird - Dashboard"
        );
    }


    private void navigateTo(
            ActionEvent event,
            String fxmlFile,
            String title
    ) throws IOException {

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

        Scene scene = new Scene(
                root,
                1000,
                700
        );

        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }


    // =========================================================
    // MONTH / WEEK VIEW
    // =========================================================

    @FXML
    private void showMonthView() {
        showMonth();
    }

    @FXML
    private void showWeekView() {
        showWeek();
    }


    // =========================================================
    // DATE NAVIGATION
    // =========================================================

    @FXML
    private void handlePrevious() {

        if (monthView.isManaged()) {

            currentMonth =
                    currentMonth.minusMonths(1);

            buildMonthCalendar();

        } else {

            currentWeek =
                    currentWeek.minusWeeks(1);

            buildWeekCalendar(
                    currentWeek.with(
                            TemporalAdjusters.previousOrSame(
                                    DayOfWeek.MONDAY
                            )
                    )
            );
        }
    }


    @FXML
    private void handleNext() {

        if (monthView.isManaged()) {

            currentMonth =
                    currentMonth.plusMonths(1);

            buildMonthCalendar();

        } else {

            currentWeek =
                    currentWeek.plusWeeks(1);

            buildWeekCalendar(
                    currentWeek.with(
                            TemporalAdjusters.previousOrSame(
                                    DayOfWeek.MONDAY
                            )
                    )
            );
        }
    }


    // =========================================================
    // MONTH VIEW
    // =========================================================

    private void showMonth() {

        monthView.setVisible(true);
        monthView.setManaged(true);

        weekView.setVisible(false);
        weekView.setManaged(false);

        buildMonthCalendar();
    }


    // =========================================================
    // WEEK VIEW
    // =========================================================

    private void showWeek() {

        monthView.setVisible(false);
        monthView.setManaged(false);

        weekView.setVisible(true);
        weekView.setManaged(true);

        LocalDate monday =
                currentWeek.with(
                        TemporalAdjusters.previousOrSame(
                                DayOfWeek.MONDAY
                        )
                );

        buildWeekCalendar(monday);
    }


    // =========================================================
    // BUILD MONTH CALENDAR
    // =========================================================

    private void buildMonthCalendar() {

        monthGrid.getChildren().clear();

        monthGrid.getColumnConstraints().clear();
        monthGrid.getRowConstraints().clear();

        dateRangeLabel.setText(
                currentMonth.format(
                        DateTimeFormatter.ofPattern("MMMM yyyy")
                )
        );

        // 7 equal columns
        for (int i = 0; i < 7; i++) {

            ColumnConstraints column =
                    new ColumnConstraints();

            column.setPercentWidth(100.0 / 7.0);

            monthGrid
                    .getColumnConstraints()
                    .add(column);
        }

        /*
         * FIXED ROW HEIGHT
         *
         * Row 0 = weekday header
         * Rows 1-6 = calendar weeks
         *
         * This prevents a day with many assignments
         * from stretching the entire calendar.
         */

        RowConstraints headerRow =
                new RowConstraints();

        headerRow.setMinHeight(30);
        headerRow.setPrefHeight(30);
        headerRow.setMaxHeight(30);

        monthGrid
                .getRowConstraints()
                .add(headerRow);


        for (int i = 0; i < 6; i++) {

            RowConstraints row =
                    new RowConstraints();

            row.setMinHeight(80);
            row.setPrefHeight(80);
            row.setMaxHeight(80);

            monthGrid
                    .getRowConstraints()
                    .add(row);
        }


        // Weekday headers

        String[] days = {
                "MON",
                "TUE",
                "WED",
                "THU",
                "FRI",
                "SAT",
                "SUN"
        };


        for (int col = 0; col < 7; col++) {

            Label header =
                    new Label(days[col]);

            header.setMaxWidth(
                    Double.MAX_VALUE
            );

            header.setAlignment(
                    Pos.CENTER
            );

            header.setStyle(
                    "-fx-font-weight: bold;" +
                            "-fx-font-size: 13px;"
            );

            monthGrid.add(
                    header,
                    col,
                    0
            );
        }


        LocalDate firstDay =
                currentMonth.atDay(1);

        int startColumn =
                firstDay
                        .getDayOfWeek()
                        .getValue() - 1;

        int daysInMonth =
                currentMonth.lengthOfMonth();


        // Build dates

        for (
                int dayNumber = 1;
                dayNumber <= daysInMonth;
                dayNumber++
        ) {

            LocalDate date =
                    currentMonth.atDay(dayNumber);

            int position =
                    startColumn + dayNumber - 1;

            int column =
                    position % 7;

            int row =
                    (position / 7) + 1;


            VBox cell =
                    new VBox(3);

            cell.setAlignment(
                    Pos.TOP_LEFT
            );

            /*
             * FIXED SIZE
             */
            cell.setMinHeight(80);
            cell.setPrefHeight(80);
            cell.setMaxHeight(80);

            cell.setMaxWidth(
                    Double.MAX_VALUE
            );

            cell.setStyle(
                    "-fx-border-color: #dddddd;" +
                            "-fx-border-radius: 6;" +
                            "-fx-background-radius: 6;" +
                            "-fx-padding: 6;" +
                            "-fx-background-color: white;"
            );


            Label number =
                    new Label(
                            String.valueOf(dayNumber)
                    );

            number.setStyle(
                    "-fx-font-size: 13px;" +
                            "-fx-font-weight: bold;"
            );


            cell.getChildren()
                    .add(number);


            addMonthEvent(
                    cell,
                    date
            );


            monthGrid.add(
                    cell,
                    column,
                    row
            );
        }

        updateRiskIndicator();
    }


    private void addMonthEvent(
            VBox cell,
            LocalDate date
    ) {

        List<Assignment> assignments =
                TaskManager.getAssignments();


        List<Assignment> matchingAssignments =
                assignments.stream()
                        .filter(
                                assignment ->
                                        assignment
                                                .getDueDate()
                                                .equals(date)
                        )
                        .toList();


        int maxVisible = 3;


        // Show at most 3 assignments

        for (
                int i = 0;
                i < Math.min(
                        maxVisible,
                        matchingAssignments.size()
                );
                i++
        ) {

            Assignment assignment =
                    matchingAssignments.get(i);

            addSmallEvent(
                    cell,
                    "• " + assignment.getTitle()
            );
        }


        // Show remaining count

        int remaining =
                matchingAssignments.size()
                        - maxVisible;


        if (remaining > 0) {

            Label more =
                    new Label(
                            "+ " + remaining + " more"
                    );

            more.setStyle(
                    "-fx-font-size: 10px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: #666666;"
            );

            cell.getChildren()
                    .add(more);
        }
    }


    private void addSmallEvent(
            VBox cell,
            String text
    ) {

        Label event =
                new Label(text);

        event.setWrapText(false);

        event.setMaxWidth(
                Double.MAX_VALUE
        );

        event.setEllipsisString("...");

        event.setStyle(
                "-fx-font-size: 10px;"
        );

        cell.getChildren()
                .add(event);
    }


    // =========================================================
    // BUILD WEEK CALENDAR
    // =========================================================

    private void buildWeekCalendar(
            LocalDate monday
    ) {

        weekGrid.getChildren().clear();

        weekGrid.getColumnConstraints().clear();
        weekGrid.getRowConstraints().clear();


        LocalDate sunday =
                monday.plusDays(6);


        dateRangeLabel.setText(
                monday.format(
                        DateTimeFormatter.ofPattern(
                                "MMM d"
                        )
                )
                        + " - "
                        + sunday.format(
                        DateTimeFormatter.ofPattern(
                                "MMM d, yyyy"
                        )
                )
        );


        // 7 columns
        for (int i = 0; i < 7; i++) {

            ColumnConstraints column =
                    new ColumnConstraints();

            column.setPercentWidth(
                    100.0 / 7.0
            );

            weekGrid
                    .getColumnConstraints()
                    .add(column);
        }


        // 1 row
        RowConstraints row =
                new RowConstraints();

        row.setPercentHeight(100);

        weekGrid
                .getRowConstraints()
                .add(row);


        for (int day = 0; day < 7; day++) {

            LocalDate date =
                    monday.plusDays(day);


            VBox column =
                    new VBox(5);

            column.setAlignment(
                    Pos.TOP_CENTER
            );

            column.setMaxSize(
                    Double.MAX_VALUE,
                    Double.MAX_VALUE
            );

            column.setStyle(
                    "-fx-border-color: #dddddd;" +
                            "-fx-border-radius: 6;" +
                            "-fx-padding: 6;"
            );


            Label header =
                    new Label(
                            date.format(
                                    DateTimeFormatter.ofPattern(
                                            "EEE"
                                    )
                            )
                                    + "\n"
                                    + date.getDayOfMonth()
                    );

            header.setAlignment(
                    Pos.CENTER
            );

            header.setStyle(
                    "-fx-font-weight: bold;" +
                            "-fx-font-size: 13px;"
            );


            column.getChildren()
                    .add(header);


            addWeekEvents(
                    column,
                    date
            );


            weekGrid.add(
                    column,
                    day,
                    0
            );
        }

        updateRiskIndicator();
    }


    private void addWeekEvents(
            VBox column,
            LocalDate date
    ) {

        List<Assignment> assignments =
                TaskManager.getAssignments();


        for (Assignment assignment : assignments) {

            if (
                    assignment
                            .getDueDate()
                            .equals(date)
            ) {

                addWeekEvent(
                        column,
                        assignment.getTitle()
                );
            }
        }
    }


    private void addWeekEvent(
            VBox column,
            String text
    ) {

        Label event =
                new Label(text);

        event.setWrapText(true);

        event.setMaxWidth(
                Double.MAX_VALUE
        );

        event.setStyle(
                "-fx-background-color: #f1f1f1;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 10;" +
                        "-fx-font-size: 12px;"
        );

        column.getChildren()
                .add(event);
    }


    // =========================================================
    // RISK INDICATOR
    // =========================================================

    private void updateRiskIndicator() {

        riskContainer
                .getChildren()
                .clear();


        for (
                Assignment assignment :
                TaskManager.getAssignments()
        ) {

            String risk =
                    RiskCalculator
                            .calculateRisk(
                                    assignment
                            );


            long daysRemaining =
                    RiskCalculator
                            .getDaysRemaining(
                                    assignment
                            );


            HBox riskRow =
                    new HBox(15);

            riskRow.setAlignment(
                    Pos.CENTER_LEFT
            );

            riskRow.setStyle(
                    "-fx-background-color: #f3f3f3;" +
                            "-fx-background-radius: 10;" +
                            "-fx-padding: 15;"
            );


            Label titleLabel =
                    new Label(
                            assignment.getTitle()
                    );

            titleLabel.setStyle(
                    "-fx-font-weight: bold;"
            );


            String dueText;


            if (daysRemaining < 0) {

                dueText =
                        "Overdue by "
                                + Math.abs(daysRemaining)
                                + " days";

            } else if (daysRemaining == 0) {

                dueText =
                        "Due today";

            } else if (daysRemaining == 1) {

                dueText =
                        "Due in 1 day";

            } else {

                dueText =
                        "Due in "
                                + daysRemaining
                                + " days";
            }


            Label dueLabel =
                    new Label(dueText);


            Label riskLabel =
                    new Label(
                            risk + " Risk"
                    );

            riskLabel.setStyle(
                    "-fx-font-weight: bold;"
            );


            riskRow.getChildren()
                    .addAll(
                            titleLabel,
                            dueLabel,
                            riskLabel
                    );


            riskContainer
                    .getChildren()
                    .add(riskRow);
        }
    }
}