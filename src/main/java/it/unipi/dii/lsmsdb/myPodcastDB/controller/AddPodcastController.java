package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.service.AuthorService;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.JsonDecode;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddPodcastController {
    @FXML
    private TextField artworkURLField;

    @FXML
    private TextField authorNameField;

    @FXML
    private TextField contentAdvisoryRatingField;

    @FXML
    private TextField podcastNameField;

    @FXML
    private DatePicker releaseDateField;

    @FXML
    private ComboBox<String> comboBoxCountry;

    @FXML
    private ComboBox<String> comboBoxPrimaryCategory;

    @FXML
    private GridPane gridSecondaryCategories;

    @FXML
    private DialogPane dialogPane;

    /********** RESET BORDER EMPTY FIELDS **********/

    @FXML
    void restoreBorderTextField(MouseEvent event) {
        ((TextField)event.getSource()).setStyle("-fx-border-radius: 4; -fx-border-color: transparent");
    }

    @FXML
    void restoreBorderComboBox(MouseEvent event) {
        ((ComboBox)event.getSource()).setStyle("-fx-border-radius: 4; -fx-border-color: transparent");
    }

    @FXML
    void restoreBorderDatePicker(MouseEvent event) {
        ((DatePicker)event.getSource()).setStyle("-fx-border-radius: 4; -fx-border-color: transparent");
    }

    /*********************************************/

    @FXML
    void btnAddPodcast(ActionEvent event) throws IOException {
        boolean emptyFields = false;

        if (podcastNameField.getText().equals("")) {
            podcastNameField.setStyle("-fx-border-radius: 4; -fx-border-color: #ff7676");
            emptyFields = true;
        }

        if (artworkURLField.getText().equals("")) {
            artworkURLField.setStyle("-fx-border-radius: 4; -fx-border-color: #ff7676");
            emptyFields = true;
        }

        if (contentAdvisoryRatingField.getText().equals("")) {
            contentAdvisoryRatingField.setStyle("-fx-border-radius: 4; -fx-border-color: #ff7676");
            emptyFields = true;
        }

        if (comboBoxCountry.getValue() == null) {
            comboBoxCountry.setStyle("-fx-border-radius: 4; -fx-border-color: #ff7676");
            emptyFields = true;
        }

        if (comboBoxPrimaryCategory.getValue() == null) {
            comboBoxPrimaryCategory.setStyle("-fx-border-radius: 4; -fx-border-color: #ff7676");
            emptyFields = true;
        }

        if (releaseDateField.getValue() == null) {
            releaseDateField.setStyle("-fx-border-radius: 4; -fx-border-color: #ff7676");
            emptyFields = true;
        }

        if (!emptyFields) {
            String podcastName = podcastNameField.getText();
            String authorId = ((Author) MyPodcastDB.getInstance().getSessionActor()).getId();
            String authorName = ((Author) MyPodcastDB.getInstance().getSessionActor()).getName();
            String artworkUrl600 = artworkURLField.getText();
            String contentAdvisoryRating = contentAdvisoryRatingField.getText();
            String country = comboBoxCountry.getValue();
            String primaryCategory = comboBoxPrimaryCategory.getValue();
            List<String> categories = new ArrayList<>();

            for (Node child : gridSecondaryCategories.getChildren()) {
                boolean isSelected = ((CheckBox) child).isSelected();
                if (isSelected)
                    categories.add(((CheckBox) child).getText());
            }

            LocalDate localDate = releaseDateField.getValue();
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            Date releaseDate = Date.from(instant);

            Podcast podcast = new Podcast("0", podcastName, authorId, authorName, "", artworkUrl600, contentAdvisoryRating, country, primaryCategory, categories, releaseDate);
            Logger.info("PODCAST TO ADD: " + podcast.toString());

            AuthorService authorService = new AuthorService();
            int addResult = authorService.addPodcast(podcast);

            if (addResult == 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initOwner(dialogPane.getScene().getWindow());
                alert.setTitle("Podcast added!");
                alert.setHeaderText(null);
                alert.setContentText("Podcast successfully added!");
                alert.setGraphic(null);
                alert.showAndWait();
                closeStage(event);

                Logger.success("Added " + podcast);
                StageManager.showPage(ViewNavigator.PODCASTPAGE.getPage(), podcast.getId());

            } else {
                Logger.error("Error during the creation of a podcast");

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(dialogPane.getScene().getWindow());
                alert.setTitle("Add podcast error");
                alert.setHeaderText(null);
                alert.setContentText("Something went wrong during the adding of the podcast.");
                alert.setGraphic(null);
                alert.showAndWait();
            }
        }
    }

    @FXML
    void btnCancel(ActionEvent event) {
        closeStage(event);
    }

    @FXML
    void exit(ActionEvent event) {
        closeStage(event);
    }

    private void closeStage(ActionEvent event) {
        Node source = (Node)event.getSource();
        Stage stage = (Stage)source.getScene().getWindow();
        stage.close();
    }

    public void initialize() throws Exception {
        // Add choice for country field
        for (String country: JsonDecode.getCountries()) {
            comboBoxCountry.getItems().add(country);
        }

        // Add choices for category fields
        int row = 0;
        int column = 0;
        for (String category: JsonDecode.getCategories()) {
            comboBoxPrimaryCategory.getItems().add(category);

            CheckBox newCategory = new CheckBox(category);
            newCategory.setStyle("-fx-font-family: Corbel; -fx-font-size: 13");
            gridSecondaryCategories.add(newCategory, column++, row);

            if (column == 3) {
                column = 0;
                row++;
            }
        }

        authorNameField.setText(((Author)MyPodcastDB.getInstance().getSessionActor()).getName());
    }
}
