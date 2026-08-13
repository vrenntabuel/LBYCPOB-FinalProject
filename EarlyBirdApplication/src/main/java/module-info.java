module ph.edu.dlsu.lbycpob.earlybirdapplication {

    requires javafx.controls;
    requires javafx.fxml;

    opens ph.edu.dlsu.lbycpob.earlybirdapplication to javafx.fxml;
    opens ph.edu.dlsu.lbycpob.earlybirdapplication.controller to javafx.fxml;

    exports ph.edu.dlsu.lbycpob.earlybirdapplication;
}