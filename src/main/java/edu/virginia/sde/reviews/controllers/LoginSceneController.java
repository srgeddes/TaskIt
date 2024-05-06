package edu.virginia.sde.reviews.controllers;

import edu.virginia.sde.reviews.SceneManager;
import edu.virginia.sde.reviews.database.DatabaseDriver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;


public class LoginSceneController {

    private Stage stage;

    @FXML
    TextField usernameField;
    @FXML
    PasswordField hiddenPasswordField;
    @FXML
    TextField visiblePasswordField;
    @FXML
    Label errorLabel;
    @FXML
    CheckBox passwordShowCheckbox;


    public LoginSceneController() {}

    public void login(ActionEvent event) throws IOException, SQLException {
        String username = (usernameField != null) ? usernameField.getText() : "";
        String password = (hiddenPasswordField != null) ? hiddenPasswordField.getText() : "";

        if (!username.isEmpty() && !password.isEmpty()) {
            DatabaseDriver databaseDriver = new DatabaseDriver();
            databaseDriver.connect();
            boolean isValidUsernameAndPassword = databaseDriver.isValidUser(username, password);
            databaseDriver.disconnect();
            databaseDriver.setCurrentUser(username);

            if (isValidUsernameAndPassword) {
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                SceneManager sceneManager = new SceneManager(stage);
                sceneManager.switchToCourseSearchScene(event, username);
            } else {
                errorLabel.setVisible(true);
            }
        } else errorLabel.setVisible(true);
    }

    public void handleKeyPressed(KeyEvent keyEvent) throws IOException, SQLException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            login(new ActionEvent(keyEvent.getSource(), keyEvent.getTarget()));
        }
    }

    @FXML
    public void incorrectUsernameOrPasswordHide() throws IOException {
        usernameField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                errorLabel.setVisible(false);
            }
        });
        hiddenPasswordField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                errorLabel.setVisible(false);
            }
        });
    }

    @FXML
    void handlePasswordShow() throws IOException{
        incorrectUsernameOrPasswordHide();
        if (passwordShowCheckbox.isSelected()) {
            visiblePasswordField.setText(hiddenPasswordField.getText());
            visiblePasswordField.setVisible(true);
            hiddenPasswordField.setVisible(false);
        } else {
            hiddenPasswordField.setText(visiblePasswordField.getText());
            hiddenPasswordField.setVisible(true);
            visiblePasswordField.setVisible(false);
        }
    }

    public void switchToCreateAccountScene(javafx.event.ActionEvent event) throws IOException {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        SceneManager sceneManager = new SceneManager(stage);
        sceneManager.switchToCreateAccountScene(event);
    }

}

