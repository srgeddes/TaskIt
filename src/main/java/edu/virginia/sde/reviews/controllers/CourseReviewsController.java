package edu.virginia.sde.reviews.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CourseReviewsController {

    Stage stage;

    @FXML
    Label courseLabel;


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCourseLabel(String course) {
        this.courseLabel.setText(course);
    }

    public void showStage(ActionEvent event) {

    }

}
