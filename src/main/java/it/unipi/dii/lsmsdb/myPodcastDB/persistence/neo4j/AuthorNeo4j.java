package it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;

import java.util.List;

public class AuthorNeo4j {

    // ------------------------------- CRUD OPERATION ----------------------------------- //

    // --------- CREATE --------- //

    public boolean addAuthor(Author author) {
        return false;
    }

    public boolean addAuthorFollowsAuthor(String author, String authorToFollow) {
        return false;
    }

    // ---------- READ ---------- //

    // --------- UPDATE --------- //

    public boolean updateAuthor(Author author) {
        return false;
    }

    // --------- DELETE --------- //

    public boolean deleteAuthor(String author) {
        return false;
    }

    public boolean removeAuthorFollowsAuthor(String author, String authorFollowed) {
        return false;
    }

    public int removeAllAuthorFollowsAuthor(String author) {
        return -1;
    }

    // ---------------------------------------------------------------------------------- //

    // --------------------------------- GRAPH QUERY ------------------------------------ //

    public List<String> showSuggestedAuthorsFollowedByFollowedUser(String username) {
        return null;
    }

    // ---------------------------------------------------------------------------------- //
}
