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
        Podcast podcast = pm.findPodcastById("54eb342567c94dacfb2a3e50");
        if (podcast.getId().equals("54eb342567c94dacfb2a3e50"))
            System.out.println("[+] findPodcastById");
        else
            System.err.println("[-] findPodcastById");


        //test addPodcast
        podcast.setName("testPodcast");
        if (!pm.addPodcast(podcast))
            System.err.println("[-] addPodcast");
        else
            System.out.println("[+] addPodcast");

        //test findPodcastsByName
        String name = "testPodcast";
        List <Podcast> podcasts = pm.findPodcastsByName(name,0);
        if (podcasts.isEmpty())
            System.err.println("[-] findPodcastsByName");
        else {
            boolean err = true;
            for (Podcast newPodcast : podcasts)
                if (!newPodcast.getName().equals(name)){
                    err = false;
                    System.err.println("[-] findPodcastsByName");
                    break;
                }
            if(err)
                System.out.println("[+] findPodcastsByName");
        }

        //test findPodcastsByAuthorId
        String authorId = "000000000000000000016776";
        podcasts = pm.findPodcastsByAuthorId(authorId,0);
        if (podcasts.isEmpty())
            System.err.println("[-] findPodcastsByAuthorId");
        else {
            boolean err = true;
            for (Podcast newPodcast : podcasts)
                if (!newPodcast.getAuthorId().equals(authorId)) {
                    err = false;
                    System.err.println("[-] findPodcastsByAuthorId");
                    break;
                }
            if(err)
                System.out.println("[+] findPodcastsByAuthorId");
        }

        //test findPodcastsByAuthorName
        String authorName = "Slate Studios";
        podcasts = pm.findPodcastsByAuthorName("Slate Studios", 0);
        if (podcasts.isEmpty())
            System.err.println("[-] findPodcastsByAuthorName");
        else {
            boolean err = true;
            for (Podcast newPodcast : podcasts)
                if (!newPodcast.getAuthorName().equals(authorName)) {
                    err = false;
                    System.out.println(newPodcast);
                    break;
                }
            if(err)
                System.out.println("[+] findPodcastsByAuthorName");
        }

        //find findPodcastsByPrimaryCategory
        String category = "Business";
        podcasts = pm.findPodcastsByPrimaryCategory(category, 0);
        if (podcasts.isEmpty())
            System.out.println("[-] findPodcastsByPrimaryCategory");
        else{
            boolean err = true;
            for( Podcast newPodcast: podcasts)
                if (!newPodcast.getPrimaryCategory().equals(category)){
                    err = false;
                    System.err.println("[-] findPodcastsByPrimaryCategory");
                    break;
                }
            if(err)
                System.out.println("[+] findPodcastsByPrimaryCategory");
        }

        //

        mongoManager.closeConnection();

    }

}
