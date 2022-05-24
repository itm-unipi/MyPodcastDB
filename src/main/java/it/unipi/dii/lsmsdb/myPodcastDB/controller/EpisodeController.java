package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Episode;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.Date;

import static java.lang.Math.floor;
import static java.lang.Math.round;

public class EpisodeController {

    @FXML
    private Text description;

    @FXML
    private Label releaseDate;

    @FXML
    private Label time;

    @FXML
    private Label title;

    private Episode episode;

    public void setData(Episode episode) {
        this.episode = episode;

        this.title.setText(episode.getName());
        this.description.setText(episode.getDescription());
        Date date = this.episode.getReleaseDate();
        this.releaseDate.setText(date.getDay() + " " + getMonth(date.getMonth()));
        this.time.setText(round(this.episode.getTimeMillis() / (1000 * 60)) + " min");
    }

    private String getMonth(int value) {
        switch (value) {
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";
            default:
                return "";
        }
    }

}