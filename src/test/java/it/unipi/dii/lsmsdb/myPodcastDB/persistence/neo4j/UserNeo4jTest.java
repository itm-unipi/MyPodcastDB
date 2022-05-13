package it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j;

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

        manager.closeConnection();

    }
    public UserNeo4jTest(){ this.userNeo4j = new UserNeo4j();}

    public void addUserTest(){

        boolean result = this.userNeo4j.addUser("user test");
        if(result)
            System.out.println("[+] addUser");
        else
            System.err.println("[-] addUser");
    }

    public void addUserLikesPodcastTest(){

        boolean result = this.userNeo4j.addUserLikesPodcast("user test", "1234567890");
        if(result)
            System.out.println("[+] addUserLikesPodcast");
        else
            System.err.println("[-] addUserLikesPodcast");
    }

    public void addUserWatchLaterPodcastTest(){

        boolean result = this.userNeo4j.addUserWatchLaterPodcast("user test", "podcast test");
        if(result)
            System.out.println("[+] addUserWatchLaterPodcast");
        else
            System.err.println("[-] addUserWatchLaterPodcast");
    }

    public void addUserFollowUserTest(){


        this.userNeo4j.addUser("user test2");
        boolean result = this.userNeo4j.addUserFollowUser("user test", "user test2");
        if(result)
            System.out.println("[+] addUserFollowUser");
        else
            System.err.println("[-] addUserFollowUser");
    }

    public void addUserFollowAuthorTest(){

        boolean result = this.userNeo4j.addUserFollowAuthor("user test", "author test");
        if(result)
            System.out.println("[+] addUserFollowAuthor");
        else
            System.err.println("[-] addUserFollowAuthor");
    }


}
