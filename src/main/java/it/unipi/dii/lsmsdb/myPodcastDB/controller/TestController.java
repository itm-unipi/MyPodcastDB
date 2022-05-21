package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.PodcastPreview;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestController {

    @FXML
    private Button buttonTest;

    @FXML
    private ImageView imageTest;

    @FXML
    private GridPane podcastList;

    @FXML
    void testClick(MouseEvent event) throws IOException {
        StageManager.showPage(ViewNavigator.TEST2.getPage());
    }

    public void initialize() throws IOException {
        Image image = new Image("https://www.gruppostea.it/wp-content/uploads/Logo_stea_positivo.png");
        imageTest.setImage(image);

        // service simulation
        List<PodcastPreview> test = new ArrayList<>();
        PodcastPreview p1 = new PodcastPreview("54eb342567c94dacfb2a3e50", "Scaling Global", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts126/v4/ab/41/b7/ab41b798-1a5c-39b6-b1b9-c7b6d29f2075/mza_4840098199360295509.jpg/600x600bb.jpg");
        PodcastPreview p2 = new PodcastPreview("34e734b09246d17dc5d56f63", "Cornerstone Baptist Church of Orlando", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts125/v4/d3/06/0f/d3060ffe-613b-74d6-9594-cca7a874cd6c/mza_12661332092752927859.jpg/60x60bb.jpg");
        test.add(p1);
        test.add(p2);

        // fill the grid
        int row = 0;
        int column = 0;
        for (PodcastPreview podcast : test) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("PodcastPreview.fxml"));

            // create new podcast element
            AnchorPane newPodcast = fxmlLoader.load();
            PodcastPreviewController controller = fxmlLoader.getController();
            controller.setData(podcast);

            // add new podcast to grid
            this.podcastList.add(newPodcast, column++, row);
        }
    }

    @FXML
    void onImageClick(MouseEvent event) {
        Logger.success("On Pippo");
    }

}