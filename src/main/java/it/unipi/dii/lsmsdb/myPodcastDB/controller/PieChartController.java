package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.cache.ImageCache;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.javatuples.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PieChartController {

    @FXML
    private PieChart pieChart;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private Label title;

    @FXML
    private Tooltip titleTooltip;

    private List<Pair<String, Integer>> statistics;
    private String[] columnsName;
    private BorderPane mainPage;

    @FXML
    void clickOnPane(MouseEvent event) throws IOException {
        BoxBlur blur = new BoxBlur(3, 3 , 3);
        this.mainPage.setEffect(blur);

        // loading the fxml file of the popup dialog
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("TotalStatistic.fxml"));

        // creating dialog Pane
        DialogPane statisticDialogPane = fxmlLoader.load();
        TotalStatisticController controller = fxmlLoader.getController();

        // pass podcast's data to dialog pane
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(this.mainPage.getScene().getWindow());
        dialog.setDialogPane(statisticDialogPane);
        dialog.setTitle(null);
        controller.setDataInteger(this.title.getText(), this.columnsName, this.statistics);

        Stage stage = (Stage)dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ImageCache.getImageFromLocalPath("/img/logo.png"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);

        dialog.showAndWait();

        this.mainPage.setEffect(null);
    }

    @FXML
    void mouseOnPane(MouseEvent event) {
        this.mainPane.setStyle("-fx-background-color: #e5e5e5; -fx-background-radius: 15; -fx-border-color: #636366; -fx-border-radius: 15;");
        this.mainPane.setCursor(Cursor.HAND);
    }

    @FXML
    void mouseOutPane(MouseEvent event) {
        this.mainPane.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-border-color: #636366; -fx-border-radius: 15;");
        this.mainPane.setCursor(Cursor.DEFAULT);
    }

    public void setData(String title, String[] columnsName, List<Pair<String, Integer>> statistics, BorderPane mainPage) {
        // save all statistics
        this.statistics = new ArrayList<>();
        this.statistics.addAll(statistics);
        this.columnsName = columnsName;
        this.mainPage = mainPage;

        // statistic management
        if (statistics.size() > 10)
            statistics = statistics.subList(0, 10);

        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        for (Pair<String, Integer> stat : statistics)
            data.add(new PieChart.Data(stat.getValue0(), stat.getValue1()));
        this.pieChart.setData(data);
        this.title.setText(title);
        this.titleTooltip.setText(title);
    }
}