package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Episode;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

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
        this.releaseDate.setText(this.episode.getReleaseDateAsString());
        this.time.setText("" + this.episode.getTimeMillis());
    }

}