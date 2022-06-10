package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.*;
import it.unipi.dii.lsmsdb.myPodcastDB.service.UserPageService;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.DialogManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ActorPreviewController {

    @FXML
    private AnchorPane actorAnchorPane;
    @FXML
    private VBox actorContainer;

    @FXML
    private ImageView actorImage;

    @FXML
    private Tooltip actorToolTip;

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
    private Label followTip;

    @FXML
    private Label removeTip;

    @FXML
    private Label cancelTip;

    private Author authorPreview;
    private User userPreview;
    private String actorType;
    private AnchorPane mainPage;
    private String visitorType;
    private String visitorName;
    private boolean followStatus = false;
    private boolean blockClickEvent = false;
    private boolean disableClick = false;

    @FXML
    void actorIn(MouseEvent event) {
        actorContainer.setStyle("-fx-background-color: #E5E5E5; -fx-background-radius: 10;");
    }

    @FXML
    void actorOut(MouseEvent event) {
        actorContainer.setStyle("-fx-background-color: transparent");
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
    void followIn(MouseEvent event){
        followTip.setVisible(true);
        if(followStatus) {
            followTip.setText("Unfollow");
            followButton.setImage(ImageCache.getImageFromLocalPath("/img/following_30px.png"));
        }
        else {followTip.setText("Follow");
            followButton.setImage(ImageCache.getImageFromLocalPath("/img/Favorite_64px.png"));
        }
    }

    @FXML
    void followOut(MouseEvent event){
        followTip.setVisible(false);
        if(!followStatus)
            followButton.setImage(ImageCache.getImageFromLocalPath("/img/following_30px.png"));
        else
            followButton.setImage(ImageCache.getImageFromLocalPath("/img/Favorite_64px.png"));
    }

    @FXML
    void  followClick(MouseEvent event){
        followTip.setVisible(false);
        removeTip.setVisible(false);
        cancelTip.setVisible(false);

        Logger.info("Like button clicked");
        blockClickEvent = true;
        UserPageService service = new UserPageService();
        int res = -1;
        if(actorType.equals("User")) {
            if (followStatus) {
                res = service.updateFollowedUser(visitorName, actorName.getText(), false);
                if (res == 0) {
                    Logger.success("Removed follow user relation successfully");
                    followButton.setImage(ImageCache.getImageFromLocalPath("/img/following_30px.png"));
                    followStatus = false;
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
                res = service.updateFollowedUser(visitorName, actorName.getText(), true);
                if (res == 0) {
                    Logger.success("Added follow user relation successfully");
                    followButton.setImage(ImageCache.getImageFromLocalPath("/img/Favorite_64px.png"));
                    followStatus = true;
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
                res = service.updateFollowedAuthor(visitorName, visitorType, actorName.getText(), false);
                if (res == 0) {
                    Logger.info("Removed author follows author relation successfully");
                    followButton.setImage(ImageCache.getImageFromLocalPath("/img/following_30px.png"));
                    followStatus = false;
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
                res = service.updateFollowedAuthor(visitorName, visitorType, actorName.getText(), true);
                if (res == 0) {
                    Logger.success("Added author follows author relation successfully");
                    followButton.setImage(ImageCache.getImageFromLocalPath("/img/Favorite_64px.png"));
                    followStatus = true;
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
    }

    @FXML
    void trashClick(MouseEvent event){

        followTip.setVisible(false);
        removeTip.setVisible(false);
        cancelTip.setVisible(false);

        if(!disableClick) {
            Logger.info("Trash button clicked");
            UserPageService service = new UserPageService();
            int res = -1;
            if (actorType.equals("Author")) {
                res = service.updateFollowedAuthor(visitorName, "User", authorPreview.getName(), false);
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
                res = service.updateFollowedUser(visitorName, userPreview.getUsername(), false);
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
                res = service.updateFollowedAuthor(visitorName, "User", authorPreview.getName(), true);
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
                res = service.updateFollowedUser(visitorName, userPreview.getUsername(), true);
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
            cancelTip.setVisible(true);
            actorAnchorPane.setOpacity(1.0);
            trashButton.setImage(ImageCache.getImageFromLocalPath("/img/refresh2.png"));
            return;
        }
        removeTip.setVisible(true);
        trashButton.setImage(ImageCache.getImageFromLocalPath("/img/delete_elem2.png"));
    }

    @FXML
    void trashOut(MouseEvent event){
        if(disableClick) {
            cancelTip.setVisible(false);
            actorAnchorPane.setOpacity(0.2);
            trashButton.setImage(ImageCache.getImageFromLocalPath("/img/refresh1.png"));
            return;
        }
        removeTip.setVisible(false);
        trashButton.setImage(ImageCache.getImageFromLocalPath("/img/delete_elem1.png"));
    }

    /**************************/
    public ActorPreviewController() {
    }


    public void setData(AnchorPane mainPage, Author author) {
        this.actorType = "Author";
        this.authorPreview = author;
        this.mainPage = mainPage;
        this.visitorType = MyPodcastDB.getInstance().getSessionType();
        if(visitorType.equals("User"))
            visitorName = ((User)MyPodcastDB.getInstance().getSessionActor()).getUsername();

        this.actorImage.setImage(ImageCache.getImageFromLocalPath(author.getPicturePath()));
        this.actorName.setText(author.getName());
        this.actorToolTip.setText(author.getName());

        followButtonArea.setVisible(false);
        if(visitorType.equals("User"))
            trashButtonArea.setVisible(true);
        else
            trashButtonArea.setVisible(false);

        followTip.setVisible(false);
        removeTip.setVisible(false);
        cancelTip.setVisible(false);

    }
    public void setData(AnchorPane mainPage, Author author, boolean isFollowed) {
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
        this.actorToolTip.setText(author.getName());

        followButtonArea.setVisible(true);
        trashButtonArea.setVisible(false);
        if(isFollowed){
            followButton.setImage(ImageCache.getImageFromLocalPath("/img/Favorite_64px.png"));
            followStatus = true;
        }

        followTip.setVisible(false);
        removeTip.setVisible(false);
        cancelTip.setVisible(false);
    }
    public void setData(AnchorPane mainPage, User user) {
        this.actorType = "User";
        this.userPreview = user;
        this.mainPage = mainPage;
        this.visitorType = MyPodcastDB.getInstance().getSessionType();
        if(visitorType.equals("User"))
            this.visitorName = ((User)MyPodcastDB.getInstance().getSessionActor()).getUsername();

        this.actorImage.setImage(ImageCache.getImageFromLocalPath(user.getPicturePath()));
        this.actorName.setText(user.getUsername());
        this.actorToolTip.setText(user.getUsername());

        followButtonArea.setVisible(false);
        if(visitorType.equals("User"))
            trashButtonArea.setVisible(true);
        else
            trashButtonArea.setVisible(false);

        followTip.setVisible(false);
        removeTip.setVisible(false);
        cancelTip.setVisible(false);
    }

    public void setData(AnchorPane mainPage, User user, boolean isFollowed) {
        this.actorType = "User";
        this.userPreview = user;
        this.visitorType = "User";
        this.visitorName = ((User)MyPodcastDB.getInstance().getSessionActor()).getUsername();;
        this.mainPage = mainPage;

        this.actorImage.setImage(ImageCache.getImageFromLocalPath(user.getPicturePath()));
        this.actorName.setText(user.getUsername());
        this.actorToolTip.setText(user.getUsername());

        followButtonArea.setVisible(true);
        trashButtonArea.setVisible(false);
        if(isFollowed){
            followButton.setImage(ImageCache.getImageFromLocalPath("/img/Favorite_64px.png"));
            followStatus = true;
        }

        followTip.setVisible(false);
        removeTip.setVisible(false);
        cancelTip.setVisible(false);
    }



}
