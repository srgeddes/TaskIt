package edu.virginia.sde.reviews.controllers;

import edu.virginia.sde.reviews.SceneManager;
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


public class LoginSceneController {

    @FXML
    TextField usernameField;
    @FXML
    PasswordField hiddenPasswordField;
    @FXML
    TextField visiblePasswordField;
    @FXML
    Label errorLabel;

    @FXML
    TextField createAccountUsernameField;
    @FXML
    TextField confirmAccountUsernameField;
    @FXML
    PasswordField createAccountPasswordField;
    @FXML
    PasswordField confirmAccountPasswordField;
    @FXML
    Label usernameDoesNotMatchError;
    @FXML
    Label passwordDoesNotMatchError;
    @FXML
    Label passwordTooShortError;
    @FXML
    CheckBox passwordShowCheckbox;





    private Stage stage;

    public LoginSceneController() {}

    public void login(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = hiddenPasswordField.getText();
        // TODO : Delete this and query the database for the username and password
        if (username.equals("1") && password.equals("1")) {
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            SceneManager sceneManager = new SceneManager(stage);
            sceneManager.switchToCourseSearchScene(event);
        } else {
            errorLabel.setVisible(true);
        }
    }

    public void handleKeyPressed(KeyEvent keyEvent) throws IOException {
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

    public void switchToLoginScene(ActionEvent event) throws IOException {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        SceneManager sceneManager = new SceneManager(stage);
        sceneManager.switchToLoginScene(event);
    }

    public void switchToCreateAccountScene(javafx.event.ActionEvent event) throws IOException {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        SceneManager sceneManager = new SceneManager(stage);
        sceneManager.switchToCreateAccountScene(event);
    }



    public void createAccount(ActionEvent event) throws IOException {
        String username1 = createAccountUsernameField.getText();
        String username2 = confirmAccountUsernameField.getText();
        String password1 = createAccountPasswordField.getText();
        String password2 = confirmAccountPasswordField.getText();

        if (username1.equals(username2) && password1.equals(password2) && !username1.isEmpty() && password1.length() >= 8) {
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            SceneManager sceneManager = new SceneManager(stage);
            sceneManager.switchToCourseSearchScene(event);
            // TODO : Update the current account with database and add them to the list of users
        } else {
            if (!username1.equals(username2) || username1.isEmpty()) {
                usernameDoesNotMatchError.setVisible(true);
            }
            else if (!password1.equals(password2)) {
                passwordDoesNotMatchError.setVisible(true);
            } else {
                passwordTooShortError.setVisible(true);
            }

        }
    }

    @FXML
    public void usernamePasswordErrorHide() throws IOException {
        createAccountUsernameField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                usernameDoesNotMatchError.setVisible(false);
            }
        });
        confirmAccountUsernameField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                usernameDoesNotMatchError.setVisible(false);
            }
        });
        createAccountPasswordField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                passwordDoesNotMatchError.setVisible(false);
                passwordTooShortError.setVisible(false);
            }
        });
        confirmAccountPasswordField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                passwordDoesNotMatchError.setVisible(false);
                passwordTooShortError.setVisible(false);
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

}

