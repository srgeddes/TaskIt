// CourseReviewsApplication.java
package edu.virginia.sde.reviews;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CourseReviewsApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        stage.setWidth(1280);
        stage.setHeight(720);
        SceneManager sceneManager = new SceneManager(stage);
//        sceneManager.initializeLoginScene(stage);
        sceneManager.initializeCourseSearchScene(stage);
    }
}
