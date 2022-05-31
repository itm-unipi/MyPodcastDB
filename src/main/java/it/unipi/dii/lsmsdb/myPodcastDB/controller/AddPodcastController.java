package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DialogPane;

public class AddPodcastController {

    @FXML
    private ChoiceBox<String> choiceBoxCategories;

    @FXML
    private ChoiceBox<String> choiceBoxCountry;

    @FXML
    private ChoiceBox<String> choiceBoxPrimaryCategory;

    public void initialize() {
        // Add choice for Country
        choiceBoxCountry.getItems().add("Choice 1");

        // Add choice for Category
        choiceBoxPrimaryCategory.getItems().add("Choice 1");

        // Add choice for Secondary Categories
        choiceBoxCategories.getItems().add("Choice 1");
    }
}
