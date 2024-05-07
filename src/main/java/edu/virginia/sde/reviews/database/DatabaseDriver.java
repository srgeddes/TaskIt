package edu.virginia.sde.reviews.database;

import java.sql.*;
import java.util.HashMap;

public class DatabaseDriver {

    private Connection connection;
    private String currentUser;

    public DatabaseDriver() {};

    public Connection getConnection() {
        return connection;
    }

    public void disconnect() throws SQLException {
        connection.close();
    }

    /**
     * Connect to a SQLite Database. This turns out Foreign Key enforcement, and disables auto-commits
     * @throws SQLException
     */
    public void connect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            throw new IllegalStateException("The connection is already opened");
        }
        connection = DriverManager.getConnection("jdbc:sqlite:course_review.sqlite");
        connection.createStatement().execute("PRAGMA foreign_keys = ON");
        connection.setAutoCommit(false);
    }


    public void commit() throws SQLException {
        connection.commit();
    }

    public void rollback() throws SQLException {
        connection.rollback();
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public void addUser(String username, String password) throws SQLException {
        String sql = "INSERT INTO Users (Username, Password) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
        }
    }

    public void removeUser(String username) throws SQLException {
        String sql = "DELETE FROM Users WHERE Username = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.executeUpdate();
        }
    }

    /**
     * @param username
     * @param password
     * @return True if the username and password match a user in the course_review database
     * @throws SQLException
     */
    public boolean isValidUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM Users WHERE Username = ? AND Password = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * @param username
     * @return True if the username matches a username already in the course_review database.
     * Does not check password.
     * @throws SQLException
     */
    public boolean doesUserExist(String username) throws SQLException {
        String sql = "SELECT * FROM Users WHERE Username = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Be aware that Course Subject/Number *are not unique* - For example, there are several different
     * courses that are "CS 4501" with different titles. You should only assume two courses are the same
     * if they have the same subject, number, *and* title.
     */

    public HashMap<String, String> getCourses() throws SQLException {
        HashMap<String, String> coursesMap = new HashMap<>();
        String sql = "SELECT * FROM Courses";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String title = rs.getString("Title");
                    double averageRating = rs.getDouble("Average_rating");
                    coursesMap.put(title, String.format("%.2f", averageRating));
                }
            }
        }
        return coursesMap;
    }

    public void addCourse(String title, String department, String catalog_number) throws SQLException {
        String sql = "INSERT INTO Courses (Title, Average_rating) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, title + " | " + department + " " + catalog_number);
            ps.setDouble(2, 0.0);
            ps.executeUpdate();
        }
    }

    public boolean doesCourseExist(String title) throws SQLException {
        String sql = "SELECT * FROM Courses WHERE Title = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, title);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void removeCourse(String title) throws SQLException {
        String sql = "DELETE FROM Courses WHERE Title = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.executeUpdate();
        }
    }


    public void updateCourseAverageRating(String title) throws SQLException {
        String sql = "UPDATE Courses SET Average_rating = ? WHERE Title = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDouble(1, calculateAverageRating(title));
            ps.setString(2, title);
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating average rating failed, no rows affected.");
            }
        }
    }


    public HashMap<String, String[]> getReviews(String course) throws SQLException {
        HashMap<String, String[]> reviews = new HashMap<>();
        String sql = "SELECT ReviewID, Comments, Rating, Time_Stamp FROM Reviews WHERE CourseTitle = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, course);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String reviewID = String.valueOf(rs.getInt("ReviewID"));
                    String comments = rs.getString("Comments");
                    String rating = rs.getString("Rating");
                    String timestamp = rs.getString("Time_Stamp");
                    reviews.put(reviewID, new String[]{comments, rating, timestamp});
                }
            }
        }
        return reviews;
    }

    public void addReview(String username, String courseTitle, String comments, int rating, String timestamp) throws SQLException {
        String sql = "INSERT INTO Reviews (Username, CourseTitle, Comments, Rating, Time_Stamp) VALUES (?, ?, ?, ?, ?)";
        // Fix the autoincremet stuff maybe
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, courseTitle);
            ps.setString(3, comments);
            ps.setInt(4, rating);
            ps.setString(5, timestamp);
            ps.executeUpdate();
        }
    }

    public boolean userAlreadyLeftReview(String username, String courseTitle) throws SQLException {
        String sql = "SELECT * FROM Reviews WHERE Username = ? AND CourseTitle = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, courseTitle);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void removeReview(String username, String courseTitle) throws SQLException {
        String sql = "DELETE FROM Reviews WHERE Username = ? AND CourseTitle = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, courseTitle);
            ps.executeUpdate();
        }
    }

    public double calculateAverageRating(String courseTitle) throws SQLException {
        String sql = "SELECT * FROM Reviews WHERE CourseTitle = ?";
        int totalReviews = 0;
        int totalRating = 0;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, courseTitle);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    totalRating += rs.getInt("Rating");
                    totalReviews++;
                }
            }
        }
        return (double) totalRating / totalReviews;
    }

    public HashMap<String, String[]> getReviewsForUser(String username) throws SQLException {
        HashMap<String, String[]> reviews = new HashMap<>();
        String sql = "SELECT * FROM Reviews WHERE Username = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String reviewID = String.valueOf(rs.getInt("ReviewID"));
                    String courseTitle = rs.getString("CourseTitle");
                    String comments = rs.getString("Comments");
                    String rating = rs.getString("Rating");
                    String timestamp = rs.getString("Time_Stamp");
                    reviews.put(reviewID, new String[]{courseTitle, comments, rating, timestamp});
                }
            }
        }
        return reviews;
    }


    public static void main(String[] args) {
        DatabaseDriver databaseDriver = new DatabaseDriver();
        try {
            databaseDriver.connect();
            TableCreator tableCreator = new TableCreator(databaseDriver.getConnection());
            tableCreator.createTables();
            databaseDriver.commit();
        } catch (SQLException e) {
            System.out.println("Error creating tables");
            try {
                databaseDriver.rollback();
            } catch (SQLException rollbackEx) {
                System.out.println("Error during rollback: " + rollbackEx.getMessage());
            }
        } finally {
            try {
                databaseDriver.disconnect();
            } catch (SQLException disconnectEx) {
                System.out.println("Error closing the database connection: " + disconnectEx.getMessage());
            }
        }
    }

}
