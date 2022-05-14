package it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import org.neo4j.driver.Value;
import org.neo4j.driver.Record;
import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

public class UserNeo4j {

    // ------------------------------- CRUD OPERATION ----------------------------------- //

    // --------- CREATE --------- //

    public boolean addUser(String username) {

        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "CREATE (u:User {username: $username})";
        Value params = parameters("username", username);

        try {
            manager.write(query, params);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean addUserLikesPodcast(String username, Podcast podcast) {
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u:User{username: $username})," +
                "(p:Podcast{podcastId: $podcastId})"+
                "CREATE (u)-[:LIKES]->(p)";
        Value params = parameters("username", username, "podcastId", podcast.getId());

        try {
            manager.write(query, params);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean addUserWatchLaterPodcast(String username, String podcastId) {
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u:User{username: $username})," +
                "(p:Podcast{podcastId: $podcastId})"+
                "CREATE (u)-[:WATCH_LATER]->(p)";
        Value params = parameters("username", username, "podcastId", podcastId);

        try {
            manager.write(query, params);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean addUserFollowUser(String username, String usernameToFollow) {
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u1:User{username: $username})," +
                "(u2:User{username: $userToFollow})"+
                "CREATE (u1)-[:FOLLOWS_USER]->(u2)";
        Value params = parameters("username", username, "userToFollow", usernameToFollow);

        try {
            manager.write(query, params);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean addUserFollowAuthor(String username, String authorName) {
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u:User{username: $username})," +
                "(a:Author{name: $authorName})"+
                "CREATE (u)-[:FOLLOWS]->(a)";
        Value params = parameters("username", username, "authorName", authorName);

        try {
            manager.write(query, params);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // ---------- READ ---------- //

    public boolean findUserByUsername(String username) {
        Neo4jManager manager = Neo4jManager.getInstance();

        List<Record> results = new ArrayList<>();
        try {
            String query = "MATCH (u:User {username: $username}) RETURN u";
            Value params = parameters("username", username);
            results = manager.read(query, params);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        for (Record record : results)
            if (record.get(0).get("name").asString().equals(username))
                return true;

        return false;
    }

    public boolean findUserFollowsUser(String username, String usernameFollowed) {
        Neo4jManager manager = Neo4jManager.getInstance();

        List<Record> results = new ArrayList<>();
        try {
            String query =  "MATCH (u1:User {username: $follower})-[r:FOLLOWS_USER]->(u2:User {username: $followed}) " +
                    "RETURN r";
            Value params = parameters("follower", username, "followed", usernameFollowed);
            manager.write(query, params);
            results = manager.read(query, params);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if (results.isEmpty())
            return false;

        return true;
    }

    public boolean findUserFollowsAuthor(String username, String authorNameFollowed) {
        Neo4jManager manager = Neo4jManager.getInstance();

        List<Record> results = new ArrayList<>();
        try {
            String query = "MATCH (u:User {username: $follower})-[r:FOLLOWS]->(a:Author {name: $followed}) " +
                    "RETURN r";
            Value params = parameters("follower", username, "followed", authorNameFollowed);
            manager.write(query, params);
            results = manager.read(query, params);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if (results.isEmpty())
            return false;

        return true;
    }

    public boolean checkUserExists(String username){
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u:User{username: $username}) RETURN u";
        Value params = parameters("username", username);
        List<Record> result = null;

        try {
            result = manager.read(query, params);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if(result != null && result.iterator().hasNext())
            return true;
        else
            return false;
    }

    public boolean checkUserLikesPodcastExists(String username, String podcastId){
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u:User{username: $username})-[r:LIKES]->(p:Podcast{podcastId: $podcastId}) RETURN r";
        Value params = parameters("username", username, "podcastId", podcastId);
        List<Record> result = null;

        try {
            result = manager.read(query, params);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if(result != null && result.iterator().hasNext())
            return true;
        else
            return false;
    }

    public boolean checkAllUserLikesPodcastExists(String username){
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u:User{username: $username})-[r:LIKES]->(p:Podcast) RETURN r";
        Value params = parameters("username", username);
        List<Record> result = null;

        try {
            result = manager.read(query, params);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if(result != null && result.iterator().hasNext())
            return true;
        else
            return false;
    }

    public boolean checkUserWatchLaterPodcastExists(String username, String podcastId){
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u:User{username: $username})-[r:WATCH_LATER]-(p:Podcast{podcastId: $podcastId}) RETURN r";
        Value params = parameters("username", username, "podcastId", podcastId);
        List<Record> result = null;

        try {
            result = manager.read(query, params);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if(result != null && result.iterator().hasNext())
            return true;
        else
            return false;
    }

    public boolean checkAllUserWatchLaterPodcastExists(String username){
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u:User{username: $username})-[r:WATCH_LATER]-(p:Podcast) RETURN r";
        Value params = parameters("username", username);
        List<Record> result = null;

        try {
            result = manager.read(query, params);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if(result != null && result.iterator().hasNext())
            return true;
        else
            return false;
    }

    public boolean checkUserFollowUserExists(String username1, String username2){
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u1:User{username: $username1})-[r:FOLLOWS_USER]->(u2:User{username: $username2}) RETURN r";
        Value params = parameters("username1", username1, "username2", username2);
        List<Record> result = null;

        try {
            result = manager.read(query, params);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if(result != null && result.iterator().hasNext())
            return true;
        else
            return false;
    }

    public boolean checkAllUserFollowUserExists(String username1){
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u1:User{username: $username1})-[r:FOLLOWS_USER]->(u2:User) RETURN r";
        Value params = parameters("username1", username1);
        List<Record> result = null;

        try {
            result = manager.read(query, params);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if(result != null && result.iterator().hasNext())
            return true;
        else
            return false;
    }

    public boolean checkUserFollowAuthorExists(String username, String authorName){
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u:User{username: $username})-[r:FOLLOWS]->(a:Author{name: $authorName}) RETURN r";
        Value params = parameters("username", username, "authorName", authorName);
        List<Record> result = null;

        try {
            result = manager.read(query, params);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if(result != null && result.iterator().hasNext())
            return true;
        else
            return false;
    }

    public boolean checkAllUserFollowAuthorExists(String username){
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u:User{username: $username})-[r:FOLLOWS]->(a:Author) RETURN r";
        Value params = parameters("username", username);
        List<Record> result = null;

        try {
            result = manager.read(query, params);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if(result != null && result.iterator().hasNext())
            return true;
        else
            return false;
    }

    // --------- UPDATE --------- //

    public boolean updateUser(String oldUsername, String newUsername) {
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u:User{username: $old})" +
                "SET u.username = $new";
        Value params = parameters("old", oldUsername, "new", newUsername);

        try {
            manager.write(query, params);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // --------- DELETE --------- //

    public boolean deleteUser(String username) { //use DETACH
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u:User{username: $username}) DETACH DELETE u";
        Value params = parameters("username", username);

        try {
            manager.write(query, params);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean deleteUserLikesPodcast(String username, String podcastId) {
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u:User)-[r:LIKES]->(p:Podcast)" + "\n" +
                        "WHERE u.username = $username and p.podcastId = $podcastId" + "\n" +
                        "DELETE r";
        Value params = parameters("username", username, "podcastId", podcastId);

        try {
            manager.write(query, params);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean deleteAllUserLikesPodcast(String username) {
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u:User)-[r:LIKES]->(p:Podcast)" + "\n" +
                "WHERE u.username = $username" + "\n" +
                "DELETE r";
        Value params = parameters("username", username);

        try {
            manager.write(query, params);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean deleteUserWatchLaterPodcast(String username, String podcastId) {
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u:User)-[r:WATCH_LATER]->(p:Podcast)" + "\n" +
                "WHERE u.username = $username and p.podcastId = $podcastId" + "\n" +
                "DELETE r";
        Value params = parameters("username", username, "podcastId", podcastId);

        try {
            manager.write(query, params);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean deleteAllUserWatchLaterPodcast(String username) {
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u:User)-[r:WATCH_LATER]->(p:Podcast)" + "\n" +
                "WHERE u.username = $username" + "\n" +
                "DELETE r";
        Value params = parameters("username", username);

        try {
            manager.write(query, params);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public boolean deleteUserFollowUser(String username, String userFollowed) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            String query =  "MATCH (u1:User {username: $follower})-[r:FOLLOWS_USER]->(u2:User {username: $followed}) " +
                            "DELETE r";
            Value params = parameters("follower", username, "followed", userFollowed);
            manager.write(query, params);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAllUserFollowUser(String username) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            String query =  "MATCH (u1:User {username: $username})-[r:FOLLOWS_USER]->() " +
                            "DELETE r";
            Value params = parameters("username", username);
            manager.write(query, params);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUserFollowAuthor(String username, String author) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            String query =  "MATCH (u:User {username: $follower})-[r:FOLLOWS]->(a:Author {name: $followed}) " +
                            "DELETE r";
            Value params = parameters("follower", username, "followed", author);
            manager.write(query, params);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAllUserFollowAuthor(String username) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            String query =  "MATCH (u:User {username: $username})-[r:FOLLOWS]->() " +
                            "DELETE r";
            Value params = parameters("username", username);
            manager.write(query, params);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------------------------------------------------------------------------------- //

    // --------------------------------- GRAPH QUERY ------------------------------------ //

    public List<String> showFollowedUsers(String username, int limit) {
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = " MATCH (u1:User { username: $username})-[r:FOLLOWS_USER]->(u2:User)" + "\n" +
                "RETURN u2" + "\n" +
                "LIMIT $limit";
        Value params = parameters("username", username, "limit", limit);
        List<Record> result = null;

        try {
            result = manager.read(query, params);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if(result == null || !result.iterator().hasNext())
            return null;

        List<String> users = new ArrayList<>();
        for(Record record : result){
            users.add(record.get(0).get("username").asString());
        }

        return users;

    }

    public List<String> showSuggestedUsersByFollowedAuthors(String username) {
        return null;
    }

    public List<String> showSuggestedUsersByLikedPodcasts(String username, int limit) {
        Neo4jManager manager = Neo4jManager.getInstance();

        List<Record> result = null;
        try {
            String query =  "MATCH (u1:User {username: $username})-[:LIKES]->(:Podcast)<-[:LIKES]-(u2) " +
                            "WHERE NOT EXISTS { (u1)-[:FOLLOWS_USER]->(u2) } " +
                            "RETURN u2 " +
                            "LIMIT $limit";
            Value params = parameters("username", username, "limit", limit);
            result = manager.read(query, params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result == null)
            return null;

        List<String> users = new ArrayList<>();
        for (Record record : result) {
            String user = record.get(0).get("username").asString();
            users.add(user);
        }

        return users;
    }

    // ---------------------------------------------------------------------------------- //
}
