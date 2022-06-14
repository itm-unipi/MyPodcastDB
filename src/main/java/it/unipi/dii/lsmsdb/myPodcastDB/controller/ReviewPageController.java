package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.*;
import it.unipi.dii.lsmsdb.myPodcastDB.service.ReviewPageService;
import it.unipi.dii.lsmsdb.myPodcastDB.cache.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.DialogManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.*;

public class ReviewPageController {

    @FXML
    private ComboBox<String> ascending;

    @FXML
    private Label author;

    @FXML
    private Label category;

    @FXML
    private ProgressBar fiveStars;

    @FXML
    private ProgressBar fourStars;

    @FXML
    private VBox gridWrapper;

    @FXML
    private ImageView home;

    @FXML
    private ImageView logout;

    @FXML
    private BorderPane mainPage;

    @FXML
    private Label noReviewsMessage;

    @FXML
    private Label numReviews;

    @FXML
    private ProgressBar oneStar;

    @FXML
    private ComboBox<String> orderBy;

    @FXML
    private ImageView podcastImage;

    @FXML
    private Label rating;

    @FXML
    private Button reload;

    @FXML
    private VBox reviewForm;

    @FXML
    private GridPane reviewGrid;

    @FXML
    private ScrollPane scroll;

    @FXML
    private TextField searchBarText;

    @FXML
    private ImageView searchButton;

    @FXML
    private Label showMore;

    @FXML
    private VBox showMoreWrapper;


    @FXML
    private ImageView star1;

    @FXML
    private ImageView star2;

    @FXML
    private ImageView star3;

    @FXML
    private ImageView star4;

    @FXML
    private ImageView star5;

    @FXML
    private Button submit;

    @FXML
    private TextArea textContent;

    @FXML
    private TextField textTitle;

    @FXML
    private ProgressBar threeStars;

    @FXML
    private Label title;

    @FXML
    private Tooltip titleTooltip;

    @FXML
    private ProgressBar twoStars;

    @FXML
    private ImageView userPicture;

    @FXML
    private VBox userPictureWrapper;

    private Review ownReview;
    private Podcast podcast;
    private List<Review> loadedReviews;
    private ReviewPageService service;

    private int row;
    private int column;
    private String selectedOrder;
    private Boolean selectedAscending;
    private int limitPerQuery;

    /**************************** Click and Enter Events ****************************/

    @FXML
    void clickOnAuthor(MouseEvent event) throws IOException {
        Logger.info("Click on author : " + this.podcast.getAuthorName());
        StageManager.showPage(ViewNavigator.AUTHORPROFILE.getPage(), this.podcast.getAuthorName());
    }

    @FXML
    void clickOnFifthStar(MouseEvent event) {
        Image star = ImageCache.getImageFromLocalPath("/img/star.png");

        this.star1.setImage(star);
        this.star2.setImage(star);
        this.star3.setImage(star);
        this.star4.setImage(star);
        this.star5.setImage(star);
        this.ownReview.setRating(5);

        Logger.info("Rating set to " + this.ownReview.getRating());
    }

    @FXML
    void clickOnFirstStar(MouseEvent event) {
        Image star = ImageCache.getImageFromLocalPath("/img/star.png");
        Image outlineStar = ImageCache.getImageFromLocalPath("/img/outline_star.png");

        this.star1.setImage(star);
        this.star2.setImage(outlineStar);
        this.star3.setImage(outlineStar);
        this.star4.setImage(outlineStar);
        this.star5.setImage(outlineStar);
        this.ownReview.setRating(1);

        Logger.info("Rating set to " + this.ownReview.getRating());
    }

    @FXML
    void clickOnFourthStar(MouseEvent event) {
        Image star = ImageCache.getImageFromLocalPath("/img/star.png");
        Image outlineStar = ImageCache.getImageFromLocalPath("/img/outline_star.png");

        this.star1.setImage(star);
        this.star2.setImage(star);
        this.star3.setImage(star);
        this.star4.setImage(star);
        this.star5.setImage(outlineStar);
        this.ownReview.setRating(4);

        Logger.info("Rating set to " + this.ownReview.getRating());
    }

    @FXML
    void clickOnHome(MouseEvent event) throws IOException {
        StageManager.showPage(ViewNavigator.HOMEPAGE.getPage());
    }

    @FXML
    void clickOnLogout(MouseEvent event) throws IOException {
        StageManager.showPage(ViewNavigator.LOGIN.getPage());
    }

    @FXML
    void clickOnReload(MouseEvent event) throws IOException {
        if (this.orderBy.getValue() == null || this.ascending.getValue() == null) {
            DialogManager.getInstance().createErrorAlert(this.mainPage, "Select order by and ascending field");
        } else {
            // reload local list with new parameters
            this.loadedReviews.clear();                         // empty old list
            this.selectedOrder = this.orderBy.getValue().equals("Date of creation") ? "createdAt" : "rating";
            this.selectedAscending = this.ascending.getValue().equals("Ascending");
            Boolean result = this.service.loadOtherReview(this.podcast, this.ownReview, this.loadedReviews, this.loadedReviews.size(), this.limitPerQuery, this.selectedOrder, this.selectedAscending);

            // if successful reload page
            if (!result) {
                DialogManager.getInstance().createErrorAlert(this.mainPage, "Failed to load reviews");
            } else {
                this.reloadReviewList();
            }
        }
    }

    @FXML
    void clickOnSearch(MouseEvent event) throws IOException {
        if (!this.searchBarText.getText().equals("")) {
            String searchString = this.searchBarText.getText();
            StageManager.showPage(ViewNavigator.SEARCH.getPage(), searchString);
        }
    }

    @FXML
    void clickOnSecondStar(MouseEvent event) {
        Image star = ImageCache.getImageFromLocalPath("/img/star.png");
        Image outlineStar = ImageCache.getImageFromLocalPath("/img/outline_star.png");

        this.star1.setImage(star);
        this.star2.setImage(star);
        this.star3.setImage(outlineStar);
        this.star4.setImage(outlineStar);
        this.star5.setImage(outlineStar);
        this.ownReview.setRating(2);

        Logger.info("Rating set to " + this.ownReview.getRating());
    }

    @FXML
    void clickOnShow(MouseEvent event) throws IOException {
        // load other reviews
        Boolean result = this.service.loadOtherReview(this.podcast, this.ownReview, this.loadedReviews, this.loadedReviews.size(), this.limitPerQuery, this.selectedOrder, this.selectedAscending);

        // if succesfull update the page
        if (!result) {
            DialogManager.getInstance().createErrorAlert(this.mainPage, "Failed to load other reviews");
        } else {
            // add new review to list
            this.reloadReviewList();

            // check if are finished
            if (this.getLoaded() == this.podcast.getReviews().size()) {
                this.showMoreWrapper.setVisible(false);
                this.showMoreWrapper.setStyle("-fx-min-width: 0; -fx-pref-width: 0; -fx-max-width: 0; -fx-min-height: 0; -fx-pref-height: 0; -fx-max-height: 0; -fx-padding: 0; -fx-margin: 0;");
            }
        }
    }

    @FXML
    void clickOnSubmit(MouseEvent event) throws IOException {
        // get the text
        String title = this.textTitle.getText();
        String content = this.textContent.getText();

        // check if there are empty fields
        if (this.ownReview.getRating() == 0 || title.equals("") || content.equals("")) {
            DialogManager.getInstance().createErrorAlert(this.mainPage, "You must fill all review's fields");
            return;
        }

        // fill the review
        this.ownReview.setTitle(title);
        this.ownReview.setContent(content);
        this.ownReview.setCreatedAt(new Date());

        // send the review
        int result = this.service.addNewReview(this.ownReview);

        // if is successful update the page
        if (result == 0) {
            // update the local podcast
            this.podcast = (Podcast)StageManager.getObjectIdentifier();
            int ratingIntermediate = (int)(this.podcast.getRating() * 10);
            this.rating.setText("" + (ratingIntermediate / 10) + "," + (ratingIntermediate % 10));
            this.numReviews.setText(" out of 5.0 • " + this.podcast.getReviews().size() + " reviews");

            // disable form
            this.disableForm();

            // if is first review make visible the grid and hide the message
            if (this.podcast.getReviews().size() == 1) {
                this.noReviewsMessage.setVisible(false);
                this.noReviewsMessage.setPadding(new Insets(-20, 0, 0, 0));
                this.gridWrapper.setVisible(true);
                this.gridWrapper.setStyle("-fx-min-width: 918; -fx-pref-width: 918; -fx-max-width: 918;");
                this.gridWrapper.setMinHeight(Region.USE_COMPUTED_SIZE);
                this.gridWrapper.setPrefHeight(Region.USE_COMPUTED_SIZE);
                this.gridWrapper.setMaxHeight(Region.USE_COMPUTED_SIZE);
            }

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("Review.fxml"));

            // create new review element
            AnchorPane newReview = fxmlLoader.load();
            ReviewController controller = fxmlLoader.getController();
            controller.setData(this.ownReview, this.mainPage, this.service, this);

            this.reviewGrid.add(newReview, this.column, this.row++);
        } else {
            DialogManager.getInstance().createErrorAlert(this.mainPage, "Failed to add new review");
        }
    }

    @FXML
    void clickOnThirdStar(MouseEvent event) {
        Image star = ImageCache.getImageFromLocalPath("/img/star.png");
        Image outlineStar = ImageCache.getImageFromLocalPath("/img/outline_star.png");

        this.star1.setImage(star);
        this.star2.setImage(star);
        this.star3.setImage(star);
        this.star4.setImage(outlineStar);
        this.star5.setImage(outlineStar);
        this.ownReview.setRating(3);

        Logger.info("Rating set to " + this.ownReview.getRating());
    }

    @FXML
    void clickOnTitle(MouseEvent event) throws IOException {
        Logger.info("Podcast ID to load : " + this.podcast.getId());
        StageManager.showPage("PodcastPage.fxml", this.podcast.getId());
    }

    @FXML
    void clickOnUser(MouseEvent event) throws IOException {
        String actorType = MyPodcastDB.getInstance().getSessionType();

        if (actorType.equals("Author"))
            StageManager.showPage(ViewNavigator.AUTHORPROFILE.getPage(), ((Author)MyPodcastDB.getInstance().getSessionActor()).getName());
        else if (actorType.equals("User"))
            StageManager.showPage(ViewNavigator.USERPAGE.getPage(), ((User)MyPodcastDB.getInstance().getSessionActor()).getUsername());
        else if (actorType.equals("Admin"))
            StageManager.showPage(ViewNavigator.ADMINDASHBOARD.getPage());
        else
            Logger.error("Unidentified Actor Type");
    }

    @FXML
    void enterOnSearch(KeyEvent event) throws IOException {
        if (event.getCode().equals(KeyCode.ENTER) && !this.searchBarText.getText().equals("")) {
            String searchString = this.searchBarText.getText();
            StageManager.showPage(ViewNavigator.SEARCH.getPage(), searchString);
        }
    }

    /******************************** Mouse on Event ********************************/

    @FXML
    void mouseOnAuthor(MouseEvent event) {
        this.author.setCursor(Cursor.HAND);
        this.author.setTextFill(Color.color(0.6, 0.6, 0.6));
    }

    @FXML
    void mouseOnFifthStar(MouseEvent event) {
        this.star5.setCursor(Cursor.HAND);

        Image star = ImageCache.getImageFromLocalPath("/img/star.png");

        this.star1.setImage(star);
        this.star2.setImage(star);
        this.star3.setImage(star);
        this.star4.setImage(star);
        this.star5.setImage(star);
    }

    @FXML
    void mouseOnFirstStar(MouseEvent event) {
        this.star1.setCursor(Cursor.HAND);

        Image star = ImageCache.getImageFromLocalPath("/img/star.png");
        Image outlineStar = ImageCache.getImageFromLocalPath("/img/outline_star.png");

        this.star1.setImage(star);
        this.star2.setImage(outlineStar);
        this.star3.setImage(outlineStar);
        this.star4.setImage(outlineStar);
        this.star5.setImage(outlineStar);
    }

    @FXML
    void mouseOnFourthStar(MouseEvent event) {
        this.star4.setCursor(Cursor.HAND);

        Image star = ImageCache.getImageFromLocalPath("/img/star.png");
        Image outlineStar = ImageCache.getImageFromLocalPath("/img/outline_star.png");

        this.star1.setImage(star);
        this.star2.setImage(star);
        this.star3.setImage(star);
        this.star4.setImage(star);
        this.star5.setImage(outlineStar);
    }

    @FXML
    void mouseOnReload(MouseEvent event) {
        this.reload.setStyle("-fx-border-color: #008CBA; -fx-background-color: white; -fx-background-radius: 8; -fx-text-fill: black; -fx-border-radius: 8;");
        this.reload.setCursor(Cursor.HAND);
    }

    @FXML
    void mouseOnSecondStar(MouseEvent event) {
        this.star2.setCursor(Cursor.HAND);

        Image star = ImageCache.getImageFromLocalPath("/img/star.png");
        Image outlineStar = ImageCache.getImageFromLocalPath("/img/outline_star.png");

        this.star1.setImage(star);
        this.star2.setImage(star);
        this.star3.setImage(outlineStar);
        this.star4.setImage(outlineStar);
        this.star5.setImage(outlineStar);
    }

    @FXML
    void mouseOnShow(MouseEvent event) {
        this.showMore.setCursor(Cursor.HAND);
        this.showMore.setTextFill(Color.color(0.6, 0.6, 0.6));
    }

    @FXML
    void mouseOnSubmit(MouseEvent event) {
        this.submit.setStyle("-fx-border-color: #4CAF50; -fx-background-color: white; -fx-background-radius: 8; -fx-text-fill: black; -fx-border-radius: 8;");
        this.submit.setCursor(Cursor.HAND);
    }

    @FXML
    void mouseOnTitle(MouseEvent event) {
        this.title.setCursor(Cursor.HAND);
        this.title.setTextFill(Color.color(0.6, 0.6, 0.6));
    }

    @FXML
    void mouseOnThirdStar(MouseEvent event) {
        this.star3.setCursor(Cursor.HAND);

        Image star = ImageCache.getImageFromLocalPath("/img/star.png");
        Image outlineStar = ImageCache.getImageFromLocalPath("/img/outline_star.png");

        this.star1.setImage(star);
        this.star2.setImage(star);
        this.star3.setImage(star);
        this.star4.setImage(outlineStar);
        this.star5.setImage(outlineStar);
    }

    /******************************* Mouse out Event ********************************/

    @FXML
    void mouseOutAuthor(MouseEvent event) {
        this.author.setCursor(Cursor.DEFAULT);
        this.author.setTextFill(Color.color(0.0, 0.0, 1.0));
    }

    @FXML
    void mouseOutFifthStar(MouseEvent event) {
        this.star5.setCursor(Cursor.DEFAULT);
        readRating();
    }

    @FXML
    void mouseOutFirstStar(MouseEvent event) {
        this.star1.setCursor(Cursor.DEFAULT);
        readRating();
    }

    @FXML
    void mouseOutFourthStar(MouseEvent event) {
        this.star4.setCursor(Cursor.DEFAULT);
        readRating();
    }

    @FXML
    void mouseOutReload(MouseEvent event) {
        this.reload.setStyle("-fx-border-color: transparent; -fx-background-color: #008CBA; -fx-background-radius: 8; -fx-text-fill: white; -fx-border-radius: 8;");
        this.reload.setCursor(Cursor.DEFAULT);
    }

    @FXML
    void mouseOutSecondStar(MouseEvent event) {
        this.star2.setCursor(Cursor.DEFAULT);
        readRating();
    }

    @FXML
    void mouseOutShow(MouseEvent event) {
        this.showMore.setCursor(Cursor.DEFAULT);
        this.showMore.setTextFill(Color.color(0.0, 0.0, 1.0));
    }

    @FXML
    void mouseOutSubmit(MouseEvent event) {
        this.submit.setStyle("-fx-border-color: transparent; -fx-background-color: #4CAF50; -fx-background-radius: 8; -fx-text-fill: white; -fx-border-radius: 8;");
        this.submit.setCursor(Cursor.DEFAULT);
    }

    @FXML
    void mouseOutTitle(MouseEvent event) {
        this.title.setCursor(Cursor.DEFAULT);
        this.title.setTextFill(Color.color(0.0, 0.0, 0.0));
    }

    @FXML
    void mouseOutThirdStar(MouseEvent event) {
        this.star3.setCursor(Cursor.DEFAULT);
        readRating();
    }

    /***************************** Initialize e Utility *****************************/

    public void initialize() throws IOException {
        // Initialize structure
        this.service = new ReviewPageService();
        this.podcast = (Podcast)StageManager.getObjectIdentifier();
        this.loadedReviews = new ArrayList<>(this.podcast.getPreloadedReviews());
        Collections.reverse(this.loadedReviews);
        this.limitPerQuery = 10;
        this.selectedOrder = "createdAt";
        this.selectedAscending = false;

        // actor recognition and info loading from service
        Boolean result = true;
        String sessionType = MyPodcastDB.getInstance().getSessionType();
        if (!sessionType.equals("User")) {
            // only the user can write review
            this.ownReview = null;
            this.disableForm();

            // if author update the profile picture
            if (sessionType.equals("Author")) {
                Author author = (Author) MyPodcastDB.getInstance().getSessionActor();
                Image picture = ImageCache.getImageFromLocalPath(author.getPicturePath());
                userPicture.setImage(picture);
            }

            // if unregisterd disable buttons
            if (sessionType.equals("Unregistered")) {
                this.userPictureWrapper.setVisible(false);
                this.userPictureWrapper.setStyle("-fx-min-width: 0; -fx-pref-width: 0; -fx-max-width: 0; -fx-min-height: 0; -fx-pref-height: 0; -fx-max-height: 0; -fx-padding: 0; -fx-margin: 0;");
            }
        } else {
            // initialize own review
            this.ownReview = new Review();
            this.ownReview.setPodcastId(podcast.getId());
            if (MyPodcastDB.getInstance().getSessionType().equals("User"))
                this.ownReview.setAuthorUsername(((User) MyPodcastDB.getInstance().getSessionActor()).getUsername());
            this.ownReview.setRating(0);

            // check if user wrote a review
            String username = ((User)MyPodcastDB.getInstance().getSessionActor()).getUsername();
            result = this.service.loadReviewPageForUser(this.podcast, username, this.ownReview);

            // profile picture
            User user = (User)MyPodcastDB.getInstance().getSessionActor();
            Image picture = ImageCache.getImageFromLocalPath(user.getPicturePath());
            userPicture.setImage(picture);

            // if user has already writter a review, disable form
            if (this.ownReview != null && this.ownReview.getTitle() != null) {
                this.disableForm();

                // put own review as first
                if (this.loadedReviews.contains(this.ownReview))
                    this.loadedReviews.remove(this.ownReview);
                else if (this.loadedReviews.size() > 9)
                    this.loadedReviews.remove(10);
                this.loadedReviews.add(0, this.ownReview);
            }
        }

        // check service result
        if (!result) {
            Platform.runLater(() -> {
                try {
                    redirect();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            // no reviews message
            if (this.loadedReviews != null && !this.loadedReviews.isEmpty()) {
                this.noReviewsMessage.setVisible(false);
                this.noReviewsMessage.setPadding(new Insets(-20, 0, 0, 0));
            } else {
                this.gridWrapper.setVisible(false);
                this.gridWrapper.setStyle("-fx-min-width: 0; -fx-pref-width: 0; -fx-max-width: 0; -fx-min-height: 0; -fx-pref-height: 0; -fx-max-height: 0; -fx-padding: 0; -fx-margin: 0;");
            }

            // set fields
            this.title.setText(this.podcast.getName());
            this.titleTooltip.setText(this.podcast.getName());
            this.author.setText(this.podcast.getAuthorName());
            this.category.setText(this.podcast.getPrimaryCategory());
            Image podcastImage = ImageCache.getImageFromURL(this.podcast.getArtworkUrl600());
            this.podcastImage.setImage(podcastImage);
            int ratingIntermediate = (int) (this.podcast.getRating() * 10);
            this.rating.setText("" + (ratingIntermediate / 10) + "," + (ratingIntermediate % 10));
            this.numReviews.setText(" out of 5.0 • " + this.podcast.getReviews().size() + " reviews");

            // calculate the progress bar for ratings
            int[] numReview = new int[5];
            for (Map.Entry<String, Integer> review : this.podcast.getReviews()) {
                switch (review.getValue()) {
                    case 1:
                        numReview[0]++;
                        break;
                    case 2:
                        numReview[1]++;
                        break;
                    case 3:
                        numReview[2]++;
                        break;
                    case 4:
                        numReview[3]++;
                        break;
                    case 5:
                        numReview[4]++;
                        break;
                }
            }
            this.oneStar.setProgress((float) numReview[0] / this.podcast.getReviews().size());
            this.twoStars.setProgress((float) numReview[1] / this.podcast.getReviews().size());
            this.threeStars.setProgress((float) numReview[2] / this.podcast.getReviews().size());
            this.fourStars.setProgress((float) numReview[3] / this.podcast.getReviews().size());
            this.fiveStars.setProgress((float) numReview[4] / this.podcast.getReviews().size());

            // insert reviews in grid
            this.reloadReviewList();

            // check if are finished
            if (this.loadedReviews.size() == this.podcast.getReviews().size()) {
                this.showMoreWrapper.setVisible(false);
                this.showMoreWrapper.setStyle("-fx-min-width: 0; -fx-pref-width: 0; -fx-max-width: 0; -fx-min-height: 0; -fx-pref-height: 0; -fx-max-height: 0; -fx-padding: 0; -fx-margin: 0;");
            }

            // initialize combo box
            this.orderBy.getItems().add("Date of creation");
            this.orderBy.getItems().add("Rating");
            this.orderBy.setValue("Date of creation");
            this.ascending.getItems().add("Ascending");
            this.ascending.getItems().add("Descending");
            this.ascending.setValue("Descending");
        }
    }

    private void readRating() {
        Image star = ImageCache.getImageFromLocalPath("/img/star.png");
        Image outlineStar = ImageCache.getImageFromLocalPath("/img/outline_star.png");

        if (this.ownReview.getRating() == 5) {
            this.star1.setImage(star);
            this.star2.setImage(star);
            this.star3.setImage(star);
            this.star4.setImage(star);
            this.star5.setImage(star);
        } else if (this.ownReview.getRating() >= 4) {
            this.star1.setImage(star);
            this.star2.setImage(star);
            this.star3.setImage(star);
            this.star4.setImage(star);
            this.star5.setImage(outlineStar);
        } else if (this.ownReview.getRating() >= 3) {
            this.star1.setImage(star);
            this.star2.setImage(star);
            this.star3.setImage(star);
            this.star4.setImage(outlineStar);
            this.star5.setImage(outlineStar);
        } else if (this.ownReview.getRating() >= 2) {
            this.star1.setImage(star);
            this.star2.setImage(star);
            this.star3.setImage(outlineStar);
            this.star4.setImage(outlineStar);
            this.star5.setImage(outlineStar);
        } else if (this.ownReview.getRating() >= 1) {
            this.star1.setImage(star);
            this.star2.setImage(outlineStar);
            this.star3.setImage(outlineStar);
            this.star4.setImage(outlineStar);
            this.star5.setImage(outlineStar);
        } else {
            this.star1.setImage(outlineStar);
            this.star2.setImage(outlineStar);
            this.star3.setImage(outlineStar);
            this.star4.setImage(outlineStar);
            this.star5.setImage(outlineStar);
        }
    }

    private void disableForm() {
        // hide form
        this.reviewForm.setVisible(false);
        this.reviewForm.setStyle("-fx-min-width: 0; -fx-pref-width: 0px; -fx-min-height: 0; -fx-pref-height: 0px");
    }

    private void enableForm() {
        // clean parameter
        Image outlineStar = ImageCache.getImageFromLocalPath("/img/outline_star.png");
        this.star1.setImage(outlineStar);
        this.star2.setImage(outlineStar);
        this.star3.setImage(outlineStar);
        this.star4.setImage(outlineStar);
        this.star5.setImage(outlineStar);
        this.textTitle.setText("");
        this.textContent.setText("");

        // show form
        this.reviewForm.setVisible(true);
        this.reviewForm.setStyle("-fx-pref-width: 918px; -fx-pref-height: 342px");
    }

    private void reloadReviewList() throws IOException {
        // empty the grid
        this.reviewGrid.getChildren().clear();
        
        // if there are reviews yet update the grid
        if (!this.podcast.getReviews().isEmpty()) {
            // if is user and it has written a review, put it as first
            if (MyPodcastDB.getInstance().getSessionType().equals("User") && this.ownReview != null && this.ownReview.getTitle() != null) {
                this.loadedReviews.remove(this.ownReview);
                this.loadedReviews.add(0, this.ownReview);
            }

            // insert reviews in grid
            this.row = 0;
            this.column = 0;
            for (Review review : this.loadedReviews) {
                // load the element
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getClassLoader().getResource("Review.fxml"));

                // create new review element
                AnchorPane newReview = fxmlLoader.load();
                ReviewController controller = fxmlLoader.getController();
                controller.setData(review, this.mainPage, this.service, this);

                // add new podcast to grid
                this.reviewGrid.add(newReview, column, row++);
            }

            // check if the show review should be reneabled
            if (!this.showMoreWrapper.isVisible() && this.getLoaded() < this.podcast.getReviews().size()) {
                this.showMoreWrapper.setVisible(true);
                this.showMoreWrapper.setStyle("-fx-pref-width: 140; -fx-pref-height: 22;");
            }
        }

        //else show the no review message
        else {
            this.noReviewsMessage.setVisible(true);
            this.noReviewsMessage.setPadding(new Insets(20, 0, 15, 0));
            this.gridWrapper.setVisible(false);
            this.gridWrapper.setStyle("-fx-min-width: 0; -fx-pref-width: 0; -fx-max-width: 0; -fx-min-height: 0; -fx-pref-height: 0; -fx-max-height: 0; -fx-padding: 0; -fx-margin: 0;");
        }
    }

    public void removeReviewFromLocalList(Review review) throws IOException {
        // update local copy of podcast
        this.podcast.deleteReview(review);
        int ratingIntermediate = (int)(this.podcast.getRating() * 10);
        this.rating.setText("" + (ratingIntermediate / 10) + "," + (ratingIntermediate % 10));
        this.numReviews.setText(" out of 5.0 • " + this.podcast.getReviews().size() + " reviews");

        // update review list in page
        this.loadedReviews.remove(review);
        this.reloadReviewList();

        // if is a User re-enable the form
        if (MyPodcastDB.getInstance().getSessionType().equals("User"))
            this.enableForm();

        // reinitialize own review
        this.ownReview = new Review();
        this.ownReview.setPodcastId(podcast.getId());
        if (MyPodcastDB.getInstance().getSessionType().equals("User"))
            this.ownReview.setAuthorUsername(((User)MyPodcastDB.getInstance().getSessionActor()).getUsername());
        this.ownReview.setRating(0);
    }

    private int getLoaded() {
        return this.loadedReviews.size();
    }

    private void redirect() throws IOException {
        // hide the text
        this.title.setText("");
        this.author.setText("");
        this.category.setText("");
        this.rating.setText("");
        this.numReviews.setText("");
        this.showMore.setText("");

        // create alert end redirect to homepage
        DialogManager.getInstance().createErrorAlert(this.mainPage, "Something goes wrong");
        StageManager.showPage(ViewNavigator.HOMEPAGE.getPage());
    }
}
