package TaskIt;

import TaskIt.Data.DatabaseDriver;
import TaskIt.Data.Models.Task;
import TaskIt.Data.Models.TaskRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Program extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        DatabaseDriver.main(new String[0]);
    }
}
