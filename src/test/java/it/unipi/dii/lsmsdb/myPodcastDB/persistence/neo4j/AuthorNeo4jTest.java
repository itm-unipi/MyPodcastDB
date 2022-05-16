package it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ConfigManager;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;

import java.util.List;
import java.util.Map;

public class AuthorNeo4jTest {
    AuthorNeo4j authorNeo4j;

    public AuthorNeo4jTest() {
        this.authorNeo4j = new AuthorNeo4j();
    }

    public void addAuthorTest() {
        Author author = new Author();
        author.setName("test");
        this.authorNeo4j.addAuthor(author.getName());
        boolean test = this.authorNeo4j.findAuthorByName("test");

        if (test)
            System.out.println("[+] addAuthor");
        else
            System.err.println("[-] addAuthor");

        this.authorNeo4j.deleteAuthor("test");
    }

    public void addAuthorFollowsAuthorTest() {
        Author author = new Author();
        author.setName("test1");
        this.authorNeo4j.addAuthor(author.getName());
        author.setName("test2");
        this.authorNeo4j.addAuthor(author.getName());
        this.authorNeo4j.addAuthorFollowsAuthor("test1", "test2");

        boolean test = this.authorNeo4j.findAuthorFollowsAuthor("test1", "test2");
        if (test)
            System.out.println("[+] addAuthorFollowsAuthor");
        else
            System.err.println("[-] addAuthorFollowsAuthor");

        this.authorNeo4j.deleteAllAuthorFollowsAuthor("test1");
        this.authorNeo4j.deleteAuthor("test1");
        this.authorNeo4j.deleteAuthor("test2");
    }

    public void updateAuthorTest() {
        Author author = new Author();
        author.setName("test");
        this.authorNeo4j.addAuthor(author.getName());
        this.authorNeo4j.updateAuthor("test", "newTest");

        boolean test1 = this.authorNeo4j.findAuthorByName("test");
        boolean test2 = this.authorNeo4j.findAuthorByName("newTest");

        if (!test1 && test2)
            System.out.println("[+] updateAuthor");
        else
            System.err.println("[-] updateAuthor");

        if (test1)
            this.authorNeo4j.deleteAuthor("test");
        else if (test2)
            this.authorNeo4j.deleteAuthor("newTest");
    }

    public void deleteAuthorTest() {
        Author author = new Author();
        author.setName("test");
        this.authorNeo4j.addAuthor(author.getName());
        this.authorNeo4j.deleteAuthor("test");
        boolean test = this.authorNeo4j.findAuthorByName("test");

        if (!test)
            System.out.println("[+] deleteAuthor");
        else
            System.err.println("[-] deleteAuthor");
    }

    public void deleteAuthorFollowsAuthorTest() {
        Author author = new Author();
        author.setName("test1");
        this.authorNeo4j.addAuthor(author.getName());
        author.setName("test2");
        this.authorNeo4j.addAuthor(author.getName());
        this.authorNeo4j.addAuthorFollowsAuthor("test1", "test2");
        this.authorNeo4j.deleteAuthorFollowsAuthor("test1", "test2");

        boolean test = this.authorNeo4j.findAuthorFollowsAuthor("test1", "test2");
        if (!test)
            System.out.println("[+] deleteAuthorFollowsAuthor");
        else
            System.err.println("[-] deleteAuthorFollowsAuthor");

        this.authorNeo4j.deleteAuthor("test1");
        this.authorNeo4j.deleteAuthor("test2");
    }

    public void deleteAllAuthorFollowsAuthorTest() {
        Author author = new Author();
        author.setName("test1");
        this.authorNeo4j.addAuthor(author.getName());
        author.setName("test2");
        this.authorNeo4j.addAuthor(author.getName());
        author.setName("test3");
        this.authorNeo4j.addAuthor(author.getName());
        this.authorNeo4j.addAuthorFollowsAuthor("test1", "test2");
        this.authorNeo4j.addAuthorFollowsAuthor("test1", "test3");
        this.authorNeo4j.deleteAllAuthorFollowsAuthor("test1");

        boolean test1 = this.authorNeo4j.findAuthorFollowsAuthor("test1", "test2");
        boolean test2 = this.authorNeo4j.findAuthorFollowsAuthor("test1", "test3");
        if (!test1 && !test2)
            System.out.println("[+] deleteAllAuthorFollowsAuthor");
        else
            System.err.println("[-] deleteAllAuthorFollowsAuthor");

        this.authorNeo4j.deleteAuthor("test1");
        this.authorNeo4j.deleteAuthor("test2");
        this.authorNeo4j.deleteAuthor("test3");
    }

    public void showMostFollowedAuthorTest() {
        List<Map.Entry<String, Integer>> results = this.authorNeo4j.showMostFollowedAuthor(10);
        if (results.get(0).getKey().equals("Sharon Hoffman") && results.get(0).getValue() == 480)
            System.out.println("[+] showMostFollowedAuthor");
        else
            System.err.println("[-] showMostFollowedAuthor");
    }

    public void showSuggestedAuthorsFollowedByFollowedUserTest() {
        List<String> authors = authorNeo4j.showSuggestedAuthorsFollowedByFollowedUser("organicmouse599943", 10);

        if (authors != null && authors.size() == 10)
            System.out.println("[+] showSuggestedAuthorsFollowedByFollowedUser");
        else
            System.err.println("[-] showSuggestedAuthorsFollowedByFollowedUser");
    }

    public static void main(String[] args) {
        Logger.initialize();
        ConfigManager.importConfig("config.xml", "src/main/java/it/unipi/dii/lsmsdb/myPodcastDB/utility/schema.xsd");
        Neo4jManager manager = Neo4jManager.getInstance();
        manager.openConnection();
        AuthorNeo4jTest test = new AuthorNeo4jTest();

        test.addAuthorTest();
        test.addAuthorFollowsAuthorTest();
        test.updateAuthorTest();
        test.deleteAuthorTest();
        test.deleteAuthorFollowsAuthorTest();
        test.deleteAllAuthorFollowsAuthorTest();
        test.showMostFollowedAuthorTest();
        test.showSuggestedAuthorsFollowedByFollowedUserTest();

        manager.closeConnection();
    }
}
