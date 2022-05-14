package it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j;

import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import org.neo4j.driver.Record;
import org.neo4j.driver.Value;

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

public class UserNeo4j {

    // ------------------------------- CRUD OPERATION ----------------------------------- //

    // --------- CREATE --------- //

    public boolean addUser(User user) {
        return false;
    }

    public boolean addUserLikesPodcast(String username, String podcastId) {
        return false;
    }

    public boolean addUserWatchLaterPodcast(String username, String podcastId) {
        return false;
    }

    public boolean addUserFollowUser(String username, String userToFollow) {
        return false;
    }

    public boolean addUserFollowAuthor(String username, String author) {
        return false;
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
            String query =  "MATCH (u:User {username: $follower})-[r:FOLLOWS]->(a:Author {name: $followed}) " +
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

    // --------- UPDATE --------- //

    public boolean updateUser(User user) {
        return false;
    }

    // --------- DELETE --------- //

    public boolean deleteUser(String user) {
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

    public List<String> showSuggestedUsersByFollowedAuthors(String username) {
        return null;
    }

    public List<String> showSuggestedUsersByLikedPodcasts(String username) {
        return null;
    }

    // ---------------------------------------------------------------------------------- //
}
