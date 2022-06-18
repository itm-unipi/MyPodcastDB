package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.*;
import it.unipi.dii.lsmsdb.myPodcastDB.service.UserPageService;
import it.unipi.dii.lsmsdb.myPodcastDB.cache.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.DialogManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;

public class ActorPreviewController {

    @FXML
    private AnchorPane actorAnchorPane;
    @FXML
    private VBox actorContainer;

    @FXML
    private ImageView actorImage;

    @FXML
    private Label actorName;

    @FXML
    private ImageView followButton;

    @FXML
    private Pane followButtonArea;

    @FXML
    private Pane trashButtonArea;

    @FXML
    private ImageView trashButton;

    @FXML
    private Label toolTip;


    private Author authorPreview = new Author();
    private User userPreview = new User();
    private String actorType;
    private AnchorPane mainPage;
    private String visitorType;
    private String visitorName;
    private boolean followStatus = false;
    private boolean blockClickEvent = false;
    private boolean disableClick = false;
    private boolean visitorMode;

    @FXML
    void actorIn(MouseEvent event) {
        actorContainer.setStyle("-fx-background-color: #E5E5E5; -fx-background-radius: 10;");
        if(!visitorMode) {
            trashButtonArea.setVisible(true);
            trashButtonArea.setOpacity(0.0);
            FadeTransition fadeAuthorImage = new FadeTransition(Duration.seconds(0.3), trashButtonArea);
            fadeAuthorImage.setFromValue(0.0);
            fadeAuthorImage.setToValue(1.0);
            fadeAuthorImage.play();
        }
        else if(visitorType.equals("User") || visitorType.equals(actorType)) {
            followButtonArea.setVisible(true);
            followButtonArea.setOpacity(0.0);
            FadeTransition fadeAuthorImage = new FadeTransition(Duration.seconds(0.3), followButtonArea);
            fadeAuthorImage.setFromValue(0.0);
            fadeAuthorImage.setToValue(1.0);
            fadeAuthorImage.play();
        }

    }

    @FXML
    void actorOut(MouseEvent event) {
        actorContainer.setStyle("-fx-background-color: transparent");
        if(!visitorMode)
            trashButtonArea.setVisible(false);
        else if(visitorType.equals("User") || visitorType.equals(actorType))
            followButtonArea.setVisible(false);
    }

    @FXML
    void onClick(MouseEvent event) throws IOException {

        if(disableClick)
            return;

        if(blockClickEvent) {
            blockClickEvent = false;
            return;
        }
        if(this.actorType.equals("Author")) {
            Logger.info(authorPreview.getId() + " : " + this.authorPreview.getName() + " selected");
            StageManager.showPage(ViewNavigator.AUTHORPROFILE.getPage(), authorPreview.getName());
        }
        else {
            Logger.info(userPreview.getUsername() + " selected");
            StageManager.showPage((ViewNavigator.USERPAGE.getPage()), userPreview.getUsername());
        }
    }


    @FXML
    void  followClick(MouseEvent event){

        Logger.info("Like button clicked");
        blockClickEvent = true;
        UserPageService service = new UserPageService();
        int res = -1;
        if(actorType.equals("User")) {
            if (followStatus) {
                res = service.updateFollowedUser(visitorName, userPreview, false);
                if (res == 0) {
                    Logger.success("Removed follow user relation successfully");
                    followButton.setImage(ImageCache.getImageFromLocalPath("/img/follow.png"));
                    followStatus = false;
                    toolTip.setText("Unfollowed");
                }
                else if(res == 3)
                    Logger.error("Follow user relation already not exists");
                else if(res == 4){
                    Logger.error("Removing follow user relation failed");
                    DialogManager.getInstance().createErrorAlert(mainPage, "Something went wrong");
                }
                else{
                    Logger.error("Unknown error");
                    DialogManager.getInstance().createErrorAlert(mainPage, "Something went wrong");
                }

            } else {
                res = service.updateFollowedUser(visitorName, userPreview, true);
                if (res == 0) {
                    Logger.success("Added follow user relation successfully");
                    followButton.setImage(ImageCache.getImageFromLocalPath("/img/unfollow.png"));
                    followStatus = true;
                    toolTip.setText("Followed");
                }
                else if(res == 1)
                    Logger.error("Follow user relation already exists");
                else if(res == 2){
                    Logger.error("Adding follow user relation failed");
                    DialogManager.getInstance().createErrorAlert(mainPage, "Something went wrong");
                }
                else{
                    Logger.error("Unknown error");
                    DialogManager.getInstance().createErrorAlert(mainPage, "Something went wrong");
                }
            }
        }
        if(actorType.equals("Author")){
            if (followStatus) {
                res = service.updateFollowedAuthor(visitorName, visitorType, authorPreview, false);
                if (res == 0) {
                    Logger.info("Removed author follows author relation successfully");
                    followButton.setImage(ImageCache.getImageFromLocalPath("/img/follow.png"));
                    followStatus = false;
                    toolTip.setText("Unfollowed");
                }
                else if(res == 3)
                    Logger.error("User Follows author relation already not exists");
                else if(res == 4){
                    Logger.error("Removing user follows author relation failed");
                    DialogManager.getInstance().createErrorAlert(mainPage, "Something went wrong");
                }
                else if(res == 7)
                    Logger.error("Author Follows author relation already not exists");
                else if(res == 8){
                    Logger.error("Removing author follows author relation failed");
                    DialogManager.getInstance().createErrorAlert(mainPage, "Something went wrong");
                }
                else{
                    Logger.error("Unknown error");
                    DialogManager.getInstance().createErrorAlert(mainPage, "Something went wrong");
                }
            } else {
                res = service.updateFollowedAuthor(visitorName, visitorType, authorPreview, true);
                if (res == 0) {
                    Logger.success("Added author follows author relation successfully");
                    followButton.setImage(ImageCache.getImageFromLocalPath("/img/unfollow.png"));
                    followStatus = true;
                    toolTip.setText("Followed");
                }
                else if(res == 1)
                    Logger.error("User Follows author relation already exists");
                else if(res == 2){
                    Logger.error("Adding user follows author relation failed");
                    DialogManager.getInstance().createErrorAlert(mainPage, "Something went wrong");
                }
                else if(res == 5)
                    Logger.error("Author Follows author relation already exists");
                else if(res == 6){
                    Logger.error("Adding author follows author relation failed");
                    DialogManager.getInstance().createErrorAlert(mainPage, "Something went wrong");
                }
                else{
                    Logger.error("Unknown error");
                    DialogManager.getInstance().createErrorAlert(mainPage, "Something went wrong");
                }
            }

        }
        if(res == 0){
            toolTip.setOpacity(0.0);
            FadeTransition fadeAuthorImage = new FadeTransition(Duration.seconds(1.8), toolTip);
            fadeAuthorImage.setFromValue(1.0);
            fadeAuthorImage.setToValue(0.0);
            fadeAuthorImage.play();
        }
    }

    @FXML
    void trashClick(MouseEvent event){

        if(!disableClick) {
            Logger.info("Trash button clicked");
            UserPageService service = new UserPageService();
            int res = -1;
            if (actorType.equals("Author")) {
                res = service.updateFollowedAuthor(visitorName, "User",authorPreview, false);
                if (res == 0)
                    Logger.info("Removed author follows author relation successfully");
                else if(res == 3)
                    Logger.error("User Follows author relation already not exists");
                else if(res == 4){
                    Logger.error("Removing user follows author relation failed");
                    DialogManager.getInstance().createErrorAlert(mainPage, "Something went wrong");
                }
                else if(res == 7)
                    Logger.error("Author Follows author relation already not exists");
                else if(res == 8){
                    Logger.error("Removing author follows author relation failed");
                    DialogManager.getInstance().createErrorAlert(mainPage, "Something went wrong");
                }
                else{
                    Logger.error("Unknown error");
                    DialogManager.getInstance().createErrorAlert(mainPage, "Something went wrong");
                }
            }
            else if(actorType.equals("User")) {
                res = service.updateFollowedUser(visitorName, userPreview, false);
                if (res == 0)
                    Logger.success("Removed follow user relation successfully");
                else if(res == 3)
                    Logger.error("Follow user relation already not exists");
                else if(res == 4){
                    Logger.error("Removing follow user relation failed");
                    DialogManager.getInstance().createErrorAlert(mainPage, "Something went wrong");
                    return;
                }
                else{
                    Logger.error("Unknown error");
                    DialogManager.getInstance().createErrorAlert(mainPage, "Something went wrong");
                    return;
                }
            }

            blockClickEvent = true;
            disableClick = true;
            actorAnchorPane.setOpacity(0.2);
            trashButton.setImage(ImageCache.getImageFromLocalPath("/img/refresh1.png"));
        }
        else{
            Logger.info("Trash button clicked");

            UserPageService service = new UserPageService();
            int res = -1;
            if (actorType.equals("Author")) {
                res = service.updateFollowedAuthor(visitorName, "User", authorPreview, true);
                if (res == 0)
                    Logger.success("Added author follows author relation successfully");
                else if(res == 1)
                    Logger.error("User Follows author relation already exists");
                else if(res == 2){
                    Logger.error("Adding user follows author relation failed");
                    DialogManager.getInstance().createErrorAlert(mainPage, "Something went wrong");
                }
                else if(res == 5)
                    Logger.error("Author Follows author relation already exists");
                else if(res == 6){
                    Logger.error("Adding author follows author relation failed");
                    DialogManager.getInstance().createErrorAlert(mainPage, "Something went wrong");
                }
                else{
                    Logger.error("Unknown error");
                    DialogManager.getInstance().createErrorAlert(mainPage, "Something went wrong");
                }
            }
            else if(actorType.equals("User")) {
                res = service.updateFollowedUser(visitorName, userPreview, true);
                if (res == 0)
                    Logger.success("Added follow user relation successfully");
                else if(res == 1)
                    Logger.error("Follow user relation already exists");
                else if(res == 2){
                    Logger.error("Adding follow user relation failed");
                    DialogManager.getInstance().createErrorAlert(mainPage, "Something went wrong");
                    return;
                }
                else{
                    Logger.error("Unknown error");
                    DialogManager.getInstance().createErrorAlert(mainPage, "Something went wrong");
                    return;
                }
            }

            blockClickEvent = true;
            disableClick = false;
            actorAnchorPane.setOpacity(1.0);
            trashButton.setImage(ImageCache.getImageFromLocalPath("/img/delete_elem1.png"));
        }
    }

    @FXML
    void trashIn(MouseEvent event){
        if(disableClick) {
            actorAnchorPane.setOpacity(1.0);
            trashButton.setImage(ImageCache.getImageFromLocalPath("/img/refresh2.png"));
            return;
        }
        trashButton.setImage(ImageCache.getImageFromLocalPath("/img/delete_elem2.png"));
    }

    @FXML
    void trashOut(MouseEvent event){
        if(disableClick) {
            actorAnchorPane.setOpacity(0.2);
            trashButton.setImage(ImageCache.getImageFromLocalPath("/img/refresh1.png"));
            return;
        }
        trashButton.setImage(ImageCache.getImageFromLocalPath("/img/delete_elem1.png"));
    }

    /**************************/
    public ActorPreviewController() {
    }


    public void setData(AnchorPane mainPage, Author author) {
        this.visitorMode = false;
        this.actorType = "Author";
        this.authorPreview = author;
        this.mainPage = mainPage;
        this.visitorType = MyPodcastDB.getInstance().getSessionType();
        if(!visitorType.equals("Admin"))
            this.visitorName = ((User)MyPodcastDB.getInstance().getSessionActor()).getUsername();
        else
            this.visitorMode = true;

        this.actorImage.setImage(ImageCache.getImageFromLocalPath(author.getPicturePath()));
        this.actorName.setText(author.getName());

        followButtonArea.setVisible(false);
        trashButtonArea.setVisible(false);
        toolTip.setOpacity(0.0);

    }
    public void setData(AnchorPane mainPage, Author author, boolean isFollowed) {
        this.visitorMode = true;
        this.actorType = "Author";
        this.authorPreview = author;
        this.mainPage = mainPage;
        this.visitorType = MyPodcastDB.getInstance().getSessionType();
        if(visitorType.equals("User"))
            this.visitorName = ((User)MyPodcastDB.getInstance().getSessionActor()).getUsername();
        else if(visitorType.equals("Author"))
            this.visitorName = ((Author)MyPodcastDB.getInstance().getSessionActor()).getName();

        this.actorImage.setImage(ImageCache.getImageFromLocalPath(author.getPicturePath()));
        this.actorName.setText(author.getName());

        trashButtonArea.setVisible(false);
        toolTip.setOpacity(0.0);
        if(isFollowed){
            followButtonArea.setVisible(true);
            followButton.setImage(ImageCache.getImageFromLocalPath("/img/unfollow.png"));
            followStatus = true;
        }
        else
            followButtonArea.setVisible(false);
    }
    public void setData(AnchorPane mainPage, User user) {
        this.actorType = "User";
        this.userPreview = user;
        this.mainPage = mainPage;
        this.visitorType = MyPodcastDB.getInstance().getSessionType();
        if(visitorType.equals("User")) {
            this.visitorMode = false;
            this.visitorName = ((User) MyPodcastDB.getInstance().getSessionActor()).getUsername();
        }
        else
            this.visitorMode = true;

        this.actorImage.setImage(ImageCache.getImageFromLocalPath(user.getPicturePath()));
        this.actorName.setText(user.getUsername());

        followButtonArea.setVisible(false);
        trashButtonArea.setVisible(false);
        toolTip.setOpacity(0.0);
    }

    public void setData(AnchorPane mainPage, User user, boolean isFollowed) {

        this.visitorMode = true;
        this.actorType = "User";
        this.userPreview = user;
        this.visitorType = "User";
        this.visitorName = ((User)MyPodcastDB.getInstance().getSessionActor()).getUsername();;
        this.mainPage = mainPage;

        this.actorImage.setImage(ImageCache.getImageFromLocalPath(user.getPicturePath()));
        this.actorName.setText(user.getUsername());

        trashButtonArea.setVisible(false);
        followButtonArea.setVisible(false);
        toolTip.setOpacity(0.0);
        if(isFollowed){

            followButton.setImage(ImageCache.getImageFromLocalPath("/img/unfollow.png"));
            followStatus = true;
        }

    }



}
