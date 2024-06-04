package TaskIt.Data.Models;

import java.sql.Timestamp;

public class Task {

    private int taskId;
    private Timestamp dueDate;
    private String description;
    private CompletionStatus completionStatus;
    private PriorityLevel priorityLevel;

    public enum CompletionStatus {
        COMPLETE,
        IN_PROGRESS,
        DONE;
    }

    public enum PriorityLevel {
        LOW,
        MODERATE,
        PRESSING,
        EXTREME
    }

    public Task(Timestamp dueDate, String description, PriorityLevel priorityLevel, CompletionStatus completionStatus) {
        this.dueDate = dueDate;
        this.description = description;
        this.priorityLevel = priorityLevel;
        this.completionStatus = completionStatus;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public Timestamp getDueDate() {
        return dueDate;
    }

    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PriorityLevel getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(PriorityLevel priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public CompletionStatus getCompletionStatus() {
        return completionStatus;
    }

    public void setCompletionStatus(CompletionStatus completionStatus) {
        this.completionStatus = completionStatus;
    }
}
