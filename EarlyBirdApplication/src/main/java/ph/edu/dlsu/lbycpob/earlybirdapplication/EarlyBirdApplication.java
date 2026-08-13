package ph.edu.dlsu.lbycpob.earlybirdapplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class EarlyBirdApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(
                EarlyBirdApplication.class.getResource("home-view.fxml")
        );

        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);

        stage.setTitle("EarlyBird");
        stage.setScene(scene);
        stage.show();
    }
}