package TaskIt.Controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.Calendar;

public class Controller {
    public void bindToTime(Label clockLabel) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), event -> {
                    Calendar time = Calendar.getInstance();
                    String hourString = String.format("%02d", time.get(Calendar.HOUR) == 0 ? 12 : time.get(Calendar.HOUR));
                    String minuteString = String.format("%02d", time.get(Calendar.MINUTE));
                    String secondString = String.format("%02d", time.get(Calendar.SECOND));
                    String ampmString = time.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
                    clockLabel.setText(hourString + ":" + minuteString);
                }),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    
}
