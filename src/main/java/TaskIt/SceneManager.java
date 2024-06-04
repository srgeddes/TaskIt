package TaskIt;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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

    public void switchToLoginScene(Event event) throws IOException {
        Node source = (Node) event.getSource();
        Parent root = FXMLLoader.load(getClass().getResource("/TaskIt/LoginView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();
    }

    public void switchToCreateAccountScene(Event event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/TaskIt/CreateAccountView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Create Account");
        stage.show();
    }
    
    public void switchToTaskListScene(Event event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/TaskIt/TaskListView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Create Account");
        stage.show();
    }
}
