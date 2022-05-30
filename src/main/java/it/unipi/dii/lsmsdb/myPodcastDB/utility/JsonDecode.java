package it.unipi.dii.lsmsdb.myPodcastDB.utility;

import java.util.ArrayList;
import java.util.List;
import java.io.FileReader;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class JsonDecode {

    private static JsonDecode jsonDecode;

    public JsonDecode(){

    }

    public static List<String> getCountries() throws Exception {

        Object object = new JSONParser().parse(new FileReader("src/main/resources/json/countries.json"));
        JSONObject jobject = (JSONObject)object;
        JSONArray jarray = (JSONArray) jobject.get("countries");
        Iterator iterator = jarray.iterator();

        List<String> countries = new ArrayList<>();
        while(iterator.hasNext())
            countries.add(iterator.next().toString());

        return countries;

    }

    public static List<String> getCategories() throws Exception{

        Object object = new JSONParser().parse(new FileReader("src/main/resources/json/categories.json"));
        JSONObject jobject = (JSONObject)object;
        JSONArray jarray = (JSONArray) jobject.get("categories");
        Iterator iterator = jarray.iterator();

        List<String> categories = new ArrayList<>();
        while(iterator.hasNext())
            categories.add(iterator.next().toString());

        return categories;

    }

    public static JsonDecode getInstance(){
        if(jsonDecode == null)
            jsonDecode = new JsonDecode();
        return jsonDecode;
    }
}
