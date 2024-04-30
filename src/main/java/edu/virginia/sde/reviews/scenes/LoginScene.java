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
            Parent root = FXMLLoader.load(getClass().getResource("/edu/virginia/sde/reviews/login.fxml"));
            scene = new Scene(root);
            String css = this.getClass().getResource("/edu/virginia/sde/reviews/login.css").toExternalForm();
            scene.getStylesheets().add(css);
            stage.setTitle("Login");
        } catch(Exception e) {
            e.printStackTrace();
        }
//        this.stage = stage;
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/edu/virginia/sde/reviews/login.fxml"));
//        scene = new Scene(fxmlLoader.load());
//        stage.setTitle("Login");
    }

    public Scene getScene() {
        return scene;
    }

}
