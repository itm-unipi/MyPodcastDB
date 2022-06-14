package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.cache.FollowedAuthorCache;
import it.unipi.dii.lsmsdb.myPodcastDB.cache.FollowedUserCache;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.cache.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.service.SearchService;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class UserSearchPreviewController {
    private User user;

    private boolean isFollowing;

    private String actorType;

    @FXML
    private Label userFound;

    @FXML
    private ImageView userPicture;

    @FXML
    private HBox boxUserPreview;

    @FXML
    private VBox boxFollowUser;

    @FXML
    private Button btnFollowUser;

    @FXML
    void onClickUser(MouseEvent event) throws IOException {
        StageManager.showPage(ViewNavigator.USERPAGE.getPage(), this.user.getUsername());
    }

    @FXML
    void onClickBtnFollowUser(MouseEvent event) throws IOException {
        SearchService searchService = new SearchService();
        if (this.actorType.equals("Author")) {
            if (!this.isFollowing) {
                this.isFollowing = searchService.followUser(this.user);
                this.btnFollowUser.setText("Unfollow");
            } else {
                this.isFollowing = !searchService.unfollowUser(this.user);
                this.btnFollowUser.setText("Follow");
            }
        } else if (this.actorType.equals("User")) {

            if (!this.isFollowing) {
                this.isFollowing = searchService.followUser(this.user);
                this.btnFollowUser.setText("Unfollow");
            } else {
                this.isFollowing = !searchService.unfollowUser(this.user);
                this.btnFollowUser.setText("Follow");
            }
        } else {
            Logger.error("Operation not allowed!");
        }
    }

    @FXML
    void onExitedBtnFollowUser(MouseEvent event) {
        btnFollowUser.setStyle("-fx-pref-width: 85; -fx-background-color: #db55e7; -fx-border-color: #db55e7; -fx-background-insets: 0; -fx-background-radius: 4; -fx-border-radius: 4");
    }

    @FXML
    void onHoverBtnFollowUser(MouseEvent event) {
        btnFollowUser.setStyle("-fx-pref-width: 85; -fx-background-color: #DA70D6; -fx-border-color: #DA70D6; -fx-background-insets: 0; -fx-background-radius: 4; -fx-border-radius: 4");
    }

    @FXML
    void changeBackgroundUser(MouseEvent event) {
        this.boxUserPreview.setStyle("-fx-background-color: #eeeeee;");
    }

    @FXML
    void restoreBackgroundUser(MouseEvent event) {
        this.boxUserPreview.setStyle("-fx-background-color: transparent;");
    }

    public void setData(User user) {
        this.user = user;
        userFound.setText(user.getUsername());
        this.actorType = MyPodcastDB.getInstance().getSessionType();

        Image image = ImageCache.getImageFromLocalPath(user.getPicturePath());
        userPicture.setImage(image);

        if (actorType.equals("Admin") || (actorType.equals("User") && this.user.getUsername().equals(((User)MyPodcastDB.getInstance().getSessionActor()).getUsername())))
            this.boxFollowUser.setVisible(false);

        if (this.boxFollowUser.isVisible()) {
            this.isFollowing = (FollowedUserCache.getUser(user.getUsername()) != null);
            if (this.isFollowing)
                this.btnFollowUser.setText("Unfollow");
        }
    }
}

