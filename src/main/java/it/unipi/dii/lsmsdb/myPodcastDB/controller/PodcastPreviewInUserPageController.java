package it.unipi.dii.lsmsdb.myPodcastDB.controller;
import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.service.UserPageService;
import it.unipi.dii.lsmsdb.myPodcastDB.cache.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.DialogManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.io.IOException;

public class PodcastPreviewInUserPageController {

    @FXML
    private  AnchorPane podcastAnchorPane;
    @FXML
    private ImageView podcastImage;

    @FXML
    private Label PodcastName;

    @FXML
    private VBox podcastContainer;

    @FXML
    private ImageView watchlistButton;

    @FXML
    private HBox buttonArea;

    @FXML
    private ImageView trashButton;

    @FXML
    private Pane trashButtonArea;


    private Podcast podcastPreview;
    private String actorName = "";
    private String actorType = "";
    boolean watchStatus = false;
    boolean blockClickEvent = false;
    private AnchorPane mainPage;
    private boolean disableClick = false;
    private String listType = "";
    private boolean visitorMode;

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
        if(actorType.equals("Author"))
            return;
        if(!visitorMode) {
            trashButtonArea.setVisible(true);
            trashButtonArea.setOpacity(0.0);
            FadeTransition fadeAuthorImage = new FadeTransition(Duration.seconds(0.2), trashButtonArea);
            fadeAuthorImage.setFromValue(0.0);
            fadeAuthorImage.setToValue(1.0);
            fadeAuthorImage.play();
        }
        else if(!watchStatus && actorType.equals("User")) {
            buttonArea.setVisible(true);
            buttonArea.setOpacity(0.0);
            FadeTransition fadeAuthorImage = new FadeTransition(Duration.seconds(0.2), buttonArea);
            fadeAuthorImage.setFromValue(0.0);
            fadeAuthorImage.setToValue(1.0);
            fadeAuthorImage.play();
        }
    }

    @FXML
    void podcastOut(MouseEvent event) {
        podcastContainer.setStyle("-fx-background-color: transparent;");
        if(actorType.equals("Author"))
            return;
        if(!visitorMode)
            trashButtonArea.setVisible(false);
        else if(!watchStatus && actorType.equals("User"))
            buttonArea.setVisible(false);
    }

    @FXML
    void watchClick(MouseEvent event){

        blockClickEvent = true;
        Logger.info("Watch button clicked");
        UserPageService service = new UserPageService();
        int res;
        if(watchStatus) {
            res = service.updateWatchlist(actorName, podcastPreview, false);
            if(res == 0){
                Logger.success("Removed watch later relation successfully");
                watchlistButton.setImage(ImageCache.getImageFromLocalPath("/img/star_64px.png"));
                watchStatus = false;
            }
            else if(res == 4)
                Logger.error("Watch later relation already not exists");
            else if(res == 5){
                Logger.error("Removing watch later relation failed");
                DialogManager.getInstance().createErrorAlert(mainPage, "Something went wrong");
            }
            else{
                Logger.error("Unknown error");
                DialogManager.getInstance().createErrorAlert(mainPage, "Something went wrong");
            }

        }
        else{
            res = service.updateWatchlist(actorName, podcastPreview, true);
            if(res == 0) {
                Logger.success("Added watch later relation successfully");
                watchlistButton.setImage(ImageCache.getImageFromLocalPath("/img/star_64fillpx.png"));
                watchStatus = true;
            }
            else if(res == 1)
                Logger.error("Watch later relation already exists");
            else if(res == 2) {
                Logger.error("Watchlist is full");
                DialogManager.getInstance().createErrorAlert(mainPage, "Your watchlist is full");
            }
            else if(res == 3){
                Logger.error("Adding watch later relation failed");
                DialogManager.getInstance().createErrorAlert(mainPage, "Something went wrong");
            }
            else{
                Logger.error("Unknown error");
                DialogManager.getInstance().createErrorAlert(mainPage, "Something went wrong");
            }
        }

    }

    @FXML
    void watchIn(MouseEvent event){
        if(watchStatus)
            watchlistButton.setImage(ImageCache.getImageFromLocalPath("/img/star_64px.png"));
        else
            watchlistButton.setImage(ImageCache.getImageFromLocalPath("/img/star_64fillpx.png"));
    }

    @FXML
    void watchOut(MouseEvent event){
        if(!watchStatus)
            watchlistButton.setImage(ImageCache.getImageFromLocalPath("/img/star_64px.png"));
        else
            watchlistButton.setImage(ImageCache.getImageFromLocalPath("/img/star_64fillpx.png"));
    }

    @FXML
    void trashClick(MouseEvent event){

        if(!disableClick){
            Logger.info("Trash button clicked");
            UserPageService service = new UserPageService();
            int res = -1;
            if(listType.equals("watchlist")) {
                res = service.updateWatchlist(actorName, podcastPreview, false);
                if (res == 0) {
                    Logger.success("Removed watch later relation successfully");
                } else if (res == 4)
                    Logger.error("Watch later relation already not exists");
                else if (res == 5) {
                    Logger.error("Removing watch later relation failed");
                    DialogManager.getInstance().createErrorAlert(mainPage, "Something went wrong");
                    return;
                } else {
                    Logger.error("Unknown error");
                    DialogManager.getInstance().createErrorAlert(mainPage, "Something went wrong");
                    return;
                }
            }
            else if(listType.equals("liked")) {
                res = service.updateLiked(actorName, podcastPreview, false);
                if(res==0)
                    Logger.success("Removed like relation successfully");
                else if(res == 3)
                    Logger.error("Like relation already not exists");
                else if(res == 4){
                    Logger.error("Removing like relation failed");
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
            podcastAnchorPane.setOpacity(0.2);
            trashButton.setImage(ImageCache.getImageFromLocalPath("/img/refresh1.png"));
            trashButton.setImage(ImageCache.getImageFromLocalPath("/img/refresh1.png"));


        }
        else{
            Logger.info("Refresh button clicked");
            UserPageService service = new UserPageService();
            int res = -1;
            if(listType.equals("watchlist")) {
                res = service.updateWatchlist(actorName, podcastPreview, true);
                if (res == 0) {
                    Logger.success("Added watch later relation successfully");
                } else if (res == 1)
                    Logger.error("Watch later relation already exists");
                else if (res == 2) {
                    Logger.error("Watchlist is full");
                    DialogManager.getInstance().createErrorAlert(mainPage, "Your watchlist is full");
                    return;
                }
                else if (res == 3) {
                    Logger.error("Adding watch later relation failed");
                    DialogManager.getInstance().createErrorAlert(mainPage, "Something went wrong");
                    return;
                } else {
                    Logger.error("Unknown error");
                    DialogManager.getInstance().createErrorAlert(mainPage, "Something went wrong");
                    return;
                }
            }
            else if(listType.equals("liked")) {
                res = service.updateLiked(actorName, podcastPreview, true);
                if(res == 0)
                    Logger.success("Added like relation successfully");
                else if(res == 1)
                    Logger.error("Like relation already exists");
                else if(res == 2){
                    Logger.error("Adding like relation failed");
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
            podcastAnchorPane.setOpacity(1.0);
            trashButton.setImage(ImageCache.getImageFromLocalPath("/img/delete_elem1.png"));

        }
    }

    @FXML
    void trashIn(MouseEvent event){

        if(disableClick) {
            podcastAnchorPane.setOpacity(1.0);
            trashButton.setImage(ImageCache.getImageFromLocalPath("/img/refresh2.png"));
            return;
        }
        trashButton.setImage(ImageCache.getImageFromLocalPath("/img/delete_elem2.png"));
    }

    @FXML
    void trashOut(MouseEvent event){
        if(disableClick) {
            podcastAnchorPane.setOpacity(0.2);
            trashButton.setImage(ImageCache.getImageFromLocalPath("/img/refresh1.png"));
            return;
        }
        trashButton.setImage(ImageCache.getImageFromLocalPath("/img/delete_elem1.png"));
    }
    /************************/

    public void setData(AnchorPane mainPage, String listType, Podcast podcast) {
        this.mainPage = mainPage;
        this.podcastPreview = podcast;
        this.listType = listType;
        this.actorType = MyPodcastDB.getInstance().getSessionType();
        if(actorType.equals("User")) {
            this.visitorMode = false;
            this.actorName = ((User) MyPodcastDB.getInstance().getSessionActor()).getUsername();
        }
        else
            this.visitorMode = true;

        Image image = ImageCache.getImageFromLocalPath("/img/logo.png");
        this.podcastImage.setImage(image);
        this.PodcastName.setText(podcast.getName());

        Platform.runLater(() -> {
            Image imageLoaded = ImageCache.getImageFromURL(podcast.getArtworkUrl600());
            this.podcastImage.setImage(imageLoaded);
        });

       buttonArea.setVisible(false);
       trashButtonArea.setVisible(false);

    }

    public void setData(AnchorPane mainPage, Podcast podcast, boolean ifInWatchlist) {
        this.visitorMode = true;
        this.mainPage = mainPage;
        this.podcastPreview = podcast;
        this.actorType = "User";
        Image image = ImageCache.getImageFromLocalPath("/img/loading.jpg");
        this.podcastImage.setImage(image);
        this.PodcastName.setText(podcast.getName());

        Platform.runLater(() -> {
            Image imageLoaded = ImageCache.getImageFromURL(podcast.getArtworkUrl600());
            this.podcastImage.setImage(imageLoaded);
        });

        buttonArea.setVisible(false);
        trashButtonArea.setVisible(false);
        this.actorName = ((User)MyPodcastDB.getInstance().getSessionActor()).getUsername();
        if(ifInWatchlist) {
            watchlistButton.setImage(ImageCache.getImageFromLocalPath("/img/star_64fillpx.png"));
            watchStatus = true;
        }
        if(ifInWatchlist)
            buttonArea.setVisible(true);

    }


}
