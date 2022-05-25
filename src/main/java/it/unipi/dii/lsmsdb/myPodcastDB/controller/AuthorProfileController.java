package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.javatuples.Triplet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AuthorProfileController {

    @FXML
    private Label authorName;

    @FXML
    private Label authorFollowing;

    @FXML
    private GridPane gridAuthorPodcasts;

    @FXML
    private GridPane gridAuthorsFollowed;

    @FXML
    private ScrollPane scrollFollowedAuthors;

    @FXML
    private ImageView searchButton;

    @FXML
    private ImageView userPicture;

    @FXML
    private ImageView home;

    @FXML
    void nextFollowedAuthor(MouseEvent event) {
        Logger.info("next followed author");
        double scrollValue = 1;
        if (scrollFollowedAuthors.getHvalue() == 1.0)
            scrollValue = -1;
        scrollFollowedAuthors.setHvalue(scrollFollowedAuthors.getHvalue() + scrollValue);
    }

    @FXML
    void backFollowedAuthor(MouseEvent event) {
        Logger.info("back followed authors");
        double scrollValue = 1;
        if (scrollFollowedAuthors.getHvalue() == 0.0)
            scrollFollowedAuthors.setHvalue(1.0);
        else
            scrollFollowedAuthors.setHvalue(scrollFollowedAuthors.getHvalue() - scrollValue);
    }

    @FXML
    void userProfile(MouseEvent event) {
        Logger.info("User profile clicked");
        //StageManager.showPage(ViewNavigator.USERPROFILE.getPage());
    }

    @FXML
    void onClickHome(MouseEvent event) throws IOException {
        StageManager.showPage(ViewNavigator.USERHOME.getPage());
        Logger.info("User Home Clicked");
    }

    @FXML
    void onEnterPressed(KeyEvent event) throws IOException {
        // TODO: E' NECESSARIO PASSARE IL PARAMETRO DI RICERCA
        if (event.getCode() == KeyCode.ENTER)
            StageManager.showPage(ViewNavigator.SEARCH.getPage());
    }

    @FXML
    void onClickSearch(MouseEvent event) throws IOException {
        // TODO: E' NECESSARIO PASSARE IL PARAMETRO DI RICERCA
        StageManager.showPage(ViewNavigator.SEARCH.getPage());
        Logger.info("Search Clicked");
    }

    public void initialize() throws IOException {
        // Temp author (profile)
        Author a = new Author();
        a.setName("Robespierre");
        authorName.setText(a.getName());
        authorFollowing.setText("Authors followed by " + a.getName());

        // Authors Followed
        List<Author> authorsFollowed = new ArrayList<>();
        Author a1 = new Author();
        a1.setName("Apple Inc.");
        Author a2 = new Author();
        a2.setName("Gino Paolino");
        authorsFollowed.add(a1);
        authorsFollowed.add(a2);

        int row = 0;
        int column = 0;
        for (Author author : authorsFollowed){
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("AuthorFollowed.fxml"));

            AnchorPane newAuthor = fxmlLoader.load();
            AuthorFollowedController controller = fxmlLoader.getController();
            controller.setData(author);

            gridAuthorsFollowed.add(newAuthor, column++, row);
        }

        // Author Podcasts
        Author author = new Author();
        author.setName("Robespierre Janjaq");

        List<String> podcastIds = new ArrayList<>();
        List<String> podcastNames = new ArrayList<>();
        List<Date> podcastReleaseDates = new ArrayList<>();

        podcastIds.add("54eb342567c94dacfb2a3e50");
        podcastIds.add("34e734b09246d17dc5d56f63");
        podcastIds.add("34e734b09246d17dc5d56f63");
        podcastIds.add("34e734b09246d17dc5d56f63");
        podcastIds.add("34e734b09246d17dc5d56f63");
        podcastIds.add("34e734b09246d17dc5d56f63");
        podcastNames.add("Scaling Global");
        podcastNames.add("Cornerstone Baptist Church of Orlando");
        podcastNames.add("Jianluca");
        podcastNames.add("Crunch");
        podcastNames.add("Jianluca");
        podcastNames.add("Crunch");
        podcastReleaseDates.add(new Date());
        podcastReleaseDates.add(new Date());
        podcastReleaseDates.add(new Date());
        podcastReleaseDates.add(new Date());
        podcastReleaseDates.add(new Date());
        podcastReleaseDates.add(new Date());

        author.setOwnPodcasts(podcastIds, podcastNames, podcastReleaseDates);

        List<Triplet<String, String, Date>>  reducedPod = author.getReducedPodcasts();

        row = 0;
        column = 0;
        for (Triplet<String, String, Date> entry: reducedPod){
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("AuthorReducedPodcast.fxml"));

            // create new podcast element
            AnchorPane newPodcast = fxmlLoader.load();
            AuthorReducedPodcastController controller = fxmlLoader.getController();
            controller.setData(entry.getValue0(), entry.getValue1(), entry.getValue2());

            // add new podcast to grid
            gridAuthorPodcasts.add(newPodcast, column, row++);
        }
    }
}

