package it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo;

import it.unipi.dii.lsmsdb.myPodcastDB.model.*;
import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AuthorMongoTest {

    AuthorMongo am;

    public AuthorMongoTest() {
        this.am = new AuthorMongo();
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
        Author newAuthor = new Author("0", "Matteo", "testPassword", "test@example.com");
        am.addAuthor(newAuthor);

        Author foundAuthor = am.findAuthorById(newAuthor.getId());

        if (foundAuthor != null && compare(newAuthor, foundAuthor))
            System.out.println("[+] addAuthor");
        else
            System.err.println("[-] addAuthor");
    }

    public void findAuthorByIdTest() {
        // Author already present in Mongo
        Author testAuthor = new Author("000000000000000000000000", "Michael Colosi", "cfcd208495d565ef66e7dff9f98764da", "michaelcolosi@example.com");
        String date = "2012-01-15 12:47:00";
        Date podDate;

        try {
            podDate = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").parse(date);
            testAuthor.addPodcast("6a70d8d5ffcc27889ba41086", "Salon and Spa Marketing Toolkit", podDate);
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
        Author a1 = new Author("0", "Author Test 1", "testPassword", "test@example.com");
        am.addAuthor(a1);

        String authorId = a1.getId();
        am.deleteAuthorById(authorId);

        if(am.findAuthorById(authorId) == null)
            System.out.println("[+] deleteAuthorById");
        else
            System.err.println("[-] deleteAuthorById");
    }


    public void deleteAuthorByNameTest() {
        Author a1 = new Author("0", "Author Test 2", "testPassword", "test@example.com");
        am.addAuthor(a1);

        String authorId = a1.getId();
        String authorName = a1.getName();
        am.deleteAuthorByName(authorName);

        if(am.findAuthorById(authorId) == null)
            System.out.println("[+] deleteAuthorByName");
        else
            System.err.println("[-] deleteAuthorByName");
    }


    public void deletePodcastOfAuthorTest() {
        Author newAuthor = new Author("0", "Test Author 3", "testPassword", "test@example.com");
        am.addAuthor(newAuthor);

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
    }

    public void deleteAllPodcastsOfAuthorTest() {
        Author newAuthor = new Author("0", "Gianluca G", "testPassword", "test@example.com");
        am.addAuthor(newAuthor);

        // Add two podcasts
        Date date = new Date();
        List<Podcast> pods = new ArrayList<Podcast>();

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
    }

    public void addPodcastToAuthorTest() {
        Date date = new Date();
        Podcast podcast = new Podcast("0", "PodcastTest", "art", "art1600", "5", "Italy", "TestCategory", null, null, null, date);

        String authorId = "627bdeaefb42e00db224c2a4";
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
    }

    public void updatePodcastOfAuthorTest() {
        // TODO
        am.updatePodcastOfAuthor("000000000000000000000000", "6a70d8d5ffcc27889ba41086", "Salon and Spa Marketing Toolkit UPDATED", "2012-01-15T12:47:00Z");

        if(am.findAuthorByPodcastId("6a70d8d5ffcc27889ba41086") != null) {
            System.out.println("[+] OK");
        } else
            System.err.println("[-] FAILED OR NOT FOUND");
    }

    public void updateAuthorTest() {
        Author newAuthor = new Author("0", "Gianluca X", "testPassword", "test@example.com");
        am.addAuthor(newAuthor);

        String authorId = newAuthor.getId();
        Author oldAuthor = am.findAuthorById(authorId);
        Author updatedAuthor = new Author(authorId, "Gianluca X", "modifiedPassword", "test@example.com");

        if(am.updateAuthor(updatedAuthor)) {
            if (am.findAuthorById(authorId) != null && !compare(oldAuthor, updatedAuthor))
                System.out.println("[+] updateAuthor");
            else
                System.err.println("[!] updateAuthor - INFO: no changes");
        } else
            System.err.println("[-] updateAuthor");
    }

    public static void main(String[] args) {
        MongoManager mongoManager = MongoManager.getInstance();
        mongoManager.openConnection();

        AuthorMongoTest test = new AuthorMongoTest();
        //test.addAuthorTest();
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
        //test.updatePodcastOfAuthorTest();
        
        mongoManager.closeConnection();
    }
}
