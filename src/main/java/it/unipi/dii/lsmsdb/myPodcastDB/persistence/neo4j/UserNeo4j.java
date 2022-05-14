package it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j;

import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import org.neo4j.driver.Record;

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

    // --------- UPDATE --------- //

    public boolean updateUser(User user) {
        return false;
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

    public List<String> showSuggestedUsersByFollowedAuthors(String username, int limit) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            String query = "MATCH (u1:User {username: $username })-[:FOLLOWS]->(:Author)<-[:FOLLOWS]-(u2:User) " +
                    "WHERE NOT EXISTS " +
                    "{ MATCH (u1)-[:FOLLOWS_USER]-(u2) } " +
                    "RETURN DISTINCT u2.username as Username " +
                    "LIMIT $limit";
            List<Record> result = manager.read(query, parameters("username", username, "limit", limit));

            if (result.isEmpty())
                return null;

            List<String> suggestedUsers = new ArrayList<String>();
            for (Record record: result)
                suggestedUsers.add(record.get("Username").asString());

            return suggestedUsers;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> showSuggestedUsersByLikedPodcasts(String username) {
        return null;
    }

    // ---------------------------------------------------------------------------------- //
}
