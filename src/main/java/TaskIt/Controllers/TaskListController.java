package TaskIt.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class TaskListController {
    @FXML
    private Label messageLabel;

    public void handleButton() {
        messageLabel.setText("You pressed the button!");
    }
}
