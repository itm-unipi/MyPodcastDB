package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import org.javatuples.Pair;

import java.util.List;
import java.util.Map.Entry;

public class PieChartController {

    @FXML
    private PieChart pieChart;

    @FXML
    private Label title;

    @FXML
    private Tooltip titleTooltip;

    public void setDataFloat(String title, List<Pair<String, Float>> statistics) {
        // statistic managment
        if (statistics.size() > 10)
            statistics = statistics.subList(0, 10);

        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        for (Pair<String, Float> stat : statistics)
            data.add(new PieChart.Data(stat.getValue0(), stat.getValue1()));
        this.pieChart.setData(data);
        this.title.setText(title);
        this.titleTooltip.setText(title);
    }

    public void setDataInteger(String title, List<Pair<String, Integer>> statistics) {
        // statistic managment
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