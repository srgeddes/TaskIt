package edu.virginia.sde.reviews.controllers;


import edu.virginia.sde.reviews.SceneManager;
import edu.virginia.sde.reviews.database.DatabaseDriver;
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
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;


public class CourseSearchController implements Initializable{

    private Stage stage;

    String currentUser;

    @FXML
    TextField courseDepartment;
    @FXML
    TextField courseCatalog;
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
    TableView<String> courseTable;
    @FXML
    TableColumn<String, String> courseNameColumn;
    @FXML
    TableColumn<String, String> courseReviewColumn;


    HashMap<String, String> courses = new HashMap<>();

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
        String catalog = courseCatalog.getText().trim();
        String title = courseTitle.getText().trim();

        String courseTitle = title + " | " + department + " " + catalog;

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
                if (!databaseDriver.doesCourseExist(courseTitle)) {
                    courseAlreadyExistsError.setVisible(false);
                    databaseDriver.addCourse(title, department, catalog);
                    databaseDriver.commit();
                    courseAddedLabel.setVisible(true);
                    setALlCourses();
                    setupTable();
                } else {
                    courseAlreadyExistsError.setVisible(true);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to connect to database");
            } finally {
                try {
                    databaseDriver.disconnect();
                } catch (SQLException e) {
                    System.out.println("Failed to disconnect from database");
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
            HashMap<String, String> coursesFromDB = databaseDriver.getCourses();
            courses.putAll(coursesFromDB);
        } catch (SQLException e) {
            System.out.println("Unable to connect to database");
        } finally {
            try {
                databaseDriver.disconnect();
            } catch (SQLException e) {
                System.out.println("Unable to disconnect from database");
            }
        }
    }

    /**
     * Convert the Hashmap of courses into a viewable form for the tableView.
     */
    public void setupTable() {
        ObservableList<String> courseAndReviews = FXCollections.observableArrayList();
        courses.forEach((course, review) -> courseAndReviews.add(course + " - Review: " + review));

        courseTable.setItems(courseAndReviews);

        courseNameColumn.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().split(" - Review: ")[0]));
        courseReviewColumn.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().split(" - Review: ")[1]));

        courseReviewColumn.setSortType(TableColumn.SortType.DESCENDING);
        courseTable.getSortOrder().add(courseReviewColumn);
        courseTable.sort();
    }

    /**
     * Handle switching to the appropriate review screen for the course that was clicked in the table
     */
    public void handleCourseClicked() {
        courseTable.setOnMouseClicked(event -> {
            if (!courseTable.getSelectionModel().isEmpty()) {
                currentCourse = courseTable.getSelectionModel().getSelectedItem();
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                SceneManager sceneManager = new SceneManager(stage);
                try {
                    sceneManager.switchToCourseReviewsScene(event, courseTable.getSelectionModel().getSelectedItem().split("- Review: ")[0], currentUser);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void addCourseErrorHide() {
        courseDepartment.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                departmentTooShortError.setVisible(false);
            }
        });
        courseCatalog.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                catalogIncorrectError.setVisible(false);
            }
        });
        courseTitle.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                titleTooShortError.setVisible(false);
                titleTooLongError.setVisible(false);
            }
        });

    }

    public void handleKeyPressed(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (keyEvent.getSource() == courseSearch) {
                // TODO : Handle the search here in the database
            } else if (keyEvent.getSource() == courseDepartment || keyEvent.getSource() == courseCatalog || keyEvent.getSource() == courseTitle) {
                addCourse(new ActionEvent(keyEvent.getSource(), keyEvent.getTarget()));
            }
        }
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
