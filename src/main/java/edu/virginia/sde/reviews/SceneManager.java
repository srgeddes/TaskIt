package edu.virginia.sde.reviews;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
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

    public void switchToLoginScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/edu/virginia/sde/reviews/LoginStyling/login.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();
    }

    public void switchToCourseSearchScene(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("/edu/virginia/sde/reviews/CourseSearchStyling/course-search.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Course Search");
        stage.show();
    }

    public void switchToCreateAccountScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/edu/virginia/sde/reviews/CreateAccountStyling/create-account.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Create Account");
        stage.show();
    }

    public void switchToCourseReviewsScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/edu/virginia/sde/reviews/CourseReviewsStyling/course-reviews.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Course Reviews");
        stage.show();
    }

    public void switchToMyReviewsScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/edu/virginia/sde/reviews/MyReviewsStyling/my-reviews.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("My Reviews");
        stage.show();
    }


}

