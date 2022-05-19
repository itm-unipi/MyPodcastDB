package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class TestController {

    @FXML
    private Button buttonTest;

    @FXML
    private ImageView imageTest;

    @FXML
    void testClick(MouseEvent event) {
        Logger.success("We did it!");
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Test2.fxml"));
        stage.setTitle("myPodcastDB");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void initialize() {
        Image image = new Image("https://www.gruppostea.it/wp-content/uploads/Logo_stea_positivo.png");
        imageTest.setImage(image);
    }

    @FXML
    void onImageClick(MouseEvent event) {
        Logger.success("On Pippo");
    }

}