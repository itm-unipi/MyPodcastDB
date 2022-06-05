package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.javatuples.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TotalStatisticController {

    @FXML
    private Button close;

    @FXML
    private TableView table;

    @FXML
    private Label title;

    @FXML
    void clickOnClose(MouseEvent event) {
        closeStage(event);
    }

    @FXML
    void mouseOnClose(MouseEvent event) {
        this.close.setStyle("-fx-border-color: #f44336; -fx-background-color: white; -fx-background-radius: 8; -fx-text-fill: black; -fx-border-radius: 8; -fx-cursor: hand;");

    }

    @FXML
    void mouseOutClose(MouseEvent event) {
        this.close.setStyle("-fx-border-color: transparent; -fx-background-color: #f44336; -fx-background-radius: 8; -fx-text-fill: white; -fx-border-radius: 8; -fx-cursor: default;");
    }

    public void setDataFloat(String title, String[] columnsName, List<Pair<String, Float>> statistics) {
        TableColumn<Map, String> col1 = new TableColumn<>(columnsName[0]);
        col1.setCellValueFactory(new MapValueFactory<>(columnsName[0]));
        TableColumn<Map, String> col2 = new TableColumn<>(columnsName[1]);
        col2.setCellValueFactory(new MapValueFactory<>(columnsName[1]));

        this.table.getColumns().add(col1);
        this.table.getColumns().add(col2);

        ObservableList<Map<String, Object>> items = FXCollections.<Map<String, Object>>observableArrayList();
        for (Pair<String, Float> stat : statistics) {
            Map<String, Object> item = new HashMap<>();
            item.put(columnsName[0], stat.getValue0());
            item.put(columnsName[1], stat.getValue1());
            items.add(item);
        }

        this.table.getItems().addAll(items);
        this.title.setText(title);
    }

    public void setDataInteger(String title, String[] columnsName, List<Pair<String, Integer>> statistics) {
        TableColumn<Map, String> col1 = new TableColumn<>(columnsName[0]);
        col1.setCellValueFactory(new MapValueFactory<>(columnsName[0]));
        TableColumn<Map, String> col2 = new TableColumn<>(columnsName[1]);
        col2.setCellValueFactory(new MapValueFactory<>(columnsName[1]));

        this.table.getColumns().add(col1);
        this.table.getColumns().add(col2);

        ObservableList<Map<String, Object>> items = FXCollections.<Map<String, Object>>observableArrayList();
        for (Pair<String, Integer> stat : statistics) {
            Map<String, Object> item = new HashMap<>();
            item.put(columnsName[0], stat.getValue0());
            item.put(columnsName[1], stat.getValue1());
            items.add(item);
        }

        this.table.getItems().addAll(items);
        this.title.setText(title);
    }

    public void setDataPodcastInteger(String title, String[] columnsName, List<Pair<Podcast, Integer>> statistics) {
        TableColumn<Map, String> col1 = new TableColumn<>(columnsName[0]);
        col1.setCellValueFactory(new MapValueFactory<>(columnsName[0]));
        TableColumn<Map, String> col2 = new TableColumn<>(columnsName[1]);
        col2.setCellValueFactory(new MapValueFactory<>(columnsName[1]));

        this.table.getColumns().add(col1);
        this.table.getColumns().add(col2);

        ObservableList<Map<String, Object>> items = FXCollections.<Map<String, Object>>observableArrayList();
        for (Pair<Podcast, Integer> stat : statistics) {
            Map<String, Object> item = new HashMap<>();
            item.put(columnsName[0], stat.getValue0().getName());
            item.put(columnsName[1], stat.getValue1());
            items.add(item);
        }

        this.table.getItems().addAll(items);
        this.title.setText(title);
    }

    private void closeStage(MouseEvent event) {
        Node source = (Node)event.getSource();
        Stage stage = (Stage)source.getScene().getWindow();
        stage.close();
    }
}