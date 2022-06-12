package it.unipi.dii.lsmsdb.myPodcastDB;

import it.unipi.dii.lsmsdb.myPodcastDB.cache.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        ImageCache.initializeCache(this);
        StageManager.initialize(stage, "MyPodcastDB", this);
    }

    public static void main(String[] args) {
        MyPodcastDB myPodcastDB = MyPodcastDB.getInstance();
        myPodcastDB.run();
        launch();
    }
}
