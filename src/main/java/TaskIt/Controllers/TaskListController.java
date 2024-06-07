package TaskIt.Controllers;

import TaskIt.Data.Models.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;


public class TaskListController implements Initializable {
    
    public List<Task> tasks; 
    
    ITaskRepository _taskRepository;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this._taskRepository = new TaskRepository();
        try {
            tasks = _taskRepository.getAllTasks();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    
    
    
    
    
    
    
    
}
