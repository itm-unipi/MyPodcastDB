package it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo;

import it.unipi.dii.lsmsdb.myPodcastDB.model.*;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AuthorMongoTest {
    public static void main(String[] args) {
        MongoManager mongoManager = MongoManager.getInstance();
        mongoManager.openConnection();
        AuthorMongo am = new AuthorMongo();

        System.out.println("\n*********************** CREATE NEW AUTHOR ***********************");
        /*
        Author newAuthor1 = new Author("0", "Matteo", "testPassword2", "test2@example.com");
        if(am.addAuthor(newAuthor1)) {
            if(am.findAuthorById(newAuthor1.getId()) != null)
                System.out.println("[+] OK");
            else
                System.err.println("[-] FAILED TO FIND");
        } else {
            System.err.println("[-] FAILED");
        }
         */


        Author newAuthor;

        System.out.println("\n*********************** FIND AUTHOR BY ID ***********************");
        newAuthor = am.findAuthorById("000000000000000000000004");
        if(newAuthor != null)
            System.out.println("[+] OK");
        else
            System.err.println("[-] FAILED");

        System.out.println("\n*********************** FIND AUTHOR BY NAME ***********************");
        newAuthor = am.findAuthorByName("Michael Colosi");
        if(newAuthor != null)
            System.out.println("[+] OK");
        else
            System.err.println("[-] FAILED");

        System.out.println("\n*********************** FIND AUTHOR BY EMAIL ***********************");
        newAuthor = am.findAuthorByEmail("michaelcolosi@example.com");
        if(newAuthor != null)
            System.out.println("[+] OK");
        else
            System.err.println("[-] FAILED");

        System.out.println("\n*********************** FIND AUTHOR BY PODCAST ID ***********************");
        newAuthor = am.findAuthorByPodcastId("9a0b19c62640a048297cea26");
        if(newAuthor != null)
            System.out.println("[+] OK");
        else
            System.err.println("[-] FAILED");

        System.out.println("\n*********************** FIND AUTHORS BY PODCAST NAME ***********************");
        List<Author> authors = am.findAuthorsByPodcastName("Iconoclash", 3);
        if(authors != null) {
            int counter = 0;
            for (Author author : authors) {
                System.out.println("[+] OK - FOUND " + (counter + 1));
                counter += 1;
            }
        } else
            System.err.println("[-] FAILED");

        System.out.println("\n*********************** DELETE AUTHOR BY ID ***********************");
        if(am.deleteAuthorById("627a8beceb66577c8e5367b3")) {
            if(am.findAuthorById("627a8beceb66577c8e5367b3") != null)
                System.err.println("[-] FAILED - USER STILL PRESENT");
            else
                System.out.println("[+] OK");
        } else
            System.err.println("[-] FAILED");

        System.out.println("\n*********************** DELETE AUTHOR BY NAME ***********************");
        if(am.deleteAuthorByName("Matteo"))
            if(am.findAuthorByName("Matteo") != null)
                System.err.println("[-] FAILED - USER STILL PRESENT");
            else
                System.out.println("[+] OK");
        else
            System.err.println("[-] FAILED");

        System.out.println("\n*********************** ADD PODCASTS TO A SPECIFIC AUTHOR ***********************");
        Date date = new Date();
        Podcast podcast = new Podcast("0", "PodcastTest", "art", "art1600", "5", "Italy", "TestCategory", null, null, null, date);

        if(am.addPodcastToAuthor("000000000000000000000004", podcast)) {
            if (am.findAuthorByPodcastId(podcast.getId()) != null)
                System.out.println("[+] OK");
            else
                System.err.println("[-] FAILED FIND");
        } else
            System.err.println("[-] FAILED");

        System.out.println("\n*********************** DELETE PODCAST OF A SPECIFIC AUTHOR BY ITS NAME ***********************");
        if(am.deletePodcastOfAuthor("000000000000000000000004", "627aab1996badc34f06140a2"))
            if(am.findAuthorByPodcastId("627aab1996badc34f06140a2") != null)
                System.err.println("[-] FAILED - PODCAST STILL PRESENT");
            else
                System.out.println("[+] OK");
        else
            System.err.println("[-] FAILED");

        System.out.println("\n*********************** DELETE ALL PODCASTS OF AN AUTHOR ***********************");
        /*
        int DeletedCount = am.deleteAllPodcastsOfAuthor("000000000000000000000004");
        if(DeletedCount >= 0)
            System.out.println("[+] OK - DELETED " + DeletedCount);
        else
            System.err.println("[-] FAILED");
         */

        System.out.println("\n*********************** UPDATE AUTHOR ***********************");
        Author updatedAuthor = new Author("000000000000000000000004", "Kennedy HS Students", "modPasswd", "kennedyhsstudents@example.com");

        if(am.updateAuthor(updatedAuthor)) {
            if (am.findAuthorById(updatedAuthor.getId()) != null)
                System.out.println("[+] OK");
            else
                System.err.println("[-] FAILED FIND");
        } else
            System.err.println("[-] FAILED");

        System.out.println("\n*********************** UPDATE PODCAST AUTHOR ***********************");
        //Date date = new Date();
        am.updatePodcastOfAuthor("000000000000000000000000", "6a70d8d5ffcc27889ba41086", "Salon and Spa Marketing Toolkit NEW", "2012-01-15T12:47:00Z");
        /*
        if(am.findAuthorByPodcastId("000000000000000000000000") != null) {
            System.out.println("[+] OK");
        } else
            System.err.println("[-] FAILED");
        */
        mongoManager.closeConnection();
    }
}
