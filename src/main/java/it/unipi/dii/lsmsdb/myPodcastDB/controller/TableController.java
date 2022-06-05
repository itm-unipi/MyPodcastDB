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
import org.javatuples.Triplet;

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

    public void setData(String title, String[] columnsName, Triplet<List<String>, List<String>, List<String>> statistics) {
        TableColumn<Map, String> col1 = new TableColumn<>(columnsName[0]);
        col1.setCellValueFactory(new MapValueFactory<>(columnsName[0]));
        TableColumn<Map, String> col2 = new TableColumn<>(columnsName[1]);
        col2.setCellValueFactory(new MapValueFactory<>(columnsName[1]));
        TableColumn<Map, String> col3 = new TableColumn<>(columnsName[2]);
        col3.setCellValueFactory(new MapValueFactory<>(columnsName[2]));

        this.table.getColumns().add(col1);
        this.table.getColumns().add(col2);
        this.table.getColumns().add(col3);

        int sizeList0 = statistics.getValue0().size();
        int sizeList1 = statistics.getValue1().size();
        int sizeList2 = statistics.getValue2().size();
        int maxSize = sizeList0;
        if (maxSize < sizeList1)
            maxSize = sizeList1;
        if (maxSize < sizeList2)
            maxSize = sizeList2;

        ObservableList<Map<String, Object>> items = FXCollections.<Map<String, Object>>observableArrayList();
        for (int i = 0; i < maxSize; i++) {
            Map<String, Object> item = new HashMap<>();
            if (i < sizeList0)
                item.put(columnsName[0], statistics.getValue0().get(i));
            if (i < sizeList1)
                item.put(columnsName[1], statistics.getValue1().get(i));
            if (i < sizeList2)
                item.put(columnsName[2], statistics.getValue2().get(i));
            items.add(item);
        }

        this.table.getItems().addAll(items);
        this.title.setText(title);
        this.titleTooltip.setText(title);
    }
}
