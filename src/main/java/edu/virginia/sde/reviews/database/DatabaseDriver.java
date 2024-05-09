package edu.virginia.sde.reviews.database;

import java.sql.*;
import java.util.ArrayList;
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

    public ArrayList<ArrayList<String>> getCourses() throws SQLException {
        ArrayList<ArrayList<String>> courses = new ArrayList<>();
        String sql = "SELECT c.CourseTitle, c.CourseDepartment, c.CourseNumber, AVG(r.Rating) as Review " +
                "FROM Courses c " +
                "JOIN reviews r on c.CourseID = r.CourseID ";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ArrayList<String> row = new ArrayList<>();
                    row.add(rs.getString("CourseTitle"));
                    row.add(rs.getString("CourseDepartment"));
                    row.add(rs.getString("CourseNumber"));
                    row.add(rs.getString("Review"));
                    courses.add(row);
                }
            }
        }
        return courses;
    }

    public ArrayList<ArrayList<String>> filterCourses(String query) throws SQLException {
        ArrayList<ArrayList<String>> courses = new ArrayList<>();
        String sql = "SELECT CourseTitle, CourseDepartment, CourseNumber " +
                "FROM Courses WHERE CourseTitle LIKE ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + query + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ArrayList<String> row = new ArrayList<>();
                    row.add(rs.getString("CourseTitle"));
                    row.add(rs.getString("CourseDepartment"));
                    row.add(rs.getString("CourseNumber"));
                    courses.add(row);
                }
            }
        }
        return courses;
    }


    public void addCourse(String title, String department, String catalog_number) throws SQLException {
        String sql = "INSERT INTO Courses (CourseTitle, CourseDepartment, CourseNumber) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setString(2, department);
            ps.setString(3, catalog_number);
            ps.executeUpdate();
        }
    }

    public boolean doesCourseExist(String title, String department, String Number) throws SQLException {
        String sql = "SELECT CourseTitle FROM Courses WHERE CourseTitle = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, title);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void removeCourse(String title) throws SQLException {
        String sql = "DELETE FROM Courses WHERE CourseTitle = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.executeUpdate();
        }
    }


    public String getCourseTitle(int courseID) {
        String sql = "SELECT CourseTitle from Courses WHERE CourseID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, courseID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    return rs.getString("CourseTitle");
                }
            }
        } catch (Exception e) {
            System.out.println("getCourseTitle, Failed to get course title: " + e);
        }
        return "";
    }

    public int getCourseID(String courseTitle) {
        String sql = "SELECT CourseID from Courses WHERE CourseTitle = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, courseTitle);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    return rs.getInt("CourseID");
                }
            }
        } catch (Exception e) {
            System.out.println("getCourseID, Failed to get course id: " + e);
        }
        return -1;
    }


    public HashMap<String, String[]> getReviews(int courseID) throws SQLException {
        HashMap<String, String[]> reviews = new HashMap<>();
        String sql = "SELECT ReviewID, Comments, Rating, Time_Stamp FROM Reviews WHERE CourseID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, courseID);
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

    public void addReview(String username, int courseID, String comments, int rating, String timestamp) throws SQLException {
        String sql = "INSERT INTO Reviews (Username, CourseID, Comments, Rating, Time_Stamp) VALUES (?, ?, ?, ?, ?)";
        // Fix the autoincremet stuff maybe
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setInt(2, courseID);
            ps.setString(3, comments);
            ps.setInt(4, rating);
            ps.setString(5, timestamp);
            ps.executeUpdate();
        }
    }

    public boolean userAlreadyLeftReview(String username, int courseID) throws SQLException {
        String sql = "SELECT * FROM Reviews WHERE Username = ? AND CourseID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setInt(2, courseID);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void removeReview(String username, int courseID) throws SQLException {
        String sql = "DELETE FROM Reviews WHERE Username = ? AND CourseID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setInt(2, courseID);
            ps.executeUpdate();
        }
    }

    public double calculateAverageRating(int courseID) throws SQLException {
        String sql = "SELECT * FROM Reviews WHERE CourseID = ?";
        int totalReviews = 0;
        int totalRating = 0;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, courseID);
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
                    String courseID = rs.getString("CourseID");
                    String comments = rs.getString("Comments");
                    String rating = rs.getString("Rating");
                    String timestamp = rs.getString("Time_Stamp");
                    reviews.put(reviewID, new String[]{courseID, comments, rating, timestamp});
                }
            }
        }
        return reviews;
    }

    public boolean removeUserReview(String username, int courseID) throws SQLException {
        String sql = "DELETE FROM Reviews WHERE Username = ? AND CourseID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setInt(2, courseID);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }


    public static void main(String[] args) {
        DatabaseDriver databaseDriver = new DatabaseDriver();
        try {
            databaseDriver.connect();
            TableCreator tableCreator = new TableCreator(databaseDriver.getConnection());
            tableCreator.createTables();
            databaseDriver.removeCourse("Discrete Math");
            databaseDriver.commit();
        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e);
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
