package TaskIt;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {
    private static SceneManager instance;
    private Stage stage;

    private SceneManager(Stage stage) {
        this.stage = stage;
    }

    public static synchronized SceneManager getInstance(Stage stage) {
        if (instance == null) {
            instance = new SceneManager(stage);
        }
        return instance;
    }

    public static synchronized SceneManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("SceneManager is not initialized. Call getInstance(Stage) first.");
        }
        return instance;
    }

    public void switchToLoginScene() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/TaskIt/Login/LoginView.fxml")); 
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/TaskIt/Shared/Shared.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Task It - Login");
        stage.show();
    }

    public void switchToCreateAccountScene() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/TaskIt/CreateAccount/CreateAccountView.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/TaskIt/Shared/Shared.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Task It - Create Account");
        stage.show();
    }

    public void switchToTaskListScene() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/TaskIt/TaskList/TaskListView.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/TaskIt/Shared/Shared.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Task It - Task List");
        stage.show();
    }


}
