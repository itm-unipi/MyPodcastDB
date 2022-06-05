package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import org.javatuples.Pair;

import java.util.List;
import java.util.Map.Entry;

public class BarChartController {

    @FXML
    private BarChart<String, Object> barChart;

    @FXML
    private Tooltip titleTooltip;

    @FXML
    private Label title;

    public void setDataFloat(String title, String typeOfData, List<Pair<String, Float>> statistics) {
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

    public void setDataInteger(String title, String typeOfData, List<Pair<String, Integer>> statistics) {
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

    public void setDataPodcastInteger(String title, String typeOfData, List<Pair<Podcast, Integer>> statistics) {
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
