package edu.virginia.sde.reviews;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginScene {

    private Scene scene;
    private Stage stage;

    public LoginScene(Stage stage) { // Constructor name corrected
        this.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoginScene.fxml"));

    }





    public Scene getScene() {
        return scene;
    }


}
