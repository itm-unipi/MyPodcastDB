package it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Episode;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ConfigManager;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.javatuples.Triplet;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class PodcastMongoTest {
    
    PodcastMongo podcastMongo;

    public PodcastMongoTest() {
        this.podcastMongo = new PodcastMongo();
    }

    public static void main(String[] args) {
        Logger.initialize();
        System.out.println("Test CRUD operation for podcasts");

        ConfigManager.importConfig("config.xml", "src/main/java/it/unipi/dii/lsmsdb/myPodcastDB/utility/schema.xsd");
        
        MongoManager mongoManager = MongoManager.getInstance();
        mongoManager.openConnection();
        
        PodcastMongoTest test = new PodcastMongoTest();
        test.testEquals();
        test.findPodcastByIdTest();
        test.addPodcastTest();
        test.findPodcastsByNameTest();
        test.findPodcastsByAuthorIdTest();
        test.findPodcastsByAuthorNameTest();
        test.findPodcastsByPrimaryCategoryTest();
        test.findPodcastsByCategoryTest();
        test.updatePodcastTest();
        test.addEpisodeToPodcastTest();
        test.addReviewToPodcastTest();
        test.deletePodcastByIdTest();
        test.deletePodcastsByNameTest();
        test.deletePodcastByAuthorIdTest();
        test.deletePodcastByAuthorNameTest();
        test.deleteEpisodeOfPodcastTest();
        test.deleteAllEpisodesTest();
        test.deleteReviewTest();
        test.deleteAllReviewsTest();

        test.showPodcastsWithHighestAverageRatingTest();
        test.showPodcastsWithHighestAverageRatingPerCountryTest();
        test.showPodcastsWithHighestNumberOfReviewsTest();
        test.showAuthorWithHighestAverageRatingTest();

        test.showCountriesWithHighestNumberOfPodcastsTest();

        mongoManager.closeConnection();

    }

    void testEquals(){
        Podcast p1 = this.podcastMongo.findPodcastById("54eb342567c94dacfb2a3e50");
        Podcast p2 = this.podcastMongo.findPodcastById("54eb342567c94dacfb2a3e50");

        if(!p1.equals(p2))
            System.err.println("[-] equals");
        else
            System.out.println("[+] equals");
    }
    
    void addPodcastTest(){
        Podcast podcast = this.podcastMongo.findPodcastById("54eb342567c94dacfb2a3e50");
        podcast.setName("testPodcast");
        if (!this.podcastMongo.addPodcast(podcast))
            System.err.println("[-] addPodcast");
        else
            System.out.println("[+] addPodcast");
    }
    
    void findPodcastByIdTest(){
        Podcast podcast = this.podcastMongo.findPodcastById("54eb342567c94dacfb2a3e50");
        if (podcast.getId().equals("54eb342567c94dacfb2a3e50"))
            System.out.println("[+] findPodcastById");
        else
            System.err.println("[-] findPodcastById");
    }
 
    void findPodcastsByNameTest(){
        String name = "testPodcast";
        List <Podcast> podcasts = this.podcastMongo.findPodcastsByName(name,0);
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
 
    void findPodcastsByAuthorIdTest(){
        String authorId = "000000000000000000016776";
        List<Podcast> podcasts = this.podcastMongo.findPodcastsByAuthorId(authorId,0);
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
 
    void findPodcastsByAuthorNameTest(){
        String authorName = "Slate Studios";
        List<Podcast> podcasts = this.podcastMongo.findPodcastsByAuthorName("Slate Studios", 0);
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
 
    void findPodcastsByPrimaryCategoryTest(){
        String primaryCategory = "Business";
        List<Podcast> podcasts = this.podcastMongo.findPodcastsByPrimaryCategory(primaryCategory, 0);

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
 
    void findPodcastsByCategoryTest(){
        String category = "Business";
        List<Podcast> podcasts = this.podcastMongo.findPodcastsByCategory(category, 0);

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

    void updatePodcastTest(){
        String name = "testPodcastAfterUpdate";
        Podcast p1 = this.podcastMongo.findPodcastsByName("testPodcast",1).get(0);
        Podcast p2 = this.podcastMongo.findPodcastById("54eb342567c94dacfb2a3e50");

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
 
        boolean result = this.podcastMongo.updatePodcast(p1);
        Podcast p3 = this.podcastMongo.findPodcastById(p1.getId());

        if (!result || p3 == null || !p2.equals(p3))
            System.err.println("[-] updatePodcast");
        else
            System.out.println("[+] updatePodcast");
    }
 
    void addEpisodeToPodcastTest(){
        String podcastName = "testPodcastAfterUpdate";
        Podcast podcast = this.podcastMongo.findPodcastsByName(podcastName,1).get(0);
        Episode newEpisode = new Episode("testEpisode", "episode for test", podcast.getReleaseDate(), 1000);
        boolean result = this.podcastMongo.addEpisodeToPodcast(podcast.getId(), newEpisode);
        Podcast podcastUpdated = this.podcastMongo.findPodcastsByName(podcastName,1).get(0);

        List<Episode> episodes = podcastUpdated.getEpisodes();
        if(!result || !episodes.contains(newEpisode))
            System.err.println("[-] addEpisodeToPodcastTest");
        else
            System.out.println("[+] addEpisodeToPodcastTest");
    }
 
    void addReviewToPodcastTest(){
        String podcastName = "testPodcastAfterUpdate";
        Podcast podcast = this.podcastMongo.findPodcastsByName(podcastName,1).get(0);
        Entry<String, Integer> newReview = new AbstractMap.SimpleEntry<>("100000000000000001021405", 5) ;
        boolean result = this.podcastMongo.addReviewToPodcast(podcast.getId(), newReview.getKey(), newReview.getValue());
        Podcast podcastUpdated = this.podcastMongo.findPodcastsByName(podcastName,1).get(0);

        List<Entry<String, Integer>> reviews = podcastUpdated.getReviews();
        if(!result || !reviews.contains(newReview))
            System.err.println("[-] addReviewToPodcastTest");
        else
            System.out.println("[+] addReviewToPodcastTest");
    }
 
    void deletePodcastByIdTest() {
        Podcast podcast = this.podcastMongo.findPodcastsByName("testPodcastAfterUpdate",1).get(0);
        String id = podcast.getId();
        if (this.podcastMongo.deletePodcastById(id) && this.podcastMongo.findPodcastById(id) == null)
            System.out.println("[+] deletePodcastById");
        else
            System.err.println("[-] deletePodcastById");
    }
 
    void deletePodcastsByNameTest() {
        String name = "testPodcast";
        Podcast podcast = this.podcastMongo.findPodcastById("54eb342567c94dacfb2a3e50");
        podcast.setName(name);
        this.podcastMongo.addPodcast(podcast);
        if ((this.podcastMongo.deletePodcastsByName(name) > 0) && this.podcastMongo.findPodcastsByName(name,0).isEmpty())
            System.out.println("[+] deletePodcastByName");
        else
            System.err.println("[-] deletePodcastByName");
    }
 
    void deletePodcastByAuthorIdTest(){
        String name = "testPodcast";
        Podcast podcast = this.podcastMongo.findPodcastById("54eb342567c94dacfb2a3e50");
        String authorId = "010000000000000000016776";
        String authorName = podcast.getAuthorName();
        podcast.setName(name);
        podcast.setAuthor(authorId, authorName);

        this.podcastMongo.addPodcast(podcast);
        if ((this.podcastMongo.deletePodcastsByAuthorId(authorId) > 0) && this.podcastMongo.findPodcastsByAuthorId(authorId,0).isEmpty())
            System.out.println("[+] deletePodcastByAuthorId");
        else
            System.err.println("[-] deletePodcastByAuthorId");
    }

    void deletePodcastByAuthorNameTest(){
        String name = "testPodcast";
        Podcast podcast = this.podcastMongo.findPodcastById("54eb342567c94dacfb2a3e50");
        String authorId = "010000000000000000016776";
        String authorName = "author test";
        podcast.setName(name);
        podcast.setAuthor(authorId, authorName);

        this.podcastMongo.addPodcast(podcast);
        if ((this.podcastMongo.deletePodcastsByAuthorName(authorName) > 0) && this.podcastMongo.findPodcastsByAuthorName(authorName,0).isEmpty())
            System.out.println("[+] deletePodcastByAuthorName");
        else
            System.err.println("[-] deletePodcastByAuthorName");
    }
    
    void deleteEpisodeOfPodcastTest(){
        String name = "testPodcast";
        Podcast podcast = this.podcastMongo.findPodcastById("54eb342567c94dacfb2a3e50");
        podcast.setName(name);
        this.podcastMongo.addPodcast(podcast);
        Episode newEpisode = new Episode("testEpisode", "episode for test", podcast.getReleaseDate(), 1000);
        this.podcastMongo.addEpisodeToPodcast(podcast.getId(), newEpisode);

        if(this.podcastMongo.deleteEpisodeOfPodcast(podcast.getId(), "testEpisode"))
            System.out.println("[+] deleteEpisodeOfPodcast");
        else
            System.out.println("[-] deleteEpisodeOfPodcast");
        
        this.podcastMongo.deletePodcastsByName("testPodcast");
    }

    void deleteAllEpisodesTest() {
        String name = "testPodcast";
        Podcast podcast = this.podcastMongo.findPodcastById("54eb342567c94dacfb2a3e50");
        podcast.setName(name);
        this.podcastMongo.addPodcast(podcast);
        Episode newEpisode1 = new Episode("testEpisode1", "episode for test", podcast.getReleaseDate(), 1000);
        this.podcastMongo.addEpisodeToPodcast(podcast.getId(), newEpisode1);
        Episode newEpisode2 = new Episode("testEpisode2", "episode for test", podcast.getReleaseDate(), 1000);
        this.podcastMongo.addEpisodeToPodcast(podcast.getId(), newEpisode2);
        Episode newEpisode3 = new Episode("testEpisode3", "episode for test", podcast.getReleaseDate(), 1000);
        this.podcastMongo.addEpisodeToPodcast(podcast.getId(), newEpisode3);

        this.podcastMongo.deleteAllEpisodesOfPodcast(podcast.getId());
        podcast = this.podcastMongo.findPodcastById(podcast.getId());

        if (podcast.getEpisodes().size() == 0)
            System.out.println("[+] deleteAllEpisodesOfPodcast");
        else
            System.err.println("[-] deleteAllEpisodesOfPodcast");

        this.podcastMongo.deletePodcastById(podcast.getId());
    }

    void deleteReviewTest() {
        String name = "testPodcast";
        Podcast podcast = this.podcastMongo.findPodcastById("54eb342567c94dacfb2a3e50");
        podcast.setName(name);
        podcast.setReviews(new ArrayList<>());
        this.podcastMongo.addPodcast(podcast);

        this.podcastMongo.addReviewToPodcast(podcast.getId(), "000000000000000000000000", 5);
        this.podcastMongo.deleteReviewOfPodcast(podcast.getId(), "000000000000000000000000");
        podcast = this.podcastMongo.findPodcastById(podcast.getId());

        if (podcast.getReviews().size() == 0)
            System.out.println("[+] deleteReviewOfPodcast");
        else
            System.err.println("[-] deleteReviewOfPodcast");

        this.podcastMongo.deletePodcastById(podcast.getId());
    }

    void deleteAllReviewsTest() {
        String name = "testPodcast";
        Podcast podcast = this.podcastMongo.findPodcastById("54eb342567c94dacfb2a3e50");
        podcast.setName(name);
        podcast.setReviews(new ArrayList<>());
        this.podcastMongo.addPodcast(podcast);

        this.podcastMongo.addReviewToPodcast(podcast.getId(), "000000000000000000000000", 1);
        this.podcastMongo.addReviewToPodcast(podcast.getId(), "000000000000000000000001", 2);
        this.podcastMongo.addReviewToPodcast(podcast.getId(), "000000000000000000000002", 3);
        this.podcastMongo.addReviewToPodcast(podcast.getId(), "000000000000000000000003", 4);
        this.podcastMongo.addReviewToPodcast(podcast.getId(), "000000000000000000000004", 5);
        this.podcastMongo.deleteAllReviewsOfPodcast(podcast.getId());
        podcast = this.podcastMongo.findPodcastById(podcast.getId());

        if (podcast.getReviews().size() == 0)
            System.out.println("[+] deleteAllReviewsOfPodcast");
        else
            System.err.println("[-] deleteAllReviewsOfPodcast");

        this.podcastMongo.deletePodcastById(podcast.getId());
    }

    void showPodcastsWithHighestAverageRatingTest() {
        System.out.println("showPodcastsWithHighestAverageRatingTest:");
        for (Pair<Podcast, Float> record : this.podcastMongo.showPodcastsWithHighestAverageRating(3))
            System.out.println(record);
    }

    void showPodcastsWithHighestAverageRatingPerCountryTest() {
        System.out.println("showPodcastsWithHighestAverageRatingPerCountryTest:");
        for (Triplet<Podcast, String, Float> record : this.podcastMongo.showPodcastsWithHighestAverageRatingPerCountry(3))
            System.out.println(record);
    }

    void showPodcastsWithHighestNumberOfReviewsTest() {
        System.out.println("showPodcastsWithHighestNumberOfReviewsTest:");
        for (Pair<Podcast, Integer> record : this.podcastMongo.showPodcastsWithHighestNumberOfReviews(3))
            System.out.println(record);
    }

    void showAuthorWithHighestAverageRatingTest(){

        List<Entry<String, Float>> authors = this.podcastMongo.showAuthorWithHighestAverageRating(10);
        if(authors == null || authors.isEmpty())
            System.err.println("[-] showAuthorWithHighestAverageRating");
        else {
            System.out.println("[+] showAuthorWithHighestAverageRating");
            for(Entry<String, Float> author : authors)
                System.out.println(author);
        }
    }

    void showCountriesWithHighestNumberOfPodcastsTest() {
        List<Pair<String, Integer>> countries = new ArrayList<>();
        countries = this.podcastMongo.showCountriesWithHighestNumberOfPodcasts(4);

        if (countries != null) {
            System.out.println("[+] showCountriesWithHighestNumberOfPodcasts");
            for (Pair<String, Integer> s: countries) {
                System.out.println(s);
            }
        } else {
            System.err.println("[-] showCountriesWithHighestNumberOfPodcasts");
        }
    }
}
