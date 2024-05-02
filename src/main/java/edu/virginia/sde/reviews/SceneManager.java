package edu.virginia.sde.reviews;

import edu.virginia.sde.reviews.controllers.CourseReviewsController;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.event.Event;
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


    public void initializeLoginScene(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/edu/virginia/sde/reviews/LoginStyling/login.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public void initializeCourseSearchScene(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/edu/virginia/sde/reviews/CourseSearchStyling/course-search.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Course Search");
        stage.setScene(scene);
        stage.show();
    }

    public void switchToLoginScene(Event event) throws IOException {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/edu/virginia/sde/reviews/LoginStyling/login.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();
    }

    public void switchToCourseSearchScene(Event event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("/edu/virginia/sde/reviews/CourseSearchStyling/course-search.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Course Search");
        stage.show();
    }

    public void switchToCreateAccountScene(Event event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/edu/virginia/sde/reviews/CreateAccountStyling/create-account.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Create Account");
        stage.show();
    }

    public void switchToCourseReviewsScene(Event event, String course) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/virginia/sde/reviews/CourseReviewsStyling/course-reviews.fxml"));
        Parent root = loader.load();
        CourseReviewsController courseReviewsController = loader.getController();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        courseReviewsController.setCourseLabel(course);
        stage.setTitle("Course Reviews");
        stage.show();
    }

    public void switchToMyReviewsScene(Event event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/edu/virginia/sde/reviews/MyReviewsStyling/my-reviews.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("My Reviews");
        stage.show();
    }


}

