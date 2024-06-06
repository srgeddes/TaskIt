package TaskIt;

import TaskIt.Data.DatabaseDriver;
import javafx.application.Application;
import javafx.stage.Stage;

public class Program extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        SceneManager sceneManager = SceneManager.getInstance(stage);
        sceneManager.switchToLoginScene();

        DatabaseDriver.main(new String[0]);
    }
}
