package it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import org.neo4j.driver.Record;
import org.neo4j.driver.Value;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

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
            String query = "MATCH (a:Author {name: $name}) RETURN a";
            Value params = parameters("name", author);
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
        if (result == null || !result.iterator().hasNext())
            return null;

        List<String> authors = new ArrayList<>();
        for (Record record : result) {
            authors.add(record.get(0).get("name").asString());
        }

        return authors;
    }

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

    public List<Entry<String, Integer>> showMostFollowedAuthor(int limit) {
        Neo4jManager manager = Neo4jManager.getInstance();

        List<Record> result = null;
        try {
            String query =  "MATCH (a:Author)<-[f:FOLLOWS]-() " +
                            "WITH a.name AS name, COUNT(f) AS followers " +
                            "RETURN name, followers " +
                            "ORDER BY followers DESC " +
                            "LIMIT $limit";
            Value params = parameters("limit", limit);
            result = manager.read(query, params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result == null)
            return null;

        List<Entry<String, Integer>> authors = new ArrayList<>();
        for (Record record : result) {
            String name = record.get("name").asString();
            int value = record.get("followers").asInt();
            Entry<String, Integer> category = new AbstractMap.SimpleEntry<>(name, value);
            authors.add(category);
        }

        return authors;
    }

    // ---------------------------------------------------------------------------------- //
}
