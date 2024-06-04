package TaskIt;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {

    private Stage stage;
    private Scene scene;
    private Parent root;

    public SceneManager(Stage stage) {
        this.stage = stage;
    }
    public void initializeLoginScene(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/TaskIt/LoginView.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }
}
