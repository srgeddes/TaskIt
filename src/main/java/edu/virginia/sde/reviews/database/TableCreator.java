package edu.virginia.sde.reviews.database;

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
                            "UserID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "Username TEXT NOT NULL UNIQUE, " +
                            "Password TEXT NOT NULL)"
            );
            st.execute(
                    "CREATE TABLE IF NOT EXISTS Courses (" +
                            "CourseID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "CourseTitle VARCHAR(255) NOT NULL," +
                            "CourseDepartment VARCHAR(50)," +
                            "CourseNumber VARCHAR(50) NOT NULL)"
            );
            st.execute(
                    "CREATE TABLE IF NOT EXISTS Reviews (" +
                            "ReviewID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "CourseID INTEGER, " +
                            "UserID INTEGER, " +
                            "Comments TEXT, " +
                            "Rating INTEGER NOT NULL, " +
                            "Time_Stamp TEXT NOT NULL, " +
                            "FOREIGN KEY (UserID) REFERENCES Users(UserID)" +
                            "FOREIGN KEY (CourseID) REFERENCES Courses(CourseID))"
            );
        }
    }
}
