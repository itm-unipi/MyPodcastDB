package it.unipi.dii.lsmsdb.myPodcastDB;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("src/java/it/unipi/dii/lsmsdb/myPodcastDB/view/Test.fxml"));
        primaryStage.setTitle("myPodcastDB");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        MyPodcastDB myPodcastDB = new MyPodcastDB();
        myPodcastDB.run();
        Application.launch(args);
    }
}
