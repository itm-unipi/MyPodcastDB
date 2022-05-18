package it.unipi.dii.lsmsdb.myPodcastDB;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Test.fxml"));
        stage.setTitle("myPodcastDB");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        MyPodcastDB myPodcastDB = new MyPodcastDB();
        myPodcastDB.run();
        launch();
    }
}
