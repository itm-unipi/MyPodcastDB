package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Admin;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Review;
import it.unipi.dii.lsmsdb.myPodcastDB.service.AdminService;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.DialogManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

public class AdminDashboardController {

    @FXML
    private ImageView actorPicture;

    @FXML
    private Button addAdmin;

    @FXML
    private HBox box1;

    @FXML
    private HBox box2;

    @FXML
    private HBox box3;

    @FXML
    private HBox box4;

    @FXML
    private HBox box5;

    @FXML
    private HBox box6;

    @FXML
    private HBox box7;

    @FXML
    private HBox box8;

    @FXML
    private HBox box9;

    @FXML
    private HBox box10;

    @FXML
    private Button cancelInfo;

    @FXML
    private Label data1;

    @FXML
    private Label data2;

    @FXML
    private Label data3;

    @FXML
    private Label data4;

    @FXML
    private Label data5;

    @FXML
    private Label data6;

    @FXML
    private Label data7;

    @FXML
    private Label data8;

    @FXML
    private Label data9;

    @FXML
    private Label data10;

    @FXML
    private Button delete;

    @FXML
    private TextField emailTextField;

    @FXML
    private ImageView home;

    @FXML
    private ImageView logout;

    @FXML
    private BorderPane mainPage;

    @FXML
    private Button modifyInfo;

    @FXML
    private HBox modifyInfoWrapper;

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
    private Label title;

    @FXML
    private Button update1;

    @FXML
    private Button update10;

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
    private Button update7;

    @FXML
    private Button update8;

    @FXML
    private Button update9;

    @FXML
    private HBox updateCancelWrapper;

    @FXML
    private Button updateInfo;

    private Admin admin;
    private AdminService service;
    private int limit;

    @FXML
    void clickOnAddAdmin(MouseEvent event) throws IOException {
        Logger.info("Add admin");

        this.mainPage.setEffect(new BoxBlur(3, 3, 3));
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("AddAdmin.fxml"));

        DialogPane addAdminDialog = new DialogPane();
        addAdminDialog.setContent(fxmlLoader.load());

        Dialog<Triplet<String, String, String>> dialog = new Dialog<>();
        dialog.setDialogPane(addAdminDialog);
        dialog.setTitle("Add new Admin");
        dialog.initOwner(this.mainPage.getScene().getWindow());

        Stage stage = (Stage)dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ImageCache.getImageFromLocalPath("/img/browse_podcasts_64px.png"));
        dialog.showAndWait();
        this.mainPage.setEffect(null);
    }
    @FXML
    void onClickActorProfile(MouseEvent event) throws IOException {
        Logger.info("Click on profile");
        StageManager.showPage(ViewNavigator.ADMINDASHBOARD.getPage());
    }

    @FXML
    void onClickHome(MouseEvent event) throws IOException {
        Logger.info("Click on home");
        StageManager.showPage(ViewNavigator.HOMEPAGE.getPage());
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
        // hide buttons and disable text fields
        this.nameTextField.setText(this.admin.getName());
        this.nameTextField.setStyle("-fx-background-color: transparent");
        this.nameTextField.setDisable(true);
        this.emailTextField.setText(this.admin.getEmail());
        this.emailTextField.setStyle("-fx-background-color: transparent");
        this.nameTextField.setDisable(true);
        this.passwordTextField.setText("********************");
        this.passwordTextField.setStyle("-fx-background-color: transparent");
        this.nameTextField.setDisable(true);
        this.modifyInfoWrapper.setVisible(true);
        this.updateCancelWrapper.setVisible(false);
        this.modifyInfoWrapper.setStyle("-fx-max-height: 40;");
        this.updateCancelWrapper.setStyle("-fx-max-height: 0;");
    }

    @FXML
    void clickOnDelete(MouseEvent event) {
        Logger.info("Delete Admin");
    }

    @FXML
    void clickOnLogout(MouseEvent event) throws IOException {
        MyPodcastDB.getInstance().setSession(null, null);
        StageManager.showPage(ViewNavigator.LOGIN.getPage());
    }

    @FXML
    void clickOnModify(MouseEvent event) {
        // show buttons and enable text fields
        this.nameTextField.setStyle("-fx-background-color: white");
        this.nameTextField.setDisable(false);
        this.emailTextField.setStyle("-fx-background-color: white");
        this.nameTextField.setDisable(false);
        this.passwordTextField.setText("");
        this.passwordTextField.setStyle("-fx-background-color: white");
        this.nameTextField.setDisable(false);
        this.modifyInfoWrapper.setVisible(false);
        this.updateCancelWrapper.setVisible(true);
        this.modifyInfoWrapper.setStyle("-fx-pref-height: 0; -fx-max-height: 0;");
        this.updateCancelWrapper.setStyle("-fx-pref-height: 39; -fx-max-height: 40;");
    }

    @FXML
    void clickOnUpdate(MouseEvent event) {
        // update admin
        this.admin.setName(this.nameTextField.getText());
        this.admin.setEmail(this.emailTextField.getText());
        this.admin.setPassword(this.passwordTextField.getText());

        // hide buttons and disable text fields
        this.nameTextField.setText(this.admin.getName());
        this.nameTextField.setStyle("-fx-background-color: transparent");
        this.nameTextField.setDisable(true);
        this.emailTextField.setText(this.admin.getEmail());
        this.emailTextField.setStyle("-fx-background-color: transparent");
        this.nameTextField.setDisable(true);
        this.passwordTextField.setText("********************");
        this.passwordTextField.setStyle("-fx-background-color: transparent");
        this.nameTextField.setDisable(true);
        this.modifyInfo.setVisible(true);
        this.updateInfo.setVisible(false);
        this.cancelInfo.setVisible(false);
        this.modifyInfo.setStyle("-fx-pref-width: 120; -fx-pref-height: 20px; -fx-background-color: #008CBA; -fx-background-radius: 10; -fx-margin-right: 10; -fx-max-height: 20;");
        this.updateInfo.setStyle("-fx-min-width: 0; -fx-pref-width: 0px; -fx-min-height: 0; -fx-pref-height: 0px; -fx-max-height: 0;");
        this.cancelInfo.setStyle("-fx-min-width: 0; -fx-pref-width: 0px; -fx-min-height: 0; -fx-pref-height: 0px; -fx-max-height: 0;");
    }

    @FXML
    void clickOnUpdate1(MouseEvent event) {
        Date updateTime = new Date();
        int result = this.service.updateAverageAgeOfUsersPerFavouriteCategory(updateTime, this.limit);
        if (result == 0) {
            this.data1.setText("Last update: " + dateAsString(updateTime) + "  ");
        } else {
            DialogManager.getInstance().createErrorAlert(this.mainPage, "Update failed");
        }
    }

    @FXML
    void clickOnUpdate2(MouseEvent event) {
        Date updateTime = new Date();
        int result = this.service.updatePodcastsWithHighestNumberOfReviews(updateTime, this.limit);
        if (result == 0) {
            this.data2.setText("Last update: " + dateAsString(updateTime) + "  ");
        } else {
            DialogManager.getInstance().createErrorAlert(this.mainPage, "Update failed");
        }
    }

    @FXML
    void clickOnUpdate3(MouseEvent event) {
        Date updateTime = new Date();
        int result = this.service.updateCountryWithHighestNumberOfPodcasts(updateTime, this.limit);
        if (result == 0) {
            this.data3.setText("Last update: " + dateAsString(updateTime) + "  ");
        } else {
            DialogManager.getInstance().createErrorAlert(this.mainPage, "Update failed");
        }
    }

    @FXML
    void clickOnUpdate4(MouseEvent event) {
        Date updateTime = new Date();
        int result = this.service.updateFavouriteCategoryForGender(updateTime, this.limit);
        if (result == 0) {
            this.data4.setText("Last update: " + dateAsString(updateTime) + "  ");
        } else {
            DialogManager.getInstance().createErrorAlert(this.mainPage, "Update failed");
        }
    }

    @FXML
    void clickOnUpdate5(MouseEvent event) {
        Date updateTime = new Date();
        int result = this.service.updateMostNumerousCategory(updateTime, this.limit);
        if (result == 0) {
            this.data5.setText("Last update: " + dateAsString(updateTime) + "  ");
        } else {
            DialogManager.getInstance().createErrorAlert(this.mainPage, "Update failed");
        }
    }

    @FXML
    void clickOnUpdate6(MouseEvent event) {
        Date updateTime = new Date();
        int result = this.service.updateMostAppreciatedCategory(updateTime, this.limit);
        if (result == 0) {
            this.data6.setText("Last update: " + dateAsString(updateTime) + "  ");
        } else {
            DialogManager.getInstance().createErrorAlert(this.mainPage, "Update failed");
        }
    }

    @FXML
    void clickOnUpdate7(MouseEvent event) {
        Date updateTime = new Date();
        int result = this.service.updateMostFollowedAuthor(updateTime, this.limit);
        if (result == 0) {
            this.data7.setText("Last update: " + dateAsString(updateTime) + "  ");
        } else {
            DialogManager.getInstance().createErrorAlert(this.mainPage, "Update failed");
        }
    }

    @FXML
    void clickOnUpdate8(MouseEvent event) {
        Date updateTime = new Date();
        int result = this.service.updateMostLikedPodcast(updateTime, this.limit);
        if (result == 0) {
            this.data8.setText("Last update: " + dateAsString(updateTime) + "  ");
        } else {
            DialogManager.getInstance().createErrorAlert(this.mainPage, "Update failed");
        }
    }

    @FXML
    void clickOnUpdate9(MouseEvent event) {
        Date updateTime = new Date();
        int result = this.service.updatePodcastsWithHighestAverageRating(updateTime, this.limit);
        if (result == 0) {
            this.data9.setText("Last update: " + dateAsString(updateTime) + "  ");
        } else {
            DialogManager.getInstance().createErrorAlert(this.mainPage, "Update failed");
        }
    }

    @FXML
    void clickOnUpdate10(MouseEvent event) {
        Date updateTime = new Date();
        int result = this.service.updatePodcastWithHighestAverageRatingPerCountry(updateTime, this.limit);
        if (result == 0) {
            this.data10.setText("Last update: " + dateAsString(updateTime) + "  ");
        } else {
            DialogManager.getInstance().createErrorAlert(this.mainPage, "Update failed");
        }
    }

    @FXML
    void mouseOnCancel(MouseEvent event) {
        this.cancelInfo.setStyle("-fx-border-color: #f44336; -fx-background-color: white; -fx-background-radius: 10; -fx-text-fill: black; -fx-border-radius: 10;-fx-cursor: hand;");
    }

    @FXML
    void mouseOnAddAdmin(MouseEvent event) {
        this.addAdmin.setStyle("-fx-border-color: #4CAF50; -fx-background-color: white; -fx-background-radius: 10; -fx-text-fill: black; -fx-border-radius: 10; -fx-cursor: hand;");
    }

    @FXML
    void mouseOnBox1(MouseEvent event) {
        this.box1.setStyle("-fx-background-color: #e5e5e5; -fx-background-radius: 10");
    }

    @FXML
    void mouseOnBox2(MouseEvent event) {
        this.box2.setStyle("-fx-background-color: #e5e5e5; -fx-background-radius: 10");
    }

    @FXML
    void mouseOnBox3(MouseEvent event) {
        this.box3.setStyle("-fx-background-color: #e5e5e5; -fx-background-radius: 10");
    }

    @FXML
    void mouseOnBox4(MouseEvent event) {
        this.box4.setStyle("-fx-background-color: #e5e5e5; -fx-background-radius: 10");
    }

    @FXML
    void mouseOnBox5(MouseEvent event) {
        this.box5.setStyle("-fx-background-color: #e5e5e5; -fx-background-radius: 10");
    }

    @FXML
    void mouseOnBox6(MouseEvent event) {
        this.box6.setStyle("-fx-background-color: #e5e5e5; -fx-background-radius: 10");
    }

    @FXML
    void mouseOnBox7(MouseEvent event) {
        this.box7.setStyle("-fx-background-color: #e5e5e5; -fx-background-radius: 10");
    }

    @FXML
    void mouseOnBox8(MouseEvent event) {
        this.box8.setStyle("-fx-background-color: #e5e5e5; -fx-background-radius: 10");
    }

    @FXML
    void mouseOnBox9(MouseEvent event) {
        this.box9.setStyle("-fx-background-color: #e5e5e5; -fx-background-radius: 10");
    }

    @FXML
    void mouseOnBox10(MouseEvent event) {
        this.box10.setStyle("-fx-background-color: #e5e5e5; -fx-background-radius: 10");
    }

    @FXML
    void mouseOnDelete(MouseEvent event) {
        this.delete.setStyle("-fx-border-color: #f44336; -fx-background-color: white; -fx-background-radius: 10; -fx-text-fill: black; -fx-border-radius: 10; -fx-cursor: hand;");
    }

    @FXML
    void mouseOnModify(MouseEvent event) {
        this.modifyInfo.setStyle("-fx-border-color: #008CBA; -fx-background-color: white; -fx-background-radius: 10; -fx-text-fill: black; -fx-border-radius: 10; -fx-cursor: hand;");
    }

    @FXML
    void mouseOnUpdate(MouseEvent event) {
        this.updateInfo.setStyle("-fx-border-color: #4CAF50; -fx-background-color: white; -fx-background-radius: 10; -fx-text-fill: black; -fx-border-radius: 10; -fx-cursor: hand;");
    }

    @FXML
    void mouseOnUpdate1(MouseEvent event) {
        this.update1.setStyle("-fx-border-color: #4CAF50; -fx-background-color: white; -fx-background-radius: 10; -fx-text-fill: black; -fx-border-radius: 10; -fx-cursor: hand;");
    }

    @FXML
    void mouseOnUpdate2(MouseEvent event) {
        this.update2.setStyle("-fx-border-color: #4CAF50; -fx-background-color: white; -fx-background-radius: 10; -fx-text-fill: black; -fx-border-radius: 10; -fx-cursor: hand;");
    }

    @FXML
    void mouseOnUpdate3(MouseEvent event) {
        this.update3.setStyle("-fx-border-color: #4CAF50; -fx-background-color: white; -fx-background-radius: 10; -fx-text-fill: black; -fx-border-radius: 10; -fx-cursor: hand;");
    }

    @FXML
    void mouseOnUpdate4(MouseEvent event) {
        this.update4.setStyle("-fx-border-color: #4CAF50; -fx-background-color: white; -fx-background-radius: 10; -fx-text-fill: black; -fx-border-radius: 10; -fx-cursor: hand;");
    }

    @FXML
    void mouseOnUpdate5(MouseEvent event) {
        this.update5.setStyle("-fx-border-color: #4CAF50; -fx-background-color: white; -fx-background-radius: 10; -fx-text-fill: black; -fx-border-radius: 10; -fx-cursor: hand;");
    }

    @FXML
    void mouseOnUpdate6(MouseEvent event) {
        this.update6.setStyle("-fx-border-color: #4CAF50; -fx-background-color: white; -fx-background-radius: 10; -fx-text-fill: black; -fx-border-radius: 10; -fx-cursor: hand;");
    }

    @FXML
    void mouseOnUpdate7(MouseEvent event) {
        this.update7.setStyle("-fx-border-color: #4CAF50; -fx-background-color: white; -fx-background-radius: 10; -fx-text-fill: black; -fx-border-radius: 10; -fx-cursor: hand;");
    }

    @FXML
    void mouseOnUpdate8(MouseEvent event) {
        this.update8.setStyle("-fx-border-color: #4CAF50; -fx-background-color: white; -fx-background-radius: 10; -fx-text-fill: black; -fx-border-radius: 10; -fx-cursor: hand;");
    }

    @FXML
    void mouseOnUpdate9(MouseEvent event) {
        this.update9.setStyle("-fx-border-color: #4CAF50; -fx-background-color: white; -fx-background-radius: 10; -fx-text-fill: black; -fx-border-radius: 10; -fx-cursor: hand;");
    }

    @FXML
    void mouseOnUpdate10(MouseEvent event) {
        this.update10.setStyle("-fx-border-color: #4CAF50; -fx-background-color: white; -fx-background-radius: 10; -fx-text-fill: black; -fx-border-radius: 10; -fx-cursor: hand;");
    }

    @FXML
    void mouseOutAddAdmin(MouseEvent event) {
        this.addAdmin.setStyle("-fx-border-color: transparent; -fx-background-color: #4CAF50; -fx-background-radius: 10; -fx-text-fill: white; -fx-border-radius: 10; -fx-cursor: default;");
    }

    @FXML
    void mouseOutBox1(MouseEvent event) {
        this.box1.setStyle("-fx-background-color: transparent; -fx-background-radius: 10");
    }

    @FXML
    void mouseOutBox2(MouseEvent event) {
        this.box2.setStyle("-fx-background-color: transparent; -fx-background-radius: 10");
    }

    @FXML
    void mouseOutBox3(MouseEvent event) {
        this.box3.setStyle("-fx-background-color: transparent; -fx-background-radius: 10");
    }

    @FXML
    void mouseOutBox4(MouseEvent event) {
        this.box4.setStyle("-fx-background-color: transparent; -fx-background-radius: 10");
    }

    @FXML
    void mouseOutBox5(MouseEvent event) {
        this.box5.setStyle("-fx-background-color: transparent; -fx-background-radius: 10");
    }

    @FXML
    void mouseOutBox6(MouseEvent event) {
        this.box6.setStyle("-fx-background-color: transparent; -fx-background-radius: 10");
    }

    @FXML
    void mouseOutBox7(MouseEvent event) {
        this.box7.setStyle("-fx-background-color: transparent; -fx-background-radius: 10");
    }

    @FXML
    void mouseOutBox8(MouseEvent event) {
        this.box8.setStyle("-fx-background-color: transparent; -fx-background-radius: 10");
    }

    @FXML
    void mouseOutBox9(MouseEvent event) {
        this.box9.setStyle("-fx-background-color: transparent; -fx-background-radius: 10");
    }

    @FXML
    void mouseOutBox10(MouseEvent event) {
        this.box10.setStyle("-fx-background-color: transparent; -fx-background-radius: 10");
    }

    @FXML
    void mouseOutCancel(MouseEvent event) {
        this.cancelInfo.setStyle("-fx-border-color: transparent; -fx-background-color: #f44336; -fx-background-radius: 10; -fx-text-fill: white; -fx-border-radius: 10; -fx-cursor: default;");
    }

    @FXML
    void mouseOutDelete(MouseEvent event) {
        this.delete.setStyle("-fx-border-color: transparent; -fx-background-color: #f44336; -fx-background-radius: 10; -fx-text-fill: white; -fx-border-radius: 10; -fx-cursor: default;");
    }

    @FXML
    void mouseOutModify(MouseEvent event) {
        this.modifyInfo.setStyle("-fx-border-color: transparent; -fx-background-color: #008CBA; -fx-background-radius: 10; -fx-text-fill: white; -fx-border-radius: 10; -fx-cursor: default;");
    }

    @FXML
    void mouseOutUpdate(MouseEvent event) {
        this.updateInfo.setStyle("-fx-border-color: transparent; -fx-background-color: #4CAF50; -fx-background-radius: 10; -fx-text-fill: white; -fx-border-radius: 10; -fx-cursor: default;");
    }

    @FXML
    void mouseOutUpdate1(MouseEvent event) {
        this.update1.setStyle("-fx-border-color: transparent; -fx-background-color: #4CAF50; -fx-background-radius: 10; -fx-text-fill: white; -fx-border-radius: 10; -fx-cursor: default;");
    }

    @FXML
    void mouseOutUpdate2(MouseEvent event) {
        this.update2.setStyle("-fx-border-color: transparent; -fx-background-color: #4CAF50; -fx-background-radius: 10; -fx-text-fill: white; -fx-border-radius: 10; -fx-cursor: default;");
    }

    @FXML
    void mouseOutUpdate3(MouseEvent event) {
        this.update3.setStyle("-fx-border-color: transparent; -fx-background-color: #4CAF50; -fx-background-radius: 10; -fx-text-fill: white; -fx-border-radius: 10; -fx-cursor: default;");
    }

    @FXML
    void mouseOutUpdate4(MouseEvent event) {
        this.update4.setStyle("-fx-border-color: transparent; -fx-background-color: #4CAF50; -fx-background-radius: 10; -fx-text-fill: white; -fx-border-radius: 10; -fx-cursor: default;");
    }

    @FXML
    void mouseOutUpdate5(MouseEvent event) {
        this.update5.setStyle("-fx-border-color: transparent; -fx-background-color: #4CAF50; -fx-background-radius: 10; -fx-text-fill: white; -fx-border-radius: 10; -fx-cursor: default;");
    }

    @FXML
    void mouseOutUpdate6(MouseEvent event) {
        this.update6.setStyle("-fx-border-color: transparent; -fx-background-color: #4CAF50; -fx-background-radius: 10; -fx-text-fill: white; -fx-border-radius: 10; -fx-cursor: default;");
    }

    @FXML
    void mouseOutUpdate7(MouseEvent event) {
        this.update7.setStyle("-fx-border-color: transparent; -fx-background-color: #4CAF50; -fx-background-radius: 10; -fx-text-fill: white; -fx-border-radius: 10; -fx-cursor: default;");
    }

    @FXML
    void mouseOutUpdate8(MouseEvent event) {
        this.update8.setStyle("-fx-border-color: transparent; -fx-background-color: #4CAF50; -fx-background-radius: 10; -fx-text-fill: white; -fx-border-radius: 10; -fx-cursor: default;");
    }

    @FXML
    void mouseOutUpdate9(MouseEvent event) {
        this.update9.setStyle("-fx-border-color: transparent; -fx-background-color: #4CAF50; -fx-background-radius: 10; -fx-text-fill: white; -fx-border-radius: 10; -fx-cursor: default;");
    }

    @FXML
    void mouseOutUpdate10(MouseEvent event) {
        this.update10.setStyle("-fx-border-color: transparent; -fx-background-color: #4CAF50; -fx-background-radius: 10; -fx-text-fill: white; -fx-border-radius: 10; -fx-cursor: default;");
    }

    public void initialize() throws IOException {
        Admin admin = (Admin)MyPodcastDB.getInstance().getSessionActor();
        this.admin = admin;
        this.service = new AdminService();
        this.title.setText("Welcome " + this.admin.getName());
        this.limit = 10;
        this.nameTextField.setText(this.admin.getName());
        this.emailTextField.setText(this.admin.getEmail());
        this.passwordTextField.setText("********************");

        // load chart
        List<String> updateTimes = this.loadCharts();
        if (updateTimes == null)
            return;

        // update the last update data of queries
        this.data1.setText("Last update: " + updateTimes.get(0) + "  ");
        this.data2.setText("Last update: " + updateTimes.get(1) + "  ");
        this.data3.setText("Last update: " + updateTimes.get(2) + "  ");
        this.data4.setText("Last update: " + updateTimes.get(3) + "  ");
        this.data5.setText("Last update: " + updateTimes.get(4) + "  ");
        this.data6.setText("Last update: " + updateTimes.get(5) + "  ");
        this.data7.setText("Last update: " + updateTimes.get(6) + "  ");
        this.data8.setText("Last update: " + updateTimes.get(7) + "  ");
        this.data9.setText("Last update: " + updateTimes.get(8) + "  ");
        this.data10.setText("Last update: " + updateTimes.get(9) + "  ");

        // settings buttons and texts
//        this.modifyInfoWrapper.setVisible(true);
//        this.updateCancelWrapper.setVisible(false);
//        this.modifyInfoWrapper.setStyle("-fx-max-height: 40;");
//        this.updateCancelWrapper.setStyle("-fx-max-height: 0;");
    }

    private List<String> loadCharts() throws IOException {
        // create the lists that will contain statistics
        List<Pair<String, Float>> averageAgeOfUsersPerFavouriteCategory = new ArrayList<>();
        List<Pair<Podcast, Integer>> podcastsWithHighestNumberOfReviews = new ArrayList<>();
        List<Pair<String, Integer>> countryWithHighestNumberOfPodcasts = new ArrayList<>();
        Triplet<List<String>, List<String>, List<String>> topFavouriteCategoriesPerGender = new Triplet<>(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        List<Pair<String, Integer>> mostNumerousCategories = new ArrayList<>();
        List<Pair<String, Integer>> mostAppreciatedCategory = new ArrayList<>();

        // load them from service
        List<String> updateTimes = this.service.loadAdminPage(averageAgeOfUsersPerFavouriteCategory, podcastsWithHighestNumberOfReviews, countryWithHighestNumberOfPodcasts, topFavouriteCategoriesPerGender, mostNumerousCategories, mostAppreciatedCategory);
        if (updateTimes == null) {
            DialogManager.getInstance().createErrorAlert(this.mainPage, "Something gone wrong!");
            return null;
        }

        // statistics creation
        int row = 1;
        int column = 0;

        // create new bar chart element
        FXMLLoader barLoader = new FXMLLoader();
        barLoader.setLocation(getClass().getClassLoader().getResource("BarChart.fxml"));
        AnchorPane newBarChart1 = barLoader.load();
        BarChartController controller1 = barLoader.getController();
        controller1.setDataFloat("Average age for favourite category", new String[] {"Category", "Average age"}, "Average age", averageAgeOfUsersPerFavouriteCategory, this.mainPage);
        this.statisticsGrid.add(newBarChart1, column++, row);

        // create new bar chart element
        barLoader = new FXMLLoader();
        barLoader.setLocation(getClass().getClassLoader().getResource("BarChart.fxml"));
        AnchorPane newBarChart2 = barLoader.load();
        BarChartController controller2 = barLoader.getController();
        controller2.setDataPodcastInteger("Podcasts with highest number of reviews", new String[] {"Podcast", "Number of reviews"}, "Number of reviews", podcastsWithHighestNumberOfReviews, this.mainPage);
        this.statisticsGrid.add(newBarChart2, column++, row);

        // create new bar chart element
        barLoader = new FXMLLoader();
        barLoader.setLocation(getClass().getClassLoader().getResource("BarChart.fxml"));
        AnchorPane newBarChart3 = barLoader.load();
        BarChartController controller3 = barLoader.getController();
        controller3.setDataInteger("Country with highest number of podcasts", new String[] {"Country", "Number of podcast"}, "Number of podcasts", countryWithHighestNumberOfPodcasts, this.mainPage);
        this.statisticsGrid.add(newBarChart3, column, row++);
        column -= 2;

        // create new pie chart element
        FXMLLoader tableLoader = new FXMLLoader();
        tableLoader.setLocation(getClass().getClassLoader().getResource("Table.fxml"));
        AnchorPane newTable = tableLoader.load();
        TableController controller4 = tableLoader.getController();
        controller4.setData("Top favourite categories", new String[]{"Female", "Male", "Not Binary"}, topFavouriteCategoriesPerGender);
        this.statisticsGrid.add(newTable, column++, row);

        // create new pie chart element
        FXMLLoader pieLoader = new FXMLLoader();
        pieLoader.setLocation(getClass().getClassLoader().getResource("PieChart.fxml"));
        AnchorPane newPieChart1 = pieLoader.load();
        PieChartController controller5 = pieLoader.getController();
        controller5.setData("Most numerous category", new String[] {"Category", "Number of podcasts"}, mostNumerousCategories, this.mainPage);
        this.statisticsGrid.add(newPieChart1, column++, row);

        // create new pie chart element
        pieLoader = new FXMLLoader();
        pieLoader.setLocation(getClass().getClassLoader().getResource("PieChart.fxml"));
        AnchorPane newPieChart2 = pieLoader.load();
        PieChartController controller6 = pieLoader.getController();
        controller6.setData("Most appreciated category", new String[] {"Category", "Number of likes"}, mostAppreciatedCategory, this.mainPage);
        this.statisticsGrid.add(newPieChart2, column, row);

        return updateTimes;
    }

    private String dateAsString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateAsString = dateFormat.format(date);
        return dateAsString;
    }
}
