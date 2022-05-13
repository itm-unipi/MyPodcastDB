package it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j;

import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import org.neo4j.driver.Value;

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

    public boolean addUserLikesPodcast(String username, String podcastId) {
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u:User{username: $username})," +
                "(p:Podcast{podcastId: $podcastId})"+
                "CREATE (u)-[:LIKES]->(p)";
        Value params = parameters("username", username, "podcastId", podcastId);

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

    public boolean addUserFollowUser(String username, String userToFollow) {
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u1:User{username: $username})," +
                "(u2:User{username: $userToFollow})"+
                "CREATE (u1)-[:FOLLOWS_USER]->(u2)";
        Value params = parameters("username", username, "userToFollow", userToFollow);

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

    public boolean deleteUser(User user) {
        return false;
    }

    public boolean deleteUserLikesPodcast(String username, String podcastId) {
        return false;
    }

    public int deleteAllUserLikesPodcast(String username) {
        return -1;
    }

    public boolean deleteUserWatchLaterPodcast(String username, String podcastId) {
        return false;
    }

    public int deleteAllUserWatchLaterPodcast(String username) {
        return -1;
    }

    public boolean deleteUserFollowUser(String username, String userFollowed) {
        return false;
    }

    public int deleteAllUserFollowUser(String username) {
        return -1;
    }

    public boolean deleteUserFollowAuthor(String username, String author) {
        return false;
    }

    public int deleteAllUserFollowAuthor(String username, String author) {
        return -1;
    }

    // ---------------------------------------------------------------------------------- //

    // --------------------------------- GRAPH QUERY ------------------------------------ //

    public List<String> showSuggestedUsersByFollowedAuthors(String username) {
        return null;
    }

    public List<String> showSuggestedUsersByLikedPodcasts(String username) {
        return null;
    }

    // ---------------------------------------------------------------------------------- //
}
