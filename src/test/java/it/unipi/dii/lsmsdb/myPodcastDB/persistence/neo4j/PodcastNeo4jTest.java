package it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ConfigManager;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

public class PodcastNeo4jTest {
    PodcastNeo4j podcastNeo4j;

    public PodcastNeo4jTest() {
        this.podcastNeo4j = new PodcastNeo4j();
    }

    public void findPodcastsByNameTest() {
        List<Podcast> podcasts = new ArrayList<>();
        podcasts = podcastNeo4j.findPodcastsByName("Scaling Global", 10);

        if(podcasts != null) {
            System.out.println("[+] findPodcastsByName");
        } else
            System.err.println("[-] findPodcastsByName");
    }

    public void findPodcastsByPodcastIdTest() {
        Podcast podcast = podcastNeo4j.findPodcastByPodcastId("54eb342567c94dacfb2a3e50");

        if(podcast != null && podcast.getName().equals("Scaling Global")) {
            System.out.println("[+] findPodcastByPodcastId");
        } else
            System.err.println("[-] findPodcastByPodcastId");
    }

    public void addPodcastTest() {
        Podcast podcast = new Podcast("0", "Podcast Test", "art", "art1600", "5", "Italy", "TestCategory", null, "Spirituality", null, new Date());

        if (podcastNeo4j.addPodcast(podcast))
            System.out.println("[+] addPodcast");
        else
            System.err.println("[-] addPodcast");
    }

    public void addPodcastBelongsToCategoryTest() {
        String category = "Spirituality";
        Podcast podcast = new Podcast("111", "Podcast Test Category", "art", "art1600", "5", "Italy", "TestCategory", null, "Spirituality", null, new Date());
        podcastNeo4j.addPodcast(podcast);

        if (podcastNeo4j.addPodcastBelongsToCategory(podcast, category) &&
                podcastNeo4j.findPodcastBelongsToCategory(podcast.getId(), category))
            System.out.println("[+] addPodcastBelongsToCategory");
        else
            System.err.println("[-] addPodcastBelongsToCategory");

        podcastNeo4j.deletePodcastBelongsToCategory(podcast.getId(), category);
        podcastNeo4j.deletePodcastByPodcastId(podcast.getId());
    }

    public void addPodcastCreatedByAuthorTest() {
        Podcast podcast = new Podcast("2", "Podcast Test Author", "art", "Apple Inc.", "5", "Italy", "TestCategory", null, "Spirituality", null, new Date());
        podcastNeo4j.addPodcast(podcast);

        if (podcastNeo4j.addPodcastCreatedByAuthor(podcast) &&
                podcastNeo4j.findPodcastCreatedByAuthor(podcast.getId(), podcast.getAuthorName()))
            System.out.println("[+] addPodcastCreatedByAuthor");
        else
            System.err.println("[-] addPodcastCreatedByAuthor");

        podcastNeo4j.deletePodcastCreatedByAuthor(podcast.getId(), podcast.getAuthorName());
        podcastNeo4j.deletePodcastByPodcastId(podcast.getId());
    }

    public void updatePodcastTest() {
        boolean result = podcastNeo4j.updatePodcast("0", "Podcast Test Updated", "18");
        Podcast podcast = podcastNeo4j.findPodcastByPodcastId("0");

        if (podcast.getName().equals("Podcast Test Updated") && podcast.getArtworkUrl600().equals("18"))
            System.out.println("[+] updatePodcast");
        else
            System.err.println("[-] updatePodcast");
    }
    public void deletePodcastByIdTest() {
        // id test 75710
        if (podcastNeo4j.deletePodcastById(75712) && podcastNeo4j.findPodcastsByName("Podcast Test", 10) != null)
            System.out.println("[+] deletePodcastById");
        else
            System.err.println("[-] deletePodcastById");
    }

    public void deletePodcastByPodcastIdTest() {
        Podcast podcast = podcastNeo4j.findPodcastByPodcastId("0");

        if (podcastNeo4j.deletePodcastByPodcastId(podcast.getId())
                && podcastNeo4j.findPodcastsByName(podcast.getName(), 10) == null) {
            System.out.println("[+] deletePodcastByPodcastId");
        } else
            System.err.println("[-] deletePodcastByPodcastId");
    }

    public void deletePodcastsByNameTest() {
        Podcast podcast1 = new Podcast("0", "Podcast Test Name", "art", "art1600", "5", "Italy", "TestCategory", null, "Spirituality", null, new Date());
        Podcast podcast2 = new Podcast("0", "Podcast Test Name", "art", "art1600", "5", "Italy", "TestCategory", null, "Spirituality", null, new Date());

        podcastNeo4j.addPodcast(podcast1);
        podcastNeo4j.addPodcast(podcast2);

        if (podcastNeo4j.deletePodcastsByName(podcast1.getName()) &&
                podcastNeo4j.findPodcastsByName(podcast1.getName(), 10) == null)
            System.out.println("[+] deletePodcastsByName");
        else
            System.err.println("[-] deletePodcastsByName");
    }

    public void deletePodcastBelongsToCategoryTest() {
        Podcast podcast = new Podcast("20", "Podcast Test Category", "art", "art1600", "5", "Italy", "TestCategory", null, "Spirituality", null, new Date());
        podcastNeo4j.addPodcast(podcast);

        podcastNeo4j.addPodcastBelongsToCategory(podcast, "Spirituality");
        podcastNeo4j.addPodcastBelongsToCategory(podcast, "Business");

        if (podcastNeo4j.deletePodcastBelongsToCategory(podcast.getId(), "Spirituality")) {
            System.out.println("[+] deletePodcastBelongsToCategory");
        } else
            System.err.println("[-] deletePodcastBelongsToCategory");

        podcastNeo4j.deletePodcastByPodcastId(podcast.getId());
    }

    public void deleteAllPodcastBelongsToCategoryTest() {
        Podcast podcast = new Podcast("15", "Podcast Test Category", "art", "art1600", "5", "Italy", "TestCategory", null, "Spirituality", null, new Date());
        podcastNeo4j.addPodcast(podcast);

        podcastNeo4j.addPodcastBelongsToCategory(podcast, "Spirituality");
        podcastNeo4j.addPodcastBelongsToCategory(podcast, "Business");

        if (podcastNeo4j.deleteAllPodcastBelongsToCategory(podcast.getId())) {
            System.out.println("[+] deleteAllPodcastBelongsToCategory");
        } else
            System.err.println("[-] deleteAllPodcastBelongsToCategory");

        podcastNeo4j.deletePodcastByPodcastId(podcast.getId());
    }

    public void showMostNumerousCategoriesTest() {
        List<Entry<String, Integer>> results = this.podcastNeo4j.showMostNumerousCategories(10);
        if (results.get(0).getKey().equals("Society & Culture") && results.get(0).getValue() == 8143)
            System.out.println("[+] showMostNumerousCategories");
        else
            System.err.println("[-] showMostNumerousCategories");
    }

    public void showSuggestedPodcastsBasedOnCategoryOfPodcastsUserLikedTest() {
        List<Podcast> results = this.podcastNeo4j.showSuggestedPodcastsBasedOnCategoryOfPodcastsUserLiked("yellowtiger876274", 10);
        if (results.get(0).getId().equals("50e391e1e88310022fb76600") && results.get(0).getName().equals("Leominster Church of Christ Sermons"))
            System.out.println("[+] showSuggestedPodcastsBasedOnCategoryOfPodcastsUserLiked");
        else
            System.err.println("[-] showSuggestedPodcastsBasedOnCategoryOfPodcastsUserLiked");
    }

    public void showPodcastsInWatchlistTest(){
        User user = new User();
        user.setUsername("whiteladybug851481");
        List<Podcast> podcasts = this.podcastNeo4j.showPodcastsInWatchlist(user.getUsername(), 10, 0);

        if(podcasts == null) {
            System.err.println("[-] showPodcastsInWatchlist");
            return;
        }
        else
            System.out.println("[+] showPodcastsInWatchlist");

        for(Podcast podcast : podcasts)
            System.out.println(podcast);
    }

    public void showLikedPodcastsByUserTest(){
        User user = new User();
        user.setUsername("whiteladybug851481");
        List<Podcast> podcasts = this.podcastNeo4j.showPodcastsInWatchlist(user.getUsername(), 10, 0);

        if(podcasts == null) {
            System.err.println("[-] showLikedPodcastsByUser");
            return;
        }
        else
            System.out.println("[+] showLikedPodcastsByUser");

        for(Podcast podcast : podcasts)
            System.out.println(podcast);
    }

    public void showMostAppreciatedCategoriesTest(){

        List<Entry<String, Integer>> categories = this.podcastNeo4j.showMostAppreciatedCategories(10);

        if(categories == null || categories.isEmpty()) {
            System.err.println("[-] showMostAppreciatedCategories");
            return;
        }
        else
            System.out.println("[+] showMostAppreciatedCategories");

        for(Entry<String, Integer> category : categories)
            System.out.println(category);
    }

    public void showSuggestedPodcastsLikedByFollowedUsersTest(){
        User user = new User();
        user.setUsername("whiteladybug851481");
        List<Podcast> podcasts = this.podcastNeo4j.showSuggestedPodcastsLikedByFollowedUsers(user, 10);

        if(podcasts == null) {
            System.err.println("[-] showSuggestedPodcastsLikedByFollowedUsers");
            return;
        }
        else
            System.out.println("[+] showSuggestedPodcastsLikedByFollowedUsers");

        for(Podcast podcast : podcasts)
            System.out.println(podcast);
    }

    public void showSuggestedPodcastsBasedOnAuthorsOfPodcastsInWatchlistTest(){
        User user = new User();
        user.setUsername("whiteladybug851481");
        List<Podcast> podcasts = this.podcastNeo4j.showSuggestedPodcastsBasedOnAuthorsOfPodcastsInWatchlist(user, 10);

        if(podcasts == null) {
            System.err.println("[-] showSuggestedPodcastsBasedOnAuthorsOfPodcastsInWatchlist");
            return;
        }
        else
            System.out.println("[+] showSuggestedPodcastsBasedOnAuthorsOfPodcastsInWatchlist");

        for(Podcast podcast : podcasts)
            System.out.println(podcast);
    }

    public void showMostLikedPodcastsTest () {
        List<Entry<Podcast, Integer>> mostLikedPodcasts = podcastNeo4j.showMostLikedPodcasts(5);

        if (mostLikedPodcasts != null && mostLikedPodcasts.size() == 5) {
            //for (Entry<String, Integer> e : mostLikedPodcasts)
            //    System.out.println(e);
            System.out.println("[+] showMostLikedPodcasts");
        } else
            System.err.println("[-] showMostLikedPodcasts");
    }

    public static void main(String[] args) {
        Logger.initialize();
        ConfigManager.importConfig("config.xml", "src/main/java/it/unipi/dii/lsmsdb/myPodcastDB/utility/schema.xsd");

        Neo4jManager manager = Neo4jManager.getInstance();
        manager.openConnection();

        PodcastNeo4jTest test = new PodcastNeo4jTest();

        test.findPodcastsByNameTest();
        test.findPodcastsByPodcastIdTest();
        test.addPodcastTest();
        test.addPodcastBelongsToCategoryTest();
        test.addPodcastCreatedByAuthorTest();
        test.updatePodcastTest();
        //test.deletePodcastByIdTest();
        test.deletePodcastByPodcastIdTest();
        test.deletePodcastsByNameTest();
        test.deletePodcastBelongsToCategoryTest();
        test.deleteAllPodcastBelongsToCategoryTest();
        test.showMostNumerousCategoriesTest();
        test.showSuggestedPodcastsBasedOnCategoryOfPodcastsUserLikedTest();
        test.showPodcastsInWatchlistTest();
        test.showLikedPodcastsByUserTest();
        test.showMostAppreciatedCategoriesTest();
        test.showSuggestedPodcastsLikedByFollowedUsersTest();
        test.showSuggestedPodcastsBasedOnAuthorsOfPodcastsInWatchlistTest();
        test.showMostLikedPodcastsTest();

        manager.closeConnection();
    }
}
