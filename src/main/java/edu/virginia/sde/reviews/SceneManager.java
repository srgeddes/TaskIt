package edu.virginia.sde.reviews;

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


    public void initializeLoginScene(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/edu/virginia/sde/reviews/LoginStyling/login.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public void initializeCourseSearchScene(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/edu/virginia/sde/reviews/CourseSearchStyling/courseSearch.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public void switchToLoginScene(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/edu/virginia/sde/reviews/LoginStyling/login.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToCourseSearchScene(javafx.event.ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("/edu/virginia/sde/reviews/CourseSearchStyling/course-search.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToCreateAccountScene(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/edu/virginia/sde/reviews/CreateAccountStyling/create-account.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}

