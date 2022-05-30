package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

import java.util.List;
import java.util.Map.Entry;

public class PieChartController {

    @FXML
    private PieChart pieChart;

    @FXML
    private Label title;

    @FXML
    private Tooltip titleTooltip;

    public void setData(String title, List<Entry<String, Object>> statistics) {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        for (Entry<String, Object> stat : statistics)
            data.add(new PieChart.Data(stat.getKey(), (float)stat.getValue()));
        this.pieChart.setData(data);
        this.title.setText(title);
        this.titleTooltip.setText(title);
    }
}