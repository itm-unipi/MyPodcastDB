package it.unipi.dii.lsmsdb.myPodcastDB.view;

import it.unipi.dii.lsmsdb.myPodcastDB.cache.ImageCache;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Optional;

public class DialogManager {

    private static DialogManager dialogManager;

    public DialogManager() {
    }

    public boolean createConfirmationAlert(BorderPane mainPage, String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", new ButtonType("OK"), new ButtonType("CANCEL"));
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ImageCache.getImageFromLocalPath("/img/browse_podcasts_64px.png"));
        alert.setTitle(null);
        alert.setHeaderText(msg);
        alert.setContentText(null);
        alert.setGraphic(new ImageView(ImageCache.getImageFromLocalPath("/img/error_100px.png")));
        alert.initOwner(mainPage.getScene().getWindow());

        mainPage.setEffect(new BoxBlur(3, 3, 3));
        Optional<ButtonType> result = alert.showAndWait();
        mainPage.setEffect(null);


        if (result.get().getText().equals(alert.getButtonTypes().get(0).getText())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean createConfirmationAlert(AnchorPane mainPage, String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", new ButtonType("OK"), new ButtonType("CANCEL"));
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ImageCache.getImageFromLocalPath("/img/browse_podcasts_64px.png"));
        alert.setTitle(null);
        alert.setHeaderText(msg);
        alert.setContentText(null);
        alert.setGraphic(new ImageView(ImageCache.getImageFromLocalPath("/img/error_100px.png")));
        alert.initOwner(mainPage.getScene().getWindow());

        mainPage.setEffect(new BoxBlur(3, 3, 3));
        Optional<ButtonType> result = alert.showAndWait();
        mainPage.setEffect(null);


        if (result.get().getText().equals(alert.getButtonTypes().get(0).getText())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean createConfirmationAlert(BorderPane mainPage, String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", new ButtonType("OK"), new ButtonType("CANCEL"));
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ImageCache.getImageFromLocalPath("/img/browse_podcasts_64px.png"));
        alert.setTitle(title);
        alert.setHeaderText(msg);
        alert.setContentText(null);
        alert.setGraphic(new ImageView(ImageCache.getImageFromLocalPath("/img/error_100px.png")));
        alert.initOwner(mainPage.getScene().getWindow());

        mainPage.setEffect(new BoxBlur(3, 3, 3));
        Optional<ButtonType> result = alert.showAndWait();
        mainPage.setEffect(null);


        if (result.get().getText().equals(alert.getButtonTypes().get(0).getText())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean createConfirmationAlert(DialogPane mainPage, String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", new ButtonType("OK"), new ButtonType("CANCEL"));
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ImageCache.getImageFromLocalPath("/img/browse_podcasts_64px.png"));
        alert.setTitle(title);
        alert.setHeaderText(msg);
        alert.setContentText(null);
        alert.setGraphic(new ImageView(ImageCache.getImageFromLocalPath("/img/error_100px.png")));
        alert.initOwner(mainPage.getScene().getWindow());

        mainPage.setEffect(new BoxBlur(3, 3, 3));
        Optional<ButtonType> result = alert.showAndWait();
        mainPage.setEffect(null);


        if (result.get().getText().equals(alert.getButtonTypes().get(0).getText())) {
            return true;
        } else {
            return false;
        }
    }

    public void createErrorAlert(BorderPane mainPage, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ImageCache.getImageFromLocalPath("/img/browse_podcasts_64px.png"));
        alert.setTitle("Error!");
        alert.setHeaderText(msg);
        alert.setGraphic(new ImageView(ImageCache.getImageFromLocalPath("/img/error_100px.png")));
        alert.setContentText(null);
        alert.initOwner(mainPage.getScene().getWindow());

        mainPage.setEffect(new BoxBlur(3, 3, 3));
        alert.showAndWait();
        mainPage.setEffect(null);
    }

    public void createErrorAlert(BorderPane mainPage, String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ImageCache.getImageFromLocalPath("/img/browse_podcasts_64px.png"));
        alert.setTitle(title);
        alert.setHeaderText(msg);
        alert.setGraphic(new ImageView(ImageCache.getImageFromLocalPath("/img/error_100px.png")));
        alert.setContentText(null);
        alert.initOwner(mainPage.getScene().getWindow());

        mainPage.setEffect(new BoxBlur(3, 3, 3));
        alert.showAndWait();
        mainPage.setEffect(null);
    }

    public void createErrorAlert(DialogPane mainPage, String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ImageCache.getImageFromLocalPath("/img/browse_podcasts_64px.png"));
        alert.setTitle(title);
        alert.setHeaderText(msg);
        alert.setGraphic(new ImageView(ImageCache.getImageFromLocalPath("/img/error_100px.png")));
        alert.setContentText(null);
        alert.initOwner(mainPage.getScene().getWindow());

        mainPage.setEffect(new BoxBlur(3, 3, 3));
        alert.showAndWait();
        mainPage.setEffect(null);
    }

    public void createErrorAlert(AnchorPane mainPage, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ImageCache.getImageFromLocalPath("/img/browse_podcasts_64px.png"));
        alert.setTitle("Error!");
        alert.setHeaderText(msg);
        alert.setGraphic(new ImageView(ImageCache.getImageFromLocalPath("/img/error_100px.png")));
        alert.setContentText(null);
        alert.initOwner(mainPage.getScene().getWindow());

        mainPage.setEffect(new BoxBlur(3, 3, 3));
        alert.showAndWait();
        mainPage.setEffect(null);
    }

    public void createInformationAlert(BorderPane mainPage, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ImageCache.getImageFromLocalPath("/img/browse_podcasts_64px.png"));
        alert.setTitle("Information!");
        alert.setHeaderText(msg);
        alert.setContentText(null);
        alert.setGraphic(new ImageView(ImageCache.getImageFromLocalPath("/img/info_80px.png")));
        alert.initOwner(mainPage.getScene().getWindow());

        mainPage.setEffect(new BoxBlur(3, 3, 3));
        alert.showAndWait();
        mainPage.setEffect(null);
    }

    public void createInformationAlert(BorderPane mainPage, String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ImageCache.getImageFromLocalPath("/img/browse_podcasts_64px.png"));
        alert.setTitle(title);
        alert.setHeaderText(msg);
        alert.setContentText(null);
        alert.setGraphic(new ImageView(ImageCache.getImageFromLocalPath("/img/info_80px.png")));
        alert.initOwner(mainPage.getScene().getWindow());

        mainPage.setEffect(new BoxBlur(3, 3, 3));
        alert.showAndWait();
        mainPage.setEffect(null);
    }

    public void createInformationAlert(DialogPane mainPage, String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ImageCache.getImageFromLocalPath("/img/browse_podcasts_64px.png"));
        alert.setTitle(title);
        alert.setHeaderText(msg);
        alert.setContentText(null);
        alert.setGraphic(new ImageView(ImageCache.getImageFromLocalPath("/img/info_80px.png")));
        alert.initOwner(mainPage.getScene().getWindow());

        mainPage.setEffect(new BoxBlur(3, 3, 3));
        alert.showAndWait();
        mainPage.setEffect(null);
    }

    public void createInformationAlert(AnchorPane mainPage, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ImageCache.getImageFromLocalPath("/img/browse_podcasts_64px.png"));
        alert.setTitle("Information!");
        alert.setHeaderText(msg);
        alert.setContentText(null);
        alert.setGraphic(new ImageView(ImageCache.getImageFromLocalPath("/img/info_80px.png")));
        alert.initOwner(mainPage.getScene().getWindow());

        mainPage.setEffect(new BoxBlur(3, 3, 3));
        alert.showAndWait();
        mainPage.setEffect(null);
    }

    public static DialogManager getInstance() {
        if (dialogManager == null)
            dialogManager = new DialogManager();

        return dialogManager;
    }
}