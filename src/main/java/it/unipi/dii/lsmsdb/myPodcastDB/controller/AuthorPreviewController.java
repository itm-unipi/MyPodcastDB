package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.service.AuthorProfileService;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;

public class AuthorPreviewController {
    private Author author;

    private final String actorType;

    @FXML
    private ImageView authorPicture;

    @FXML
    private VBox boxAuthorImage;

    @FXML
    private Button btnFollowAuthor;

    @FXML
    private Label nameAuthorFollowed;

    @FXML
    private HBox boxCounterFollowers;

    @FXML
    private Label counterFollowersLabel;

    public AuthorPreviewController() {
        this.actorType = MyPodcastDB.getInstance().getSessionType();;
    }

    @FXML
    void onAuthor(MouseEvent event) {

        if ((this.actorType.equals("Author") && !((Author) MyPodcastDB.getInstance().getSessionActor()).getName().equals(this.author.getName())) || this.actorType.equals("User")) {
            boxAuthorImage.setStyle("-fx-background-color:  #eaeaea; -fx-background-radius: 100; -fx-border-color: #eaeaea; -fx-border-radius: 100;");
            FadeTransition fadeBackground = new FadeTransition(Duration.seconds(1), boxAuthorImage);
            fadeBackground.play();

            FadeTransition fadeAuthorImage = new FadeTransition(Duration.seconds(0.3), authorPicture);
            fadeAuthorImage.setFromValue(1.0);
            fadeAuthorImage.setToValue(0.2);
            fadeAuthorImage.play();

            btnFollowAuthor.setVisible(true);
            btnFollowAuthor.setStyle("-fx-pref-width: 80; -fx-min-width: 80; -fx-pref-height: 30; -fx-min-height: 30; -fx-background-color: white; -fx-background-radius: 12; -fx-border-radius: 12;");

            FadeTransition fadeButton = new FadeTransition(Duration.seconds(0.7), btnFollowAuthor);
            fadeButton.setFromValue(0);
            fadeButton.setToValue(1.0);
            fadeButton.play();
        }
    }

    @FXML
    void outAuthor(MouseEvent event) {

        if ((this.actorType.equals("Author") && !((Author) MyPodcastDB.getInstance().getSessionActor()).getName().equals(this.author.getName())) || this.actorType.equals("User")) {
            boxAuthorImage.setStyle("-fx-background-color: white; -fx-background-radius: 100; -fx-border-color: #eaeaea; -fx-border-radius: 100;");
            FadeTransition fadeBackground = new FadeTransition(Duration.seconds(0.3), boxAuthorImage);
            fadeBackground.play();

            FadeTransition fadeAuthorImage = new FadeTransition(Duration.seconds(0.3), authorPicture);
            fadeAuthorImage.setFromValue(0.3);
            fadeAuthorImage.setToValue(1.0);
            fadeAuthorImage.play();

            btnFollowAuthor.setVisible(false);
            btnFollowAuthor.setStyle("-fx-pref-width: 0; -fx-min-width: 0; -fx-pref-height: 30; -fx-min-height: 30; -fx-background-color: white; -fx-background-radius: 12; -fx-border-radius: 12;");

            FadeTransition fadeButton = new FadeTransition(Duration.seconds(0.3), btnFollowAuthor);
            fadeButton.setFromValue(1.0);
            fadeButton.setToValue(0);
            fadeButton.play();
        }
    }

    @FXML
    void onMouseHoverFollowButton(MouseEvent event) {
        btnFollowAuthor.setStyle("-fx-pref-width: 80; -fx-min-width: 80; -fx-pref-height: 30; -fx-min-height: 30; -fx-background-color: #FAFAFAFF; -fx-background-radius: 12; -fx-border-radius: 12;");
    }

    @FXML
    void onMouseExitedFollowButton(MouseEvent event) {
        btnFollowAuthor.setStyle("-fx-pref-width: 80; -fx-min-width: 80; -fx-pref-height: 30; -fx-min-height: 30; -fx-background-color: white; -fx-background-radius: 12; -fx-border-radius: 12;");
    }

    @FXML
    void onClickAuthorName(MouseEvent event) throws IOException {
        StageManager.showPage(ViewNavigator.AUTHORPROFILE.getPage(), this.author.getName());
    }

    @FXML
    void onClickBtnFollowAuthor(MouseEvent event) {

        AuthorProfileService authorProfileService = new AuthorProfileService();
        if (this.actorType.equals("Author")) {

            if (btnFollowAuthor.getText().equals("Follow")) {
                authorProfileService.followAuthorAsAuthor(this.author.getName());
                btnFollowAuthor.setText("Unfollow");

                // Increment followers counter
                updateFollowerCounter(true);
            } else {
                authorProfileService.unfollowAuthorAsAuthor(this.author.getName());
                btnFollowAuthor.setText("Follow");

                // Decrement followers counter
                updateFollowerCounter(false);
            }
        } else if (this.actorType.equals("User")) {

            if (btnFollowAuthor.getText().equals("Follow")) {
                authorProfileService.followAuthorAsUser(this.author.getName());
                btnFollowAuthor.setText("Unfollow");
                updateFollowerCounter(true);
            } else {
                authorProfileService.unfollowAuthorAsUser(this.author.getName());
                btnFollowAuthor.setText("Follow");
                updateFollowerCounter(false);
            }

        } else {
            Logger.error("Operation not allowed!");
        }
    }

    void updateFollowerCounter(boolean increment) {
        int followers = Integer.parseInt(counterFollowersLabel.getText());

        if (increment)
            followers++;
        else
            followers--;

        counterFollowersLabel.setText(String.valueOf(followers));
    }

    public void setData(Author author, boolean follow, int typeLabel, String valueLabel) {
        this.author = author;
        nameAuthorFollowed.setText(author.getName());

        Image image = ImageCache.getImageFromLocalPath(this.author.getPicturePath());
        authorPicture.setImage(image);

        if (follow)
            this.btnFollowAuthor.setText("Unfollow");

        if (typeLabel == 1) {
            boxCounterFollowers.setVisible(true);
            boxCounterFollowers.setStyle("-fx-pref-height: 20;");
            counterFollowersLabel.setText(valueLabel);
        }
    }
}
