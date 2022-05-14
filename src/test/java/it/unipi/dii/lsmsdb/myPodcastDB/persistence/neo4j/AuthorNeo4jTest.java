package it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j;

import java.util.List;

public class AuthorNeo4jTest {
    AuthorNeo4j authorNeo4j;

    public AuthorNeo4jTest() {
        this.authorNeo4j = new AuthorNeo4j();
    }

    public void showSuggestedAuthorsFollowedByFollowedUserTest() {
        List<String> authors = authorNeo4j.showSuggestedAuthorsFollowedByFollowedUser("organicmouse599943", 10);

        if (authors != null && authors.size() == 10)
            System.out.println("[+] showSuggestedAuthorsFollowedByFollowedUser");
        else
            System.err.println("[-] showSuggestedAuthorsFollowedByFollowedUser");
    }

    public static void main(String[] args) {
        Neo4jManager manager = Neo4jManager.getInstance();
        manager.openConnection();

        AuthorNeo4jTest test = new AuthorNeo4jTest();
        test.showSuggestedAuthorsFollowedByFollowedUserTest();
        manager.closeConnection();
    }
}
