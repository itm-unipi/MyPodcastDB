package it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import org.javatuples.Pair;
import org.neo4j.driver.Record;
import org.neo4j.driver.Value;

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

public class AuthorNeo4j {

    // ------------------------------- CRUD OPERATION ----------------------------------- //

    // --------- CREATE --------- //

    public boolean addAuthor(String authorName, String picturePath) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            String query = "CREATE (a:Author {name: $name, picturePath: $picturePath})";
            Value params = parameters("name", authorName, "picturePath", picturePath);
            manager.write(query, params);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addAuthorFollowsAuthor(String authorName, String authorToFollow) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            String query =  "MATCH (a1:Author {name: $follower}) " +
                            "MATCH (a2:Author {name: $toFollow}) " +
                            "CREATE (a1)-[:FOLLOWS_AUTHOR]->(a2)";
            Value params = parameters("follower", authorName, "toFollow", authorToFollow);
            manager.write(query, params);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------- READ ---------- //

    public boolean findAuthorByName(String authorName) {
        Neo4jManager manager = Neo4jManager.getInstance();

        List<Record> results = new ArrayList<>();
        try {
            String query = "MATCH (a:Author {name: $name}) RETURN a";
            Value params = parameters("name", authorName);
            results = manager.read(query, params);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        for (Record record : results)
            if (record.get(0).get("name").asString().equals(authorName))
                return true;

        return false;
    }

    public boolean findAuthorFollowsAuthor(String authorName, String authorNameFollowed) {
        Neo4jManager manager = Neo4jManager.getInstance();

        List<Record> results = new ArrayList<>();
        try {
            String query =  "MATCH (a1:Author {name: $follower})-[r:FOLLOWS_AUTHOR]->(a2:Author {name: $followed}) " +
                    "RETURN r";
            Value params = parameters("follower", authorName, "followed", authorNameFollowed);
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

    public boolean deleteAuthor(String authorName) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            String query = "MATCH (a:Author {name: $name}) DETACH DELETE a";
            Value params = parameters("name", authorName);
            manager.write(query, params);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAuthorFollowsAuthor(String authorName, String authorNameFollowed) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            String query =  "MATCH (a1:Author {name: $follower})-[r:FOLLOWS_AUTHOR]->(a2:Author {name: $followed}) " +
                            "DELETE r";
            Value params = parameters("follower", authorName, "followed", authorNameFollowed);
            manager.write(query, params);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAllAuthorFollowsAuthor(String authorName) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            String query =  "MATCH (a1:Author {name: $name})-[r:FOLLOWS_AUTHOR]->() " +
                            "DELETE r";
            Value params = parameters("name", authorName);
            manager.write(query, params);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------------------------------------------------------------------------------- //

    // --------------------------------- GRAPH QUERY ------------------------------------ //

    public List<Author> showFollowedAuthorsByUser(String username, int limit) {
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = " MATCH (u:User { username: $username})-[r:FOLLOWS]->(a:Author)" + "\n" +
                "RETURN a" + "\n" +
                "LIMIT $limit";
        Value params = parameters("username", username, "limit", limit);
        List<Record> result = null;

        try {
            result = manager.read(query, params);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (result == null || !result.iterator().hasNext())
            return null;

        List<Author> authors = new ArrayList<>();
        for (Record record : result) {
            String name = record.get(0).get("name").asString();
            String picturePath = record.get(0).get("picturePath").asString();
            Author author = new Author("", name, picturePath);
            authors.add(author);
        }

        return authors;
    }

    public List<Author> showFollowedAuthorsByAuthor(String name, int limit) {
        Neo4jManager manager = Neo4jManager.getInstance();
        String query = " MATCH (a1:Author { name: $name})-[r:FOLLOWS_AUTHOR]->(a2:Author)" + "\n" +
                "RETURN a2" + "\n" +
                "LIMIT $limit";
        Value params = parameters("name", name, "limit", limit);
        List<Record> result = null;

        try {
            result = manager.read(query, params);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (result == null || !result.iterator().hasNext())
            return null;

        List<Author> authors = new ArrayList<>();
        for (Record record : result) {
            String authorName = record.get(0).get("name").asString();
            String picturePath = record.get(0).get("picturePath").asString();
            Author author = new Author("", authorName, picturePath);
            authors.add(author);
        }

        return authors;
    }

    public List<Author> showSuggestedAuthorsFollowedByFollowedUser(String username, int limit) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            String query = "MATCH (u:User { username: $username })-[:FOLLOWS_USER]-(:User)-[:FOLLOWS]->(a1:Author) " +
                    "WHERE NOT EXISTS " +
                    "{ MATCH (u)-[:FOLLOWS]->(a1) } " +
                    "RETURN DISTINCT a1 " +
                    "LIMIT $limit";
            List<Record> result = manager.read(query, parameters("username", username, "limit", limit));

            if (result.isEmpty())
                return null;

            List<Author> authors = new ArrayList<>();
            for (Record record : result) {
                String name = record.get(0).get("name").asString();
                String picturePath = record.get(0).get("picturePath").asString();
                Author author = new Author("", name, picturePath);
                authors.add(author);
            }

            return authors;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Pair<Author, Integer>> showMostFollowedAuthor(int limit) {
        Neo4jManager manager = Neo4jManager.getInstance();

        List<Record> result = null;
        try {
            String query =  "MATCH (a:Author)<-[f:FOLLOWS]-() " +
                            "WITH a.name AS name, a.picturePath AS picturePath, COUNT(f) AS followers " +
                            "RETURN name, picturePath, followers " +
                            "ORDER BY followers DESC " +
                            "LIMIT $limit";
            Value params = parameters("limit", limit);
            result = manager.read(query, params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result == null)
            return null;

        List<Pair<Author, Integer>> authors = new ArrayList<>();
        for (Record record : result) {
            String name = record.get("name").asString();
            String picturePath = record.get("picturePath").asString();
            int follows = record.get("followers").asInt();
            Author author = new Author("", name, picturePath);
            authors.add(new Pair<>(author, follows));
        }

        return authors;
    }

    // ---------------------------------------------------------------------------------- //
}
