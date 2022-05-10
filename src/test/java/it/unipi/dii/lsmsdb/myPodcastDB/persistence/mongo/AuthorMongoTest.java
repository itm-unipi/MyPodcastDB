package it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;

public class AuthorMongoTest {
    public static void main(String[] args) {

        System.out.println("Test Author Mongo");

        MongoManager mongoManager = MongoManager.getInstance();
        mongoManager.openConnection();
        AuthorMongo am = new AuthorMongo();

        /*
        Author newAuthor = new Author("0", "Matteo", "testPassword2", "test2@example.com");
        if(am.addAuthor(newAuthor)) {
            System.out.println("Success! \n" + newAuthor.toString());
        } else {
            System.out.println("Fail!");
        }
        */

        System.out.println("\n*********************** FIND AUTHOR BY ID ***********************");
        Author newAuthor2 = am.findAuthorById("000000000000000000000005");
        if(newAuthor2 != null) {
            System.out.println(newAuthor2.toString());
        } else {
            System.out.println("Author not found!");
        }

        System.out.println("\n*********************** FIND AUTHOR BY NAME ***********************");
        Author newAuthor3 = am.findAuthorByName("Michael Colosi");
        if(newAuthor3 != null) {
            System.out.println(newAuthor3.toString());
        } else {
            System.out.println("Author not found!");
        }

        System.out.println("\n*********************** FIND AUTHOR BY EMAIL ***********************");
        Author newAuthor4 = am.findAuthorByEmail("michaelcolosi@example.com");
        if(newAuthor4 != null) {
            System.out.println(newAuthor4.toString());
        } else {
            System.out.println("Author not found!");
        }

        mongoManager.closeConnection();
    }
}
