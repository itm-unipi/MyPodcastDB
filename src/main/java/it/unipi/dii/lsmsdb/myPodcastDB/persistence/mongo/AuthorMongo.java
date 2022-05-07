package it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;

import java.util.List;

public class AuthorMongo {

    // ------------------------------- CRUD OPERATION ----------------------------------- //

    // --------- CREATE --------- //

    public boolean addAuthor(Author author) {
        // author.setId(risultato.getObjectId("_id").toString());
        return false;
    }

    // ---------- READ ---------- //

    public Author findAuthorById(String id) {
        return null;
    }

    public Author findAuthorByName(String name) {
        return null;
    }

    public Author findAuthorByEmail(String email) {
        return null;
    }

    public Author findAuthorByPodcastId(String podcastId) {
        return null;
    }

    public Author findAuthorByPodcastName(String podcastName) {
        return null;
    }

    public List<Author> findAuthorsByCountry(String country) { return null; }

    // --------- UPDATE --------- //

    public boolean updateAuthor(Author author) {
        return false;
    }

    public boolean addPodcastToAuthor(String authorId, Podcast podcast) { return false; }

    // --------- DELETE --------- //

    public boolean deleteAuthorById(String id) {
        return false;
    }

    public boolean deleteAuthorByName(String id) {
        return false;
    }

    public boolean deletePodcastOfAuthor(String authorId, String podcastId) { return false; }

    public int deleteAllPodcastsOfAuthor(String authorId) {
        return -1;
    }

    // ---------------------------------------------------------------------------------- //
}
