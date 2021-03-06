package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Episode;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.service.PodcastPageService;
import it.unipi.dii.lsmsdb.myPodcastDB.cache.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.view.DialogManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import static java.lang.Math.round;

public class EpisodeController {
    @FXML
    private HBox buttonWrapper;

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

    @FXML
    private Tooltip tootipTitle;

    @FXML
    private Button updateEpisode;

    private PodcastPageService service;
    private Podcast podcast;

    private Episode episode;

    private BorderPane mainPage;
    private PodcastPageController podcastPageController;

    @FXML
    void clickOnDelete(MouseEvent event) throws IOException {
        // create the blur
        BoxBlur blur = new BoxBlur(3, 3 , 3);
        this.mainPage.setEffect(blur);

        // button handling
        boolean confirm = DialogManager.getInstance().createConfirmationAlert(this.mainPage, "Do you really want to delete this episode?");
        if (confirm) {
            int result = this.service.deleteEpisode(this.podcast, this.episode);

            // if delete is succesfull remove episode from grid
            if (result == 0) {
                this.podcastPageController.updatePodcastPage();
            } else {
                DialogManager.getInstance().createErrorAlert(this.mainPage, "Something goes wrong!");
            }
        }

        this.mainPage.setEffect(null);
    }

    @FXML
    void clickOnUpdate(MouseEvent event) throws IOException {
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
        editController.setData(modifiedEpisode, false, this.mainPage);

        Stage stage = (Stage)dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ImageCache.getImageFromLocalPath("/img/logo.png"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);

        dialog.showAndWait();

        // check if modified
        if (!modifiedEpisode.getName().equals(this.episode.getName()) || !modifiedEpisode.getDescription().equals(this.episode.getDescription()) || !modifiedEpisode.getReleaseDate().equals(this.episode.getReleaseDate()) || modifiedEpisode.getTimeMillis() != this.episode.getTimeMillis()) {
            // delete old version
            int resDel = this.service.deleteEpisode(this.podcast, this.episode);

            // if is succesfull update the page
            if (resDel == 0) {
                // add new version
                int resAdd = this.service.addEpisode(this.podcast, modifiedEpisode);

                if (resAdd == 0) {
                    // update local podcast
                    for (Episode ep : this.podcast.getEpisodes()) {
                        // if is the modified episode update it
                        if (ep.getName().equals(this.episode.getName())) {
                            if (!modifiedEpisode.getName().equals(this.episode.getName()))
                                ep.setName(modifiedEpisode.getName());
                            if (!modifiedEpisode.getDescription().equals(this.episode.getDescription()))
                                ep.setDescription(modifiedEpisode.getDescription());
                            if (!modifiedEpisode.getReleaseDate().equals(this.episode.getReleaseDate()))
                                ep.setReleaseDate(modifiedEpisode.getReleaseDate());
                            if (modifiedEpisode.getTimeMillis() != this.episode.getTimeMillis())
                                ep.setTimeMillis(modifiedEpisode.getTimeMillis());

                            break;
                        }
                    }

                    // update page
                    setData(modifiedEpisode, this.podcast, this.mainPage, this.service, this.podcastPageController);
                } else {
                    DialogManager.getInstance().createErrorAlert(this.mainPage, "Failed to update episode");
                }
            } else {
                DialogManager.getInstance().createErrorAlert(this.mainPage, "Failed to update episode");
            }
        }

        this.mainPage.setEffect(null);
    }

    public void setData(Episode episode, Podcast podcast, BorderPane mainPage, PodcastPageService service, PodcastPageController podcastPageController) {
        this.episode = episode;
        this.mainPage = mainPage;
        this.podcast = podcast;
        this.service = service;
        this.podcastPageController = podcastPageController;

        this.title.setText(episode.getName());
        this.tootipTitle.setText(episode.getName());
        this.description.setText(episode.getDescription());
        String[] date = this.episode.getReleaseDate().toString().split(" ");
        this.releaseDate.setText(date[2] + " " + date[1]);
        this.time.setText(round(this.episode.getTimeMillis() / (1000 * 60)) + " min");

        // actor recognition
        String sessionType = MyPodcastDB.getInstance().getSessionType();
        if (sessionType.equals("User") || (sessionType.equals("Author") && !((Author)MyPodcastDB.getInstance().getSessionActor()).getName().equals(this.podcast.getAuthorName())) || sessionType.equals("Unregistered")) {
            // visitator
            this.updateEpisode.setVisible(false);
            this.deleteEpisode.setVisible(false);
            this.buttonWrapper.setStyle("-fx-min-width: 0; -fx-pref-width: 0; -fx-max-width: 0;");
            this.title.setStyle("-fx-min-width: 575; -fx-pref-width: 575; -fx-max-width: 575;");
        } else if (sessionType.equals("Admin")) {
            this.updateEpisode.setVisible(false);
            this.updateEpisode.setStyle("-fx-min-width: 0; -fx-pref-width: 0; -fx-max-width: 0;");
            this.buttonWrapper.setStyle("-fx-min-width: 40; -fx-pref-width: 40; -fx-max-width: 40;");
            this.title.setStyle("-fx-min-width: 530; -fx-pref-width: 530; -fx-max-width: 530;");
        }
    }
}