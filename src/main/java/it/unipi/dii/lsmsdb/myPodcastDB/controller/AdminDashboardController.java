package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Admin;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Review;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class AdminDashboardController {

    @FXML
    private ImageView actorPicture;

    @FXML
    private Button cancelInfo;

    @FXML
    private Button delete;

    @FXML
    private TextField emailTextField;

    @FXML
    private ImageView home;

    @FXML
    private Button modifyInfo;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private ImageView searchButton;

    @FXML
    private TextField searchText;

    @FXML
    private GridPane statisticsGrid;

    @FXML
    private Button update1;

    @FXML
    private Button update2;

    @FXML
    private Button update3;

    @FXML
    private Button update4;

    @FXML
    private Button update5;

    @FXML
    private Button update6;

    @FXML
    private Button updateInfo;

    @FXML
    void onClickActorProfile(MouseEvent event) {
        Logger.info("Click on profile");
    }

    @FXML
    void onClickHome(MouseEvent event) {
        Logger.info("Click on home");
    }

    @FXML
    void onClickSearch(MouseEvent event) throws IOException {
        Logger.info("Click on search");

        String searchString = this.searchText.getText();
        StageManager.showPage(ViewNavigator.SEARCH.getPage(), searchString);
    }

    @FXML
    void onEnterPressedSearch(KeyEvent event) throws IOException {
        Logger.info("Enter on search");

        if (event.getCode().equals(KeyCode.ENTER)) {
            String searchString = this.searchText.getText();
            StageManager.showPage(ViewNavigator.SEARCH.getPage(), searchString);
        }
    }

    @FXML
    void clickOnCancel(MouseEvent event) {

    }

    @FXML
    void clickOnDelete(MouseEvent event) {

    }

    @FXML
    void clickOnModify(MouseEvent event) {

    }

    @FXML
    void clickOnUpdate(MouseEvent event) {

    }

    @FXML
    void clickOnUpdate1(MouseEvent event) {

    }

    @FXML
    void clickOnUpdate2(MouseEvent event) {

    }

    @FXML
    void clickOnUpdate3(MouseEvent event) {

    }

    @FXML
    void clickOnUpdate4(MouseEvent event) {

    }

    @FXML
    void clickOnUpdate5(MouseEvent event) {

    }

    @FXML
    void clickOnUpdate6(MouseEvent event) {

    }

    @FXML
    void mouseOnCancel(MouseEvent event) {

    }

    @FXML
    void mouseOnDelete(MouseEvent event) {

    }

    @FXML
    void mouseOnModify(MouseEvent event) {

    }

    @FXML
    void mouseOnUpdate(MouseEvent event) {

    }

    @FXML
    void mouseOnUpdate1(MouseEvent event) {

    }

    @FXML
    void mouseOnUpdate2(MouseEvent event) {

    }

    @FXML
    void mouseOnUpdate3(MouseEvent event) {

    }

    @FXML
    void mouseOnUpdate4(MouseEvent event) {

    }

    @FXML
    void mouseOnUpdate5(MouseEvent event) {

    }

    @FXML
    void mouseOnUpdate6(MouseEvent event) {

    }

    @FXML
    void mouseOutCancel(MouseEvent event) {

    }

    @FXML
    void mouseOutDelete(MouseEvent event) {

    }

    @FXML
    void mouseOutModify(MouseEvent event) {

    }

    @FXML
    void mouseOutUpdate(MouseEvent event) {

    }

    @FXML
    void mouseOutUpdate1(MouseEvent event) {

    }

    @FXML
    void mouseOutUpdate2(MouseEvent event) {

    }

    @FXML
    void mouseOutUpdate3(MouseEvent event) {

    }

    @FXML
    void mouseOutUpdate4(MouseEvent event) {

    }

    @FXML
    void mouseOutUpdate5(MouseEvent event) {

    }

    @FXML
    void mouseOutUpdate6(MouseEvent event) {

    }

    public void initialize() throws IOException {
        Admin admin = (Admin)MyPodcastDB.getInstance().getSessionActor();
        Logger.info("Admin logged : " + admin.getName());

        // example statistic
        List<Entry<String, Object>> statistics1 = new ArrayList<>();
        statistics1.add(new AbstractMap.SimpleEntry<>("Test1", 10.0f));
        statistics1.add(new AbstractMap.SimpleEntry<>("Test2", 10.0f));
        statistics1.add(new AbstractMap.SimpleEntry<>("Test3", 30.0f));
        statistics1.add(new AbstractMap.SimpleEntry<>("Test4", 10.0f));
        statistics1.add(new AbstractMap.SimpleEntry<>("Test5", 10.0f));
        List<Entry<String, Object>> statistics2 = new ArrayList<>();
        statistics2.add(new AbstractMap.SimpleEntry<>("Test1", 10));
        statistics2.add(new AbstractMap.SimpleEntry<>("Test2", 10));
        statistics2.add(new AbstractMap.SimpleEntry<>("Test3", 30));
        statistics2.add(new AbstractMap.SimpleEntry<>("Test4", 10));
        statistics2.add(new AbstractMap.SimpleEntry<>("Test5", 10));

        // statistics creation
        int row = 1;
        int column = 0;

        // create new bar chart element
        FXMLLoader barLoader = new FXMLLoader();
        barLoader.setLocation(getClass().getClassLoader().getResource("BarChart.fxml"));
        AnchorPane newBarChart1 = barLoader.load();
        BarChartController controller1 = barLoader.getController();
        controller1.setData("Average age for favourite category", statistics1);
        this.statisticsGrid.add(newBarChart1, column++, row);

        // create new bar chart element
        barLoader = new FXMLLoader();
        barLoader.setLocation(getClass().getClassLoader().getResource("BarChart.fxml"));
        AnchorPane newBarChart2 = barLoader.load();
        BarChartController controller2 = barLoader.getController();
        controller2.setData("Podcasts with highest number of reviews", statistics1);
        this.statisticsGrid.add(newBarChart2, column++, row);

        // create new bar chart element
        barLoader = new FXMLLoader();
        barLoader.setLocation(getClass().getClassLoader().getResource("BarChart.fxml"));
        AnchorPane newBarChart3 = barLoader.load();
        BarChartController controller3 = barLoader.getController();
        controller3.setData("Country with highest number of podcasts\n", statistics1);
        this.statisticsGrid.add(newBarChart3, column, row++);
        column -= 2;

        // create new pie chart element
        FXMLLoader tableLoader = new FXMLLoader();
        tableLoader.setLocation(getClass().getClassLoader().getResource("Table.fxml"));
        AnchorPane newTable = tableLoader.load();
        TableController controller4 = tableLoader.getController();
        controller4.setData("Top favourite categories", new String[]{"Gender", "Category"}, statistics1);
        this.statisticsGrid.add(newTable, column++, row);

        // create new pie chart element
        FXMLLoader pieLoader = new FXMLLoader();
        pieLoader.setLocation(getClass().getClassLoader().getResource("PieChart.fxml"));
        AnchorPane newPieChart1 = pieLoader.load();
        PieChartController controller5 = pieLoader.getController();
        controller5.setData("Most numerous category", statistics1);
        this.statisticsGrid.add(newPieChart1, column++, row);

        // create new pie chart element
        pieLoader = new FXMLLoader();
        pieLoader.setLocation(getClass().getClassLoader().getResource("PieChart.fxml"));
        AnchorPane newPieChart2 = pieLoader.load();
        PieChartController controller6 = pieLoader.getController();
        controller6.setData("Most appreciated category", statistics1);
        this.statisticsGrid.add(newPieChart2, column, row);

        // settings buttons and texts
        this.updateInfo.setVisible(false);
        this.cancelInfo.setVisible(false);
        this.nameTextField.setText(admin.getName());
        this.emailTextField.setText(admin.getEmail());
        this.passwordTextField.setText("********************");
    }
}
