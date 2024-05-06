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
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS Users (" +
                            "Username TEXT PRIMARY KEY, " +
                            "Password TEXT NOT NULL)"
            );
        }
    }
}
