package edu.virginia.sde.reviews;

import edu.virginia.sde.reviews.scenes.CourseSearchScene;
import edu.virginia.sde.reviews.scenes.LoginScene;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Map;

public class SceneManager {
    private Map<String, Scene> scenes = new HashMap<>();
    private Stage stage;

    public SceneManager(Stage stage) {
        this.stage = stage;
    }

    public void addScene(String name, Scene scene) {
        scenes.put(name, scene);
    }

    public void setActiveScene(String name) {
        Scene scene = scenes.get(name);
        if (scene != null) {
            stage.setScene(scene);
        } else {
            System.out.println("scene not found");
        }

    }

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
//        CourseSearchScene courseSearchScene = new CourseSearchScene(stage);
        // TODO : Add other scenes here
        addScene("login", loginScene.getScene());
//        addScene("search", courseSearchScene.getScene());
    }

}

