package TaskIt.Data.Models;

import java.sql.SQLException;
import java.util.List;

public interface ITaskRepository {
    
    List<Task> getAllTasks() throws SQLException;
    void addTask(Task task) throws SQLException;
    void updateTask(Task task) throws SQLException;
    void deleteTask(Task task) throws SQLException;
    Task getTaskById(int id) throws SQLException;
    
}
