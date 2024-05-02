package edu.virginia.sde.reviews.controllers;

import edu.virginia.sde.reviews.SceneManager;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        reviews.put("krf9mp", new String[]{"Introduction to Programming | CS 1110", "4", timestamp.toString()});
        reviews.put("Alex", new String[]{"Discrete Math | CS 2120", "2", timestamp.toString()});
        reviews.put("Levi", new String[]{"Computer Systems and Organizations | CS 2130", "1", timestamp.toString()});

        ObservableList<String[]> data = FXCollections.observableArrayList();
        data.addAll(reviews.values());

        courseNameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> param) {
                return new SimpleStringProperty(param.getValue()[0]);
            }
        });

        ratingColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> param) {
                return new SimpleStringProperty(param.getValue()[1]);
            }
        });

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        timestampColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], LocalDateTime>, ObservableValue<LocalDateTime>>() {
            @Override
            public ObservableValue<LocalDateTime> call(TableColumn.CellDataFeatures<String[], LocalDateTime> param) {
                String timestampStr = param.getValue()[2];
                try {
                    LocalDateTime dateTime = LocalDateTime.parse(timestampStr, formatter);
                    return new SimpleObjectProperty<>(dateTime);
                } catch (Exception e) {
                    return new SimpleObjectProperty<>(LocalDateTime.MIN); // Handle parsing error
                }
            }
        });

        reviewsTable.setItems(data);
        courseNameColumn.setSortType(TableColumn.SortType.ASCENDING);
        reviewsTable.getSortOrder().add(courseNameColumn);
        reviewsTable.sort();

        reviewsTable.setOnMouseClicked(event -> {
            // TODO :
            if (!reviewsTable.getSelectionModel().isEmpty()) {
                // TODO : Open Course Review Screen for this particular course
                currentCourse = reviewsTable.getSelectionModel().getSelectedItem()[0];
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                SceneManager sceneManager = new SceneManager(stage);
                try {
                    sceneManager.switchToCourseReviewsScene(event, currentCourse);
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
        sceneManager.switchToMyReviewsScene(event);
    }

    public void switchToCourseSearchScene(ActionEvent event) throws IOException {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        SceneManager sceneManager = new SceneManager(stage);
        sceneManager.switchToCourseSearchScene(event);
    }
}
