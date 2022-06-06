package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ImageCache;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.javatuples.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class BarChartController {

    @FXML
    private BarChart<String, Object> barChart;

    @FXML
    private Tooltip titleTooltip;

    @FXML
    private Label title;

    List<Pair<String, Float>> statisticsFloat;
    List<Pair<String, Integer>> statisticsInt;
    List<Pair<Podcast, Integer>> statisticsPodInt;
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

        // choose the version of statistics
        if (this.statisticsFloat != null)
            controller.setDataFloat(this.title.getText(), this.columnsName, this.statisticsFloat);
        else if (this.statisticsInt != null)
            controller.setDataInteger(this.title.getText(), this.columnsName, this.statisticsInt);
        else
            controller.setDataPodcastInteger(this.title.getText(), this.columnsName, this.statisticsPodInt);

        Stage stage = (Stage)dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ImageCache.getImageFromLocalPath("/img/logo.png"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);

        dialog.showAndWait();

        this.mainPage.setEffect(null);
    }

    public void setDataFloat(String title, String[] columnsName,  String typeOfData, List<Pair<String, Float>> statistics, BorderPane mainPage) {
        // save all statistics
        this.statisticsFloat = new ArrayList<>();
        this.statisticsFloat.addAll(statistics);
        this.columnsName = columnsName;
        this.mainPage = mainPage;

        // statistic management
        if (statistics.size() > 10)
            statistics = statistics.subList(0, 10);

        // creation of axis
        CategoryAxis xaxis = new CategoryAxis();
        NumberAxis yaxis = new NumberAxis(0.1,2,0.1);
        xaxis.setLabel("Podcasts");
        yaxis.setLabel("Ratings");

        // plot the statistics
        XYChart.Series<String, Object> series = new XYChart.Series<>();
        series.setName(typeOfData);
        for (Pair<String, Float> stat : statistics) {
            String name = stat.getValue0();
            if (name.length() > 16)
                name = name.substring(0, 15);
            series.getData().add(new XYChart.Data(name, stat.getValue1()));
        }

        this.barChart.getData().add(series);
        this.title.setText(title);
        this.titleTooltip.setText(title);
    }

    public void setDataInteger(String title, String[] columnsName, String typeOfData, List<Pair<String, Integer>> statistics, BorderPane mainPage) {
        // save all statistics
        this.statisticsInt = new ArrayList<>();
        this.statisticsInt.addAll(statistics);
        this.columnsName = columnsName;
        this.mainPage = mainPage;

        // statistic management
        if (statistics.size() > 10)
            statistics = statistics.subList(0, 10);

        // creation of axis
        CategoryAxis xaxis = new CategoryAxis();
        NumberAxis yaxis = new NumberAxis(0.1,2,0.1);
        xaxis.setLabel("Podcasts");
        yaxis.setLabel("Ratings");

        // plot the statistics
        XYChart.Series<String, Object> series = new XYChart.Series<>();
        series.setName(typeOfData);
        for (Pair<String, Integer> stat : statistics) {
            String name = stat.getValue0();
            if (name.length() > 16)
                name = name.substring(0, 15);
            series.getData().add(new XYChart.Data(name, stat.getValue1()));
        }

        this.barChart.getData().add(series);
        this.title.setText(title);
        this.titleTooltip.setText(title);
    }

    public void setDataPodcastInteger(String title, String[] columnsName, String typeOfData, List<Pair<Podcast, Integer>> statistics, BorderPane mainPage) {
        // save all statistics
        this.statisticsPodInt = new ArrayList<>();
        this.statisticsPodInt.addAll(statistics);
        this.columnsName = columnsName;
        this.mainPage = mainPage;

        // statistic management
        if (statistics.size() > 10)
            statistics = statistics.subList(0, 10);

        // creation of axis
        CategoryAxis xaxis = new CategoryAxis();
        NumberAxis yaxis = new NumberAxis(0.1,2,0.1);
        xaxis.setLabel("Podcasts");
        yaxis.setLabel("Ratings");

        // plot the statistics
        XYChart.Series<String, Object> series = new XYChart.Series<>();
        series.setName(typeOfData);
        for (Pair<Podcast, Integer> stat : statistics) {
            String name = stat.getValue0().getName();
            if (name.length() > 16)
                name = name.substring(0, 15);
            series.getData().add(new XYChart.Data(name, stat.getValue1()));
        }

        this.barChart.getData().add(series);
        this.title.setText(title);
        this.titleTooltip.setText(title);
    }
}
