package edu.virginia.sde.reviews;

import edu.virginia.sde.reviews.scenes.CourseSearchScene;
import edu.virginia.sde.reviews.scenes.LoginScene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SceneManager {
    private Map<String, Scene> scenes = new HashMap<>();
    private Stage stage;
    private Scene scene;
    private Parent root;

    public SceneManager(Stage stage) {
        this.stage = stage;
    }

    public void addScene(String name, Scene scene) {
        scenes.put(name, scene);
    }

    public void switchToLoginScene() {

    }
    public void switchToLoginScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/edu/virginia/sde/reviews/LoginStyling/login.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToCourseSearchScene(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/edu/virginia/sde/reviews/CourseSearchStyling/course-search.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();     }

    public Map<String, Scene> getScenes() {
        return scenes;
    }

    public Scene getScene(String name) {
        if (scenes.containsKey(name)) {
            return scenes.get(name);
        }
        return null;
    }

    public void initScenes() throws Exception {
        LoginScene loginScene = new LoginScene(stage);
        CourseSearchScene courseSearchScene = new CourseSearchScene(stage);
        // add other scenes here
        addScene("login", loginScene.getScene());
        addScene("search", courseSearchScene.getScene());
    }
}

