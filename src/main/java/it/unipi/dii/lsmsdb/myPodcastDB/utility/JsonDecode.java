package it.unipi.dii.lsmsdb.myPodcastDB.utility;

import java.util.ArrayList;
import java.util.List;

public class JsonDecode {

    private static JsonDecode jsonDecode;

    public JsonDecode(){


    }

    public static List<String> getCountries(){

        //TODO import them from json file in resources/json
        List<String> countries = new ArrayList<>();
        countries.add("Italy");
        countries.add("Germany");
        countries.add("France");
        countries.add("USA");
        countries.add("Canada");
        countries.add("Spain");
        countries.add("Turkey");
        countries.add("UK");
        countries.add("Mexico");
        countries.add("China");

        return countries;

    }

    public static List<String> getCategories(){

        //TODO import them from json file in resources/json
        List<String> categories = new ArrayList<>();
        categories.add("Horror");
        categories.add("Crime");
        categories.add("Business");
        categories.add("Sport");
        categories.add("Videogames");
        categories.add("Foods");
        categories.add("Healt");
        categories.add("Nature");
        categories.add("History");
        categories.add("Geography");

        return categories;

    }

    public static JsonDecode getInstance(){
        if(jsonDecode == null)
            jsonDecode = new JsonDecode();
        return jsonDecode;
    }
}
