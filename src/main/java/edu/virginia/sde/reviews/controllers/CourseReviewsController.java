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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.ResourceBundle;

public class CourseReviewsController implements Initializable {

    Stage stage;

    @FXML
    Label courseLabel;
    @FXML
    TextArea commentsTextArea;
    @FXML
    TextField ratingTextField;
    @FXML
    Label ratingErrorLabel;
    @FXML
    Label commentErrorLabel;

    @FXML
    TableView<String[]> reviewsTable;
    @FXML
    TableColumn<String[], String> commentsColumn;
    @FXML
    TableColumn<String[], String> ratingColumn;
    @FXML
    TableColumn<String[], LocalDateTime> timestampColumn;

    @FXML
    TextField usernameSearchTextField;

    // TODO : database
    // key = user id
    // value = [comment, rating(1-5), timestamp]
    // remember that comments are optional. First index needs to be empty string if
    // theres no comments
    HashMap<String, String[]> reviews = new HashMap<>();
    String currentCourse;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        reviews.put("krf9mp", new String[]{"ddddddddddddddddddddddddddddddd", "4", timestamp.toString()});
        reviews.put("Alex", new String[]{"This course was not good", "2", timestamp.toString()});
        reviews.put("Levi", new String[]{"", "1", timestamp.toString()});

        ObservableList<String[]> data = FXCollections.observableArrayList();
        data.addAll(reviews.values());

        commentsColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
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
        timestampColumn.setSortType(TableColumn.SortType.DESCENDING);
        reviewsTable.getSortOrder().add(timestampColumn);
        reviewsTable.sort();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCourseLabel(String course) {
        this.courseLabel.setText(course);
    }

    public void addReview(ActionEvent event) throws IOException {
        String comments = commentsTextArea.getText();
        String rating = ratingTextField.getText();

        boolean validRating = rating.matches("[12345]");
        boolean validComment = comments.length() <= 600;
        if (comments.length() <= 600 && validRating) {
            commentErrorLabel.setVisible(false);
            ratingErrorLabel.setVisible(false);
            System.out.println("Valid Review");
            // TODO : Add review to database
        } else {
            commentErrorLabel.setVisible(!validComment);
            ratingErrorLabel.setVisible(!commentErrorLabel.isVisible());
        }
    }

    public void searchForReview() {
        // TODO : Update table with only the review that the user made showing or show it first or something

    }

    public void handleKeyPressed(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (keyEvent.getSource() == commentsTextArea || keyEvent.getSource() == ratingTextField) {
                // TODO : Handle the search here in the database
                addReview(new ActionEvent(keyEvent.getSource(), keyEvent.getTarget()));
            } else if (keyEvent.getSource() == usernameSearchTextField) {
                searchForReview();
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
