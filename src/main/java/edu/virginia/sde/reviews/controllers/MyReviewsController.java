package edu.virginia.sde.reviews.controllers;

import edu.virginia.sde.reviews.SceneManager;
import edu.virginia.sde.reviews.database.DatabaseDriver;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.ResourceBundle;

public class MyReviewsController implements Initializable {
    Stage stage;

    @FXML
    TableView<String[]> reviewsTable;
    @FXML
    TableColumn<String[], String> courseNameColumn;
    @FXML
    TableColumn<String[], String> ratingColumn;
    @FXML
    TableColumn<String[], String> timestampColumn;

    @FXML
    Label usernameLabel;

    @FXML
    TextField courseSearchField;

    HashMap<String, String[]> reviews = new HashMap<>();
    String currentCourse;

    String currentUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            fetchReviewsFromDB(currentUser);
            setupTable();
            setUsernameLabel(currentUser);
        });
        setTableMouseClick();
    }

    public void fetchReviewsFromDB(String username) {
        DatabaseDriver databaseDriver = new DatabaseDriver();
        try {
            databaseDriver.connect();
            reviews = databaseDriver.getReviewsForUser(username);
        } catch (SQLException e) {
            System.out.println("fetchReviewsFromDB, Unable to get reviews from DB: " + e);
        } finally {
            try {
                databaseDriver.disconnect();
            } catch (SQLException e) {
                System.out.println("Unable to disconnect from database: " + e);
            }
        }
    }

    public void setupTable() {
        ObservableList<String[]> data = FXCollections.observableArrayList();
        data.addAll(reviews.values());

        courseNameColumn.setCellValueFactory(param -> new SimpleStringProperty( param.getValue()[1] + " " + param.getValue()[2] + " | " + param.getValue()[0]));
        ratingColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[3]));

        timestampColumn.setCellValueFactory(param -> {
            String timestampStr = param.getValue()[4];
            if (timestampStr != null && !timestampStr.isEmpty()) {
                try {
                    return new SimpleStringProperty(timestampStr);
                } catch (Exception e) {
                    System.out.println("Failed to Parse");
                    return new SimpleStringProperty(""); // Handle parsing error
                }
            } else {
               return new SimpleStringProperty("");
            }
        });

        reviewsTable.setItems(data);
        courseNameColumn.setSortType(TableColumn.SortType.ASCENDING);
        reviewsTable.getSortOrder().add(courseNameColumn);
        reviewsTable.sort();
    }


    public void setTableMouseClick() {
        DatabaseDriver databaseDriver = new DatabaseDriver();
        reviewsTable.setOnMouseClicked(event -> {
            if (!reviewsTable.getSelectionModel().isEmpty()) {
                currentCourse = reviewsTable.getSelectionModel().getSelectedItem()[0];
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                SceneManager sceneManager = new SceneManager(stage);
                try {
                    databaseDriver.connect();
                    sceneManager.switchToCourseReviewsScene(event, databaseDriver.getCourseID(currentCourse), currentUser);
                } catch (Exception e) {
                    System.out.println("Could not set CourseID");
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

    public void switchToCourseSearchScene(ActionEvent event) throws IOException {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        SceneManager sceneManager = new SceneManager(stage);
        sceneManager.switchToCourseSearchScene(event, currentUser);
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public void setUsernameLabel(String label) {
        usernameLabel.setText(currentUser + "'s Reviews");
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
