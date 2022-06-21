package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.service.PodcastPageService;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.JsonDecode;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.DialogManager;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PodcastUpdateController {

    @FXML
    private TextField artworkUrl;

    @FXML
    private Button cancel;

    @FXML
    private TextField contentAdvisory;

    @FXML
    private ComboBox<String> country;

    @FXML
    private TextField name;

    @FXML
    private Button update;

    private Podcast podcast;

    @FXML
    private ComboBox<String> primaryCategory;

    @FXML
    private DatePicker releaseDate;

    @FXML
    private GridPane secondaryCategory;
    private BorderPane mainPage;
    private PodcastPageService service;
    private PodcastPageController podcastPageController;

    @FXML
    void clickOnCancel(MouseEvent event) {
        closeStage(event);
    }

    @FXML
    void clickOnUpdate(MouseEvent event) throws IOException {
        Podcast updatedPodcast = new Podcast(this.podcast);

        // update the local podcast
        if (!this.name.getText().equals(this.podcast.getName()) && !this.name.getText().equals(""))
            updatedPodcast.setName(this.name.getText());
        if (!this.country.getValue().equals(this.podcast.getCountry()) && !this.country.getValue().equals(""))
            updatedPodcast.setCountry(this.country.getValue());
        if (!this.contentAdvisory.getText().equals(this.podcast.getContentAdvisoryRating()) && !this.contentAdvisory.getText().equals(""))
            updatedPodcast.setContentAdvisoryRating(this.contentAdvisory.getText());
        if (!this.primaryCategory.getValue().equals(this.podcast.getPrimaryCategory()) && !this.primaryCategory.getValue().equals(""))
            updatedPodcast.setPrimaryCategory(this.primaryCategory.getValue());
        if (!this.artworkUrl.getText().equals(this.podcast.getArtworkUrl600()) && !this.artworkUrl.getText().equals(""))
            updatedPodcast.setArtworkUrl600(this.artworkUrl.getText());
        if (this.releaseDate.getValue() != null) {
            LocalDate localDate = this.releaseDate.getValue();
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            Date releaseDate = Date.from(instant);
            if (this.podcast.getReleaseDate() != releaseDate)
                updatedPodcast.setReleaseDate(releaseDate);
        }

        // secondary category update
        List<String> checkedCategories = new ArrayList<>();
        for (Node child : this.secondaryCategory.getChildren()) {
            boolean isSelected = ((CheckBox) child).isSelected();
            if (isSelected)
                checkedCategories.add(((CheckBox) child).getText());
        }
        if (!this.podcast.getCategories().equals(checkedCategories))
            updatedPodcast.setCategories(checkedCategories);

        // check if modified
        if (!this.podcast.equals(updatedPodcast)) {
            // update podcast on persistence
            int result = this.service.updatePodcast(this.podcast, updatedPodcast);

            // if update is succesfull update the page
            if (result == 0) {
                this.podcast.copy(updatedPodcast);

                // update page
                this.podcastPageController.updatePodcastPage();
            }

            // error in update
            else if (result != 0) {
                DialogManager.getInstance().createErrorAlert(this.mainPage, "Failed to update episode");
            }
        }

        closeStage(event);
    }

    @FXML
    void mouseOnCancel(MouseEvent event) {
        this.cancel.setStyle("-fx-border-color: #f44336; -fx-background-color: white; -fx-background-radius: 8; -fx-text-fill: black; -fx-border-radius: 8; -fx-cursor: hand;");
    }

    @FXML
    void mouseOnUpdate(MouseEvent event) {
        this.update.setStyle("-fx-border-color: #4CAF50; -fx-background-color: white; -fx-background-radius: 8; -fx-text-fill: black; -fx-border-radius: 8; -fx-cursor: hand;");
    }

    @FXML
    void mouseOutCancel(MouseEvent event) {
        this.cancel.setStyle("-fx-border-color: transparent; -fx-background-color: #f44336; -fx-background-radius: 8; -fx-text-fill: white; -fx-border-radius: 8; -fx-cursor: default;");
    }

    @FXML
    void mouseOutUpdate(MouseEvent event) {
        this.update.setStyle("-fx-border-color: transparent; -fx-background-color: #4CAF50; -fx-background-radius: 8; -fx-text-fill: white; -fx-border-radius: 8; -fx-cursor: default;");
    }

    public void setData(Podcast podcast, BorderPane mainPage, PodcastPageService service, PodcastPageController podcastPageController) throws Exception {
        // initialize the local podcast
        this.podcast = podcast;
        this.mainPage = mainPage;
        this.service = service;
        this.podcastPageController = podcastPageController;

        // load countries list
        for (String country: JsonDecode.getCountries()) {
            this.country.getItems().add(country);
        }
        this.country.setValue(podcast.getCountry());

        // load category list
        int row = 0;
        int column = 0;
        for (String category: JsonDecode.getCategories()) {
            this.primaryCategory.getItems().add(category);

            CheckBox newCategory = new CheckBox(category);
            newCategory.setStyle("-fx-font-family: Corbel; -fx-font-size: 13");
            this.secondaryCategory.add(newCategory, column++, row);

            // if podcast belongs to this category checks it
            if (podcast.getCategories().contains(category))
                newCategory.setSelected(true);
            else
                newCategory.setSelected(false);

            if (column == 3) {
                column = 0;
                row++;
            }
        }
        this.primaryCategory.setValue(podcast.getPrimaryCategory());

        // setup text fields
        this.name.setText(podcast.getName());
        this.contentAdvisory.setText(podcast.getContentAdvisoryRating());
        this.artworkUrl.setText(podcast.getArtworkUrl600());

        // setup date picker
        this.releaseDate.setValue(Instant.ofEpochMilli(this.podcast.getReleaseDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
    }

    public void initialize() {

    }

    private void closeStage(MouseEvent event) {
        Node source = (Node)event.getSource();
        Stage stage = (Stage)source.getScene().getWindow();
        stage.close();
    }
}