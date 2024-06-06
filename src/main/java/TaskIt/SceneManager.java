package TaskIt;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {
    private static SceneManager instance;
    private Stage stage;

    // Private constructor to prevent instantiation
    private SceneManager(Stage stage) {
        this.stage = stage;
    }

    // Public method to get the singleton instance
    public static synchronized SceneManager getInstance(Stage stage) {
        if (instance == null) {
            instance = new SceneManager(stage);
        }
        return instance;
    }

    // Overloaded method to get the instance without passing the stage
    public static synchronized SceneManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("SceneManager is not initialized. Call getInstance(Stage) first.");
        }
        return instance;
    }

    public void switchToLoginScene() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/TaskIt/LoginView.fxml")); 
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/TaskIt/Shared/Shared.css").toExternalForm());
        stage.setScene(scene);
        
        stage.show();
    }

    public void switchToCreateAccountScene() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/TaskIt/CreateAccountView.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/TaskIt/Shared/Shared.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public void switchToTaskListScene() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/TaskIt/TaskListView.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/TaskIt/Shared/Shared.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }


}
