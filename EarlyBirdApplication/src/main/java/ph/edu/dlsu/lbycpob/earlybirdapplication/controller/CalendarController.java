package ph.edu.dlsu.lbycpob.earlybirdapplication.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import ph.edu.dlsu.lbycpob.earlybirdapplication.model.Assignment;
import ph.edu.dlsu.lbycpob.earlybirdapplication.service.RiskCalculator;
import ph.edu.dlsu.lbycpob.earlybirdapplication.service.TaskManager;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

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

    private YearMonth currentMonth = YearMonth.of(2025, 5);
    private LocalDate currentWeek = LocalDate.of(2025, 5, 21);

    @FXML
    public void initialize() {

        showMonth();
        updateRiskIndicator();
    }

    @FXML
    private void showMonthView() {
        showMonth();
    }

    @FXML
    private void showWeekView() {
        showWeek();
    }

    @FXML
    private void handlePrevious() {
        if (monthView.isManaged()) {
            currentMonth = currentMonth.minusMonths(1);
            buildMonthCalendar();
        } else {
            currentWeek = currentWeek.minusWeeks(1);
            buildWeekCalendar(
                    currentWeek.with(
                            TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)
                    )
            );
        }
    }

    @FXML
    private void handleNext() {
        if (monthView.isManaged()) {
            currentMonth = currentMonth.plusMonths(1);
            buildMonthCalendar();
        } else {
            currentWeek = currentWeek.plusWeeks(1);
            buildWeekCalendar(
                    currentWeek.with(
                            TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)
                    )
            );
        }
    }

    private void showMonth() {

        monthView.setVisible(true);
        monthView.setManaged(true);

        weekView.setVisible(false);
        weekView.setManaged(false);

        buildMonthCalendar();
    }

    private void showWeek() {

        monthView.setVisible(false);
        monthView.setManaged(false);

        weekView.setVisible(true);
        weekView.setManaged(true);

        LocalDate monday = currentWeek.with(
                TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)
        );

        buildWeekCalendar(monday);
    }

    private void buildMonthCalendar() {

        monthGrid.getChildren().clear();

        // IMPORTANT: clear old constraints
        monthGrid.getColumnConstraints().clear();
        monthGrid.getRowConstraints().clear();

        dateRangeLabel.setText(
                currentMonth.format(
                        DateTimeFormatter.ofPattern("MMMM yyyy")
                )
        );

        // 7 fixed columns
        for (int i = 0; i < 7; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100.0 / 7.0);
            monthGrid.getColumnConstraints().add(column);
        }

        // 6 fixed rows
        for (int i = 0; i < 6; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(16.6667);
            monthGrid.getRowConstraints().add(row);
        }

        String[] days = {
                "MON", "TUE", "WED",
                "THU", "FRI", "SAT", "SUN"
        };

        for (int col = 0; col < 7; col++) {

            Label header = new Label(days[col]);

            header.setMaxWidth(Double.MAX_VALUE);
            header.setAlignment(Pos.CENTER);

            header.setStyle(
                    "-fx-font-weight: bold;" +
                            "-fx-font-size: 13px;"
            );

            monthGrid.add(header, col, 0);
        }

        LocalDate firstDay = currentMonth.atDay(1);

        int startColumn = firstDay.getDayOfWeek().getValue() - 1;
        int daysInMonth = currentMonth.lengthOfMonth();

        for (int dayNumber = 1; dayNumber <= daysInMonth; dayNumber++) {

            LocalDate date = currentMonth.atDay(dayNumber);

            int position = startColumn + dayNumber - 1;

            int column = position % 7;
            int row = (position / 7) + 1;

            VBox cell = new VBox(5);

            cell.setAlignment(Pos.TOP_LEFT);
            cell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            cell.setStyle(
                    "-fx-border-color: #dddddd;" +
                            "-fx-border-radius: 6;" +
                            "-fx-background-radius: 6;" +
                            "-fx-padding: 8;" +
                            "-fx-background-color: white;"
            );

            Label number = new Label(String.valueOf(dayNumber));

            number.setStyle(
                    "-fx-font-size: 14px;" +
                            "-fx-font-weight: bold;"
            );

            cell.getChildren().add(number);

            addMonthEvent(cell, date);

            monthGrid.add(cell, column, row);
        }
        updateRiskIndicator();
    }

    private void addMonthEvent(VBox cell, LocalDate date) {

        if (date.equals(LocalDate.of(2025, 5, 7))) {
            addSmallEvent(cell, "• Quiz");
        }

        if (date.equals(LocalDate.of(2025, 5, 10))) {
            addSmallEvent(cell, "• Report");
        }

        if (date.equals(LocalDate.of(2025, 5, 14))) {
            addSmallEvent(cell, "• Lab");
        }

        if (date.equals(LocalDate.of(2025, 5, 22))) {
            addSmallEvent(cell, "• Exam");
        }

        if (date.equals(LocalDate.of(2025, 5, 25))) {
            addSmallEvent(cell, "• Project Due");
        }
    }

    private void addSmallEvent(VBox cell, String text) {

        Label event = new Label(text);

        event.setWrapText(true);
        event.setStyle("-fx-font-size: 11px;");

        cell.getChildren().add(event);
    }

    private void buildWeekCalendar(LocalDate monday) {

        weekGrid.getChildren().clear();

        // IMPORTANT: clear old constraints
        weekGrid.getColumnConstraints().clear();
        weekGrid.getRowConstraints().clear();

        LocalDate sunday = monday.plusDays(6);

        dateRangeLabel.setText(
                monday.format(
                        DateTimeFormatter.ofPattern("MMM d")
                )
                        + " - "
                        + sunday.format(
                        DateTimeFormatter.ofPattern("MMM d, yyyy")
                )
        );

        // 7 fixed columns
        for (int i = 0; i < 7; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100.0 / 7.0);
            weekGrid.getColumnConstraints().add(column);
        }

        // 1 fixed row
        RowConstraints row = new RowConstraints();
        row.setPercentHeight(100);
        weekGrid.getRowConstraints().add(row);

        for (int day = 0; day < 7; day++) {

            LocalDate date = monday.plusDays(day);

            VBox column = new VBox(10);

            column.setAlignment(Pos.TOP_CENTER);
            column.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            column.setStyle(
                    "-fx-border-color: #dddddd;" +
                            "-fx-border-radius: 6;" +
                            "-fx-padding: 10;"
            );

            Label header = new Label(
                    date.format(
                            DateTimeFormatter.ofPattern("EEE")
                    )
                            + "\n"
                            + date.getDayOfMonth()
            );

            header.setAlignment(Pos.CENTER);

            header.setStyle(
                    "-fx-font-weight: bold;" +
                            "-fx-font-size: 13px;"
            );

            column.getChildren().add(header);

            addWeekEvents(column, date);

            weekGrid.add(column, day, 0);
        }
        updateRiskIndicator();
    }

    private void addWeekEvents(VBox column, LocalDate date) {

        if (date.equals(LocalDate.of(2025, 5, 19))) {
            addWeekEvent(column, "Study Session");
            addWeekEvent(column, "Lab Work");
        }

        if (date.equals(LocalDate.of(2025, 5, 21))) {
            addWeekEvent(column, "Study Session");
            addWeekEvent(column, "Quiz Prep");
        }

        if (date.equals(LocalDate.of(2025, 5, 22))) {
            addWeekEvent(column, "Math Lecture");
        }

        if (date.equals(LocalDate.of(2025, 5, 24))) {
            addWeekEvent(column, "English Class");
        }

        if (date.equals(LocalDate.of(2025, 5, 25))) {
            addWeekEvent(column, "Assignment Time");
        }
    }

    private void addWeekEvent(VBox column, String text) {

        Label event = new Label(text);

        event.setWrapText(true);
        event.setMaxWidth(Double.MAX_VALUE);

        event.setStyle(
                "-fx-background-color: #f1f1f1;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 10;" +
                        "-fx-font-size: 12px;"
        );

        column.getChildren().add(event);
    }

    private void updateRiskIndicator() {

        riskContainer.getChildren().clear();

        for (Assignment assignment : TaskManager.getAssignments()) {

            String risk = RiskCalculator.calculateRisk(assignment);

            long daysRemaining =
                    RiskCalculator.getDaysRemaining(assignment);

            HBox riskRow = new HBox(15);

            riskRow.setAlignment(Pos.CENTER_LEFT);

            riskRow.setStyle(
                    "-fx-background-color: #f3f3f3;" +
                            "-fx-background-radius: 10;" +
                            "-fx-padding: 15;"
            );

            Label titleLabel =
                    new Label(assignment.getTitle());

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

                dueText = "Due today";

            } else if (daysRemaining == 1) {

                dueText = "Due in 1 day";

            } else {

                dueText =
                        "Due in "
                                + daysRemaining
                                + " days";
            }

            Label dueLabel =
                    new Label(dueText);

            Label riskLabel =
                    new Label(risk + " Risk");

            riskLabel.setStyle(
                    "-fx-font-weight: bold;"
            );

            riskRow.getChildren().addAll(
                    titleLabel,
                    dueLabel,
                    riskLabel
            );

            riskContainer.getChildren().add(riskRow);
        }
    }

}