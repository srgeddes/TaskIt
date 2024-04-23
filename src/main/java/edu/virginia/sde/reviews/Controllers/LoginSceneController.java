package edu.virginia.sde.reviews.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class LoginSceneController {
    @FXML
    private Label messageLabel;

    public void handleButton() {
        messageLabel.setText("You pressed the button!");
    }
}
