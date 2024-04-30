package edu.virginia.sde.reviews.scenes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CourseSearchScene {

    private Scene scene;
    private Stage stage;

    public CourseSearchScene(Stage stage) throws Exception{
        this.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("course-search.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Course Search");
    }

    public Scene getScene() {
        return scene;
    }

}
