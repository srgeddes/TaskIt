package edu.virginia.sde.reviews.scenes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginScene {

    private Scene scene;
    private Stage stage;

    public LoginScene(Stage stage) throws Exception {
        this.stage = stage;
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/virginia/sde/reviews/LoginStyling/login.fxml"));
            scene = new Scene(root);
            String css = this.getClass().getResource("/edu/virginia/sde/reviews/LoginStyling/login.css").toExternalForm();
            scene.getStylesheets().add(css);
            stage.setTitle("Login");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public Scene getScene() {
        return scene;
    }

}
