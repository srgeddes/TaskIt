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

public class CreateAccountController {

    // TODO : Implement show password here:
    @FXML
    CheckBox passwordShowCheckbox;

    @FXML
    TextField usernameField;
    @FXML
    TextField confirmUsernameField;
    @FXML
    PasswordField passwordField;
    @FXML
    PasswordField confirmPasswordField;
    @FXML
    Label createAccountError;
    private Stage stage;


    public CreateAccountController() {}



    public void createAccount(ActionEvent event) throws IOException, SQLException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmUsername = confirmUsernameField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty()) {
            createAccountError.setText("Username cannot be empty");
            createAccountError.setVisible(true);
            return;
        }

        if (!username.equals(confirmUsername)) {
            createAccountError.setText("Usernames do not match");
            createAccountError.setVisible(true);
            return;
        }

        if (!password.equals(confirmPassword)) {
            createAccountError.setText("Passwords do not match");
            createAccountError.setVisible(true);
            return;
        }

        if (password.length() < 8) {
            createAccountError.setText("Password must be at least 8 characters");
            createAccountError.setVisible(true);
            return;
        }



        // TODO : Check to make sure that the user is not already in the database

        DatabaseDriver databaseDriver = new DatabaseDriver();
        databaseDriver.connect();
        if (!databaseDriver.doesUserExist(username)) {
            databaseDriver.addUser(username, password);
            databaseDriver.commit();
            databaseDriver.setCurrentUser(username);

            databaseDriver.disconnect();
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            SceneManager sceneManager = new SceneManager(stage);
            sceneManager.switchToLoginScene(event);

        } else {
            databaseDriver.disconnect();
            createAccountError.setText("Account already exists");
            createAccountError.setVisible(true);
            return;
        }
        createAccountError.setVisible(false);
    }

    public void handleKeyPressed(KeyEvent keyEvent) throws IOException, SQLException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            createAccount(new ActionEvent(keyEvent.getSource(), keyEvent.getTarget()));
        }
    }

    public void switchToLoginScene(ActionEvent event) throws IOException {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneManager sceneManager = new SceneManager(stage);
        sceneManager.switchToLoginScene(event);
    }


    // TODO : IMPLEMENT THIS
//    @FXML
//    void handlePasswordShow() throws IOException{
//        incorrectUsernameOrPasswordHide();
//        if (passwordShowCheckbox.isSelected()) {
//            visiblePasswordField.setText(hiddenPasswordField.getText());
//            visiblePasswordField.setVisible(true);
//            hiddenPasswordField.setVisible(false);
//        } else {
//            hiddenPasswordField.setText(visiblePasswordField.getText());
//            hiddenPasswordField.setVisible(true);
//            visiblePasswordField.setVisible(false);
//        }
//    }

}
