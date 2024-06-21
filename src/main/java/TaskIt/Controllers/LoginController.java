package TaskIt.Controllers;

import TaskIt.Data.Models.IUserRepository;
import TaskIt.Data.Models.User;
import TaskIt.Data.Models.UserRepository;
import TaskIt.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController extends Controller implements Initializable {

    IUserRepository _userRepository;
    SceneManager sceneManager; 
    
    @FXML
    TextField usernameField; 
    @FXML 
    TextField passwordField;
    @FXML
    Label clockLabel; 
    
    @FXML 
    Label invalidUserErrorLabel; 
    
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this._userRepository = new UserRepository();
        this.sceneManager = SceneManager.getInstance();
        super.bindToTime(clockLabel);
    }
    
    public void handleLogin(ActionEvent actionEvent) throws SQLException, IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (_userRepository.isValidUser(username, password)) {
            User user = _userRepository.getUserByUsername(username); 
            _userRepository.setCurrentUser(user);
            sceneManager.switchToTaskListScene();
        }   else {
            showValidationErrorMessage(); 
        }
    }
    
    public void handleCreateAccount(ActionEvent actionEvent) throws IOException {
        sceneManager.switchToCreateAccountScene();
    }
    
    public void handleFieldClick(MouseEvent actionEvent) throws IOException {
        hideValidationErrorMessage(); 
    }
    
    public void showValidationErrorMessage() {
        invalidUserErrorLabel.setVisible(true);
    }
    
    public void hideValidationErrorMessage() {
        invalidUserErrorLabel.setVisible(false);
    }

    
}
