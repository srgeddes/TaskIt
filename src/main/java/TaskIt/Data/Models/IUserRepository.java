package TaskIt.Data.Models;

import java.sql.SQLException;
import java.util.List;

public interface IUserRepository {
    
    public List<User> getAllUsers() throws SQLException; 
    public void addUser(User user) throws SQLException;
    public void updateUser(User user) throws SQLException;
    public void deleteUser(User user) throws SQLException;
    public User getUserById(int id) throws SQLException; 
}
