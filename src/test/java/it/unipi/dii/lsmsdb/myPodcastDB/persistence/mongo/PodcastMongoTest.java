package it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Episode;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map.Entry;

public class PodcastMongoTest {

    public static void main(String[] args) {
        System.out.println("Test CRUD operation for podcasts");
        
        MongoManager mongoManager = MongoManager.getInstance();
        mongoManager.openConnection();

        PodcastMongo pm = new PodcastMongo();

        testEquals(pm);
        findPodcastByIdTest(pm);
        addPodcastTest(pm);
        findPodcastsByNameTest(pm);
        findPodcastsByAuthorIdTest(pm);
        findPodcastsByAuthorNameTest(pm);
        findPodcastsByPrimaryCategoryTest(pm);
        findPodcastsByCategoryTest(pm);
        updatePodcastTest(pm);
        addEpisodeToPodcastTest(pm);
        addReviewToPodcastTest(pm);

        mongoManager.closeConnection();

    }

    static void testEquals(PodcastMongo pm){

        Podcast p1 = pm.findPodcastById("54eb342567c94dacfb2a3e50");
        Podcast p2 = pm.findPodcastById("54eb342567c94dacfb2a3e50");

        if(!p1.equals(p2))
            System.err.println("[-] equals");
        else
            System.out.println("[+] equals");
    }

    static void addPodcastTest(PodcastMongo pm){

        Podcast podcast = pm.findPodcastById("54eb342567c94dacfb2a3e50");
        podcast.setName("testPodcast");
        if (!pm.addPodcast(podcast))
            System.err.println("[-] addPodcast");
        else
            System.out.println("[+] addPodcast");

    }

    static void findPodcastByIdTest(PodcastMongo pm){

        Podcast podcast = pm.findPodcastById("54eb342567c94dacfb2a3e50");
        if (podcast.getId().equals("54eb342567c94dacfb2a3e50"))
            System.out.println("[+] findPodcastById");
        else
            System.err.println("[-] findPodcastById");
    }

    static void findPodcastsByNameTest(PodcastMongo pm){
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
    }

    static void findPodcastsByAuthorIdTest(PodcastMongo pm){

        String authorId = "000000000000000000016776";
        List<Podcast> podcasts = pm.findPodcastsByAuthorId(authorId,0);
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
    }

    static void findPodcastsByAuthorNameTest(PodcastMongo pm){

        String authorName = "Slate Studios";
        List<Podcast> podcasts = pm.findPodcastsByAuthorName("Slate Studios", 0);
        if (podcasts.isEmpty())
            System.err.println("[-] findPodcastsByAuthorName");
        else {
            boolean err = true;
            for (Podcast newPodcast : podcasts)
                if (!newPodcast.getAuthorName().equals(authorName)) {
                    err = false;
                    System.err.println("[-] findPodcastsByAuthorName");
                    break;
                }
            if(err)
                System.out.println("[+] findPodcastsByAuthorName");
        }


    }

    static void findPodcastsByPrimaryCategoryTest(PodcastMongo pm){

        String primaryCategory = "Business";
        List<Podcast> podcasts = pm.findPodcastsByPrimaryCategory(primaryCategory, 0);
        if (podcasts.isEmpty())
            System.err.println("[-] findPodcastsByPrimaryCategory");
        else{
            boolean err = true;
            for( Podcast newPodcast: podcasts)
                if (!newPodcast.getPrimaryCategory().equals(primaryCategory)){
                    err = false;
                    System.err.println("[-] findPodcastsByPrimaryCategory");
                    break;
                }
            if(err)
                System.out.println("[+] findPodcastsByPrimaryCategory");
        }


    }

    static void findPodcastsByCategoryTest(PodcastMongo pm){

        String category = "Business";
        List<Podcast> podcasts = pm.findPodcastsByCategory(category, 0);
        if (podcasts.isEmpty())
            System.err.println("[-] findPodcastsByCategory");
        else{
            boolean err = true;
            for( Podcast newPodcast: podcasts)
                if (!newPodcast.getCategories().contains(category)){
                    err = false;
                    System.err.println("[-] findPodcastsByCategory");
                    break;
                }
            if(err)
                System.out.println("[+] findPodcastsByCategory");
        }
    }

    static void updatePodcastTest(PodcastMongo pm){

        String name = "testPodcastAfterUpdate";
        Podcast p1 = pm.findPodcastsByName("testPodcast",1).get(0);
        Podcast p2 = pm.findPodcastById("54eb342567c94dacfb2a3e50");

        p1.setName(name);
        p1.setCountry(p2.getCountry());
        p1.setArtworkUrl60(p2.getArtworkUrl60());
        p1.setArtworkUrl600(p2.getArtworkUrl600());
        p1.setAuthor(p2.getAuthorId(), p2.getAuthorName());
        p1.setPrimaryCategory(p2.getPrimaryCategory());
        p1.setCategories(p2.getCategories());
        p1.setContentAdvisoryRating(p2.getContentAdvisoryRating());
        p1.setReleaseDate(p2.getReleaseDate());

        p2.setName(name);

        boolean result = pm.updatePodcast(p1);
        Podcast p3 = pm.findPodcastById(p1.getId());
        if (!result || p3 == null || !p2.equals(p3))
            System.err.println("[-] updatePodcast");
        else
            System.out.println("[+] updatePodcast");
    }

    static void addEpisodeToPodcastTest(PodcastMongo pm){

        String podcastName = "testPodcastAfterUpdate";
        Podcast podcast = pm.findPodcastsByName(podcastName,1).get(0);
        Episode newEpisode = new Episode("testEpisode", "episode for test", podcast.getReleaseDate(), 1000);
        boolean result = pm.addEpisodeToPodcast(podcast.getId(), newEpisode);
        Podcast podcastUpdated = pm.findPodcastsByName(podcastName,1).get(0);
        List<Episode> episodes = podcastUpdated.getEpisodes();
        if(!result || !episodes.contains(newEpisode))
            System.err.println("[-] addEpisodeToPodcastTest");
        else
            System.out.println("[+] addEpisodeToPodcastTest");
    }

    static void addReviewToPodcastTest(PodcastMongo pm){

        String podcastName = "testPodcastAfterUpdate";
        Podcast podcast = pm.findPodcastsByName(podcastName,1).get(0);
        Entry<String, Integer> newReview = new AbstractMap.SimpleEntry<>("100000000000000001021405", 5) ;
        boolean result = pm.addReviewToPodcast(podcast.getId(), newReview.getKey(), newReview.getValue());
        Podcast podcastUpdated = pm.findPodcastsByName(podcastName,1).get(0);
        List<Entry<String, Integer>> reviews = podcastUpdated.getReviews();
        if(!result || !reviews.contains(newReview))
            System.err.println("[-] addReviewToPodcastTest");
        else
            System.out.println("[+] addReviewToPodcastTest");
    }


}
