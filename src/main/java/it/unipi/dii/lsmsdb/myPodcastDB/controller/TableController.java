package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.MapValueFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TableController {

    @FXML
    private TableView table;

    @FXML
    private Label title;

    @FXML
    private Tooltip titleTooltip;

    public void setData(String title, String[] columnsName, List<Entry<String, Object>> statistics) {
        TableColumn<Map, String> col1 = new TableColumn<>(columnsName[0]);
        col1.setCellValueFactory(new MapValueFactory<>(columnsName[0]));
        TableColumn<Map, String> col2 = new TableColumn<>(columnsName[1]);
        col2.setCellValueFactory(new MapValueFactory<>(columnsName[1]));

        this.table.getColumns().add(col1);
        this.table.getColumns().add(col2);


        ObservableList<Map<String, Object>> items = FXCollections.<Map<String, Object>>observableArrayList();
        for (Entry<String, Object> stat : statistics) {
            Map<String, Object> item = new HashMap<>();
            item.put(columnsName[0], stat.getKey());
            item.put(columnsName[1], stat.getValue());
            items.add(item);
        }

        this.table.getItems().addAll(items);
        this.title.setText(title);
        this.titleTooltip.setText(title);
    }
}
