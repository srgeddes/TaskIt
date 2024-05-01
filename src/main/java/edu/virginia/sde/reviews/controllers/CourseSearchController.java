package edu.virginia.sde.reviews.controllers;


import edu.virginia.sde.reviews.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;


public class CourseSearchController implements Initializable{

    private Stage stage;
    private Scene scene;


    // add course
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
    TableView<String> courseTable;
    @FXML
    TableColumn<String, String> courseNameColumn;
    @FXML
    TableColumn<String, String> courseReviewColumn;

    @FXML
    Label courseLabel;

    @FXML
    ImageView userImagev;

    @FXML
    MenuItem reviewsMenuItem;
    @FXML
    MenuItem logoutMenuItem;

    // List of Courses in the Database as Strings
    // TODO : Create and Array of courses like this from the Database
//    String[] courses = {"Introduction to Programming | CS 1110", "Data Structures and Algorithms | CS 2100", "Discrete Math | CS 2120"};
    HashMap<String, String> courses = new HashMap<>();

    String currentCourse;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        Image userIcon = new Image("src/main/resources/edu/virginia/sde/reviews/CourseSearchStyling/thing.jpeg");
//        userImagev.setImage(userIcon);

        // TODO : Replace this with the data from the database
        courses.put("Introduction to Programming | CS 1110", "4.3");
        courses.put("Data Structures and Algorithms | CS 2100", "5.0");
        courses.put("Discrete Math | CS 2120", "1.0");

        ObservableList<String> courseAndReviews = FXCollections.observableArrayList();
        courses.forEach((course, review) -> courseAndReviews.add(course + " - Review: " + review));

        courseTable.setItems(courseAndReviews);

        courseNameColumn.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().split(" - Review: ")[0]));
        courseReviewColumn.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().split(" - Review: ")[1]));

        courseTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                String[] parts = newValue.split(" - Review: ");
                courseLabel.setText(parts[0] + " has a review of " + parts[1]);
            }
        });
    }

    public void switchToLoginScene(ActionEvent event) throws IOException {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        SceneManager sceneManager = new SceneManager(stage);
        sceneManager.switchToLoginScene(event);
    }

    public void switchToMyReviewsScene(ActionEvent event) throws IOException {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        SceneManager sceneManager = new SceneManager(stage);
        sceneManager.switchToMyReviewsScene(event);
    }

    public void addCourse(ActionEvent event) throws IOException {
        String department = courseDepartment.getText().trim();
        String catalog = courseCatalog.getText().trim();
        String title = courseTitle.getText().trim();

        boolean isDepartmentValid = department.matches("[a-zA-Z]{2,4}");

        boolean isCatalogValid = catalog.matches("\\d{4}");

        boolean isTitleTooShort = title.length() < 2;
        boolean isTitleTooLong = title.length() > 50;

        if (isDepartmentValid && isCatalogValid && !isTitleTooLong && !isTitleTooShort) {
            department = department.toUpperCase();
            System.out.println("department added");
            // TODO : Add course to Database
        } else {
            if (!isDepartmentValid) {
                departmentTooShortError.setVisible(true);
            }
            if (!isCatalogValid) {
                catalogIncorrectError.setVisible(true);
            }
            if (isTitleTooLong) {
                titleTooShortError.setVisible(true);
            } else if (isTitleTooShort) {
                titleTooLongError.setVisible(true);
            }
        }
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

}
