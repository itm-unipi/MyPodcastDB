package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

import java.util.List;
import java.util.Map.Entry;

public class BarChartController {

    @FXML
    private BarChart<String, Object> barChart;

    @FXML
    private Tooltip titleTooltip;

    @FXML
    private Label title;

    public void setData(String title, List<Entry<String, Object>> statistics) {
        // creation of axis
        CategoryAxis xaxis = new CategoryAxis();
        NumberAxis yaxis = new NumberAxis(0.1,2,0.1);
        xaxis.setLabel("Podcasts");
        yaxis.setLabel("Ratings");

        // plot the statistics
        XYChart.Series<String, Object> series = new XYChart.Series<>();
        series.setName("Podcasts average");
        for (Entry<String, Object> stat : statistics)
            series.getData().add(new XYChart.Data(stat.getKey(), stat.getValue()));

        this.barChart.getData().add(series);
        this.title.setText(title);
        this.titleTooltip.setText(title);
    }
}
