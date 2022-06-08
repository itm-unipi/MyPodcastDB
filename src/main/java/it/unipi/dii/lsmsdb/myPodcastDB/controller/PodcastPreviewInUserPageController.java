package it.unipi.dii.lsmsdb.myPodcastDB.controller;
import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Admin;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.service.UserPageService;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.DialogManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.io.IOException;

public class PodcastPreviewInUserPageController {

    @FXML
    private  AnchorPane podcastAnchorPane;
    @FXML
    private ImageView podcastImage;

    @FXML
    private Label PodcastName;

    private Podcast podcastPreview;

    @FXML
    private Tooltip podcastToolTip;

    @FXML
    private VBox podcastContainer;

    @FXML
    private ImageView likeButton;

    @FXML
    private ImageView watchlistButton;

    @FXML
    private HBox buttonArea;

    @FXML
    private ImageView trashButton;

    @FXML
    private Pane trashButtonArea;

    @FXML
    private Label likeTip;

    @FXML
    private Label watchTip;

    @FXML
    private Label removeTip;

    @FXML
    private Label cancelTip;

    private String actorName = "";
    private String actorType = "";
    boolean likeStatus = false;
    boolean watchStatus = false;
    boolean blockClickEvent = false;
    private AnchorPane mainPage;
    private boolean disableClick = false;
    private String listType = "";

    @FXML
    void onClick(MouseEvent event) throws IOException {
        if(disableClick)
            return;
        if(blockClickEvent){
            blockClickEvent = false;
            return;
        }
        Logger.info(podcastPreview.getId() + " : " + this.podcastPreview.getName() + " selected");
        StageManager.showPage(ViewNavigator.PODCASTPAGE.getPage(), this.podcastPreview.getId());
    }

    @FXML
    void podcastIn(MouseEvent event) {
        podcastContainer.setStyle("-fx-background-color: #E5E5E5; -fx-background-radius: 10;");
    }

    @FXML
    void podcastOut(MouseEvent event) {
        podcastContainer.setStyle("-fx-background-color: transparent;");
    }

    @FXML
    void watchClick(MouseEvent event){

        likeTip.setVisible(false);
        watchTip.setVisible(false);
        removeTip.setVisible(false);
        cancelTip.setVisible(false);

        blockClickEvent = true;
        Logger.info("Watch button clicked");
        UserPageService service = new UserPageService();
        int res = -1;
        if(watchStatus) {
            res = service.updateWatchlist(actorName, podcastPreview.getId(), false);
            if(res == 0){
                watchlistButton.setImage(ImageCache.getImageFromLocalPath("/img/star_64px.png"));
                watchStatus = false;
            }
        }
        else{
            res = service.updateWatchlist(actorName, podcastPreview.getId(), true);
            if(res == 0) {
                watchlistButton.setImage(ImageCache.getImageFromLocalPath("/img/star_64fillpx.png"));
                watchStatus = true;
            }
        }

        String logMsg = "";
        String dialogMsg = "";
        switch(res){
            case 0:
                Logger.success("Updating watchlist success");
                break;
            case 1:
                logMsg = "User account not exists on Neo4j";
                dialogMsg = "Your account not exists";
                break;
            case 2:
                logMsg = "Podcast not found in neo4j";
                dialogMsg = "Podcast not found";
                break;
            case 3:
                logMsg = "LIKES relation already exists";
                dialogMsg = "it's already liked";
                break;
            case 4:
                logMsg = "Adding likes relation failed";
                dialogMsg = "Operation failed";
                break;
            case 5:
                logMsg = "LIKES relation already not exists";
                dialogMsg = "it's not liked";
                break;
            case 6:
                logMsg = "Removing likes relation failed";
                dialogMsg = "Operation failed";
                break;
            case -1:
                logMsg = "Unknown error";
                dialogMsg = "Unknown error";
                break;
        }
        if(res != 0){
            Logger.error(logMsg);
            DialogManager.getInstance().createErrorAlert(mainPage, dialogMsg);
        }
    }

    @FXML
    void watchIn(MouseEvent event){
        watchTip.setVisible(true);
        if(watchStatus) {
            watchTip.setText("UnWatch");
            watchlistButton.setImage(ImageCache.getImageFromLocalPath("/img/star_64px.png"));
        }
        else {
            watchTip.setText("Watch later");
            watchlistButton.setImage(ImageCache.getImageFromLocalPath("/img/star_64fillpx.png"));
        }
    }

    @FXML
    void watchOut(MouseEvent event){
        watchTip.setVisible(false);
        if(!watchStatus)
            watchlistButton.setImage(ImageCache.getImageFromLocalPath("/img/star_64px.png"));
        else
            watchlistButton.setImage(ImageCache.getImageFromLocalPath("/img/star_64fillpx.png"));
    }

    @FXML
    void likeClick(MouseEvent event){

        likeTip.setVisible(false);
        watchTip.setVisible(false);
        removeTip.setVisible(false);
        cancelTip.setVisible(false);

        Logger.info("Like button clicked");
        blockClickEvent = true;
        UserPageService service = new UserPageService();
        int res = -1;
        if(likeStatus) {
            res = service.updateLiked(actorName, podcastPreview.getId(), false);
            if(res==0) {
                likeButton.setImage(ImageCache.getImageFromLocalPath("/img/following_30px.png"));
                likeStatus = false;
            }
        }
        else{
            res = service.updateLiked(actorName, podcastPreview.getId(), true);
            if(res == 0){
                likeButton.setImage(ImageCache.getImageFromLocalPath("/img/Favorite_64px.png"));
                likeStatus = true;
            }
        }
        String logMsg = "";
        String dialogMsg = "";
        switch(res){
            case 0:
                Logger.success("Updating liked success");
                break;
            case 1:
                logMsg = "User account not exists on Neo4j";
                dialogMsg = "Your account not exists";
                break;
            case 2:
                logMsg = "Podcast not found in neo4j";
                dialogMsg = "Podcast not found";
                break;
            case 3:
                logMsg = "WATCH_LATER relation already exists";
                dialogMsg = "it's already in your watchlist";
                break;
            case 4:
                logMsg = "Adding watch later relation failed";
                dialogMsg = "Operation failed";
                break;
            case 5:
                logMsg = "WATCH_LATER relation already not exists";
                dialogMsg = "it's not in your watchlist";
                break;
            case 6:
                logMsg = "Removing waatch later relation failed";
                dialogMsg = "Operation failed";
                break;
            case -1:
                logMsg = "Unknown error";
                dialogMsg = "Unknown error";
                break;
        }
        if(res != 0){
            Logger.error(logMsg);
            DialogManager.getInstance().createErrorAlert(mainPage, dialogMsg);
        }

    }

    @FXML
    void likeIn(MouseEvent event){
        likeTip.setVisible(true);
        if(likeStatus) {
            likeTip.setText("Dislike");
            likeButton.setImage(ImageCache.getImageFromLocalPath("/img/following_30px.png"));
        }
        else {
            likeTip.setText("Like");
            likeButton.setImage(ImageCache.getImageFromLocalPath("/img/Favorite_64px.png"));
        }
    }

    @FXML
    void likeOut(MouseEvent event){
        likeTip.setVisible(false);
        if(!likeStatus)
            likeButton.setImage(ImageCache.getImageFromLocalPath("/img/following_30px.png"));
        else
            likeButton.setImage(ImageCache.getImageFromLocalPath("/img/Favorite_64px.png"));
    }

    @FXML
    void trashClick(MouseEvent event){

        likeTip.setVisible(false);
        watchTip.setVisible(false);
        removeTip.setVisible(false);
        cancelTip.setVisible(false);

        if(!disableClick){
            Logger.info("Trash button clicked");
            UserPageService service = new UserPageService();
            int res = -1;
            if(listType.equals("watchlist"))
                res = service.updateWatchlist(actorName, podcastPreview.getId(), false);
            else if(listType.equals("liked"))
                res = service.updateLiked(actorName, podcastPreview.getId(), false);

            String logMsg = "";
            String dialogMsg = "";
            switch(res){
                case 0:
                    Logger.success("Updating " + listType + " success");
                    break;
                case 1:
                    logMsg = "User account not exists on Neo4j";
                    dialogMsg = "Your account not exists";
                    break;
                case 2:
                    logMsg = "Podcast not found in neo4j";
                    dialogMsg = "Podcast not found";
                    break;
                case 3:
                    logMsg = "It's already in the " + listType + " list";
                    dialogMsg = "it's already in your " + listType;
                    break;
                case 4:
                    logMsg = "Adding in " + listType + " failed";
                    dialogMsg = "Operation failed";
                    break;
                case 5:
                    logMsg = "It's not exits in " + listType + " list";
                    dialogMsg = "it's not in your " + listType;
                    break;
                case 6:
                    logMsg = "Removing from " + listType + " failed";
                    dialogMsg = "Operation failed";
                    break;
                case -1:
                    logMsg = "Unknown error";
                    dialogMsg = "Unknown error";
                    break;
            }
            if(res != 0){
                Logger.error(logMsg);
                DialogManager.getInstance().createErrorAlert(mainPage, dialogMsg);
                return;
            }
            blockClickEvent = true;
            disableClick = true;
            podcastAnchorPane.setOpacity(0.2);
            trashButton.setImage(ImageCache.getImageFromLocalPath("/img/refresh1.png"));
            trashButton.setImage(ImageCache.getImageFromLocalPath("/img/refresh1.png"));


        }
        else{
            Logger.info("Refresh button clicked");
            UserPageService service = new UserPageService();
            int res = -1;
            if(listType.equals("watchlist"))
                res = service.updateWatchlist(actorName, podcastPreview.getId(), true);
            else if(listType.equals("liked"))
                res = service.updateLiked(actorName, podcastPreview.getId(), true);

            String logMsg = "";
            String dialogMsg = "";
            switch(res){
                case 0:
                    Logger.success("Updating " + listType + " success");
                    break;
                case 1:
                    logMsg = "User account not exists on Neo4j";
                    dialogMsg = "Your account not exists";
                    break;
                case 2:
                    logMsg = "Podcast not found in neo4j";
                    dialogMsg = "Podcast not found";
                    break;
                case 3:
                    logMsg = "It's already in the " + listType + " list";
                    dialogMsg = "it's already in your " + listType;
                    break;
                case 4:
                    logMsg = "Adding in " + listType + " failed";
                    dialogMsg = "Operation failed";
                    break;
                case 5:
                    logMsg = "It's not exits in " + listType + " list";
                    dialogMsg = "it's not in your " + listType;
                    break;
                case 6:
                    logMsg = "Removing from " + listType + " failed";
                    dialogMsg = "Operation failed";
                    break;
                case -1:
                    logMsg = "Unknown error";
                    dialogMsg = "Unknown error";
                    break;
            }
            if(res != 0){
                Logger.error(logMsg);
                DialogManager.getInstance().createErrorAlert(mainPage, dialogMsg);
                return;
            }
            blockClickEvent = true;
            disableClick = false;
            podcastAnchorPane.setOpacity(1.0);
            trashButton.setImage(ImageCache.getImageFromLocalPath("/img/delete_elem1.png"));

        }
    }

    @FXML
    void trashIn(MouseEvent event){

        if(disableClick) {
            cancelTip.setVisible(true);
            podcastAnchorPane.setOpacity(1.0);
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
            podcastAnchorPane.setOpacity(0.2);
            trashButton.setImage(ImageCache.getImageFromLocalPath("/img/refresh1.png"));
            return;
        }
        removeTip.setVisible(false);
        trashButton.setImage(ImageCache.getImageFromLocalPath("/img/delete_elem1.png"));
    }
    /************************/

    public void setData(AnchorPane mainPage, String listType, Podcast podcast) {
        this.mainPage = mainPage;
        this.podcastPreview = podcast;
        this.listType = listType;
        this.actorType = MyPodcastDB.getInstance().getSessionType();
        if(actorType.equals("User"))
            this.actorName = ((User)MyPodcastDB.getInstance().getSessionActor()).getUsername();

        Image image = ImageCache.getImageFromLocalPath("/img/logo.png");
        this.podcastImage.setImage(image);
        this.PodcastName.setText(podcast.getName());
        this.podcastToolTip.setText(podcast.getName());

        Platform.runLater(() -> {
            Image imageLoaded = ImageCache.getImageFromURL(podcast.getArtworkUrl600());
            this.podcastImage.setImage(imageLoaded);
        });

       buttonArea.setVisible(false);
       if(actorType.equals("User"))
           trashButtonArea.setVisible(true);
       else
           trashButtonArea.setVisible(false);

        likeTip.setVisible(false);
        watchTip.setVisible(false);
        removeTip.setVisible(false);
        cancelTip.setVisible(false);

    }

    public void setData(AnchorPane mainPage, Podcast podcast, boolean ifInWatchlist, boolean ifLiked) {
        this.mainPage = mainPage;
        this.podcastPreview = podcast;

        Image image = ImageCache.getImageFromLocalPath("/img/logo.png");
        this.podcastImage.setImage(image);
        this.PodcastName.setText(podcast.getName());
        this.podcastToolTip.setText(podcast.getName());

        Platform.runLater(() -> {
            Image imageLoaded = ImageCache.getImageFromURL(podcast.getArtworkUrl600());
            this.podcastImage.setImage(imageLoaded);
        });

        buttonArea.setVisible(true);
        trashButtonArea.setVisible(false);
        this.actorName = ((User)MyPodcastDB.getInstance().getSessionActor()).getUsername();
        if(ifInWatchlist) {
            watchlistButton.setImage(ImageCache.getImageFromLocalPath("/img/star_64fillpx.png"));
            watchStatus = true;
        }
        if(ifLiked) {
            likeButton.setImage(ImageCache.getImageFromLocalPath("/img/Favorite_64px.png"));
            likeStatus = true;
        }

        likeTip.setVisible(false);
        watchTip.setVisible(false);
        removeTip.setVisible(false);
        cancelTip.setVisible(false);

    }


}
