package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;

import java.util.List;
import java.util.Map.Entry;

public class PieChartController {

    @FXML
    private PieChart pieChart;

    public void setData(List<Entry<String, Float>> statistics) {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        for (Entry<String, Float> stat : statistics)
            data.add(new PieChart.Data(stat.getKey(), stat.getValue()));
        pieChart.setData(data);
    }

}