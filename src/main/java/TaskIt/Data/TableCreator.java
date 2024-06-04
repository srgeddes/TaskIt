package TaskIt.Data;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TableCreator {

    private Connection connection;

    public TableCreator(Connection connection) {
        this.connection = connection;
    }

    public void createTables() throws SQLException {
        try (Statement st = connection.createStatement()) {
            st.execute(
                    "CREATE TABLE IF NOT EXISTS Users (" +
                            "UserId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "Username TEXT NOT NULL UNIQUE, " +
                            "Password TEXT NOT NULL)"
            );
            st.execute(
                    "CREATE TABLE IF NOT EXISTS Tasks (" +
                            "TaskId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "Description TEXT NOT NULL, " +
                            "PriorityLevel TEXT NOT NULL, " +
                            "DueDate TEXT NOT NULL, " +
                            "CompletionStatus TEXT NOT NULL, " +
                            "UserId INTEGER, " +
                            "FOREIGN KEY (UserId) REFERENCES Users(UserId))"
            );
        }
    }
}
