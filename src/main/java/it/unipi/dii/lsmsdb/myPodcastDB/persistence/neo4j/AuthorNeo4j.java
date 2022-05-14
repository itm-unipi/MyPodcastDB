package it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import org.neo4j.driver.Record;
import org.neo4j.driver.Value;

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

public class AuthorNeo4j {

    // ------------------------------- CRUD OPERATION ----------------------------------- //

    // --------- CREATE --------- //

    public boolean addAuthor(Author author) {
        return false;
    }

    public boolean addAuthorFollowsAuthor(String author, String authorToFollow) {
        return false;
    }

    // ---------- READ ---------- //

    // --------- UPDATE --------- //

    public boolean updateAuthor(Author author) {
        return false;
    }

    // --------- DELETE --------- //

    public boolean deleteAuthor(String author) {
        return false;
    }

    public boolean removeAuthorFollowsAuthor(String author, String authorFollowed) {
        return false;
    }

    public int removeAllAuthorFollowsAuthor(String author) {
        return -1;
    }

    // ---------------------------------------------------------------------------------- //

    // --------------------------------- GRAPH QUERY ------------------------------------ //

    public List<String> showFollowedAuthors(User user, int limit) {
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = " MATCH (u:User { username: $username})-[r:FOLLOWS]->(a:Author)" + "\n" +
                "RETURN a" + "\n" +
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

        List<String> authors = new ArrayList<>();
        for(Record record : result){
            authors.add(record.get(0).get("name").asString());
        }

        return authors;

    }

    public List<String> showSuggestedAuthorsFollowedByFollowedUser(String username) {
        return null;
    }

    // ---------------------------------------------------------------------------------- //
}
