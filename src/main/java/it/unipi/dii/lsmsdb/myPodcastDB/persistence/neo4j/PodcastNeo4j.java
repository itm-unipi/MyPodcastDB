package it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import static org.neo4j.driver.Values.parameters;
import org.neo4j.driver.Record;
import org.neo4j.driver.Value;

public class PodcastNeo4j {

    // ------------------------------- CRUD OPERATION ----------------------------------- //

    // --------- CREATE --------- //

    public boolean addPodcast(Podcast podcast) {
        return false;
    }

    public boolean addPodcastBelongsToCategory(Podcast podcast) {
        return false;
    }

    public boolean addPodcastCreatedByAuthor(Podcast podcast) {
        return false;
    }

    // ---------- READ ---------- //

    public List<Entry<String, String>> findPodcastsByName(String name) {
        Neo4jManager manager = Neo4jManager.getInstance();
        List<Record> result = null;
        try {
            result = manager.read(
                    "MATCH (p:Podcast {name: $name}) RETURN p LIMIT 10",
                    parameters("name", name)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result == null)
            return null;

        List<Entry<String, String>> podcasts = new ArrayList<>();
        for (Record record : result) {
            String podcastId = record.get(0).get("podcastId").asString();
            String podcastName = record.get(0).get("name").asString();

            Entry<String, String> podcast = new AbstractMap.SimpleEntry<>(podcastId, podcastName);
            podcasts.add(podcast);
        }

        return podcasts;
    }

    // --------- UPDATE --------- //

    public boolean updatePodcast(Podcast podcast) {
        return false;
    }

    // --------- DELETE --------- //

    public boolean deletePodcastById(int id) {
        return false;
    }

    public boolean deletePodcastByPodcastId(String id) {
        return false;
    }

    public int deletePodcastsByName(String name) {
        return -1;
    }

    public boolean deletePodcastBelongsToCategory(String podcastId, String category) {
        return false;
    }

    public int deleteAllPodcastBelongsToCategory(String podcastId) {
        return -1;
    }

    public boolean deletePodcastCreatedByAuthor(String podcastId, String authorName) {
        return false;
    }

    public int deleteAllPodcastCreatedByAuthor(String podcastId) {
        return -1;
    }

    // ---------------------------------------------------------------------------------- //

    // --------------------------------- GRAPH QUERY ------------------------------------ //

    public List<Entry<String, String>> showPodcastsInWatchlist(String username, int limit) {
        return null;
    }

    public List<Entry<String, String>> showMostLikedPodcasts(int limit) {
        return null;
    }

    public List<Entry<String, Integer>> showMostNumerousCategories(int limit) {
        Neo4jManager manager = Neo4jManager.getInstance();

        List<Record> result = null;
        try {
            String query =  "MATCH (c:Category)<-[b:BELONGS_TO]-() " +
                            "WITH c.name as name, COUNT(b) AS belonging " +
                            "RETURN name, belonging " +
                            "ORDER BY belonging DESC " +
                            "LIMIT $limit";
            Value params = parameters("limit", limit);
            result = manager.read(query, params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result == null)
            return null;

        List<Entry<String, Integer>> categories = new ArrayList<>();
        for (Record record : result) {
            String name = record.get("name").asString();
            int value = record.get("belonging").asInt();
            Entry<String, Integer> category = new AbstractMap.SimpleEntry<>(name, value);
            categories.add(category);
        }

        return categories;
    }

    public List<String> showMostAppreciatedCategories(int limit) {
        return null;
    }

    public List<Entry<String, String>> showSuggestedPodcastsLikedByFollowedUsers(String username, int limit) {
        return null;
    }

    public List<Entry<String, String>> showSuggestedPodcastsBasedOnCategoryOfPodcastsUserLiked(String username, int limit) {
        Neo4jManager manager = Neo4jManager.getInstance();

        List<Record> result = null;
        try {
            String query =  "MATCH (u:User {username: $username})-[:LIKES]->(liked:Podcast) " +
                            "MATCH (liked)-[:BELONGS_TO]->(:Category)<-[:BELONGS_TO]-(p:Podcast) " +
                            "WHERE NOT EXISTS {MATCH (u)-[:LIKES]->(p)} " +
                            "RETURN p.name as name, p.podcastId as pid " +
                            "LIMIT $limit";
            Value params = parameters("username", username, "limit", limit);
            result = manager.read(query, params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result == null)
            return null;

        List<Entry<String, String>> podcasts = new ArrayList<>();
        for (Record record : result) {
            String id = record.get("pid").asString();
            String name = record.get("name").asString();
            Entry<String, String> podcast = new AbstractMap.SimpleEntry<>(id, name);
            podcasts.add(podcast);
        }

        return podcasts;
    }

    public List<Entry<String, String>> showSuggestedPodcastsBasedOnAuthorsOfPodcastsInWatchlist(String username, int limit) {
        return null;
    }

    // ---------------------------------------------------------------------------------- //
}
