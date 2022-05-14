package it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.neo4j.driver.Values.parameters;
import org.neo4j.driver.Record;

public class PodcastNeo4j {

    // ------------------------------- CRUD OPERATION ----------------------------------- //

    // --------- CREATE --------- //

    public boolean addPodcast(Podcast podcast) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            manager.write(
                    "CREATE (p:Podcast {name: $name, podcastId: $podcastId})",
                    parameters("name", podcast.getName(), "podcastId", podcast.getId())
            );
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addPodcastBelongsToCategory(Podcast podcast, String category) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            String query = "MATCH (p:Podcast { podcastId: $podcastId }) " +
                           "MATCH (c:Category { name: $name }) " +
                           "CREATE (p)-[r:BELONGS_TO]->(c)";
            manager.write(query, parameters("podcastId", podcast.getId(), "name", category));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addPodcastCreatedByAuthor(Podcast podcast) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            String query = "MATCH (p:Podcast { podcastId: $podcastId }) " +
                           "MATCH (a:Author { name: $name }) " +
                           "CREATE (p)-[r:CREATED_BY]->(a)";
            manager.write(query, parameters("podcastId", podcast.getId(), "name", podcast.getAuthorName()));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------- READ ---------- //

    public List<Entry<String, String>> findPodcastsByName(String name) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            List<Record> result = manager.read(
                    "MATCH (p:Podcast { name: $name }) RETURN p LIMIT 10",
                    parameters("name", name)
            );

            if (result.isEmpty())
                return null;

            List<Entry<String, String>> podcasts = new ArrayList<>();
            for (Record record : result) {
                String podcastId = record.get(0).get("podcastId").asString();
                String podcastName = record.get(0).get("name").asString();

                Entry<String, String> podcast = new AbstractMap.SimpleEntry<>(podcastId, podcastName);
                podcasts.add(podcast);
            }

            return podcasts;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean findPodcastCreatedByAuthor(String podcastId, String authorUsername) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            String query = "MATCH (p:Podcast { podcastId: $podcastId })-[r:CREATED_BY]->(a:Author { name: $authorUsername }) RETURN r";
            List<Record> result = manager.read(query, parameters("podcastId", podcastId, "authorUsername", authorUsername));
            return !result.isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean findPodcastBelongsToCategory(String podcastId, String category) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            String query = "MATCH (p:Podcast { podcastId: $podcastId })-[r:BELONGS_TO]->(c:Category { name: $category }) RETURN r";
            List<Record> result = manager.read(query, parameters("podcastId", podcastId, "category", category));
            return !result.isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // --------- UPDATE --------- //

    public boolean updatePodcast(Podcast podcast) {
        return false;
    }

    // --------- DELETE --------- //

    public boolean deletePodcastById(int id) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            manager.write(
                    "MATCH (p:Podcast) WHERE id(p) = $id DETACH DELETE p",
                    parameters("id", id)
            );
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePodcastByPodcastId(String id) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            manager.write(
                    "MATCH (p:Podcast { podcastId: $podcastId }) DETACH DELETE p",
                    parameters("podcastId", id)
            );
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePodcastsByName(String name) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            manager.write(
                    "MATCH (p:Podcast { name: $name }) DETACH DELETE p",
                    parameters("name", name)
            );
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePodcastBelongsToCategory(String podcastId, String category) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            String query = "MATCH (p:Podcast { podcastId: $podcastId })-[r:BELONGS_TO]->(c:Category { name: $name })" +
                            "DELETE r" ;
            manager.write(query, parameters("podcastId", podcastId, "name", category));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAllPodcastBelongsToCategory(String podcastId) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            String query = "MATCH (p:Podcast { podcastId: $podcastId })-[r:BELONGS_TO]-(c)" +
                           "DELETE r";
            manager.write(query, parameters("podcastId", podcastId));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePodcastCreatedByAuthor(String podcastId, String authorName) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            String query = "MATCH (p:Podcast { podcastId: $podcastId })-[r:CREATED_BY]-> (a:Author { name: $name })" +
                           "DELETE r" ;
            manager.write(query, parameters("podcastId", podcastId, "name", authorName));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------------------------------------------------------------------------------- //

    // --------------------------------- GRAPH QUERY ------------------------------------ //

    public List<Entry<String, String>> showPodcastsInWatchlist(String username, int limit) {
        return null;
    }

    public List<Entry<String, Integer>> showMostLikedPodcasts(int limit) {
        Neo4jManager manager = Neo4jManager.getInstance();

        try {
            String query = "MATCH (p:Podcast)-[l:LIKES]-() " +
                    "WITH p.name as PodcastName, COUNT(l) as Likes " +
                    "RETURN PodcastName, Likes " +
                    "ORDER BY Likes DESC " +
                    "LIMIT $limit";
            List<Record> result = manager.read(query, parameters("limit", limit));

            if (result.isEmpty())
                return null;

            List<Entry<String, Integer>> mostLikedPodcasts = new ArrayList<>();
            for (Record record: result) {
                String podcastName = record.get("PodcastName").asString();
                int likes = record.get("Likes").asInt();

                Entry<String, Integer> podcast = new AbstractMap.SimpleEntry<>(podcastName, likes);
                mostLikedPodcasts.add(podcast);
            }

            return mostLikedPodcasts;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> showMostNumerousCategories(int limit) {
        return null;
    }

    public List<String> showMostAppreciatedCategories(int limit) {
        return null;
    }

    public List<Entry<String, String>> showSuggestedPodcastsLikedByFollowedUsers(String username, int limit) {
        return null;
    }

    public List<Entry<String, String>> showSuggestedPodcastsBasedOnCategoryOfPodcastsUserLiked(String username, int limit) {
        return null;
    }

    public List<Entry<String, String>> showSuggestedPodcastsBasedOnAuthorsOfPodcastsInWatchlist(String username, int limit) {
        return null;
    }

    // ---------------------------------------------------------------------------------- //
}
