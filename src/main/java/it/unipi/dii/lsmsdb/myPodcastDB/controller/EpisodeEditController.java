package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Episode;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static java.lang.Math.round;

public class EpisodeEditController {

    @FXML
    private Button cancel;

    @FXML
    private TextField description;

    @FXML
    private TextField duration;

    @FXML
    private Label headerLabel;

    @FXML
    private DatePicker releaseDate;

    @FXML
    private TextField title;

    @FXML
    private Button update;

    private Episode episode;
    private Boolean newEpisode;

    @FXML
    void clickOnCancel(MouseEvent event) {
        Logger.info("Cancel");
    }

    @FXML
    void clickOnUpdate(MouseEvent event) {
        // creation
        if (this.newEpisode) {
            // null controll
            if (this.title.getText() == "" || this.description.getText() == "" || this.releaseDate.getValue() == null || this.duration.getText() == "") {
                Logger.error("Invalid Input");
                return;
            }

            // set the value
            this.episode.setName(this.title.getText());
            this.episode.setDescription(this.description.getText());
            LocalDate localDate = this.releaseDate.getValue();
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            this.episode.setReleaseDate(Date.from(instant));
            this.episode.setTimeMillis(Integer.parseInt(this.duration.getText()) * 60 * 1000);

            Logger.info("New Episode : " + this.episode.toString());
        }

        // update
        else {
            if (!this.title.getText().equals("") && !this.title.getText().equals(this.episode.getName()))
                this.episode.setName(this.title.getText());
            if (!this.description.getText().equals("") && !this.description.getText().equals(this.episode.getDescription()))
                this.episode.setDescription(this.description.getText());
            if (this.releaseDate.getValue() != null) {
                LocalDate localDate = this.releaseDate.getValue();
                Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
                Date releaseDate = Date.from(instant);
                if (this.episode.getReleaseDate() != releaseDate)
                    this.episode.setReleaseDate(releaseDate);
            }
            if (!this.title.getText().equals("")) {
                int timeMillis = Integer.parseInt(this.duration.getText()) * 60 * 1000;
                if (timeMillis != this.episode.getTimeMillis())
                    this.episode.setTimeMillis(timeMillis);
            }

            Logger.info(this.episode.toString());
        }
    }

    @FXML
    void mouseOnCancel(MouseEvent event) {
        this.cancel.setStyle("-fx-border-color: #f44336; -fx-background-color: white; -fx-background-radius: 10; -fx-text-fill: black; -fx-border-radius: 10; -fx-max-height: 20; -fx-cursor: hand;");
    }

    @FXML
    void mouseOnUpdate(MouseEvent event) {
        this.update.setStyle("-fx-border-color: #4CAF50; -fx-background-color: white; -fx-background-radius: 10; -fx-text-fill: black; -fx-border-radius: 10; -fx-max-height: 20; -fx-cursor: hand;");
    }

    @FXML
    void mouseOutCancel(MouseEvent event) {
        this.cancel.setStyle("-fx-border-color: transparent; -fx-background-color: #f44336; -fx-background-radius: 10; -fx-text-fill: white; -fx-border-radius: 10; -fx-max-height: 20; -fx-cursor: default;");
    }

    @FXML
    void mouseOutUpdate(MouseEvent event) {
        this.update.setStyle("-fx-border-color: transparent; -fx-background-color: #4CAF50; -fx-background-radius: 10; -fx-text-fill: white; -fx-border-radius: 10; -fx-max-height: 20; -fx-cursor: default;");
    }

    public void setData(Episode episode, Boolean creation) {
        // set local status
        this.episode = episode;
        this.newEpisode = creation;

        // if are creating a new episode modify title and button
        if (creation) {
            this.headerLabel.setText("Add new episode");
            this.update.setText("Add");
        }

        // else setup the text field
        else {
            this.title.setText(episode.getName());
            this.description.setText(episode.getDescription());
            this.duration.setText("" + round((episode.getTimeMillis() / 100 ) / 60));
        }
    }

    public void initialize() {
        // TODO: rimuovere tutta sta parte
        Episode episode = new Episode("Test", "Description", new Date(), 6000000);
        this.setData(episode, false);
    }
}
