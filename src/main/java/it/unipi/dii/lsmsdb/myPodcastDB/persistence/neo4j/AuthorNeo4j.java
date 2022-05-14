package it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import org.neo4j.driver.Record;

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

    public List<String> showSuggestedAuthorsFollowedByFollowedUser(String username, int limit) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            String query = "MATCH (u:User { username: $username })-[:FOLLOWS_USER]-(:User)-[:FOLLOWS]->(a1:Author) " +
                    "WHERE NOT EXISTS " +
                    "{ MATCH (u)-[:FOLLOWS]->(a1) }" +
                    "RETURN DISTINCT a1.name as AuthorName " +
                    "LIMIT $limit";
            List<Record> result = manager.read(query, parameters("username", username, "limit", limit));

            if (result.isEmpty())
                return null;

            List<String> authors = new ArrayList<String>();
            for (Record record: result)
                authors.add(record.get("AuthorName").asString());

            return authors;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ---------------------------------------------------------------------------------- //
}
