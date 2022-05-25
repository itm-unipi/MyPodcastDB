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

public class SearchController {
    @FXML
    private GridPane gridFoundAuthors;

    @FXML
    private GridPane gridFoundUsers;

    @FXML
    private GridPane gridFoundPodcasts;

    @FXML
    private ImageView home;

    @FXML
    private ImageView searchButton;

    @FXML
    private Label searchingForText;

    @FXML
    private ImageView userPicture;

    @FXML
    void onClickSearch(MouseEvent event) throws IOException {
        // TODO: E' NECESSARIO PASSARE IL PARAMETRO DI RICERCA
        StageManager.showPage(ViewNavigator.SEARCH.getPage());
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

    public void initialize() throws IOException {
        //this.searchingForText.setText();

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

        int row = 0;
        int column = 0;
        for (Triplet<String, String, Date> entry: reducedPod){
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("AuthorReducedPodcast.fxml"));

            AnchorPane newPodcast = fxmlLoader.load();
            AuthorReducedPodcastController controller = fxmlLoader.getController();
            controller.setData(entry.getValue0(), entry.getValue1(), entry.getValue2());

            gridFoundPodcasts.add(newPodcast, column, row++);
        }
    }
}
