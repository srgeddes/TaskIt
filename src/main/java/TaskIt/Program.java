package TaskIt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Program extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-world.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Riley Geddes");
        stage.setScene(scene);
        stage.show();
    }
}