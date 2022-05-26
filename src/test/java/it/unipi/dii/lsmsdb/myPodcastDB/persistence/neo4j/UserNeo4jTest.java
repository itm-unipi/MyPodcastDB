package it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ConfigManager;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;

import java.util.List;
import java.util.Scanner;

public class UserNeo4jTest {
    PodcastNeo4j podcastNeo4j;
    UserNeo4j userNeo4j;
    AuthorNeo4j authorNeo4j;

    public static void  main(String[] args){
        Logger.initialize();
        ConfigManager.importConfig("config.xml", "src/main/java/it/unipi/dii/lsmsdb/myPodcastDB/utility/schema.xsd");

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
        test.showFollowedUserTest();
        test.showSuggestedUsersByLikedPodcastsTest();
        test.showSuggestedUsersByFollowedAuthorsTest();

        manager.closeConnection();
    }

    public UserNeo4jTest(){
        this.userNeo4j = new UserNeo4j();
        this.authorNeo4j = new AuthorNeo4j();
        this.podcastNeo4j = new PodcastNeo4j();
    }

    public void addUserTest(){
        // prerequisites
        Podcast podcast = new Podcast();
        podcast.setName("podcast test");
        podcast.setId("1234567890");
        this.podcastNeo4j.addPodcast(podcast);
        podcast.setName("podcast test1");
        podcast.setId("1234567891");
        this.podcastNeo4j.addPodcast(podcast);
        podcast.setName("podcast test2");
        podcast.setId("1234567892");
        this.podcastNeo4j.addPodcast(podcast);
        podcast.setName("podcast test3");
        podcast.setId("1234567893");
        this.podcastNeo4j.addPodcast(podcast);
        podcast.setName("podcast test4");
        podcast.setId("1234567894");
        this.podcastNeo4j.addPodcast(podcast);
        Author author = new Author();
        author.setName("author test");
        this.authorNeo4j.addAuthor(author.getName());
        author.setName("author test1");
        this.authorNeo4j.addAuthor(author.getName());
        author.setName("author test2");
        this.authorNeo4j.addAuthor(author.getName());
        author.setName("author test3");
        this.authorNeo4j.addAuthor(author.getName());

        User user = new User();
        user.setUsername("user test");
        user.setPictureMedium("picture");
        boolean result = this.userNeo4j.addUser(user.getUsername(), user.getPictureMedium());
        if(result && this.userNeo4j.findUserByUsername(user.getUsername()))
            System.out.println("[+] addUser");
        else
            System.err.println("[-] addUser");

    }

    public void addUserLikesPodcastTest(){

        User user = new User();
        Podcast podcast = new Podcast();
        user.setUsername("user test");
        podcast.setId("1234567890");

        boolean result = this.userNeo4j.addUserLikesPodcast(user.getUsername(), podcast.getId());
        if(result && this.userNeo4j.checkUserLikesPodcastExists(user.getUsername(), podcast.getId()))
            System.out.println("[+] addUserLikesPodcast");
        else
            System.err.println("[-] addUserLikesPodcast");

    }

    public void addUserWatchLaterPodcastTest(){

        User user = new User();
        Podcast podcast = new Podcast();
        user.setUsername("user test");
        podcast.setId("1234567891");

        boolean result = this.userNeo4j.addUserWatchLaterPodcast(user.getUsername(), podcast.getId());
        if(result && this.userNeo4j.checkUserWatchLaterPodcastExists(user.getUsername(), podcast.getId()))
            System.out.println("[+] addUserWatchLaterPodcast");
        else
            System.err.println("[-] addUserWatchLaterPodcast");

    }

    public void addUserFollowUserTest(){

        User user1 = new User();
        User user2 = new User();
        user1.setUsername("user test");
        user1.setPictureMedium("picture");
        user2.setUsername("user test2");
        user2.setPictureMedium("picture2");

        this.userNeo4j.addUser(user2.getUsername(), user2.getPictureMedium());
        boolean result = this.userNeo4j.addUserFollowUser(user1.getUsername(), user2.getUsername());
        if(result && this.userNeo4j.findUserFollowsUser(user1.getUsername(), user2.getUsername()))
            System.out.println("[+] addUserFollowUser");
        else
            System.err.println("[-] addUserFollowUser");

    }

    public void addUserFollowAuthorTest(){

        User user = new User();
        Author author = new Author();
        user.setUsername("user test");
        author.setName("author test");
        boolean result = this.userNeo4j.addUserFollowAuthor(user.getUsername(), author.getName());
        if(result && this.userNeo4j.findUserFollowsAuthor(user.getUsername(), author.getName()))
            System.out.println("[+] addUserFollowAuthor");
        else
            System.err.println("[-] addUserFollowAuthor");

    }

    public void updateUserTest(){

        String oldUsername = "user test";
        String newUsername = "user test3";
        String newPicture  = "picture3";

        boolean result = this.userNeo4j.updateUser(oldUsername, newUsername, newPicture);
        if(result && this.userNeo4j.findUserByUsername(newUsername))
            System.out.println("[+] updateUser");
        else
            System.err.println("[-] updateUser");

    }

    public void deleteUserTest(){

        User user = new User();
        user.setUsername("user test4");
        user.setPictureMedium("picture4");
        this.userNeo4j.addUser(user.getUsername(), user.getPictureMedium());

        boolean result = this.userNeo4j.deleteUser(user.getUsername());
        if(result && !this.userNeo4j.findUserByUsername(user.getUsername()))
            System.out.println("[+] deleteUser");
        else
            System.err.println("[-] deleteUser");
    }

    public void deleteUserLikesPodcastTest(){

        User user = new User();
        Podcast podcast = new Podcast();
        user.setUsername("user test3");
        podcast.setId("1234567890");

        boolean result = this.userNeo4j.deleteUserLikesPodcast(user.getUsername(), podcast.getId());
        if(result && !this.userNeo4j.checkUserLikesPodcastExists(user.getUsername(), podcast.getId()))
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
        this.userNeo4j.addUserLikesPodcast(user.getUsername(), podcast1.getId());
        this.userNeo4j.addUserLikesPodcast(user.getUsername(), podcast2.getId());
        this.userNeo4j.addUserLikesPodcast(user.getUsername(), podcast3.getId());

        boolean result = this.userNeo4j.deleteAllUserLikesPodcast(user.getUsername());
        if(result && !this.userNeo4j.checkAllUserLikesPodcastExists(user.getUsername()))
            System.out.println("[+] deleteAllUserLikesPodcast");
        else
            System.err.println("[-] deleteAllUserLikesPodcast");

    }

    public void deleteUserWatchLaterPodcastTest(){

        User user = new User();
        Podcast podcast = new Podcast();
        user.setUsername("user test3");
        podcast.setId("1234567891");

        boolean result = this.userNeo4j.deleteUserWatchLaterPodcast(user.getUsername(), podcast.getId());
        if(result && !this.userNeo4j.checkUserWatchLaterPodcastExists(user.getUsername(), podcast.getId()))
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
        this.userNeo4j.addUserWatchLaterPodcast(user.getUsername(), podcast1.getId());
        this.userNeo4j.addUserWatchLaterPodcast(user.getUsername(), podcast2.getId());
        this.userNeo4j.addUserWatchLaterPodcast(user.getUsername(), podcast3.getId());

        boolean result = this.userNeo4j.deleteAllUserWatchLaterPodcast(user.getUsername());
        if(result && !this.userNeo4j.checkAllUserWatchLaterPodcastExists(user.getUsername()))
            System.out.println("[+] deleteAllUserWatchLaterPodcast");
        else
            System.err.println("[-] deleteAllUserWatchLaterPodcast");
    }


    private void showFollowedUserTest() {
        User user = new User();
        user.setUsername("silverelephant716273");
        List<User> users = this.userNeo4j.showFollowedUsers(user.getUsername(), 10);
        if(users != null)
            System.out.println("[+] showFollowedUserTest");
        else
            System.err.println("[-] showFollowedUserTest");

        for(User u : users)
            System.out.println(u);

        // clean all entities created for tests
        this.userNeo4j.deleteUser("user test2");
        this.userNeo4j.deleteUser("user test3");
        this.podcastNeo4j.deletePodcastByPodcastId("1234567890");
        this.podcastNeo4j.deletePodcastByPodcastId("1234567891");
        this.podcastNeo4j.deletePodcastByPodcastId("1234567892");
        this.podcastNeo4j.deletePodcastByPodcastId("1234567893");
        this.podcastNeo4j.deletePodcastByPodcastId("1234567894");
        this.authorNeo4j.deleteAuthor("author test");
        this.authorNeo4j.deleteAuthor("author test1");
        this.authorNeo4j.deleteAuthor("author test2");
        this.authorNeo4j.deleteAuthor("author test3");
    }

    public void deleteUserFollowUserTest() {
        User user = new User();
        user.setUsername("test1");
        user.setPictureMedium("picture1");
        this.userNeo4j.addUser(user.getUsername(), user.getPictureMedium());
        user.setUsername("test2");
        user.setPictureMedium("picture2");
        this.userNeo4j.addUser(user.getUsername(), user.getPictureMedium());
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
        user.setPictureMedium("picture1");
        this.userNeo4j.addUser(user.getUsername(), user.getPictureMedium());
        user.setUsername("test2");
        user.setPictureMedium("picture2");
        this.userNeo4j.addUser(user.getUsername(), user.getPictureMedium());
        user.setUsername("test3");
        user.setPictureMedium("picture3");
        this.userNeo4j.addUser(user.getUsername(), user.getPictureMedium());
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
        user.setPictureMedium("picture1");
        this.userNeo4j.addUser(user.getUsername(), user.getPictureMedium());
        Author author = new Author();
        author.setName("test2");
        this.authorNeo4j.addAuthor(author.getName());

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
        user.setPictureMedium("picture1");
        this.userNeo4j.addUser(user.getUsername(), user.getPictureMedium());
        Author author = new Author();
        author.setName("test2");
        this.authorNeo4j.addAuthor(author.getName());
        author.setName("test3");
        this.authorNeo4j.addAuthor(author.getName());

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
        List<User> users = this.userNeo4j.showSuggestedUsersByLikedPodcasts("whiterabbit394794", 25);
        boolean test = false;
        for (User user : users)
            if (user.getUsername().equals("whiteelephant974216"))
                test = true;

        if (test)
            System.out.println("[+] showSuggestedUsersByLikedPodcasts");
        else
            System.err.println("[-] showSuggestedUsersByLikedPodcasts");
    }

    public void showSuggestedUsersByFollowedAuthorsTest() {
        List<User> suggestedUsers = userNeo4j.showSuggestedUsersByFollowedAuthors("whiterabbit394794", 4);

        if (suggestedUsers != null && suggestedUsers.size() == 4) {
            System.out.println("[+] showSuggestedUsersByFollowedAuthors");
        } else
            System.err.println("[-] showSuggestedUsersByFollowedAuthors");
    }
}
