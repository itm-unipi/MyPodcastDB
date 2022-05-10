package it.unipi.dii.lsmsdb.myPodcastDB;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.MongoManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.PodcastMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.Neo4jManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.PodcastNeo4j;

import java.util.List;
import java.util.Map.Entry;

public class Main {
    public static void main(String[] args) {

        System.out.println("Test");

        // serie di operazioni da usare nei controller per mongo
        MongoManager mongoManager = MongoManager.getInstance();
        mongoManager.openConnection();

        PodcastMongo pm = new PodcastMongo();
        Podcast podcast = pm.findPodcastById("54eb342567c94dacfb2a3e50");
        System.out.println(podcast);
        System.out.println(podcast.getReleaseDateAsString());

        mongoManager.closeConnection();

        // serie di operazioni da usare nei controller per neo4j
        /*
        Neo4jManager neo4jManager = Neo4jManager.getInstance();
        neo4jManager.openConnection();

        PodcastNeo4j pn = new PodcastNeo4j();
        List<Entry<String, String>> podcasts = pn.findPodcastsByName("Vence View Podcast");
        for (Entry<String, String> result : podcasts) {
            System.out.println(result.getKey() + " " + result.getValue());
        }

        neo4jManager.closeConnection();
        */
    }
}
