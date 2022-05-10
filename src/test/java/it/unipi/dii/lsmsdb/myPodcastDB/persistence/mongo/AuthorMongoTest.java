package it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;

import java.util.List;

public class AuthorMongoTest {
    public static void main(String[] args) {
        MongoManager mongoManager = MongoManager.getInstance();
        mongoManager.openConnection();
        AuthorMongo am = new AuthorMongo();

        System.out.println("\n*********************** CREATE NEW AUTHOR ***********************");
        Author newAuthor1 = new Author("0", "Matteo", "testPassword2", "test2@example.com");
        if(am.addAuthor(newAuthor1)) {
            System.out.println("[+] OK");
        } else {
            System.err.println("[-] FAILED");
        }

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
        newAuthor = am.findAuthorByPodcastId("b027e4d3c91a9f36d2a828b7551dc3db");
        if(newAuthor != null)
            System.out.println("[+] OK");
        else
            System.err.println("[-] FAILED");

        System.out.println("\n*********************** FIND AUTHORS BY PODCAST NAME ***********************");
        List<Author> authors = am.findAuthorsByPodcastName("Iconoclash", 3);
        if(authors != null) {
            int counter = 0;
            for (Author author : authors) {
                System.out.println("[+] OK - FOUND " + counter);
                counter += 1;
            }
        } else
            System.err.println("[-] FAILED");

        mongoManager.closeConnection();
    }
}
