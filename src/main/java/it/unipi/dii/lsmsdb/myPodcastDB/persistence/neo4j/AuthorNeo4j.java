package it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import org.neo4j.driver.Record;
import org.neo4j.driver.Value;

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

public class AuthorNeo4j {

    // ------------------------------- CRUD OPERATION ----------------------------------- //

    // --------- CREATE --------- //

    public boolean addAuthor(Author author) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            String query = "CREATE (a:Author {name: $name})";
            Value params = parameters("name", author.getName());
            manager.write(query, params);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addAuthorFollowsAuthor(String author, String authorToFollow) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            String query =  "MATCH (a1:Author {name: $follower}) " +
                            "MATCH (a2:Author {name: $toFollow}) " +
                            "CREATE (a1)-[:FOLLOWS_AUTHOR]->(a2)";
            Value params = parameters("follower", author, "toFollow", authorToFollow);
            manager.write(query, params);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------- READ ---------- //

    public boolean findAuthorByName(String author) {
        Neo4jManager manager = Neo4jManager.getInstance();

        List<Record> results = new ArrayList<>();
        try {
            String query = "MATCH (a:Author {name: $oldName}) RETURN a";
            Value params = parameters("oldName", author);
            results = manager.read(query, params);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        for (Record record : results)
            if (record.get(0).get("name").asString().equals(author))
                return true;

        return false;
    }

    public boolean findAuthorFollowsAuthor(String author, String authorFollowed) {
        Neo4jManager manager = Neo4jManager.getInstance();

        List<Record> results = new ArrayList<>();
        try {
            String query =  "MATCH (a1:Author {name: $follower})-[r:FOLLOWS_AUTHOR]->(a2:Author {name: $followed}) " +
                    "RETURN r";
            Value params = parameters("follower", author, "followed", authorFollowed);
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

    public boolean updateAuthor(String oldName, String newName) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            String query =  "MATCH (a:Author {name: $oldName}) " +
                            "SET a.name = $newName";
            Value params = parameters("oldName", oldName, "newName", newName);
            manager.write(query, params);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // --------- DELETE --------- //

    public boolean deleteAuthor(String author) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            String query = "MATCH (a:Author {name: $name}) DETACH DELETE a";
            Value params = parameters("name", author);
            manager.write(query, params);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAuthorFollowsAuthor(String author, String authorFollowed) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            String query =  "MATCH (a1:Author {name: $follower})-[r:FOLLOWS_AUTHOR]->(a2:Author {name: $followed}) " +
                            "DELETE r";
            Value params = parameters("follower", author, "followed", authorFollowed);
            manager.write(query, params);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAllAuthorFollowsAuthor(String author) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            String query =  "MATCH (a1:Author {name: $name})-[r:FOLLOWS_AUTHOR]->() " +
                            "DELETE r";
            Value params = parameters("name", author);
            manager.write(query, params);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------------------------------------------------------------------------------- //

    // --------------------------------- GRAPH QUERY ------------------------------------ //

    public List<String> showSuggestedAuthorsFollowedByFollowedUser(String username) {
        return null;
    }

    // ---------------------------------------------------------------------------------- //
}
