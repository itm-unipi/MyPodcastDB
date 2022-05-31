package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class PodcastUpdateController {

    @FXML
    private TextField artworkUrl;

    @FXML
    private Button cancel;

    @FXML
    private TextField category;

    @FXML
    private TextField contentAdvisory;

    @FXML
    private TextField country;

    @FXML
    private TextField name;

    @FXML
    private Button update;

    private Podcast podcast;

    @FXML
    private DatePicker releaseDate;

    @FXML
    void clickOnCancel(MouseEvent event) {
        Logger.info("Cancel");
    }

    @FXML
    void clickOnUpdate(MouseEvent event) {
        // update the local podcast
        if (!this.name.getText().equals(this.podcast.getName()) && !this.name.getText().equals(""))
            this.podcast.setName(this.name.getText());
        if (!this.country.getText().equals(this.podcast.getCountry()) && !this.country.getText().equals(""))
            this.podcast.setCountry(this.country.getText());
        if (!this.contentAdvisory.getText().equals(this.podcast.getContentAdvisoryRating()) && !this.contentAdvisory.getText().equals(""))
            this.podcast.setContentAdvisoryRating(this.contentAdvisory.getText());
        if (!this.category.getText().equals(this.podcast.getPrimaryCategory()) && !this.category.getText().equals(""))
            this.podcast.setPrimaryCategory(this.category.getText());
        if (!this.artworkUrl.getText().equals(this.podcast.getArtworkUrl600()) && !this.artworkUrl.getText().equals(""))
            this.podcast.setArtworkUrl600(this.artworkUrl.getText());
        if (this.releaseDate.getValue() != null) {
            LocalDate localDate = this.releaseDate.getValue();
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            Date releaseDate = Date.from(instant);
            if (this.podcast.getReleaseDate() != releaseDate)
                this.podcast.setReleaseDate(releaseDate);
        }

        Logger.info(this.podcast.toString());
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

    public void setData(Podcast podcast) {
        // initialize the local podcast
        this.podcast = podcast;

        // setup text fields
        this.name.setText(podcast.getName());
        this.country.setText(podcast.getCountry());
        this.contentAdvisory.setText(podcast.getContentAdvisoryRating());
        this.category.setText(podcast.getPrimaryCategory());
        this.artworkUrl.setText(podcast.getArtworkUrl600());
    }

    public void initialize() {
        // TODO: da togliere tutta sta funzione
        this.podcast = new Podcast();
        this.podcast.setName("test1");
        this.podcast.setContentAdvisoryRating("test2");
        this.podcast.setPrimaryCategory("test3");
        this.podcast.setCountry("test4");
        this.podcast.setArtworkUrl600("test5");
        this.podcast.setReleaseDate(new Date());

        setData(podcast);
    }
}