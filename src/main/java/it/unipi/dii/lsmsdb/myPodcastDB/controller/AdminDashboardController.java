package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Admin;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Review;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class AdminDashboardController {

    @FXML
    private ImageView actorPicture;

    @FXML
    private ImageView home;

    @FXML
    private ImageView searchButton;

    @FXML
    private TextField searchText;

    @FXML
    private GridPane statisticsGrid;

    @FXML
    void onClickActorProfile(MouseEvent event) {
        Logger.info("Click on profile");
    }

    @FXML
    void onClickHome(MouseEvent event) {
        Logger.info("Click on home");
    }

    @FXML
    void onClickSearch(MouseEvent event) throws IOException {
        Logger.info("Click on search");

        String searchString = this.searchText.getText();
        StageManager.showPage(ViewNavigator.SEARCH.getPage(), searchString);
    }

    @FXML
    void onEnterPressedSearch(KeyEvent event) throws IOException {
        Logger.info("Enter on search");

        if (event.getCode().equals(KeyCode.ENTER)) {
            String searchString = this.searchText.getText();
            StageManager.showPage(ViewNavigator.SEARCH.getPage(), searchString);
        }
    }

    public void initialize() throws IOException {
        String name = ((Admin)MyPodcastDB.getInstance().getSessionActor()).getName();
        Logger.info("Admin logged : " + name);

        // example statistic
        List<Entry<String, Float>> statistics = new ArrayList<>();
        statistics.add(new AbstractMap.SimpleEntry<>("Test1", 10.0f));
        statistics.add(new AbstractMap.SimpleEntry<>("Test2", 10.0f));
        statistics.add(new AbstractMap.SimpleEntry<>("Test3", 30.0f));
        statistics.add(new AbstractMap.SimpleEntry<>("Test4", 10.0f));
        statistics.add(new AbstractMap.SimpleEntry<>("Test5", 10.0f));

        // statistics creation
        int row = 0;
        int column = 0;
        while (row == 0) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("PieChart.fxml"));

            // create new review element
            AnchorPane newChart = fxmlLoader.load();
            PieChartController controller = fxmlLoader.getController();
            controller.setData(statistics);

            // add new podcast to grid
            this.statisticsGrid.add(newChart, column, row++);
        }
    }
}
