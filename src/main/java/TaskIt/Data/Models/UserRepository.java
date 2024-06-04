package TaskIt.Data.Models;

import TaskIt.Data.DatabaseDriver;

import java.sql.SQLException;
import java.util.List;

public class UserRepository implements IUserRepository {

    private final DatabaseDriver dbDriver = DatabaseDriver.getInstance();
    private final int CurrentUserId = dbDriver.currentUserId;

    public List<User> getAllUsers() throws SQLException {
        return dbDriver.getAllUsers(); 
    }

    public void addUser(User user) throws SQLException {
        dbDriver.addUser(user.Username, user.Password);
    }

    public void updateUser(User user) throws SQLException {
        dbDriver.updateUser(user);
    }

    public void deleteUser(User user) throws SQLException {
        dbDriver.deleteUser(user);
    }

    public User getUserById(int id) throws SQLException {
        return dbDriver.getUserById(id); 
    }
}
