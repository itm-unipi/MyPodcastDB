package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.*;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ActorPreviewController {

    private Author authorPreview;

    private User userPreview;

    private String actorType;

    @FXML
    private VBox actorContainer;

    @FXML
    private ImageView actorImage;

    @FXML
    private Tooltip actorToolTip;

    @FXML
    private Label actorName;

    public ActorPreviewController() {
    }

    public void setData(Author author) {
        actorType = "Author";
        this.authorPreview = author;

        Image image = ImageCache.getImageFromURL(author.getPicturePath());
        this.actorImage.setImage(image);
        this.actorName.setText(author.getName());
        this.actorToolTip.setText(author.getName());
    }

    public void setData(User user) {
        actorType = "User";
        this.userPreview = user;

        Image image = ImageCache.getImageFromURL(user.getPicturePath());
        this.actorImage.setImage(image);
        this.actorName.setText(user.getUsername());
        this.actorToolTip.setText(user.getUsername());
    }

    @FXML
    void actorIn(MouseEvent event) {
        actorContainer.setStyle("-fx-background-color: f2f2f2; -fx-background-radius: 10;");
    }

    @FXML
    void actorOut(MouseEvent event) {
        actorContainer.setStyle("-fx-background-color: white");
    }

    @FXML
    void onClick(MouseEvent event) throws IOException {

        if(actorType.equals("Author")) {
            Logger.info(authorPreview.getId() + " : " + this.authorPreview.getName() + " selected");
            StageManager.showPage(ViewNavigator.AUTHORPROFILE.getPage(), authorPreview.getId());
        }
        else {
            Logger.info(userPreview.getUsername() + " selected");
            StageManager.showPage((ViewNavigator.USERPAGE.getPage()), userPreview.getUsername());
        }
    }

}
