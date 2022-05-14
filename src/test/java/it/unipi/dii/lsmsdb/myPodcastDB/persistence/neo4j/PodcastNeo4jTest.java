package it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;

import java.util.AbstractMap;
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

        for (Entry<String, String> e: podcasts)
            System.out.println(e.getKey() + " " + e.getValue());
    }

    public void addPodcastTest() {
        Podcast podcast = new Podcast("0", "Podcast Test", "art", "art1600", "5", "Italy", "TestCategory", null, "Spirituality", null, new Date());

        if (podcastNeo4j.addPodcast(podcast))
            System.out.println("[+] addPodcast");
        else
            System.err.println("[-] addPodcast");
    }

    public void addPodcastBelongsToCategoryTest() {
        Podcast podcast = new Podcast("1", "Podcast Test Category", "art", "art1600", "5", "Italy", "TestCategory", null, "Spirituality", null, new Date());
        podcastNeo4j.addPodcast(podcast);

        if (podcastNeo4j.addPodcastBelongsToCategory(podcast, "Spirituality"))
            System.out.println("[+] addPodcastBelongsToCategory");
        else
            System.err.println("[-] addPodcastBelongsToCategory");

        podcastNeo4j.deletePodcastBelongsToCategory("1", "Spirituality");
        podcastNeo4j.deletePodcastByPodcastId("1");
    }

    public void addPodcastCreatedByAuthorTest() {
        Podcast podcast = new Podcast("2", "Podcast Test Author", "art", "Apple Inc.", "5", "Italy", "TestCategory", null, "Spirituality", null, new Date());
        podcastNeo4j.addPodcast(podcast);

        if (podcastNeo4j.addPodcastCreatedByAuthor(podcast))
            System.out.println("[+] addPodcastCreatedByAuthor");
        else
            System.err.println("[-] addPodcastCreatedByAuthor");

        podcastNeo4j.deletePodcastCreatedByAuthor("2", "Apple Inc.");
        podcastNeo4j.deletePodcastByPodcastId("2");
    }

    public void deletePodcastByIdTest() {
        // id test 75710
        if (podcastNeo4j.deletePodcastById(75712)) {
            if(podcastNeo4j.findPodcastsByName("Podcast Test") != null)
                System.out.println("[+] deletePodcastById");
        } else
            System.err.println("[-] deletePodcastById");
    }

    public void deletePodcastByPodcastIdTest() {
        if (podcastNeo4j.deletePodcastByPodcastId("0")) {
            if(podcastNeo4j.findPodcastsByName("Podcast Test") != null)
                System.out.println("[+] deletePodcastByPodcastId");
        } else
            System.err.println("[-] deletePodcastByPodcastId");
    }

    public void deletePodcastsByNameTest() {
        Podcast podcast1 = new Podcast("0", "Podcast Test Name", "art", "art1600", "5", "Italy", "TestCategory", null, "Spirituality", null, new Date());
        Podcast podcast2 = new Podcast("0", "Podcast Test Name", "art", "art1600", "5", "Italy", "TestCategory", null, "Spirituality", null, new Date());

        podcastNeo4j.addPodcast(podcast1);
        podcastNeo4j.addPodcast(podcast2);

        if (podcastNeo4j.deletePodcastsByName(podcast1.getName())) {
            if(podcastNeo4j.findPodcastsByName(podcast1.getName()) != null)
                System.out.println("[+] deletePodcastsByName");
        } else
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

        podcastNeo4j.deleteAllPodcastBelongsToCategory("20");
        podcastNeo4j.deletePodcastByPodcastId("20");
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

        podcastNeo4j.deletePodcastByPodcastId("15");
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
        test.deleteAllPodcastBelongsToCategoryTest();;

        manager.closeConnection();
    }
}
