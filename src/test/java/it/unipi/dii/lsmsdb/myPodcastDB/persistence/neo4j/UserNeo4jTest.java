package it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;

public class UserNeo4jTest {

    UserNeo4j userNeo4j;

    public static void  main(String[] args){

        Neo4jManager manager = Neo4jManager.getInstance();
        manager.openConnection();
        UserNeo4jTest test = new UserNeo4jTest();

        test.addUserTest();
        test.addUserLikesPodcastTest();
        test.addUserWatchLaterPodcastTest();
        test.addUserFollowUserTest();
        test.addUserFollowAuthorTest();
        test.updateUserTest();
        test.deleteUserTest();
        test.deleteUserLikesPodcastTest();
        test.deleteAllUserLikesPodcastTest();
        test.deleteUserWatchLaterPodcastTest();
        test.deleteAllUserWatchLaterPodcastTest();
        test.deleteUserFollowUserTest();
        test.deleteAllUserFollowUserTest();
        test.deleteUserFollowAuthorTest();
        test.deleteAllUserFollowAuthorTest();

        manager.closeConnection();

    }

    public UserNeo4jTest(){ this.userNeo4j = new UserNeo4j();}

    /*
    CYPHER TO TEST
    CREATE (p:Podcast{name: "podcast test", podcastId: "1234567890", class: "test"});
    CREATE (p:Podcast{name: "podcast test1", podcastId: "1234567891", class: "test"});
    CREATE (p:Podcast{name: "podcast test2", podcastId: "1234567892", class: "test"});
    CREATE (p:Podcast{name: "podcast test3", podcastId: "1234567893", class: "test"});
    CREATE (p:Podcast{name: "podcast test4", podcastId: "1234567894", class: "test"});
    CREATE (a:Author{name: "author test", class: "test"});
    CREATE (a:Author{name: "author test1", class: "test"});
    CREATE (a:Author{name: "author test2", class: "test"});
    CREATE (a:Author{name: "author test3", class: "test"});
    MATCH (p:Podcast{class:"test"}), (a:Author{class:"test"}) RETURN p,a;

    TO CLEAN
    MATCH (p:Podcast{class:"test"}), (a:Author{class:"test"}) DELETE p,a;

    */

    public void addUserTest(){

        User user = new User();
        user.setUsername("user test");
        boolean result = this.userNeo4j.addUser(user);
        if(result && this.userNeo4j.checkUserExists(user))
            System.out.println("[+] addUser");
        else
            System.err.println("[-] addUser");

    }

    public void addUserLikesPodcastTest(){

        User user = new User();
        Podcast podcast = new Podcast();
        user.setUsername("user test");
        podcast.setId("1234567890");

        boolean result = this.userNeo4j.addUserLikesPodcast(user, podcast);
        if(result && this.userNeo4j.checkUserLikesPodcastExists(user, podcast))
            System.out.println("[+] addUserLikesPodcast");
        else
            System.err.println("[-] addUserLikesPodcast");

    }

    public void addUserWatchLaterPodcastTest(){

        User user = new User();
        Podcast podcast = new Podcast();
        user.setUsername("user test");
        podcast.setId("1234567891");

        boolean result = this.userNeo4j.addUserWatchLaterPodcast(user, podcast);
        if(result && this.userNeo4j.checkUserWatchLaterPodcastExists(user, podcast))
            System.out.println("[+] addUserWatchLaterPodcast");
        else
            System.err.println("[-] addUserWatchLaterPodcast");

    }

    public void addUserFollowUserTest(){

        User user1 = new User();
        User user2 = new User();
        user1.setUsername("user test");
        user2.setUsername("user test2");

        this.userNeo4j.addUser(user2);
        boolean result = this.userNeo4j.addUserFollowUser(user1,user2);
        if(result && this.userNeo4j.checkUserFollowUserExists(user1, user2))
            System.out.println("[+] addUserFollowUser");
        else
            System.err.println("[-] addUserFollowUser");

    }

    public void addUserFollowAuthorTest(){

        User user = new User();
        Author author = new Author();
        user.setUsername("user test");
        author.setName("author test");
        boolean result = this.userNeo4j.addUserFollowAuthor(user, author);
        if(result && this.userNeo4j.checkUserFollowAuthorExists(user, author))
            System.out.println("[+] addUserFollowAuthor");
        else
            System.err.println("[-] addUserFollowAuthor");

    }

    public void updateUserTest(){

        User user = new User();
        user.setUsername("user test");
        String newUsername = "user test3";
        boolean result = this.userNeo4j.updateUser(user, newUsername);
        if(result && user.getUsername().equals(newUsername))
            System.out.println("[+] updateUser");
        else
            System.err.println("[-] updateUser");

    }

    public void deleteUserTest(){

        User user = new User();
        user.setUsername("user test4");
        this.userNeo4j.addUser(user);

        boolean result = this.userNeo4j.deleteUser(user);
        if(result && !this.userNeo4j.checkUserExists(user))
            System.out.println("[+] deleteUser");
        else
            System.err.println("[-] deleteUser");

    }

    public void deleteUserLikesPodcastTest(){

        User user = new User();
        Podcast podcast = new Podcast();
        user.setUsername("user test3");
        podcast.setId("1234567890");

        boolean result = this.userNeo4j.deleteUserLikesPodcast(user, podcast);
        if(result && !this.userNeo4j.checkUserLikesPodcastExists(user, podcast))
            System.out.println("[+] deleteUserLikesPodcast");
        else
            System.err.println("[-] deleteUserLikesPodcast");

    }

    public void deleteAllUserLikesPodcastTest(){

        User user = new User();
        Podcast podcast1 = new Podcast();
        Podcast podcast2 = new Podcast();
        Podcast podcast3 = new Podcast();
        user.setUsername("user test3");
        podcast1.setId("1234567892");
        podcast2.setId("1234567893");
        podcast3.setId("1234567894");
        this.userNeo4j.addUserLikesPodcast(user, podcast1);
        this.userNeo4j.addUserLikesPodcast(user, podcast2);
        this.userNeo4j.addUserLikesPodcast(user, podcast3);

        boolean result = this.userNeo4j.deleteAllUserLikesPodcast(user);
        if(result && !this.userNeo4j.checkAllUserLikesPodcastExists(user))
            System.out.println("[+] deleteAllUserLikesPodcast");
        else
            System.err.println("[-] deleteAllUserLikesPodcast");

    }

    public void deleteUserWatchLaterPodcastTest(){

        User user = new User();
        Podcast podcast = new Podcast();
        user.setUsername("user test3");
        podcast.setId("1234567891");

        boolean result = this.userNeo4j.deleteUserWatchLaterPodcast(user, podcast);
        if(result && !this.userNeo4j.checkUserWatchLaterPodcastExists(user, podcast))
            System.out.println("[+] deleteUserWatchLaterPodcast");
        else
            System.err.println("[-] deleteUserWatchLaterPodcast");

    }

    public void deleteAllUserWatchLaterPodcastTest(){

        User user = new User();
        Podcast podcast1 = new Podcast();
        Podcast podcast2 = new Podcast();
        Podcast podcast3 = new Podcast();
        user.setUsername("user test3");
        podcast1.setId("1234567892");
        podcast2.setId("1234567893");
        podcast3.setId("1234567894");
        this.userNeo4j.addUserWatchLaterPodcast(user, podcast1);
        this.userNeo4j.addUserWatchLaterPodcast(user, podcast2);
        this.userNeo4j.addUserWatchLaterPodcast(user, podcast3);

        boolean result = this.userNeo4j.deleteAllUserWatchLaterPodcast(user);
        if(result && !this.userNeo4j.checkAllUserWatchLaterPodcastExists(user))
            System.out.println("[+] deleteAllUserWatchLaterPodcast");
        else
            System.err.println("[-] deleteAllUserWatchLaterPodcast");


    }

    public void deleteUserFollowUserTest(){

        User user1 = new User();
        User user2 = new User();
        user1.setUsername("user test3");
        user2.setUsername("user test2");

        boolean result = this.userNeo4j.deleteUserFollowUser(user1, user2);
        if(result && !this.userNeo4j.checkUserFollowUserExists(user1, user2))
            System.out.println("[+] deleteUserFollowUser");
        else
            System.err.println("[-] deleteUserFollowUser");

        this.userNeo4j.deleteUser(user2);


    }

    public void deleteAllUserFollowUserTest(){

        User user = new User();
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        user.setUsername("user test3");
        user.setUsername("user test5");
        user.setUsername("user test6");
        user.setUsername("user test7");
        this.userNeo4j.addUser(user1);
        this.userNeo4j.addUser(user2);
        this.userNeo4j.addUser(user3);
        this.userNeo4j.addUserFollowUser(user, user1);
        this.userNeo4j.addUserFollowUser(user, user2);
        this.userNeo4j.addUserFollowUser(user, user3);

        boolean result = this.userNeo4j.deleteAllUserFollowUser(user);
        if(result && !this.userNeo4j.checkAllUserFollowUserExists(user))
            System.out.println("[+] deleteAllUserFollowUser");
        else
            System.err.println("[-] deleteAllUserFollowUser");

        this.userNeo4j.deleteUser(user1);
        this.userNeo4j.deleteUser(user2);
        this.userNeo4j.deleteUser(user3);
    }

    public void deleteUserFollowAuthorTest(){

        User user = new User();
        Author author = new Author();
        user.setUsername("user test3");
        author.setName("author test");

        boolean result = this.userNeo4j.deleteUserFollowAuthor(user, author);
        if(result && !this.userNeo4j.checkUserFollowAuthorExists(user, author))
            System.out.println("[+] deleteUserFollowAuthor");
        else
            System.err.println("[-] deleteUserFollowAuthor");

    }

    public void deleteAllUserFollowAuthorTest(){

        User user = new User();
        Author author1 = new Author();
        Author author2 = new Author();
        Author author3 = new Author();
        user.setUsername("user test3");
        author1.setName("author test1");
        author1.setName("author test2");
        author1.setName("author test3");
        this.userNeo4j.addUserFollowAuthor(user, author1);
        this.userNeo4j.addUserFollowAuthor(user, author2);
        this.userNeo4j.addUserFollowAuthor(user, author3);

        boolean result = this.userNeo4j.deleteAllUserFollowAuthor(user);
        if(result && !this.userNeo4j.checkAllUserFollowAuthorExists(user))
            System.out.println("[+] deleteAllUserFollowAuthor");
        else
            System.err.println("[-] deleteAllUserFollowAuthor");

        this.userNeo4j.deleteUser(user);

    }

}
