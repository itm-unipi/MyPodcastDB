package it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import org.neo4j.driver.Value;

import java.util.List;

import static org.neo4j.driver.Values.parameters;
import org.neo4j.driver.Record;

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

    public boolean deleteUserFollowUser(User user1, User user2) {
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u1:User)-[r:FOLLOWS_USER]->(u2:User)" + "\n" +
                "WHERE u1.username = $username1 and u2.username = $username2" + "\n" +
                "DELETE r";
        Value params = parameters("username1", user1.getUsername(), "username2", user2.getUsername());

        try {
            manager.write(query, params);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean deleteAllUserFollowUser(User user) {
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u1:User)-[r:FOLLOWS_USER]->(u2:User)" + "\n" +
                "WHERE u1.username = $username" + "\n" +
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

    public boolean deleteUserFollowAuthor(User user, Author author) {
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u:User)-[r:FOLLOWS]->(a:Author)" + "\n" +
                "WHERE u.username = $username and a.name = $authorName" + "\n" +
                "DELETE r";
        Value params = parameters("username", user.getUsername(), "authorName", author.getName());

        try {
            manager.write(query, params);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean deleteAllUserFollowAuthor(User user) {
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = "MATCH (u:User)-[r:FOLLOWS]->(a:Author)" + "\n" +
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
