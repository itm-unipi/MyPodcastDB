package it.unipi.dii.lsmsdb.myPodcastDB.view;

import it.unipi.dii.lsmsdb.myPodcastDB.Main;
import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.controller.PodcastPageController;
import it.unipi.dii.lsmsdb.myPodcastDB.controller.PodcastPreviewController;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.Neo4jManager;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class StageManager {
    private static Stage primaryStage;
    private static Application application;
    private static String objectIdentifier;

    public static void initialize(Stage stage, String title, Application main) throws IOException {
        primaryStage = stage;
        application = main;
        Parent root = FXMLLoader.load(application.getClass().getClassLoader().getResource(ViewNavigator.LOGIN.getPage()));
        primaryStage.setTitle(title);
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(ImageCache.getImageFromLocalPath("/img/logo.png" ));
        primaryStage.show();
        MyPodcastDB.getInstance().setSessionPage(ViewNavigator.LOGIN.getPage());
    }

    public static void showPage(String newPage) throws IOException {
        MyPodcastDB.getInstance().setSessionPage(newPage);
        Parent root = FXMLLoader.load(application.getClass().getClassLoader().getResource(newPage));
        primaryStage.getScene().setRoot(root);
        primaryStage.show();
    }

    public static void showPage(String newPage, String objectIdentifier) throws IOException {
        setObjectIdentifier(objectIdentifier);
        showPage(newPage);
    }

    public static String getObjectIdentifier() {
        return objectIdentifier;
    }

    public static void setObjectIdentifier(String objectIdentifier) {
        StageManager.objectIdentifier = objectIdentifier;
    }
}
