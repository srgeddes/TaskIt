package edu.virginia.sde.reviews.controllers;

import edu.virginia.sde.reviews.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;


public class LoginSceneController {
    @FXML
    private Label messageLabel;

    private SceneManager sceneManager;  // Add this field
    private Stage stage;

    public LoginSceneController() {}

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public void handleButton() {

    }
}

