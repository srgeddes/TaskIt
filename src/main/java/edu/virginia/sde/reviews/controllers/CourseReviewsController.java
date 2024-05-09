package edu.virginia.sde.reviews.controllers;

import edu.virginia.sde.reviews.SceneManager;
import edu.virginia.sde.reviews.database.DatabaseDriver;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.ResourceBundle;

public class CourseReviewsController implements Initializable {

    Stage stage;

    @FXML
    Label courseLabel;
    int courseID;
    @FXML
    Label averageRatingLabel;
    @FXML
    TextArea commentsTextArea;

    @FXML
    Button submitReview;

    @FXML
    Label commentErrorLabel;
    @FXML
    Label reviewAlreadyLeftError;
    @FXML
    Label reviewLeftLabel;
    @FXML
    Label reviewDeletedLabel;
    @FXML
    Label noReviewFoundError;


    @FXML
    Slider ratingSlider;
    @FXML
    Label sliderValueLabel;

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

    String currentUser;

    HashMap<String, String[]> reviews = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            fetchReviewsFromDB(courseID);
            setupTableColumns();
            setupSlider();
            setCourseLabel(courseID);
            setAverageRatingLabel(courseID);
            updateSubmitButton();
        });
    }

    private void setupTableColumns() {
        ObservableList<String[]> data = FXCollections.observableArrayList();
        data.addAll(reviews.values());
        reviewsTable.setItems(data);

        commentsColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[0]));
        ratingColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[1]));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        timestampColumn.setCellValueFactory(param -> {
            String timestampStr = param.getValue()[2];
            try {
                LocalDateTime dateTime = LocalDateTime.parse(timestampStr, formatter);
                return new SimpleObjectProperty<>(dateTime);
            } catch (Exception e) {
                return new SimpleObjectProperty<>(LocalDateTime.MIN); // Handle parsing error
            }
        });
        timestampColumn.setSortType(TableColumn.SortType.DESCENDING);
        reviewsTable.getSortOrder().add(timestampColumn);
        reviewsTable.sort();
    }


    public void fetchReviewsFromDB(int courseID) {
        DatabaseDriver databaseDriver = new DatabaseDriver();
        try {
            databaseDriver.connect();
            reviews = databaseDriver.getReviews(courseID);
        } catch (SQLException e) {
            System.out.println("No reviews have been made for this course: " + e);
        } finally {
            try {
                databaseDriver.disconnect();
            } catch (SQLException e) {
                System.out.println("fetchReviewsFromDB, Failed to disconnect from database: " + e);
            }
        }
    }


    public String getAverageRating(int courseID) {
        DatabaseDriver databaseDriver = new DatabaseDriver();
        try {
            databaseDriver.connect();
            double averageRating = databaseDriver.calculateAverageRating(courseID);
            if (Double.isNaN(averageRating)) {
                return "0.00";
            }
            return String.format("%.2f", averageRating);
        } catch (SQLException e) {
            System.out.println("getAverageRating, Unable to get average rating for this course: " + e);
        } finally {
            try {
                databaseDriver.disconnect();
            } catch (SQLException e) {
                System.out.println("getAverageRating, Unable to disconnect from database when getting average rating for Course ID " + courseID);
            }
        }
        return "0.00";
    }

    public void updateSubmitButton() {
        DatabaseDriver databaseDriver = new DatabaseDriver();
        try {
            databaseDriver.connect();
            if (databaseDriver.userAlreadyLeftReview(currentUser, courseID)) {
                submitReview.setText("Edit Review");
            } else {
                submitReview.setText("Submit");
            }
        } catch (SQLException e) {
            System.out.println("updateSubmitButton, Failed to add review: " + e);
        } finally {
            try {
                databaseDriver.disconnect();
            } catch (SQLException e) {
                System.out.println("updateSubmitButton, Failed to disconnect from database: " + e);
            }
        }
    }

    public void addReview(ActionEvent event) throws IOException {
        String comments = commentsTextArea.getText();
        int rating = Integer.parseInt(sliderValueLabel.getText());
        boolean validComment = comments.length() <= 600;

        if (validComment) {
            commentErrorLabel.setVisible(false);
            DatabaseDriver databaseDriver = new DatabaseDriver();
            try {
                databaseDriver.connect();
                if (!(databaseDriver.userAlreadyLeftReview(currentUser, courseID))) {
                    reviewAlreadyLeftError.setVisible(false);
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    databaseDriver.addReview(currentUser, courseID, comments, rating, timestamp.toString());
                    databaseDriver.commit();
                    reviewAlreadyLeftError.setVisible(false);
                    reviewLeftLabel.setVisible(true);
                    commentsTextArea.setText("");
                    fetchReviewsFromDB(courseID);
                    setupTableColumns();
                    setCourseLabel(courseID);
                    setAverageRatingLabel(courseID);
                    updateSubmitButton();
                } else {
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    databaseDriver.editReview(currentUser, courseID, comments, rating, timestamp.toString());
                    databaseDriver.commit();
                    reviewLeftLabel.setVisible(false);
//                    reviewAlreadyLeftError.setVisible(true);
                    commentsTextArea.setText("");
                    fetchReviewsFromDB(courseID);
                    setupTableColumns();
                    setCourseLabel(courseID);
                    setAverageRatingLabel(courseID);
                    updateSubmitButton();
                }
            } catch (SQLException e) {
                System.out.println("addReview, Failed to add review: " + e);
            } finally {
                try {
                    databaseDriver.disconnect();
                } catch (SQLException e) {
                    System.out.println("addReview, Failed to disconnect from database: " + e);
                }
            }
            commentErrorLabel.setVisible(false);

        } else {
            commentErrorLabel.setVisible(true);
        }
    }

    public void deleteUserReview() {
        DatabaseDriver databaseDriver = new DatabaseDriver();
        try {
            databaseDriver.connect();
            boolean reviewDeleted = databaseDriver.removeUserReview(currentUser, courseID);
            databaseDriver.commit();
            fetchReviewsFromDB(courseID);
            setupTableColumns();
            setCourseLabel(courseID);
            setAverageRatingLabel(courseID);
            updateSubmitButton();
            if (reviewDeleted) {
                reviewDeletedLabel.setVisible(true);
                noReviewFoundError.setVisible(false);
            } else {
                reviewDeletedLabel.setVisible(false);
                noReviewFoundError.setVisible(true);
            }

        } catch (SQLException e) {
            System.out.println("deleteUserReview, Failed to disconnect from database: " + e);
        } finally {
            try {
                databaseDriver.disconnect();
            } catch (SQLException e) {
                System.out.println("deleteUserReview, Failed to disconnect from database: " + e);
            }
        }
    }


    public void handleKeyPressed(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (keyEvent.getSource() == commentsTextArea) {
                addReview(new ActionEvent(keyEvent.getSource(), keyEvent.getTarget()));
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

    private void updateLabelPosition() {
        double thumbWidth = ratingSlider.lookup(".thumb").getBoundsInParent().getWidth();
        double trackWidth = ratingSlider.lookup(".track").getBoundsInParent().getWidth();
        double value = ratingSlider.getValue();
        double min = ratingSlider.getMin();
        double max = ratingSlider.getMax();
        double percent = (value - min) / (max - min);
        double thumbPos = percent * (trackWidth - thumbWidth) + thumbWidth / 2;
        sliderValueLabel.setLayoutX(ratingSlider.getLayoutX() + thumbPos - sliderValueLabel.getWidth() / 2);
        sliderValueLabel.setLayoutY(ratingSlider.getLayoutY() - sliderValueLabel.getHeight() - 5);
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCourseLabel(int courseID) {
        DatabaseDriver databaseDriver = new DatabaseDriver();
        try {
            databaseDriver.connect();
            this.courseLabel.setText(databaseDriver.getCourseTitle(courseID));
            this.courseID = courseID;
        } catch (SQLException e) {
            System.out.println("Could not connect to DB");
        } finally {
            try {
                databaseDriver.disconnect();
            } catch (SQLException disconnectEx) {
                System.out.println("Error closing the database connection: " + disconnectEx.getMessage());
            }
        }

    }

    public void setAverageRatingLabel(int courseID) {
        DatabaseDriver databaseDriver = new DatabaseDriver();
        try {
            databaseDriver.connect();
            String averageRating = getAverageRating(courseID);
            if (averageRating.equals("0.00")) {
                this.averageRatingLabel.setText("");
            } else {
                this.averageRatingLabel.setText("Average Rating: " + averageRating);
            }
            this.courseID = courseID;
        } catch (SQLException e) {
            System.out.println("Could not connect to DB");
        } finally {
            try {
                databaseDriver.disconnect();
            } catch (SQLException disconnectEx) {
                System.out.println("Error closing the database connection: " + disconnectEx.getMessage());
            }
        }
    }

    public void setCourse(int course) {
        this.courseID = course;
    }

    public void setupSlider() {
        StringConverter<Number> format = new NumberStringConverter("0");
        sliderValueLabel.textProperty().bind(Bindings.createStringBinding(() ->
                format.toString(ratingSlider.getValue()), ratingSlider.valueProperty()));
        ratingSlider.valueProperty().addListener((obs, oldval, newVal) -> updateLabelPosition());
        Platform.runLater(this::updateLabelPosition);
    }
}
