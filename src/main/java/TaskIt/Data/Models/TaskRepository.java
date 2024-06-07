package TaskIt.Data.Models;

import TaskIt.Data.DatabaseDriver;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository extends Repository implements ITaskRepository {

    private final DatabaseDriver dbDriver = DatabaseDriver.getInstance();

    public List<Task> getAllTasks() throws SQLException {
        List<Task> tasks = new ArrayList<>();
        try {
            super.ConnectToDb();
            tasks = dbDriver.getAllTasks();
        } catch (SQLException e) {
            System.out.println("Unable to get tasks");
        }
        super.DisconnectFromDb();
        return tasks;
    } 

    public void addTask(Task task) throws SQLException {
        super.ConnectToDb();
        dbDriver.addTask(task);
        dbDriver.commit();
        super.DisconnectFromDb();
    }

    public void updateTask(Task task) throws SQLException {
        super.ConnectToDb();
        dbDriver.updateTask(task);
        dbDriver.commit();
        super.DisconnectFromDb();
    }

    public void deleteTask(Task task) throws SQLException {
        super.ConnectToDb();
        dbDriver.deleteTask(task);
        dbDriver.commit();
        super.DisconnectFromDb();
    }

    public Task getTaskById(int id) throws SQLException {
        super.ConnectToDb();
        Task task = dbDriver.getTaskById(id);
        super.DisconnectFromDb();
        return task;
    }

    public boolean doesTaskExist(int id) throws SQLException {
        super.ConnectToDb();
        boolean rs = dbDriver.doesTaskExist(id);
        super.DisconnectFromDb();
        return rs;
    }
    
}
