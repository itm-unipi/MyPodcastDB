package it.unipi.dii.lsmsdb.myPodcastDB;

import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        StageManager.initialize(stage, "myPodcastDB", this);
    }

    public static void main(String[] args) {
        MyPodcastDB myPodcastDB = new MyPodcastDB();
        myPodcastDB.run();
        launch();
    }
}
