// CourseReviewsApplication.java
package edu.virginia.sde.reviews;

import javafx.application.Application;
import javafx.stage.Stage;

public class CourseReviewsApplication extends Application {

    @Override
    public void start(Stage stage) {
        LoginScene loginScene = new LoginScene(stage);
        stage.setScene(loginScene.getScene());
        stage.setTitle("Course Reviews Login");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
