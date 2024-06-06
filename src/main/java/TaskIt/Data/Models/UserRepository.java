package TaskIt.Data.Models;

import TaskIt.Data.DatabaseDriver;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository extends Repositories implements IUserRepository {

    private final DatabaseDriver dbDriver = DatabaseDriver.getInstance();

    public List<User> getAllUsers() throws SQLException {
        super.ConnectToDb();
        List<User> users = dbDriver.getAllUsers();
        super.DisconnectFromDb();
        return users;
    }

    public void addUser(User user) throws SQLException {
        super.ConnectToDb();
        dbDriver.addUser(user.Username, user.Password);
        dbDriver.commit();
        super.DisconnectFromDb();
    }

    public void updateUser(User user) throws SQLException {
        super.ConnectToDb();
        dbDriver.updateUser(user);
        dbDriver.commit();
        super.DisconnectFromDb();
    }

    public void deleteUser(User user) throws SQLException {
        super.ConnectToDb();
        dbDriver.deleteUser(user);
        dbDriver.commit();
        super.DisconnectFromDb();
    }

    public User getUserById(int id) throws SQLException {
        super.ConnectToDb();
        User user = dbDriver.getUserById(id);
        super.DisconnectFromDb();
        return user;
    }
    
    public User getUserByUsername(String username) throws SQLException {
        super.ConnectToDb();
        User user = dbDriver.getUserByUsername(username);
        super.DisconnectFromDb();
        return user; 
    }
    
    public boolean isValidUser(String username, String password) throws SQLException {
        super.ConnectToDb();
        boolean rs = dbDriver.isValidUser(username, password);
        super.DisconnectFromDb();
        return rs;
    }
    
    public void setCurrentUser(User user) throws SQLException {
        dbDriver.setCurrentUser(user);
    }
    
    public User getCurrentUser() throws SQLException {
        return dbDriver.getCurrentUser(); 
    }
}
