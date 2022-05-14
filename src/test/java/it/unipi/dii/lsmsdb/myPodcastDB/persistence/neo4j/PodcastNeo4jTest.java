package it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;

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
        List<Entry<String, String>> podcasts = new ArrayList<>();
        podcasts = podcastNeo4j.findPodcastsByName("Scaling Global");

        if(podcasts != null) {
            System.out.println("[+] findPodcastsByName");
        } else
            System.err.println("[-] findPodcastsByName");
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

    public void deletePodcastByIdTest() {
        // id test 75710
        if (podcastNeo4j.deletePodcastById(75712) && podcastNeo4j.findPodcastsByName("Podcast Test") != null)
            System.out.println("[+] deletePodcastById");
        else
            System.err.println("[-] deletePodcastById");
    }

    public void deletePodcastByPodcastIdTest() {
        Podcast podcast = new Podcast("200", "Podcast Test 200", "art", "Apple Inc.", "5", "Italy", "TestCategory", null, "Spirituality", null, new Date());
        podcastNeo4j.addPodcast(podcast);

        if (podcastNeo4j.deletePodcastByPodcastId(podcast.getId())
                && podcastNeo4j.findPodcastsByName(podcast.getName()) == null) {
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
                podcastNeo4j.findPodcastsByName(podcast1.getName()) == null)
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

        podcastNeo4j.deleteAllPodcastBelongsToCategory(podcast.getId());
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
        List<Entry<String, String>> results = this.podcastNeo4j.showSuggestedPodcastsBasedOnCategoryOfPodcastsUserLiked("yellowtiger876274", 10);
        if (results.get(0).getKey().equals("5505c5469d6cf22b9a12fae9") && results.get(0).getValue().equals("Recommended Movie Squad"))
            System.out.println("[+] showSuggestedPodcastsBasedOnCategoryOfPodcastsUserLiked");
        else
            System.err.println("[-] showMostNumerousCategories");
    }

    public void showPodcastsInWatchlistTest(){
        User user = new User();
        user.setUsername("whiteladybug851481");
        List<Entry<String, String>> podcasts = this.podcastNeo4j.showPodcastsInWatchlist(user, 10);

        if(podcasts == null) {
            System.err.println("showPodcastsInWatchlist");
            return;
        }
        else
            System.out.println("showPodcastsInWatchlist");

        for(Entry<String, String> podcast : podcasts)
            System.out.println(podcast);
    }

    public void showMostAppreciatedCategoriesTest(){

        List<Entry<String, Integer>> categories = this.podcastNeo4j.showMostAppreciatedCategories(10);

        if(categories == null || categories.isEmpty()) {
            System.err.println("showMostAppreciatedCategories");
            return;
        }
        else
            System.out.println("showMostAppreciatedCategories");

        for(Entry<String, Integer> category : categories)
            System.out.println(category);
    }

    public void showSuggestedPodcastsLikedByFollowedUsersTest(){
        User user = new User();
        user.setUsername("whiteladybug851481");
        List<Entry<String, String>> podcasts = this.podcastNeo4j.showSuggestedPodcastsLikedByFollowedUsers(user, 10);

        if(podcasts == null) {
            System.err.println("showSuggestedPodcastsLikedByFollowedUsers");
            return;
        }
        else
            System.out.println("showSuggestedPodcastsLikedByFollowedUsers");

        for(Entry<String, String> podcast : podcasts)
            System.out.println(podcast);
    }

    public void showSuggestedPodcastsBasedOnAuthorsOfPodcastsInWatchlistTest(){
        User user = new User();
        user.setUsername("whiteladybug851481");
        List<Entry<String, String>> podcasts = this.podcastNeo4j.showSuggestedPodcastsBasedOnAuthorsOfPodcastsInWatchlist(user, 10);

        if(podcasts == null) {
            System.err.println("showSuggestedPodcastsBasedOnAuthorsOfPodcastsInWatchlist");
            return;
        }
        else
            System.out.println("showSuggestedPodcastsBasedOnAuthorsOfPodcastsInWatchlist");

        for(Entry<String, String> podcast : podcasts)
            System.out.println(podcast);
    }

    public static void main(String[] args) {
        System.out.println("Test");

        Neo4jManager manager = Neo4jManager.getInstance();
        manager.openConnection();

        PodcastNeo4jTest test = new PodcastNeo4jTest();

        test.findPodcastsByNameTest();
        test.addPodcastTest();
        test.addPodcastBelongsToCategoryTest();
        test.addPodcastCreatedByAuthorTest();
        //test.deletePodcastByIdTest();
        test.deletePodcastByPodcastIdTest();
        test.deletePodcastsByNameTest();
        test.deletePodcastBelongsToCategoryTest();
        test.deleteAllPodcastBelongsToCategoryTest();
        test.showMostNumerousCategoriesTest();
        test.showSuggestedPodcastsBasedOnCategoryOfPodcastsUserLikedTest();
        test.showPodcastsInWatchlistTest();
        test.showMostAppreciatedCategoriesTest();
        test.showSuggestedPodcastsLikedByFollowedUsersTest();
        test.showSuggestedPodcastsBasedOnAuthorsOfPodcastsInWatchlistTest();

        manager.closeConnection();
    }
}
