package it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.Neo4jManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.PodcastNeo4j;

import java.util.List;
import java.util.Map;

public class PodcastMongoTest {

    public static void main(String[] args) {
        System.out.println("Test CRUD operation for podcasts");

        // serie di operazioni da usare nei controller per mongo
        MongoManager mongoManager = MongoManager.getInstance();
        mongoManager.openConnection();

        PodcastMongo pm = new PodcastMongo();

        //test findPodcastById
        Podcast podcast = pm.findPodcastById("54eb342567c94dacfb2a3e50");
        System.out.println(podcast);
        System.out.println(podcast.getReleaseDateAsString());

        //test addPodcast
        podcast.setName("test1");
        String newId = pm.addPodcast(podcast);
        System.out.println("id: " + newId);

        mongoManager.closeConnection();

    }
}
