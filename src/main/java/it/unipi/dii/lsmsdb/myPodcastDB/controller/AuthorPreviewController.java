package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;

public class AuthorPreviewController {
    private Author author;

    @FXML
    private ImageView authorPicture;

    @FXML
    private VBox boxAuthorImage;

    @FXML
    private Button followAuthor;

    @FXML
    private Label nameAuthorFollowed;

    @FXML
    void onAuthor(MouseEvent event) {
        boxAuthorImage.setStyle("-fx-background-color:  #eaeaea; -fx-background-radius: 100px; -fx-border-color: #eaeaea; -fx-border-radius: 100px;");
        FadeTransition fadeBackground = new FadeTransition(Duration.seconds(1), boxAuthorImage);
        fadeBackground.play();

        FadeTransition fadeAuthorImage = new FadeTransition(Duration.seconds(0.3), authorPicture);
        fadeAuthorImage.setFromValue(1.0);
        fadeAuthorImage.setToValue(0.2);
        fadeAuthorImage.play();

        followAuthor.setVisible(true);
        followAuthor.setStyle("-fx-pref-width: 80px; -fx-min-width: 80px; -fx-pref-height: 30px; -fx-min-height: 30px; -fx-background-color: white; -fx-background-radius: 12px; -fx-border-radius: 12px;");

        FadeTransition fadeButton = new FadeTransition(Duration.seconds(0.7), followAuthor);
        fadeButton.setFromValue(0);
        fadeButton.setToValue(1.0);
        fadeButton.play();
    }

    @FXML
    void outAuthor(MouseEvent event) {
        boxAuthorImage.setStyle("-fx-background-color: white; -fx-background-radius: 100px; -fx-border-color: #eaeaea; -fx-border-radius: 100px;");
        FadeTransition fadeBackground = new FadeTransition(Duration.seconds(0.3), boxAuthorImage);
        fadeBackground.play();

        FadeTransition fadeAuthorImage = new FadeTransition(Duration.seconds(0.3), authorPicture);
        fadeAuthorImage.setFromValue(0.3);
        fadeAuthorImage.setToValue(1.0);
        fadeAuthorImage.play();

        followAuthor.setVisible(false);
        followAuthor.setStyle("-fx-pref-width: 0px; -fx-min-width: 0px; -fx-pref-height: 30px; -fx-min-height: 30px; -fx-background-color: white; -fx-background-radius: 12px; -fx-border-radius: 12px;");

        FadeTransition fadeButton = new FadeTransition(Duration.seconds(0.3), followAuthor);
        fadeButton.setFromValue(1.0);
        fadeButton.setToValue(0);
        fadeButton.play();
    }

    @FXML
    void onMouseHoverFollowButton(MouseEvent event) {
        followAuthor.setStyle("-fx-pref-width: 80px; -fx-min-width: 80px; -fx-pref-height: 30px; -fx-min-height: 30px; -fx-background-color: #FAFAFAFF; -fx-background-radius: 12px; -fx-border-radius: 12px;");
    }

    @FXML
    void onMouseExitedFollowButton(MouseEvent event) {
        followAuthor.setStyle("-fx-pref-width: 80px; -fx-min-width: 80px; -fx-pref-height: 30px; -fx-min-height: 30px; -fx-background-color: white; -fx-background-radius: 12px; -fx-border-radius: 12px;");
    }

    @FXML
    void onClickAuthorName(MouseEvent event) throws IOException {
        Logger.info("Clicked on Author name: " + this.author.getName());
        StageManager.showPage(ViewNavigator.AUTHORPROFILE.getPage(), this.author.getName());
    }

    @FXML
    void onClickFollowAuthor(MouseEvent event) {
        Logger.info("Clicked on Follow " + this.author.getName());
        // TODO: change button text from Follow to Unfollow and viceversa
    }

    public void setData(Author author) {
        this.author = author;
        nameAuthorFollowed.setText(author.getName());

        Image image = ImageCache.getImageFromLocalPath(author.getPicturePath());
        authorPicture.setImage(image);
    }
}
