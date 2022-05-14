package it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j;

import java.util.List;

public class UserNeo4jTest {
    UserNeo4j userNeo4j;

    public UserNeo4jTest() {
        this.userNeo4j = new UserNeo4j();
    }

    public void showSuggestedUsersByFollowedAuthorsTest() {
        List<String> suggestedUsers = userNeo4j.showSuggestedUsersByFollowedAuthors("organicmouse599943", 4);

        if (suggestedUsers != null && suggestedUsers.size() == 4) {
            System.out.println("[+] showSuggestedUsersByFollowedAuthors");
        } else
            System.err.println("[-] showSuggestedUsersByFollowedAuthors");
    }

    public static void main(String[] args) {
        Neo4jManager manager = Neo4jManager.getInstance();
        manager.openConnection();

        UserNeo4jTest test = new UserNeo4jTest();
        test.showSuggestedUsersByFollowedAuthorsTest();
        manager.closeConnection();
    }
}
