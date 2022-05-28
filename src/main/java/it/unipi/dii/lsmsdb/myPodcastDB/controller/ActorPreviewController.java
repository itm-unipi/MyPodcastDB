package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.*;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class ActorPreviewController {

    private Author authorPreview;
    private User userPreview;

    @FXML
    private VBox actorContainer;

    @FXML
    private ImageView actorImage;

    @FXML
    private Tooltip actorToolTip;

    @FXML
    private Label actorName;

    public ActorPreviewController() {
        authorPreview = null;
        userPreview = null;
    }

    public void setData(Author author) {
        this.authorPreview = author;

        Image image = new Image(author.getPicturePath());
        this.actorImage.setImage(image);
        this.actorName.setText(author.getName());
        this.actorToolTip.setText(author.getName());
    }

    public void setData(User user) {
        this.userPreview = user;

        Image image = new Image(user.getPicturePath());
        this.actorImage.setImage(image);
        this.actorName.setText(user.getUsername());
        this.actorToolTip.setText(user.getUsername());
    }

    @FXML
    void actorIn(MouseEvent event) {
        actorContainer.setStyle("-fx-background-color: f2f2f2; -fx-background-radius: 10;");
        actorName.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");
    }

    @FXML
    void actorOut(MouseEvent event) {
        actorContainer.setStyle("-fx-background-color: white");
        actorName.setStyle("-fx-font-size: 10; -fx-font-weight: bold");
    }

    @FXML
    void onClick(MouseEvent event) {

        if(authorPreview != null)
            Logger.info(authorPreview.getId() + " : " + this.authorPreview.getName() + " selected");
        else
            Logger.info(userPreview.getUsername() + " selected");
    }

}
