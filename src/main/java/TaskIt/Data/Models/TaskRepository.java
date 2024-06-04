package TaskIt.Data.Models;

import TaskIt.Data.DatabaseDriver;

import java.sql.SQLException;
import java.util.List;

public class TaskRepository implements ITaskRepository {

    private final DatabaseDriver dbDriver = DatabaseDriver.getInstance();
    private final int CurrentUserId = dbDriver.currentUserId; 

    public List<Task> getAllTasks() throws SQLException {
        try {
            dbDriver.connect();
        } catch (SQLException e) {
            System.out.println("Error connecting to the database");
        } finally {
            dbDriver.disconnect();
        }
        return dbDriver.getAllTasks();
    }

    public void addTask(Task task) throws SQLException {
        try {
            dbDriver.connect();
        } catch (SQLException e) {
            System.out.println("Error connecting to the database");
        } finally {
            dbDriver.disconnect();
        } 
        dbDriver.addTask(task);
    }

    public void updateTask(Task task) throws SQLException {
        dbDriver.updateTask(task);
    }

    public void deleteTask(Task task) throws SQLException {
        dbDriver.deleteTask(task);
    }

    public Task getTaskById(int id) throws SQLException {
        return dbDriver.getTaskById(id); 
    }

    public boolean doesTaskExist(int id) throws SQLException {
        return dbDriver.doesTaskExist(id); 
    }

    public void addUser(String username, String password) throws SQLException {
        dbDriver.connect();
        dbDriver.addUser(username, password);
        dbDriver.commit();
        dbDriver.disconnect();
    }

    private void openConnection() throws SQLException {
        try {
            dbDriver.connect();
        } catch (SQLException e) {
            System.out.println("Error connecting to the database");
        } 
    }

    private void closeConnection() throws SQLException {
        try {
            dbDriver.disconnect();
        } catch (SQLException e) {
            System.out.println("Error connecting to the database");
        } 
    }
}
