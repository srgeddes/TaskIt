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
    TableColumn<String[], LocalDateTime> timestampColumn;

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
            System.out.println("Unable to get reviews from DB");
        } finally {
            try {
                databaseDriver.disconnect();
            } catch (SQLException e) {
                System.out.println("Unable to disconnect from database");
            }
        }
    }

    public void setupTable() {
        ObservableList<String[]> data = FXCollections.observableArrayList();
        data.addAll(reviews.values());

        courseNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[0]));
        ratingColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[2]));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        timestampColumn.setCellValueFactory(param -> {
            String timestampStr = param.getValue()[3];
            try {
                LocalDateTime dateTime = LocalDateTime.parse(timestampStr, formatter);
                return new SimpleObjectProperty<>(dateTime);
            } catch (Exception e) {
                return new SimpleObjectProperty<>(LocalDateTime.MIN);
            }
        });

        reviewsTable.setItems(data);
        courseNameColumn.setSortType(TableColumn.SortType.ASCENDING);
        reviewsTable.getSortOrder().add(courseNameColumn);
        reviewsTable.sort();
    }

    public void setTableMouseClick() {
        reviewsTable.setOnMouseClicked(event -> {
            if (!reviewsTable.getSelectionModel().isEmpty()) {
                currentCourse = reviewsTable.getSelectionModel().getSelectedItem()[0];
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                SceneManager sceneManager = new SceneManager(stage);
                try {
                    sceneManager.switchToCourseReviewsScene(event, currentCourse, currentUser);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void handleKeyPressed(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (keyEvent.getSource().equals(courseSearchField)) {
                // TODO : Update table with review they search for first or something like that
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

    public void switchToCourseSearchScene(ActionEvent event) throws IOException {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        SceneManager sceneManager = new SceneManager(stage);
        sceneManager.switchToCourseSearchScene(event, currentUser);
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public void setUsernameLabel(String label) {
        usernameLabel.setText(currentUser + " Reviews");
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
