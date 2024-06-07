package TaskIt.Controllers;

import TaskIt.Data.Models.IUserRepository;
import TaskIt.Data.Models.User;
import TaskIt.Data.Models.UserRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CreateAccountController implements Initializable {

    IUserRepository _userRepository;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this._userRepository = new UserRepository();
    }

    @FXML
    TextField usernameField;
    @FXML
    TextField passwordField;
    @FXML
    TextField confirmPasswordField;
    @FXML
    Label usernameErrorLabel;
    @FXML
    Label passwordErrorLabel;

    public void createAccount(ActionEvent event) throws SQLException {
        String username = usernameField.getText(); 
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        
        // TODO : Check to make sure valid password and not empty 
        
        if (isValidPassword(password, confirmPassword)) {
            User user = new User(0, username, password); 
            _userRepository.addUser(user);
        } else {
            // TODO : Check if the user already exists first
        } 
    }
    
    public boolean isValidPassword(String password, String confirmPassword) {
        return password.equals(confirmPassword) && password.length() >= 6;
    }
    

    
}
