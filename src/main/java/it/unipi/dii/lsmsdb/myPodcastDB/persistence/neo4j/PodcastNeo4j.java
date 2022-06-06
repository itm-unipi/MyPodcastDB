package it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import static org.neo4j.driver.Values.parameters;

import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import org.neo4j.driver.Record;
import org.neo4j.driver.Value;

public class PodcastNeo4j {

    // ------------------------------- CRUD OPERATION ----------------------------------- //

    // --------- CREATE --------- //

    public boolean addPodcast(Podcast podcast) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            manager.write(
                    "CREATE (p:Podcast {name: $name, podcastId: $podcastId, artworkUrl600: $artworkUrl600})",
                    parameters("name", podcast.getName(), "podcastId", podcast.getId(), "artworkUrl600", podcast.getArtworkUrl600())
            );
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addPodcastBelongsToCategory(Podcast podcast, String category) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            String query = "MATCH (p:Podcast { podcastId: $podcastId }) " +
                           "MATCH (c:Category { name: $name }) " +
                           "CREATE (p)-[r:BELONGS_TO]->(c)";
            manager.write(query, parameters("podcastId", podcast.getId(), "name", category));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addPodcastCreatedByAuthor(Podcast podcast) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            String query = "MATCH (p:Podcast { podcastId: $podcastId }) " +
                           "MATCH (a:Author { name: $name }) " +
                           "CREATE (p)-[r:CREATED_BY]->(a)";
            manager.write(query, parameters("podcastId", podcast.getId(), "name", podcast.getAuthorName()));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------- READ ---------- //

    public List<Podcast> findPodcastsByName(String name, int limit) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            List<Record> result = manager.read(
                    "MATCH (p:Podcast { name: $name }) RETURN p LIMIT $limit",
                    parameters("name", name, "limit", limit)
            );

            if (result.isEmpty())
                return null;

            List<Podcast> podcasts = new ArrayList<>();
            for (Record record : result) {
                String podcastId = record.get(0).get("podcastId").asString();
                String podcastName = record.get(0).get("name").asString();
                String artworkUrl600 = record.get(0).get("artworkUrl600").asString();

                Podcast podcast = new Podcast(podcastId, podcastName, artworkUrl600);
                podcasts.add(podcast);
            }

            return podcasts;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Podcast findPodcastByPodcastId(String podcastId) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            List<Record> result = manager.read(
                    "MATCH (p:Podcast { podcastId: $podcastId }) RETURN p",
                    parameters("podcastId", podcastId)
            );

            if (result.isEmpty())
                return null;

            for (Record record : result) {
                String podcastName = record.get(0).get("name").asString();
                String artworkUrl600 = record.get(0).get("artworkUrl600").asString();

                Podcast podcast = new Podcast(podcastId, podcastName, artworkUrl600);
                return podcast;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public boolean findPodcastCreatedByAuthor(String podcastId, String authorUsername) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            String query = "MATCH (p:Podcast { podcastId: $podcastId })-[r:CREATED_BY]->(a:Author { name: $authorUsername }) RETURN r";
            List<Record> result = manager.read(query, parameters("podcastId", podcastId, "authorUsername", authorUsername));
            return !result.isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean findPodcastBelongsToCategory(String podcastId, String category) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            String query = "MATCH (p:Podcast { podcastId: $podcastId })-[r:BELONGS_TO]->(c:Category { name: $category }) RETURN r";
            List<Record> result = manager.read(query, parameters("podcastId", podcastId, "category", category));
            return !result.isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // --------- UPDATE --------- //

    public boolean updatePodcast(String podcastId, String newName, String newArtwork) {
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (p:Podcast{podcastId: $podcastId})" +
                "SET p.name = $newName, p.artworkUrl600 = $newArtwork";
        Value params = parameters("podcastId", podcastId, "newName", newName, "newArtwork", newArtwork);

        try {
            manager.write(query, params);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // --------- DELETE --------- //

    public boolean deletePodcastById(int id) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            manager.write(
                    "MATCH (p:Podcast) WHERE id(p) = $id DETACH DELETE p",
                    parameters("id", id)
            );
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePodcastByPodcastId(String id) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            manager.write(
                    "MATCH (p:Podcast { podcastId: $podcastId }) DETACH DELETE p",
                    parameters("podcastId", id)
            );
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePodcastsByName(String name) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            manager.write(
                    "MATCH (p:Podcast { name: $name }) DETACH DELETE p",
                    parameters("name", name)
            );
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePodcastBelongsToCategory(String podcastId, String category) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            String query = "MATCH (p:Podcast { podcastId: $podcastId })-[r:BELONGS_TO]->(c:Category { name: $name })" +
                            "DELETE r" ;
            manager.write(query, parameters("podcastId", podcastId, "name", category));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAllPodcastBelongsToCategory(String podcastId) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            String query = "MATCH (p:Podcast { podcastId: $podcastId })-[r:BELONGS_TO]-(c)" +
                           "DELETE r";
            manager.write(query, parameters("podcastId", podcastId));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePodcastCreatedByAuthor(String podcastId, String authorName) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            String query = "MATCH (p:Podcast { podcastId: $podcastId })-[r:CREATED_BY]-> (a:Author { name: $name })" +
                           "DELETE r" ;
            manager.write(query, parameters("podcastId", podcastId, "name", authorName));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------------------------------------------------------------------------------- //

    // --------------------------------- GRAPH QUERY ------------------------------------ //

    public List<Podcast> showPodcastsInWatchlist(String username, int limit, int skip) {

        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u:User { username: $username})-[r:WATCH_LATER]->(p:Podcast)" + "\n"+
                "RETURN p " + "\n"+
                "SKIP $skip" + "\n" +
                "LIMIT $limit";
        Value params = parameters("username", username, "limit", limit, "skip", skip);
        List<Record> result = null;

        try {
            result = manager.read(query, params);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if(result == null || !result.iterator().hasNext())
            return null;

        List<Podcast> podcasts = new ArrayList<>();
        for(Record record : result){
            String podcastName = record.get(0).get("name").asString();
            String podcastId = record.get(0).get("podcastId").asString();
            String artworkUrl600 = record.get(0).get("artworkUrl600").asString();

            Podcast podcast = new Podcast(podcastId, podcastName, artworkUrl600);
            podcasts.add(podcast);
        }

        return podcasts;
    }

    public List<Podcast> showLikedPodcastsByUser(String username, int limit, int skip) {

        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u:User { username: $username})-[r:LIKES]->(p:Podcast)" + "\n"+
                "RETURN p " + "\n"+
                "SKIP $skip" + "\n" +
                "LIMIT $limit";
        Value params = parameters("username", username, "limit", limit, "skip", skip);
        List<Record> result = null;

        try {
            result = manager.read(query, params);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if(result == null || !result.iterator().hasNext())
            return null;

        List<Podcast> podcasts = new ArrayList<>();
        for(Record record : result){
            String podcastName = record.get(0).get("name").asString();
            String podcastId = record.get(0).get("podcastId").asString();
            String artworkUrl600 = record.get(0).get("artworkUrl600").asString();

            Podcast podcast = new Podcast(podcastId, podcastName, artworkUrl600);
            podcasts.add(podcast);
        }

        return podcasts;
    }

    public List<Entry<Podcast, Integer>> showMostLikedPodcasts(int limit) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            String query = "MATCH (p:Podcast)-[l:LIKES]-() " +
                    "WITH p.name as PodcastName, p.podcastId as PodcastId, p.artworkUrl600 as Artwork, " +
                    "COUNT(l) as Likes " +
                    "RETURN PodcastName, PodcastId, Artwork, Likes " +
                    "ORDER BY Likes DESC " +
                    "LIMIT $limit";
            List<Record> result = manager.read(query, parameters("limit", limit));

            if (result.isEmpty())
                return null;

            List<Entry<Podcast, Integer>> mostLikedPodcasts = new ArrayList<>();
            for (Record record: result) {
                String podcastId = record.get("PodcastId").asString();
                String podcastName = record.get("PodcastName").asString();
                String artworkUrl600 = record.get("Artwork").asString();
                int likes = record.get("Likes").asInt();

                Podcast podcast = new Podcast(podcastId, podcastName, artworkUrl600);
                Entry<Podcast, Integer> newPodcast = new AbstractMap.SimpleEntry<>(podcast, likes);
                mostLikedPodcasts.add(newPodcast);
            }

            return mostLikedPodcasts;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

    public List<Entry<String, Integer>> showMostAppreciatedCategories(int limit) {
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = " MATCH (c:Category)<-[:BELONGS_TO]-(p:Podcast)<-[l:LIKES]-(:User)" + "\n" +
                "WITH c, count(l) as likes" + "\n" +
                "RETURN c.name as name, likes" + "\n" +
                "ORDER BY likes desc" + "\n" +
                "LIMIT $limit";
        Value params = parameters("limit", limit);
        List<Record> result = null;

        try {
            result = manager.read(query, params);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if(result == null || !result.iterator().hasNext())
            return null;

        List<Entry<String, Integer>> categories = new ArrayList<>();
        for(Record record : result){
            String categoryName = record.get("name").asString();
            int likes = record.get("likes").asInt();
            Entry<String, Integer> category = new AbstractMap.SimpleEntry<>(categoryName, likes);
            categories.add(category);
        }

        return categories;
    }

    public List<Podcast> showSuggestedPodcastsLikedByFollowedUsers(User user, int limit) {
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (source:User{username: $username})-[:FOLLOWS_USER]->(u:User)-[:LIKES]->(p:Podcast)," + "\n" +
                "(p)<-[l:LIKES]-(:User)" + "\n" +
                "WHERE NOT EXISTS { match (source)-[:LIKES]->(p) }" + "\n" +
                "and NOT EXISTS { match (source)-[:WATCH_LATER]->(p) }" + "\n" +
                "WITH p.name as name, p.podcastId as id, p.artworkUrl600 as artwork, count(l) as likes" + "\n" +
                "RETURN name, id, artwork, likes" + "\n" +
                "ORDER BY likes desc" + "\n" +
                "LIMIT $limit" ;
        Value params = parameters("username", user.getUsername(), "limit", limit);
        List<Record> result = null;

        try {
            result = manager.read(query, params);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if(result == null || !result.iterator().hasNext())
            return null;

        List<Podcast> podcasts = new ArrayList<>();
        for(Record record : result){
            String podcastName = record.get("name").asString();
            String podcastId = record.get("id").asString();
            String artworkUrl600 = record.get("artwork").asString();

            Podcast podcast = new Podcast(podcastId, podcastName, artworkUrl600);
            podcasts.add(podcast);
        }

        return podcasts;
    }

    public List<Podcast> showSuggestedPodcastsBasedOnCategoryOfPodcastsUserLiked(String username, int limit) {
        Neo4jManager manager = Neo4jManager.getInstance();

        List<Record> result = null;
        try {
            String query =  "MATCH (u:User {username: $username})-[:LIKES]->(liked:Podcast) " +
                            "MATCH (liked)-[:BELONGS_TO]->(:Category)<-[:BELONGS_TO]-(p:Podcast) " +
                            "WHERE NOT EXISTS {MATCH (u)-[:LIKES]->(p)} " +
                            "RETURN p.name as name, p.podcastId as pid, p.artworkUrl600 as artwork " +
                            "LIMIT $limit";
            Value params = parameters("username", username, "limit", limit);
            result = manager.read(query, params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result == null)
            return null;

        List<Podcast> podcasts = new ArrayList<>();
        for (Record record : result) {
            String id = record.get("pid").asString();
            String name = record.get("name").asString();
            String artworkUrl600 = record.get("artwork").asString();

            Podcast podcast = new Podcast(id, name, artworkUrl600);
            podcasts.add(podcast);
        }

        return podcasts;
    }

    public List<Podcast> showSuggestedPodcastsBasedOnAuthorsOfPodcastsInWatchlist(User user, int limit) {
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (s:User{username: $username})-[w:WATCH_LATER]->(p1:Podcast)-[c1:CREATED_BY]->(a:Author)," + "\n" +
                "(a)<-[c2:CREATED_BY]-(p2:Podcast)" + "\n" +
                "WHERE NOT EXISTS { match (s)-[:WATCH_LATER]->(p2) }" + "\n" +
                "RETURN p2.name as name, p2.podcastId as id, p2.artworkUrl600 as artwork" + "\n" +
                "LIMIT $limit";
        Value params = parameters("username", user.getUsername(), "limit", limit);
        List<Record> result = null;

        try {
            result = manager.read(query, params);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if(result == null || !result.iterator().hasNext())
            return null;

        List<Podcast> podcasts = new ArrayList<>();
        for(Record record : result){
            String podcastName = record.get("name").asString();
            String podcastId = record.get("id").asString();
            String artworkUrl600 = record.get("artwork").asString();

            Podcast podcast = new Podcast(podcastId, podcastName, artworkUrl600);
            podcasts.add(podcast);
        }

        return podcasts;
    }

    // ---------------------------------------------------------------------------------- //
}
