package it.unipi.dii.lsmsdb.myPodcastDB.controller;

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

    private Author authorPreview;
    private User userPreview;
    private String actorType;
    private AnchorPane mainPage;
    private String visitorType;
    private String visitorName;
    private boolean followStatus = false;
    private boolean blockClickEvent = false;

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
        if(followStatus)
            followButton.setImage(ImageCache.getImageFromLocalPath("/img/following_30px.png"));
        else
            followButton.setImage(ImageCache.getImageFromLocalPath("/img/Favorite_64px.png"));
    }

    @FXML
    void followOut(MouseEvent event){
        if(!followStatus)
            followButton.setImage(ImageCache.getImageFromLocalPath("/img/following_30px.png"));
        else
            followButton.setImage(ImageCache.getImageFromLocalPath("/img/Favorite_64px.png"));
    }

    @FXML
    void  followClick(MouseEvent event){
        Logger.info("Like button clicked");
        blockClickEvent = true;
        UserPageService service = new UserPageService();
        int res = -1;
        if(actorType.equals("User")) {
            if (followStatus) {
                res = service.updateFollowedUser(visitorName, actorName.getText(), false);
                if (res == 0) {
                    followButton.setImage(ImageCache.getImageFromLocalPath("/img/following_30px.png"));
                    followStatus = false;
                }
            } else {
                res = service.updateFollowedUser(visitorName, actorName.getText(), true);
                if (res == 0) {
                    followButton.setImage(ImageCache.getImageFromLocalPath("/img/Favorite_64px.png"));
                    followStatus = true;
                }
            }
        }
        if(actorType.equals("Author")){
            if (followStatus) {
                res = service.updateFollowedAuthor(visitorName, visitorType, actorName.getText(), false);
                if (res == 0) {
                    followButton.setImage(ImageCache.getImageFromLocalPath("/img/following_30px.png"));
                    followStatus = false;
                }
            } else {
                res = service.updateFollowedAuthor(visitorName, visitorType, actorName.getText(), true);
                if (res == 0) {
                    followButton.setImage(ImageCache.getImageFromLocalPath("/img/Favorite_64px.png"));
                    followStatus = true;
                }
            }
        }
        String logMsg = "";
        String dialogMsg = "";
        switch(res){
            case 0:
                Logger.success("Updating liked success");
                break;
            case 1:
                logMsg = "Visitor account not exists on Neo4j";
                dialogMsg = "Your account not exists";
                break;
            case 2:
                logMsg = "Actor not found in neo4j";
                dialogMsg = "Account not found";
                break;
            case 3:
                logMsg = "Follow relation already exists";
                dialogMsg = "it's already followed";
                break;
            case 4:
                logMsg = "Adding follow relation failed";
                dialogMsg = "Operation failed";
                break;
            case 5:
                logMsg = "Follow relation already not exists";
                dialogMsg = "it's not followed";
                break;
            case 6:
                logMsg = "Removing follow relation failed";
                dialogMsg = "Operation failed";
                break;
            case -1:
                logMsg = "Unknown error";
                dialogMsg = "Unknown error";
        }
        if(res != 0){
            Logger.error(logMsg);
            DialogManager.getInstance().createErrorAlert(mainPage, dialogMsg);
        }
    }

    @FXML
    void trashClick(MouseEvent event){
        Logger.info("Trash button clicked");
        blockClickEvent = true;
    }

    @FXML
    void trashIn(MouseEvent event){
        trashButton.setImage(ImageCache.getImageFromLocalPath("/img/delete_elem2.png"));
    }

    @FXML
    void trashOut(MouseEvent event){
        trashButton.setImage(ImageCache.getImageFromLocalPath("/img/delete_elem1.png"));
    }

    /**************************/
    public ActorPreviewController() {
    }


    public void setData(AnchorPane mainPage, Author author, String visitorType) {
        this.actorType = "Author";
        this.authorPreview = author;
        this.mainPage = mainPage;

        Image image = ImageCache.getImageFromLocalPath(author.getPicturePath());
        this.actorImage.setImage(image);
        this.actorName.setText(author.getName());
        this.actorToolTip.setText(author.getName());

        followButtonArea.setVisible(false);
        if(!visitorType.equals("Admin"))
            trashButtonArea.setVisible(true);
        else
            trashButtonArea.setVisible(false);

    }
    public void setData(AnchorPane mainPage, Author author, String visitorType, String visitorName, boolean isFollowed) {
        this.actorType = "Author";
        this.visitorType = visitorType;
        this.visitorName = visitorName;
        this.authorPreview = author;
        this.mainPage = mainPage;

        Image image = ImageCache.getImageFromLocalPath(author.getPicturePath());
        this.actorImage.setImage(image);
        this.actorName.setText(author.getName());
        this.actorToolTip.setText(author.getName());

        followButtonArea.setVisible(true);
        trashButtonArea.setVisible(false);
        if(isFollowed){
            followButton.setImage(ImageCache.getImageFromLocalPath("/img/Favorite_64px.png"));
            followStatus = true;
        }
    }
    public void setData(AnchorPane mainPage, User user, String visitorType) {
        this.actorType = "User";
        this.userPreview = user;
        this.mainPage = mainPage;

        Image image = ImageCache.getImageFromLocalPath(user.getPicturePath());
        this.actorImage.setImage(image);
        this.actorName.setText(user.getUsername());
        this.actorToolTip.setText(user.getUsername());

        followButtonArea.setVisible(false);
        if(!visitorType.equals("Admin"))
            trashButtonArea.setVisible(true);
        else
            trashButtonArea.setVisible(false);
    }

    public void setData(AnchorPane mainPage, User user, String visitorName, boolean isFollowed) {
        this.actorType = "User";
        this.userPreview = user;
        this.visitorType = "User";
        this.visitorName = visitorName;
        this.mainPage = mainPage;

        Image image = ImageCache.getImageFromLocalPath(user.getPicturePath());
        this.actorImage.setImage(image);
        this.actorName.setText(user.getUsername());
        this.actorToolTip.setText(user.getUsername());

        followButtonArea.setVisible(true);
        trashButtonArea.setVisible(false);
        if(isFollowed){
            followButton.setImage(ImageCache.getImageFromLocalPath("/img/Favorite_64px.png"));
            followStatus = true;
        }
    }



}
