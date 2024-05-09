package edu.virginia.sde.reviews.controllers;


import edu.virginia.sde.reviews.SceneManager;
import edu.virginia.sde.reviews.database.DatabaseDriver;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;

import javax.xml.crypto.Data;


public class CourseSearchController implements Initializable{

    private Stage stage;

    String currentUser;

    @FXML
    TextField courseDepartment;
    @FXML
    TextField courseNumber;
    @FXML
    TextField courseTitle;
    @FXML
    Label catalogIncorrectError;
    @FXML
    Label titleTooLongError;
    @FXML
    Label titleTooShortError;
    @FXML
    Label departmentTooShortError;
    @FXML
    Label courseAlreadyExistsError;
    @FXML
    Label courseAddedLabel;

    @FXML
    TextField courseSearch;
    @FXML
    TableView<ArrayList<String>> courseTable;
    @FXML
    private TableColumn<ArrayList<String>, String> courseTitleColumn;
    @FXML
    private TableColumn<ArrayList<String>, String> courseDepartmentColumn;
    @FXML
    private TableColumn<ArrayList<String>, String> courseNumberColumn;
    @FXML
    private TableColumn<ArrayList<String>, String> courseReviewColumn;


    ArrayList<ArrayList<String>> courses = new ArrayList<>();

    String currentCourse;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setALlCourses();
        setupTable();
        handleCourseClicked();
    }


    /**
     * Add Course to the Database if it meets these requirements:
     * Department = 2 letters
     * Catalog # = 4 digits
     * Title = all letters and length is between 2 and 50
     */
    public void addCourse(ActionEvent event) throws IOException {
        String department = courseDepartment.getText().toUpperCase().trim();
        String catalog = courseNumber.getText().trim();
        String title = courseTitle.getText().trim();


        boolean isDepartmentValid = department.matches("[a-zA-Z]{2,4}");

        boolean isCatalogValid = catalog.matches("\\d{4}");

        boolean isTitleTooShort = title.length() < 2;
        boolean isTitleTooLong = title.length() > 50;

        if (isDepartmentValid && isCatalogValid && !isTitleTooLong && !isTitleTooShort) {
            titleTooShortError.setVisible(false);
            titleTooLongError.setVisible(false);
            department = department.toUpperCase();

            DatabaseDriver databaseDriver = new DatabaseDriver();
            try {
                databaseDriver.connect();
                if (!databaseDriver.doesCourseExist(title, department, catalog)) {
                    databaseDriver.addCourse(title, department, catalog);
                    databaseDriver.commit();
                    hideAllAddErrorLabels();
                    courseAddedLabel.setVisible(true);
                    hideAllAddErrorLabels();
                    setALlCourses();
                    setupTable();
                } else {
                    hideAllAddErrorLabels();
                    courseAlreadyExistsError.setVisible(true);
                }
            } catch (SQLException e) {
                System.out.println("addCourse, Failed to add course: " + e);
            } finally {
                try {
                    databaseDriver.disconnect();
                } catch (SQLException e) {
                    System.out.println("addCourse, Failed to disconnect from database: " + e);
                }
            }
        } else {
            courseAddedLabel.setVisible(false);
            departmentTooShortError.setVisible(!isDepartmentValid);
            catalogIncorrectError.setVisible(!isCatalogValid);
            if (isTitleTooLong) {
                titleTooLongError.setVisible(true);
            } else {
                titleTooShortError.setVisible(false);
            }
            if (isTitleTooShort) {
                titleTooShortError.setVisible(true);
            } else {
                titleTooShortError.setVisible(false);
            }
        }
    }

    /**
     * Update the Hashmap of courses with the courses from the database
     */
    public void setALlCourses() {
        DatabaseDriver databaseDriver = new DatabaseDriver();
        try {
            databaseDriver.connect();
            courses = databaseDriver.getCourses();
        } catch (SQLException e) {
            System.out.println("setALlCourses, Unable to connect to database: " + e);
        } finally {
            try {
                databaseDriver.disconnect();
            } catch (SQLException e) {
                System.out.println("setALlCourses, Unable to disconnect from database: " + e);
            }
        }
    }

    /**
     * Convert the Hashmap of courses into a viewable form for the tableView.
     */
    @SuppressWarnings("unchecked")
    public void setupTable() {
        ObservableList<ArrayList<String>> courseDetails = FXCollections.observableArrayList();

        courseDetails.addAll(courses);

        // Set the table's items
        courseTable.setItems(courseDetails);

        courseTitleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));  // Course title at index 0
        courseDepartmentColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));  // Department at index 1
        courseNumberColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));  // Course number at index 2
        courseReviewColumn.setCellValueFactory(cellData ->  new SimpleStringProperty(cellData.getValue().get(3)));  // Course rating at index 3

        courseTable.getSortOrder().addAll(courseTitleColumn, courseDepartmentColumn, courseNumberColumn);
        courseTable.sort();
    }

    /**
     * Handle switching to the appropriate review screen for the course that was clicked in the table
     */
    public void handleCourseClicked() {
        DatabaseDriver databaseDriver = new DatabaseDriver();
        courseTable.setOnMouseClicked(event -> {
            if (!courseTable.getSelectionModel().isEmpty()) {
                currentCourse = courseTable.getSelectionModel().getSelectedItem().get(0);
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                SceneManager sceneManager = new SceneManager(stage);
                try {
                    databaseDriver.connect();
                    sceneManager.switchToCourseReviewsScene(event, databaseDriver.getCourseID(courseTable.getSelectionModel().getSelectedItem().get(0)), currentUser);
                } catch (Exception e) {
                    System.out.println("Couldn't get connect to database and get CourseID");
                } finally {
                    try {
                        databaseDriver.disconnect();
                    } catch (SQLException disconnectEx) {
                        System.out.println("Error closing the database connection: " + disconnectEx.getMessage());
                    }
                }
            }
        });
    }

    public void addCourseErrorHide() {
        courseDepartment.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                departmentTooShortError.setVisible(false);
                courseAddedLabel.setVisible(false);
                courseAlreadyExistsError.setVisible(false);
            }
        });
        courseNumber.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                catalogIncorrectError.setVisible(false);
                courseAddedLabel.setVisible(false);
                courseAlreadyExistsError.setVisible(false);
            }
        });
        courseTitle.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                titleTooShortError.setVisible(false);
                titleTooLongError.setVisible(false);
                courseAddedLabel.setVisible(false);
                courseAlreadyExistsError.setVisible(false);
            }
        });

    }

    public void handleKeyPressed(KeyEvent keyEvent) throws IOException, SQLException {
        DatabaseDriver databaseDriver = new DatabaseDriver();
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (keyEvent.getSource() == courseSearch) {
                databaseDriver.connect();
                courses = databaseDriver.filterCourses(courseSearch.getText());
                setupTable();
            } else if (keyEvent.getSource() == courseDepartment || keyEvent.getSource() == courseNumber || keyEvent.getSource() == courseTitle) {
                addCourse(new ActionEvent(keyEvent.getSource(), keyEvent.getTarget()));
            }
        }
    }

    public void hideAllAddErrorLabels() {
        departmentTooShortError.setVisible(false);
        catalogIncorrectError.setVisible(false);
        titleTooShortError.setVisible(false);
        titleTooLongError.setVisible(false);
        courseAlreadyExistsError.setVisible(false);
    }

    public void switchToLoginScene(ActionEvent event) throws IOException {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        SceneManager sceneManager = new SceneManager(stage);
        sceneManager.switchToLoginScene(event);
    }

    public void switchToMyReviewsScene(ActionEvent event) throws IOException {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        SceneManager sceneManager = new SceneManager(stage);
        sceneManager.switchToMyReviewsScene(event, currentUser);
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
