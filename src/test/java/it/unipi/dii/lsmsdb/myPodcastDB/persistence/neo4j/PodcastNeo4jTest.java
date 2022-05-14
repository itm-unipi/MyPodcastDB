package it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j;

import java.util.List;
import java.util.Map.Entry;

public class PodcastNeo4jTest {
    PodcastNeo4j podcastNeo4j;

    public PodcastNeo4jTest() {
        this.podcastNeo4j = new PodcastNeo4j();
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

    public static void main(String[] args) {
        Neo4jManager manager = Neo4jManager.getInstance();
        manager.openConnection();
        PodcastNeo4jTest test = new PodcastNeo4jTest();

        test.showMostNumerousCategoriesTest();
        test.showSuggestedPodcastsBasedOnCategoryOfPodcastsUserLikedTest();

        manager.closeConnection();
    }
}
