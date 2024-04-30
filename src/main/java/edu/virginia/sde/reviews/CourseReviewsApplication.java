// CourseReviewsApplication.java
package edu.virginia.sde.reviews;

import edu.virginia.sde.reviews.controllers.LoginSceneController;
import edu.virginia.sde.reviews.scenes.LoginScene;
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
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/virginia/sde/reviews/LoginStyling/login.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {}
        SceneManager sceneManager = new SceneManager(stage);
        sceneManager.switchToLoginScene();

        stage.show();
    }


}
