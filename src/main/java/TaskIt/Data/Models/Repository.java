package TaskIt.Data.Models;

import TaskIt.Data.DatabaseDriver;

import java.sql.SQLException;

public class Repository {
    DatabaseDriver dbDriver = DatabaseDriver.getInstance();
    
    public void ConnectToDb() throws SQLException {
        try {
            dbDriver.connect();
        } catch (SQLException e) {
            System.out.println("Unable to connect to database");
        } 
    }
    
    public void DisconnectFromDb() throws SQLException {
        try {
            dbDriver.disconnect();
        } catch (SQLException e) {
            System.out.println("Unable to disconnect from database");
        }
    } 
}
