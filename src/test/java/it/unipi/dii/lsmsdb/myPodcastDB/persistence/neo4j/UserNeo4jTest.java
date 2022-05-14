package it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;

import java.util.List;

public class UserNeo4jTest {
    UserNeo4j userNeo4j;
    AuthorNeo4j authorNeo4j;

    public UserNeo4jTest() {
        this.userNeo4j = new UserNeo4j();
        this.authorNeo4j = new AuthorNeo4j();
    }

    public void deleteUserFollowUserTest() {
        User user = new User();
        user.setUsername("test1");
        this.userNeo4j.addUser(user);
        user.setUsername("test2");
        this.userNeo4j.addUser(user);
        this.userNeo4j.addUserFollowUser("test1", "test2");
        this.userNeo4j.deleteUserFollowUser("test1", "test2");

        boolean test = this.userNeo4j.findUserFollowsUser("test1", "test2");
        if (!test)
            System.out.println("[+] deleteUserFollowUser");
        else
            System.err.println("[-] deleteUserFollowUser");

        this.userNeo4j.deleteUser("test1");
        this.userNeo4j.deleteUser("test2");
    }

    public void deleteAllUserFollowUserTest() {
        User user = new User();
        user.setUsername("test1");
        this.userNeo4j.addUser(user);
        user.setUsername("test2");
        this.userNeo4j.addUser(user);
        user.setUsername("test3");
        this.userNeo4j.addUser(user);
        this.userNeo4j.addUserFollowUser("test1", "test2");
        this.userNeo4j.addUserFollowUser("test1", "test3");
        this.userNeo4j.deleteAllUserFollowUser("test1");

        boolean test1 = this.userNeo4j.findUserFollowsUser("test1", "test2");
        boolean test2 = this.userNeo4j.findUserFollowsUser("test1", "test3");
        if (!test1 && !test2)
            System.out.println("[+] deleteUserFollowUserTest");
        else
            System.err.println("[-] deleteUserFollowUserTest");

        this.userNeo4j.deleteUser("test1");
        this.userNeo4j.deleteUser("test2");
        this.userNeo4j.deleteUser("test3");
    }

    public void deleteUserFollowAuthorTest() {
        User user = new User();
        user.setUsername("test1");
        this.userNeo4j.addUser(user);
        Author author = new Author();
        author.setName("test2");
        this.authorNeo4j.addAuthor(author);

        this.userNeo4j.addUserFollowAuthor("test1", "test2");
        this.userNeo4j.deleteUserFollowAuthor("test1", "test2");

        boolean test = this.userNeo4j.findUserFollowsAuthor("test1", "test2");
        if (!test)
            System.out.println("[+] deleteUserFollowAuthor");
        else
            System.err.println("[-] deleteUserFollowAuthor");

        this.userNeo4j.deleteUser("test1");
        this.authorNeo4j.deleteAuthor("test2");
    }

    public void deleteAllUserFollowAuthorTest() {
        User user = new User();
        user.setUsername("test1");
        this.userNeo4j.addUser(user);
        Author author = new Author();
        author.setName("test2");
        this.authorNeo4j.addAuthor(author);
        author.setName("test3");
        this.authorNeo4j.addAuthor(author);

        this.userNeo4j.addUserFollowAuthor("test1", "test2");
        this.userNeo4j.addUserFollowAuthor("test1", "test3");
        this.userNeo4j.deleteAllUserFollowAuthor("test1");

        boolean test1 = this.userNeo4j.findUserFollowsAuthor("test1", "test2");
        boolean test2 = this.userNeo4j.findUserFollowsAuthor("test1", "test3");
        if (!test1 && !test2)
            System.out.println("[+] deleteAllUserFollowAuthor");
        else
            System.err.println("[-] deleteAllUserFollowAuthor");

        this.userNeo4j.deleteUser("test1");
        this.authorNeo4j.deleteAuthor("test2");
        this.authorNeo4j.deleteAuthor("test3");
    }

    void showSuggestedUsersByLikedPodcastsTest() {
        List<String> users = this.userNeo4j.showSuggestedUsersByLikedPodcasts("whiterabbit394794", 25);
        boolean test = false;
        for (String username : users)
            if (username.equals("heavyfish188030"))
                test = true;

        if (test)
            System.out.println("[+] showSuggestedUsersByLikedPodcasts");
        else
            System.err.println("[-] showSuggestedUsersByLikedPodcasts");
    }

    public static void main(String[] args) {
        Neo4jManager manager = Neo4jManager.getInstance();
        manager.openConnection();
        UserNeo4jTest test = new UserNeo4jTest();

        test.showSuggestedUsersByLikedPodcastsTest();

        manager.closeConnection();
    }
}
