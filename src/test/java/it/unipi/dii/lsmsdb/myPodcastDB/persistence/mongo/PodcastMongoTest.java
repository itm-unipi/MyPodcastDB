package it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.Neo4jManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.PodcastNeo4j;

import java.util.ArrayList;
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
        System.out.println("findPodcastById...");
        Podcast podcast = pm.findPodcastById("54eb342567c94dacfb2a3e50");
        System.out.println(podcast);
        System.out.println("/-------------------/");


        //test addPodcast
        System.out.println("addPodcast...");
        podcast.setName("testPodcast");
        String newId = pm.addPodcast(podcast);
        System.out.println("id: " + newId);
        System.out.println("/-------------------/");

        //test findPodcastsByName
        System.out.println("findPodcastsByName...");
        newId = pm.addPodcast(podcast);
        System.out.println("id: " + newId);
        List <Podcast> podcasts = pm.findPodcastsByName("testPodcast",0);
        if( podcasts.isEmpty())
            System.out.println("podcast not found");
        else
            for( Podcast newPodcast: podcasts)
                System.out.println(newPodcast);
        System.out.println("/-------------------/");

        //test findPodcastsByAuthorId
        System.out.println("findPodcastsByAuthorId...");
        podcasts = pm.findPodcastsByAuthorId("000000000000000000016776",2);
        if( podcasts.isEmpty())
            System.out.println("podcast not found");
        else
            for( Podcast newPodcast: podcasts)
                System.out.println(newPodcast);
        System.out.println("/-------------------/");

        //test findPodcastsByAuthorName
        System.out.println("findPodcastsByAuthorName...");
        podcasts = pm.findPodcastsByAuthorName("Slate Studios", 3);
        if( podcasts.isEmpty())
            System.out.println("podcast not found");
        else
            for( Podcast newPodcast: podcasts)
                System.out.println(newPodcast);
        System.out.println("/-------------------/");

        //find findPodcastsByPrimaryCategory
        System.out.println("findPodcastsByPrimaryCategory...");
        podcasts = pm.findPodcastsByPrimaryCategory("Business", 2);
        if( podcasts.isEmpty())
            System.out.println("podcast not found");
        else
            for( Podcast newPodcast: podcasts)
                System.out.println(newPodcast);
        System.out.println("/-------------------/");

        mongoManager.closeConnection();

    }
}
