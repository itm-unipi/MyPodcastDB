package it.unipi.dii.lsmsdb.myPodcastDB.view;

import it.unipi.dii.lsmsdb.myPodcastDB.Main;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.Neo4jManager;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StageManager {
    private static Stage primaryStage;
    private static Application application;

    public static void initialize(Stage stage, String title, Application main) throws IOException {
        primaryStage = stage;
        application = main;
        Parent root = FXMLLoader.load(application.getClass().getClassLoader().getResource(ViewNavigator.SIGNUP.getPage()));
        primaryStage.setTitle(title);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void showPage(String newPage) throws IOException {
        Parent root = FXMLLoader.load(application.getClass().getClassLoader().getResource(newPage));
        primaryStage.getScene().setRoot(root);
        primaryStage.show();
    }
}
