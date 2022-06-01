package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Episode;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

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

    private BorderPane mainPage;

    @FXML
    private Button updateEpisode;

    @FXML
    void clickOnDelete(MouseEvent event) {
        Logger.info("Delete episode");
    }

    @FXML
    void clickOnUpdate(MouseEvent event) throws IOException {
        Logger.info("Update episode");

        BoxBlur blur = new BoxBlur(3, 3 , 3);
        this.mainPage.setEffect(blur);

        // loading the fxml file of the popup dialog
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("EpisodeEdit.fxml"));

        // creating dialog Pane
        DialogPane episodeEditDialogPane = fxmlLoader.load();
        EpisodeEditController editController = fxmlLoader.getController();

        // create a copy of episode
        Episode modifiedEpisode = new Episode();
        modifiedEpisode.setName(this.episode.getName());
        modifiedEpisode.setDescription(this.episode.getDescription());
        modifiedEpisode.setReleaseDate(this.episode.getReleaseDate());
        modifiedEpisode.setTimeMillis(this.episode.getTimeMillis());

        // pass episode's data to dialog pane
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(this.mainPage.getScene().getWindow());
        dialog.setDialogPane(episodeEditDialogPane);
        dialog.setTitle("Update Podcast");
        editController.setData(modifiedEpisode, false);

        Stage stage = (Stage)dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ImageCache.getImageFromLocalPath("/img/logo.png"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);

        dialog.showAndWait();

        // check if modified
        if (!modifiedEpisode.getName().equals(this.episode.getName()) || !modifiedEpisode.getDescription().equals(this.episode.getDescription()) || !modifiedEpisode.getReleaseDateAsString().equals(this.episode.getReleaseDateAsString()) /*|| modifiedEpisode.getTimeMillis() != this.episode.getTimeMillis()*/) {
            // update page
            Logger.info("Modified episode : " + this.episode.toString());
            setData(modifiedEpisode, this.mainPage);
        }

        this.mainPage.setEffect(null);
    }

    public void setData(Episode episode, BorderPane mainPage) {
        this.episode = episode;
        this.mainPage = mainPage;

        this.title.setText(episode.getName());
        this.description.setText(episode.getDescription());
        String[] date = this.episode.getReleaseDate().toString().split(" ");
        this.releaseDate.setText(date[2] + " " + date[1]);
        this.time.setText(round(this.episode.getTimeMillis() / (1000 * 60)) + " min");
    }
}