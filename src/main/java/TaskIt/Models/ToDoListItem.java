package TaskIt.Models;

import java.sql.Timestamp;
import java.time.DateTimeException;

public class ToDoListItem {
    
    public Timestamp DueDate; 
    public String Description;
    int PriorityLevel;
    
    public ToDoListItem(Timestamp DueDate, String Description, int PriorityLevel) {
        this.DueDate = DueDate;
        this.Description = Description;
        this.PriorityLevel = PriorityLevel;
    }
}
