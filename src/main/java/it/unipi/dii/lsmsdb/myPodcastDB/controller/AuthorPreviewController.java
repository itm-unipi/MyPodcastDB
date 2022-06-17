package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.cache.FollowedAuthorCache;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.service.AuthorProfileService;
import it.unipi.dii.lsmsdb.myPodcastDB.cache.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.service.HomePageService;
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

    private int typeLabel;

    private final String actorType;

    private boolean isFollowing;

    @FXML
    private ImageView authorPicture;

    @FXML
    private VBox boxAuthorImage;

    @FXML
    private VBox boxFollowAuthor;

    @FXML
    private ImageView followStatus;

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
            boxFollowAuthor.setVisible(true);
            boxFollowAuthor.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-border-radius: 20; -fx-border-color:  #f7f7f7");
            FadeTransition fadeButton = new FadeTransition(Duration.seconds(0.4), boxFollowAuthor);
            fadeButton.setFromValue(0);
            fadeButton.setToValue(1.0);
            fadeButton.play();
        }
    }

    @FXML
    void outAuthor(MouseEvent event) {
        if ((this.actorType.equals("Author") && !((Author) MyPodcastDB.getInstance().getSessionActor()).getName().equals(this.author.getName())) || this.actorType.equals("User")) {
            boxFollowAuthor.setVisible(false);
            FadeTransition fadeButton = new FadeTransition(Duration.seconds(0.4), boxFollowAuthor);
            fadeButton.setFromValue(1.0);
            fadeButton.setToValue(0);
            fadeButton.play();
        }
    }

    @FXML
    void onAuthorBox(MouseEvent event) {
        boxAuthorImage.setStyle("-fx-background-color:  #f0f0f0; -fx-background-radius: 100; -fx-border-color: #e3e3e3; -fx-border-radius: 100;");
    }

    @FXML
    void outAuthorBox(MouseEvent event) {
        boxAuthorImage.setStyle("-fx-background-color: white; -fx-background-radius: 100; -fx-border-color: #eaeaea; -fx-border-radius: 100;");
    }

    @FXML
    void onHoverFollow(MouseEvent event) {
        boxFollowAuthor.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-border-radius: 20; -fx-border-color:  #eaeaea");
        boxAuthorImage.setStyle("-fx-background-color:  #f0f0f0; -fx-background-radius: 100; -fx-border-color: #e3e3e3; -fx-border-radius: 100;");
    }

    @FXML
    void onExitedFollow(MouseEvent event) {
        boxFollowAuthor.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-border-radius: 20; -fx-border-color:  #f7f7f7");
        boxAuthorImage.setStyle("-fx-background-color: white; -fx-background-radius: 100; -fx-border-color: #eaeaea; -fx-border-radius: 100;");
    }

    @FXML
    void onClickAuthorName(MouseEvent event) throws IOException {
        StageManager.showPage(ViewNavigator.AUTHORPROFILE.getPage(), this.author.getName());
    }

    @FXML
    void onClickAuthor(MouseEvent event) throws IOException {
        StageManager.showPage(ViewNavigator.AUTHORPROFILE.getPage(), this.author.getName());
    }

    @FXML
    void onClickFollowAuthor(MouseEvent event) {
        if (MyPodcastDB.getInstance().getSessionPage().equals("AuthorProfile.fxml")) {
            Logger.info("Follow/Unfollow from author profile page");
            fromAuthorProfile();
        } else if (MyPodcastDB.getInstance().getSessionPage().equals("HomePage.fxml")) {
            Logger.info("Follow/Unfollow from homepage");
            fromHomepage();
        } else {
            Logger.error("Error: page undefined");
        }
    }

    void fromAuthorProfile() {
        AuthorProfileService authorProfileService = new AuthorProfileService();
        if (this.actorType.equals("Author")) {
            if (!this.isFollowing) {
                this.isFollowing = authorProfileService.followAuthorAsAuthor(this.author);
                this.followStatus.setImage(ImageCache.getImageFromLocalPath("/img/unfollow.png"));
            } else {
                this.isFollowing = !authorProfileService.unfollowAuthorAsAuthor(this.author);
                this.followStatus.setImage(ImageCache.getImageFromLocalPath("/img/follow.png"));
            }
        } else if (this.actorType.equals("User")) {
            if (!this.isFollowing) {
                this.isFollowing = authorProfileService.followAuthorAsUser(this.author);
                this.followStatus.setImage(ImageCache.getImageFromLocalPath("/img/unfollow.png"));
            } else {
                this.isFollowing = !authorProfileService.unfollowAuthorAsUser(this.author);
                this.followStatus.setImage(ImageCache.getImageFromLocalPath("/img/follow.png"));
            }
        } else {
            Logger.error("Operation not allowed!");
        }
    }

    void fromHomepage() {
        HomePageService homePageService = new HomePageService();
        if (this.actorType.equals("Author")) {
            if (!this.isFollowing) {
                this.isFollowing = homePageService.followAuthorAsAuthor(this.author);
                this.followStatus.setImage(ImageCache.getImageFromLocalPath("/img/unfollow.png"));
            } else {
                this.isFollowing = !homePageService.unfollowAuthorAsAuthor(this.author);
                this.followStatus.setImage(ImageCache.getImageFromLocalPath("/img/follow.png"));
            }
        } else if (this.actorType.equals("User")) {
            if (!this.isFollowing) {
                this.isFollowing = homePageService.followAuthorAsUser(this.author);
                this.followStatus.setImage(ImageCache.getImageFromLocalPath("/img/unfollow.png"));
            } else {
                this.isFollowing = !homePageService.unfollowAuthorAsUser(this.author);
                this.followStatus.setImage(ImageCache.getImageFromLocalPath("/img/follow.png"));
            }
        } else {
            Logger.error("Operation not allowed!");
        }
    }

    void updateFollowersCounter(boolean increment) {
        int followers = Integer.parseInt(counterFollowersLabel.getText());

        if (increment)
            followers++;
        else
            followers--;

        counterFollowersLabel.setText(String.valueOf(followers));
    }

    public void setData(Author author, int typeLabel, String valueLabel) {
        this.author = author;
        this.nameAuthorFollowed.setText(author.getName());

        Image image = ImageCache.getImageFromLocalPath(this.author.getPicturePath());
        this.authorPicture.setImage(image);

        this.isFollowing = (FollowedAuthorCache.getAuthor(author.getName()) != null);
        if (this.isFollowing) {
            this.followStatus.setImage(ImageCache.getImageFromLocalPath("/img/unfollow.png"));
        }

        this.typeLabel = typeLabel;
        if (this.typeLabel == 1) {
            this.boxCounterFollowers.setVisible(true);
            this.boxCounterFollowers.setStyle("-fx-pref-height: 20;");
            this.counterFollowersLabel.setText(valueLabel);
        }
    }
}
