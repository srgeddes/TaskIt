package TaskIt.Data;

import TaskIt.Data.Models.Task;
import TaskIt.Data.Models.User;
import javafx.beans.property.StringProperty;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseDriver {
    private static DatabaseDriver instance;

    private Connection connection;

    public int currentUserId;

    private DatabaseDriver() {
    }

    public static synchronized DatabaseDriver getInstance() {
        if (instance == null) {
            instance = new DatabaseDriver();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void disconnect() throws SQLException {
        connection.close();
    }

    public void connect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            throw new IllegalArgumentException("The connection is already opened");
        }
        connection = DriverManager.getConnection("jdbc:sqlite:TaskItDb.sqlite");
        connection.createStatement().execute("PRAGMA foreign_keys = ON");
        connection.setAutoCommit(false);
    }

    public void commit() throws SQLException {
        connection.commit();
    }

    public void rollback() throws SQLException {
        connection.rollback();
    }

    public void setCurrentUser(int userId) throws SQLException {
        this.currentUserId = 1; 
    }

    public void addTask(Task task) throws SQLException {
        String sql = "INSERT INTO Tasks (UserId, Description, PriorityLevel, DueDate, CompletionStatus) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, currentUserId);
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getPriorityLevel().toString());
            ps.setTimestamp(4, task.getDueDate());
            ps.setString(5, task.getCompletionStatus().toString());
            ps.executeUpdate();
        }
    }

    public List<Task> getAllTasks() throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM Tasks WHERE UserId = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, this.currentUserId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int taskId = rs.getInt("TaskId");
                    String description = rs.getString("Description");
                    Task.PriorityLevel priority = Task.PriorityLevel.valueOf(rs.getString("PriorityLevel"));
                    Timestamp dueDate = rs.getTimestamp("DueDate");
                    Task.CompletionStatus status = Task.CompletionStatus.valueOf(rs.getString("CompletionStatus"));

                    Task task = new Task(dueDate, description, priority, status);
                    task.setTaskId(taskId);
                    tasks.add(task);
                }
            }
        }
        return tasks;
    }


    public void updateTask(Task task) throws SQLException {
        String sql = "UPDATE Tasks SET Description = ?, PriorityLevel = ?, DueDate = ?, CompletionStatus = ? WHERE TaskId = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, task.getDescription());
            ps.setString(2, task.getPriorityLevel().toString());
            ps.setString(3, task.getDueDate().toString());
            ps.setString(4, task.getCompletionStatus().toString());
            ps.setInt(5, task.getTaskId());
            ps.executeUpdate();
        }
    }

    public void deleteTask(Task task) throws SQLException {
        String sql = "DELETE FROM Tasks WHERE TaskId = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, task.getTaskId());
            ps.executeUpdate();
        }
    }

    public Task getTaskById(int taskId) throws SQLException {
        String sql = "SELECT * FROM Tasks WHERE TaskId = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String description = rs.getString("TaskDescription");
                    Task.PriorityLevel priority = Task.PriorityLevel.valueOf(rs.getString("PriorityLevel"));
                    Timestamp dueDate = rs.getTimestamp("TaskDueDate");
                    Task.CompletionStatus status = Task.CompletionStatus.valueOf(rs.getString("CompletionStatus"));
                    return new Task(dueDate, description, priority, status);
                } else {
                    return null; // Task not found
                }
            }
        }
    }
    
    public boolean doesTaskExist(int taskId) throws SQLException {
        String sql = "SELECT * FROM Tasks WHERE TaskId = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, taskId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String description = rs.getString("Description");
                    Task.PriorityLevel priority = Task.PriorityLevel.valueOf(rs.getString("PriorityLevel"));
                    Timestamp dueDate = rs.getTimestamp("TaskDueDate");
                    Task.CompletionStatus status = Task.CompletionStatus.valueOf(rs.getString("CompletionStatus"));
                    return taskId == rs.getInt("TaskId");
                }
            }
        }
        return false; 
    }
    
    public void addUser(String username, String password) throws SQLException {
        String sql = "INSERT INTO Users (Username, Password) VALUES (?, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
        }
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int userId = rs.getInt("UserId");
                    String username = rs.getString("Username");
                    String password = rs.getString("Password");
                    User user = new User(userId, username, password); 
                    users.add(user);
                }
            }

        } 
        return users;  
    }
    
    public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO Users (Username, Password) VALUES (?, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.executeUpdate();
        } 
    }
    
    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE Users SET Username = ?, Password = ? WHERE UserId = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
        }
    }

    public void deleteUser(User user) throws SQLException {
        String sql = "DELETE FROM Users WHERE UserId = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, user.getId());
            ps.executeUpdate();
        }
    }

    public User getUserById(int id) throws SQLException {
        String sql = "SELECT * FROM Users WHERE UserId = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String username = rs.getString("Username");
                    String password = rs.getString("Password");
                    return new User(id, username, password);
                }
            }
        }
        return null; 
    }

    public static void main(String[] args) {
        DatabaseDriver dbDriver = new DatabaseDriver();
        try {
            dbDriver.connect();
            TableCreator tableCreator = new TableCreator(dbDriver.getConnection());
            tableCreator.createTables();
            dbDriver.commit();
        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e);
            try {
                dbDriver.rollback();
            } catch (SQLException rollbackEx) {
                System.out.println("Error during rollback: " + rollbackEx.getMessage());
            }
        } finally {
            try {
                dbDriver.disconnect();
            } catch (SQLException disconnectEx) {
                System.out.println("Error closing the database connection: " + disconnectEx.getMessage());
            }
        }
    }
}
