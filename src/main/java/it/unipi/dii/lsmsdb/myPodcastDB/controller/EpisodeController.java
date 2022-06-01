package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Episode;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import static java.lang.Math.round;

public class EpisodeController {

    @FXML
    private Button deleteEpisode;

    @FXML
    private Text description;

    @FXML
    private Label releaseDate;

    @FXML
    private Label time;

    @FXML
    private Label title;

    private Episode episode;

    @FXML
    private Button updateEpisode;

    @FXML
    void clickOnDelete(MouseEvent event) {
        Logger.info("Delete episode");
    }

    @FXML
    void clickOnUpdate(MouseEvent event) {
        Logger.info("Update episode");
    }

    public void setData(Episode episode) {
        this.episode = episode;

        this.title.setText(episode.getName());
        this.description.setText(episode.getDescription());
        String[] date = this.episode.getReleaseDate().toString().split(" ");
        this.releaseDate.setText(date[2] + " " + date[1]);
        this.time.setText(round(this.episode.getTimeMillis() / (1000 * 60)) + " min");
    }
}