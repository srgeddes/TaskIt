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
                            "Username TEXT PRIMARY KEY, " +
                            "Password TEXT NOT NULL)"
            );
            st.execute(
                    "CREATE TABLE IF NOT EXISTS Courses (" +
                            "Title TEXT PRIMARY KEY, " +
                            "Department TEXT NOT NULL, " +
                            "Catalog_Number TEXT NOT NULL, " +
                            "Average_rating DECIMAL(10, 2) NOT NULL)"
            );
            st.execute(
                    "CREATE TABLE IF NOT EXISTS Reviews (" +
                            "ReviewID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "Username TEXT NOT NULL, " +
                            "CourseTitle TEXT NOT NULL, " +
                            "Comments TEXT, " +
                            "Rating INTEGER NOT NULL, " +
                            "TimeStamp TEXT NOT NULL, " +
                            "FOREIGN KEY (Username) REFERENCES Users(Username))"
            );
        }
    }




}
