package it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo;

import it.unipi.dii.lsmsdb.myPodcastDB.model.*;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ConfigManager;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AuthorMongoTest {

    AuthorMongo am;

    public AuthorMongoTest() {
        this.am = new AuthorMongo();
    }

    public Author addAuthorForTest() {
        Author newAuthor = new Author("0", "Matteo", "testPassword", "test@example.com", "");
        am.addAuthor(newAuthor);
        return newAuthor;
    }

    public void deleteAuthorForTest(String authorId) {
        am.deleteAuthorById(authorId);
    }

    public static boolean compare(Author a1, Author a2) {
        if (!a1.getId().equals(a2.getId()))
            return false;
        if (!a1.getName().equals(a2.getName()))
            return false;
        if (!a1.getPassword().equals(a2.getPassword()))
            return false;
        if (!a1.getEmail().equals(a2.getEmail()))
            return false;

        // TODO: redefine equals for podcasts
        return true;
    }

    public void addAuthorTest() {
        Author newAuthor = addAuthorForTest();
        Author foundAuthor = am.findAuthorById(newAuthor.getId());

        if (foundAuthor != null && compare(newAuthor, foundAuthor))
            System.out.println("[+] addAuthor");
        else
            System.err.println("[-] addAuthor");

        deleteAuthorForTest(newAuthor.getId());
    }

    public void findAuthorByIdTest() {
        // Author already present in Mongo
        Author testAuthor = new Author("000000000000000000000000", "Michael Colosi", "cfcd208495d565ef66e7dff9f98764da", "michaelcolosi@example.com", "");
        String date = "2012-01-15 12:47:00";
        Date podDate;

        try {
            podDate = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").parse(date);
            testAuthor.addPodcast(new Podcast("6a70d8d5ffcc27889ba41086", "Salon and Spa Marketing Toolkit", podDate, "", ""));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Author foundAuthor = am.findAuthorById("000000000000000000000000");

        if (foundAuthor != null && compare(foundAuthor, testAuthor))
            System.out.println("[+] findAuthorById");
        else
            System.err.println("[-] findAuthorById");
    }

    public void findAuthorByNameTest() {
        Author a1 = am.findAuthorById("000000000000000000000000");
        Author a2 = am.findAuthorByName("Michael Colosi");

        if (a1 != null && a2 != null && compare(a2, a1))
            System.out.println("[+] findAuthorByName");
        else
            System.err.println("[-] findAuthorByName");

    }

    public void findAuthorByEmailTest() {
        Author a1 = am.findAuthorById("000000000000000000000000");
        Author a2 = am.findAuthorByEmail("michaelcolosi@example.com");

        if (a1 != null && a2 != null && compare(a2, a1))
            System.out.println("[+] findAuthorByEmail");
        else
            System.err.println("[-] findAuthorByEmail");

    }

    public void findAuthorByPodcastIdTest() {
        Author a1 = am.findAuthorById("000000000000000000000000");
        Author a2 = am.findAuthorByPodcastId("6a70d8d5ffcc27889ba41086");

        if (a1 != null && a2 != null && compare(a2, a1))
            System.out.println("[+] findAuthorByPodcastId");
        else
            System.err.println("[-] findAuthorByPodcastId");

    }

    public void findAuthorByPodcastNameTest() {
        List<Author> authorList = am.findAuthorsByPodcastName("Salon and Spa Marketing Toolkit", 0);
        Author a2;

        boolean fail = false;
        for (Author a1: authorList) {
            a2 = am.findAuthorById(a1.getId());

            if (a2 == null || !compare(a2, a1)){
                fail = true;
                break;
            }
        }

        if (!fail)
            System.out.println("[+] findAuthorByPodcastName");
        else
            System.err.println("[-] findAuthorByPodcastName");
    }

    public void deleteAuthorByIdTest() {
        Author a1 = addAuthorForTest();

        String authorId = a1.getId();
        am.deleteAuthorById(authorId);

        if(am.findAuthorById(authorId) == null)
            System.out.println("[+] deleteAuthorById");
        else {
            System.err.println("[-] deleteAuthorById");
            deleteAuthorForTest(a1.getId());
        }
    }


    public void deleteAuthorByNameTest() {
        Author a1 = addAuthorForTest();

        String authorId = a1.getId();
        String authorName = a1.getName();
        am.deleteAuthorByName(authorName);

        if(am.findAuthorById(authorId) == null)
            System.out.println("[+] deleteAuthorByName");
        else {
            System.err.println("[-] deleteAuthorByName");
            deleteAuthorForTest(a1.getId());
        }
    }


    public void deletePodcastOfAuthorTest() {
        Author newAuthor = addAuthorForTest();

        // Add a new podcast
        Date date = new Date();
        Podcast podcast = new Podcast("0", "PodcastTest-1", "art", "art1600", "5", "Italy", "TestCategory1", null, null, null, date);

        am.addPodcastToAuthor(newAuthor.getId(), podcast);

        if (am.deletePodcastOfAuthor(newAuthor.getId(), podcast.getId()))
            if (am.findAuthorByPodcastId(podcast.getId()) == null)
                System.out.println("[+] deletePodcastOfAuthor");
            else
                System.err.println("[-] deletePodcastOfAuthor");
        else
            System.err.println("[-] deletePodcastOfAuthor");

        deleteAuthorForTest(newAuthor.getId());
    }

    public void deleteAllPodcastsOfAuthorTest() {
        Author newAuthor = addAuthorForTest();

        // Add two podcasts
        Date date = new Date();
        List<Podcast> pods = new ArrayList<>();

        Podcast podcast1 = new Podcast("0", "PodcastTest1", "art", "art1600", "5", "Italy", "TestCategory1", null, null, null, date);
        pods.add(podcast1);
        Podcast podcast2 = new Podcast("0", "PodcastTest2", "art", "art1600", "5", "Italy", "TestCategory2", null, null, null, date);
        pods.add(podcast2);

        am.addPodcastToAuthor(newAuthor.getId(), podcast1);
        am.addPodcastToAuthor(newAuthor.getId(), podcast2);

        boolean fail = false;
        if (am.deleteAllPodcastsOfAuthor(newAuthor.getId()))
            for(Podcast podcast: pods) {
                if (am.findAuthorByPodcastId(podcast.getId()) != null){
                    fail = true;
                    break;
                }
            }
        else
            System.err.println("[-] deleteAllPodcastsOfAuthor");

        if(!fail)
            System.out.println("[+] deleteAllPodcastsOfAuthor");
        else
            System.err.println("[-] deleteAllPodcastsOfAuthor");

        deleteAuthorForTest(newAuthor.getId());
    }

    public void addPodcastToAuthorTest() {
        Author newAuthor = addAuthorForTest();
        String authorId = newAuthor.getId();

        Date date = new Date();
        Podcast podcast = new Podcast("0", "PodcastTest", "art", "art1600", "5", "Italy", "TestCategory", null, null, null, date);

        if (am.addPodcastToAuthor(authorId, podcast)) {
            // try to find the new podcast
            Author a = am.findAuthorByPodcastId(podcast.getId());

            // check if the author of the found podcast is the same
            if (a != null && a.getId().equals(authorId))
                System.out.println("[+] addPodcastToAuthor");
            else
                System.err.println("[-] addPodcastToAuthor");
        } else
            System.err.println("[-] addPodcastToAuthor");

        deleteAuthorForTest(newAuthor.getId());
    }

    public void updatePodcastOfAuthorTest() {
        Author newAuthor = addAuthorForTest();
        String authorId = newAuthor.getId();

        Date date = new Date();
        Podcast podcast = new Podcast("0", "PodcastTest", "art", "art1600", "5", "Italy", "TestCategory", null, null, null, date);
        am.addPodcastToAuthor(authorId, podcast);

        int podcastIndex = 0;
        if(am.updatePodcastOfAuthor(authorId, podcastIndex, podcast.getId(), "Podcast Test UPDATED", "2012-01-15T12:47:00Z"))
            if(am.findAuthorByPodcastId(podcast.getId()) != null)
                System.out.println("[+] updatePodcastOfAuthor");
            else
                System.err.println("[-] updatePodcastOfAuthorTest");
        else
            System.err.println("[-] updatePodcastOfAuthorTest");

        deleteAuthorForTest(newAuthor.getId());
    }

    public void updateAuthorTest() {
        Author newAuthor = addAuthorForTest();
        String authorId = newAuthor.getId();

        Author oldAuthor = am.findAuthorById(authorId);
        Author updatedAuthor = new Author(authorId, "Matteo", "modifiedPassword", "test@example.com", "");

        if(am.updateAuthor(updatedAuthor)) {
            if (am.findAuthorById(authorId) != null && !compare(oldAuthor, updatedAuthor))
                System.out.println("[+] updateAuthor");
            else
                System.err.println("[!] updateAuthor - INFO: no changes");
        } else
            System.err.println("[-] updateAuthor");

        deleteAuthorForTest(newAuthor.getId());
    }

    public static void main(String[] args) {
        Logger.initialize();
        ConfigManager.importConfig("config.xml", "src/main/java/it/unipi/dii/lsmsdb/myPodcastDB/utility/schema.xsd");

        MongoManager mongoManager = MongoManager.getInstance();
        mongoManager.openConnection();

        AuthorMongoTest test = new AuthorMongoTest();
        test.addAuthorTest();
        test.findAuthorByIdTest();
        test.findAuthorByNameTest();
        test.findAuthorByEmailTest();
        test.findAuthorByPodcastIdTest();
        test.findAuthorByPodcastNameTest();

        test.deleteAuthorByIdTest();
        test.deleteAuthorByNameTest();
        test.deletePodcastOfAuthorTest();
        test.deleteAllPodcastsOfAuthorTest();

        test.addPodcastToAuthorTest();

        test.updateAuthorTest();
        test.updatePodcastOfAuthorTest();

        mongoManager.closeConnection();
    }
}
