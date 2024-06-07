package TaskIt.Controllers;

import TaskIt.Data.Models.IUserRepository;
import TaskIt.Data.Models.User;
import TaskIt.Data.Models.UserRepository;
import TaskIt.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    IUserRepository _userRepository;
    SceneManager sceneManager; 
    
    @FXML
    TextField usernameField; 
    @FXML 
    TextField passwordField;
    
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this._userRepository = new UserRepository();
        this.sceneManager = SceneManager.getInstance();
    }
    
    public void handleLogin(ActionEvent actionEvent) throws SQLException, IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (_userRepository.isValidUser(username, password)) {
            User user = _userRepository.getUserByUsername(username); 
            _userRepository.setCurrentUser(user);
            sceneManager.switchToTaskListScene();
        }   else {
            // TODO : Show Error Label 
        }
        
            
    }
    
    public void handleCreateAccount(ActionEvent actionEvent) throws IOException {
        sceneManager.switchToCreateAccountScene();
    }
}
