package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Date;
import java.util.List;

public class AuthorReducedPodcastController {
    private String podcastId;

    @FXML
    private Label podcastCategory;

    @FXML
    private Label podcastReleaseDate;

    @FXML
    private ImageView podcastImage;

    @FXML
    private Label podcastName;

    @FXML
    private HBox reducedPodcast;

    @FXML
    void onClick(MouseEvent event) {
        Logger.info(podcastId + " | " + podcastName.getText() + " | " + podcastReleaseDate.getText()); // + " | " + podcastCategory.getText());
    }

    @FXML
    void OnMouseHover(MouseEvent event) {
        reducedPodcast.setStyle("-fx-background-color: #eeeeee");
    }

    @FXML
    void onMouseExited(MouseEvent event) {
        reducedPodcast.setStyle("-fx-background-color: transparent");
    }

    public void setData(String podcastId, String podcastName, Date podcastReleaseDate) { // String Category, String getArtworkUrl600 ) {
        this.podcastId = podcastId;
        this.podcastName.setText(podcastName);
        //this.podcastCategory.setText(podcastCategory);

        // Setting release date
        String[] tokens = podcastReleaseDate.toString().split(" ");
        String date = tokens[2] + " " + tokens[1] + " " + tokens[5];
        this.podcastReleaseDate.setText(date);

        // Setting image preview
        //Image image = new Image(podcastArtworkUrl600());
        //podcastImage.setImage(image);
    }

}
