package it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import org.neo4j.driver.Value;

import java.util.AbstractMap;
import org.neo4j.driver.Record;
import org.neo4j.driver.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.neo4j.driver.Values.parameters;
import org.neo4j.driver.Record;

import static org.neo4j.driver.Values.parameters;

public class UserNeo4j {

    // ------------------------------- CRUD OPERATION ----------------------------------- //

    // --------- CREATE --------- //

    public boolean addUser(User user) {

        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "CREATE (u:User {username: $username})";
        Value params = parameters("username", user.getUsername());

        try {
            manager.write(query, params);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean addUserLikesPodcast(User user, Podcast podcast) {
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u:User{username: $username})," +
                "(p:Podcast{podcastId: $podcastId})"+
                "CREATE (u)-[:LIKES]->(p)";
        Value params = parameters("username", user.getUsername(), "podcastId", podcast.getId());

        try {
            manager.write(query, params);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean addUserWatchLaterPodcast(User user, Podcast podcast) {
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u:User{username: $username})," +
                "(p:Podcast{podcastId: $podcastId})"+
                "CREATE (u)-[:WATCH_LATER]->(p)";
        Value params = parameters("username", user.getUsername(), "podcastId", podcast.getId());

        try {
            manager.write(query, params);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean addUserFollowUser(User user, User userToFollow) {
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u1:User{username: $username})," +
                "(u2:User{username: $userToFollow})"+
                "CREATE (u1)-[:FOLLOWS_USER]->(u2)";
        Value params = parameters("username", user.getUsername(), "userToFollow", userToFollow.getUsername());

        try {
            manager.write(query, params);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean addUserFollowAuthor(User user, Author author) {
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u:User{username: $username})," +
                "(a:Author{name: $authorName})"+
                "CREATE (u)-[:FOLLOWS]->(a)";
        Value params = parameters("username", user.getUsername(), "authorName", author.getName());

        try {
            manager.write(query, params);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // ---------- READ ---------- //

    public boolean findUserByUsername(String user) {
        Neo4jManager manager = Neo4jManager.getInstance();

        List<Record> results = new ArrayList<>();
        try {
            String query = "MATCH (u:User {username: $username}) RETURN u";
            Value params = parameters("username", user);
            results = manager.read(query, params);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        for (Record record : results)
            if (record.get(0).get("name").asString().equals(user))
                return true;

        return false;
    }

    public boolean findUserFollowsUser(String username, String userFollowed) {
        Neo4jManager manager = Neo4jManager.getInstance();

        List<Record> results = new ArrayList<>();
        try {
            String query =  "MATCH (u1:User {username: $follower})-[r:FOLLOWS_USER]->(u2:User {username: $followed}) " +
                    "RETURN r";
            Value params = parameters("follower", username, "followed", userFollowed);
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

    public boolean findUserFollowsAuthor(String username, String authorFollowed) {
        Neo4jManager manager = Neo4jManager.getInstance();

        List<Record> results = new ArrayList<>();
        try {
            String query = "MATCH (u:User {username: $follower})-[r:FOLLOWS]->(a:Author {name: $followed}) " +
                    "RETURN r";
            Value params = parameters("follower", username, "followed", authorFollowed);
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

    public boolean checkUserExists(User user){
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u:User{username: $username}) RETURN u";
        Value params = parameters("username", user.getUsername());
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

    public boolean checkUserLikesPodcastExists(User user, Podcast podcast){
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u:User{username: $username})-[r:LIKES]->(p:Podcast{podcastId: $podcastId}) RETURN r";
        Value params = parameters("username", user.getUsername(), "podcastId", podcast.getId());
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

    public boolean checkAllUserLikesPodcastExists(User user){
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u:User{username: $username})-[r:LIKES]->(p:Podcast) RETURN r";
        Value params = parameters("username", user.getUsername());
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

    public boolean checkUserWatchLaterPodcastExists(User user, Podcast podcast){
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u:User{username: $username})-[r:WATCH_LATER]-(p:Podcast{podcastId: $podcastId}) RETURN r";
        Value params = parameters("username", user.getUsername(), "podcastId", podcast.getId());
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

    public boolean checkAllUserWatchLaterPodcastExists(User user){
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u:User{username: $username})-[r:WATCH_LATER]-(p:Podcast) RETURN r";
        Value params = parameters("username", user.getUsername());
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

    public boolean checkUserFollowUserExists(User user1, User user2){
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u1:User{username: $username1})-[r:FOLLOWS_USER]->(u2:User{username: $username2}) RETURN r";
        Value params = parameters("username1", user1.getUsername(), "username2", user2.getUsername());
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

    public boolean checkAllUserFollowUserExists(User user1){
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u1:User{username: $username1})-[r:FOLLOWS_USER]->(u2:User) RETURN r";
        Value params = parameters("username1", user1.getUsername());
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

    public boolean checkUserFollowAuthorExists(User user, Author author){
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u:User{username: $username})-[r:FOLLOWS]->(a:Author{name: $authorName}) RETURN r";
        Value params = parameters("username", user.getUsername(), "authorName", author.getName());
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

    public boolean checkAllUserFollowAuthorExists(User user){
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u:User{username: $username})-[r:FOLLOWS]->(a:Author) RETURN r";
        Value params = parameters("username", user.getUsername());
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

    public boolean updateUser(User user, String newUsername) {
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u:User{username: $old})" +
                "SET u.username = $new";
        Value params = parameters("old", user.getUsername(), "new", newUsername);

        try {
            manager.write(query, params);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        user.setUsername(newUsername);
        return true;
    }

    // --------- DELETE --------- //

    public boolean deleteUser(User user) { //use DETACH
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u:User{username: $username}) DETACH DELETE u";
        Value params = parameters("username", user.getUsername());

        try {
            manager.write(query, params);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean deleteUserLikesPodcast(User user, Podcast podcast) {
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u:User)-[r:LIKES]->(p:Podcast)" + "\n" +
                        "WHERE u.username = $username and p.podcastId = $podcastId" + "\n" +
                        "DELETE r";
        Value params = parameters("username", user.getUsername(), "podcastId", podcast.getId());

        try {
            manager.write(query, params);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean deleteAllUserLikesPodcast(User user) {
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u:User)-[r:LIKES]->(p:Podcast)" + "\n" +
                "WHERE u.username = $username" + "\n" +
                "DELETE r";
        Value params = parameters("username", user.getUsername());

        try {
            manager.write(query, params);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean deleteUserWatchLaterPodcast(User user, Podcast podcast) {
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u:User)-[r:WATCH_LATER]->(p:Podcast)" + "\n" +
                "WHERE u.username = $username and p.podcastId = $podcastId" + "\n" +
                "DELETE r";
        Value params = parameters("username", user.getUsername(), "podcastId", podcast.getId());

        try {
            manager.write(query, params);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean deleteAllUserWatchLaterPodcast(User user) {
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u:User)-[r:WATCH_LATER]->(p:Podcast)" + "\n" +
                "WHERE u.username = $username" + "\n" +
                "DELETE r";
        Value params = parameters("username", user.getUsername());

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

    public List<String> showFollowedUsers(User user, int limit) {
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = " MATCH (u1:User { username: $username})-[r:FOLLOWS_USER]->(u2:User)" + "\n" +
                "RETURN u2" + "\n" +
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

        List<String> users = new ArrayList<>();
        for(Record record : result){
            users.add(record.get(0).get("username").asString());
        }

        return users;

    }

    public List<String> showSuggestedUsersByFollowedAuthors(String username) {
        return null;
    }

    public List<String> showSuggestedUsersByLikedPodcasts(String username) {
        return null;
    }

    // ---------------------------------------------------------------------------------- //
}
