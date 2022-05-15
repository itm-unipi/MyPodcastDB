package it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j;

<<<<<<< HEAD
<<<<<<< HEAD
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;

=======
>>>>>>> c0d11c1 (Added Graph Queries)
=======
>>>>>>> 463092ce9e88689d910a301663b9d9a1008798e2
import java.util.List;
import java.util.Map.Entry;

public class PodcastNeo4jTest {
<<<<<<< HEAD
<<<<<<< HEAD

    PodcastNeo4j podcastNeo4j;

    PodcastNeo4jTest(){ podcastNeo4j = new PodcastNeo4j();}

    public static void main(String[] args){

=======
=======
>>>>>>> 463092ce9e88689d910a301663b9d9a1008798e2
    PodcastNeo4j podcastNeo4j;

    public PodcastNeo4jTest() {
        this.podcastNeo4j = new PodcastNeo4j();
    }

    public void showMostNumerousCategoriesTest() {
        List<Entry<String, Integer>> results = this.podcastNeo4j.showMostNumerousCategories(10);
        if (results.get(0).getKey().equals("Society & Culture") && results.get(0).getValue() == 8143)
            System.out.println("[+] showMostNumerousCategories");
        else
            System.err.println("[-] showMostNumerousCategories");
    }

    public void showSuggestedPodcastsBasedOnCategoryOfPodcastsUserLikedTest() {
        List<Entry<String, String>> results = this.podcastNeo4j.showSuggestedPodcastsBasedOnCategoryOfPodcastsUserLiked("yellowtiger876274", 10);
        if (results.get(0).getKey().equals("5505c5469d6cf22b9a12fae9") && results.get(0).getValue().equals("Recommended Movie Squad"))
            System.out.println("[+] showSuggestedPodcastsBasedOnCategoryOfPodcastsUserLiked");
        else
            System.err.println("[-] showMostNumerousCategories");
    }

    public static void main(String[] args) {
<<<<<<< HEAD
>>>>>>> c0d11c1 (Added Graph Queries)
=======
>>>>>>> 463092ce9e88689d910a301663b9d9a1008798e2
        Neo4jManager manager = Neo4jManager.getInstance();
        manager.openConnection();
        PodcastNeo4jTest test = new PodcastNeo4jTest();

<<<<<<< HEAD
<<<<<<< HEAD
        test.showPodcastsInWatchlistTest();
        test.showMostAppreciatedCategoriesTest();
        test.showSuggestedPodcastsLikedByFollowedUsersTest();
        test.showSuggestedPodcastsBasedOnAuthorsOfPodcastsInWatchlistTest();


        manager.closeConnection();
    }


    public void showPodcastsInWatchlistTest(){
        User user = new User();
        user.setUsername("whiteladybug851481");
        List<Entry<String, String>> podcasts = this.podcastNeo4j.showPodcastsInWatchlist(user, 10);

        if(podcasts == null) {
            System.err.println("showPodcastsInWatchlist");
            return;
        }
        else
            System.out.println("showPodcastsInWatchlist");

        for(Entry<String, String> podcast : podcasts)
            System.out.println(podcast);
    }

    public void showMostAppreciatedCategoriesTest(){

        List<Entry<String, Integer>> categories = this.podcastNeo4j.showMostAppreciatedCategories(10);

        if(categories == null || categories.isEmpty()) {
            System.err.println("showMostAppreciatedCategories");
            return;
        }
        else
            System.out.println("showMostAppreciatedCategories");

        for(Entry<String, Integer> category : categories)
            System.out.println(category);
    }

    public void showSuggestedPodcastsLikedByFollowedUsersTest(){
        User user = new User();
        user.setUsername("whiteladybug851481");
        List<Entry<String, String>> podcasts = this.podcastNeo4j.showSuggestedPodcastsLikedByFollowedUsers(user, 10);

        if(podcasts == null) {
            System.err.println("showSuggestedPodcastsLikedByFollowedUsers");
            return;
        }
        else
            System.out.println("showSuggestedPodcastsLikedByFollowedUsers");

        for(Entry<String, String> podcast : podcasts)
            System.out.println(podcast);
    }

    public void showSuggestedPodcastsBasedOnAuthorsOfPodcastsInWatchlistTest(){
        User user = new User();
        user.setUsername("whiteladybug851481");
        List<Entry<String, String>> podcasts = this.podcastNeo4j.showSuggestedPodcastsBasedOnAuthorsOfPodcastsInWatchlist(user, 10);

        if(podcasts == null) {
            System.err.println("showSuggestedPodcastsBasedOnAuthorsOfPodcastsInWatchlist");
            return;
        }
        else
            System.out.println("showSuggestedPodcastsBasedOnAuthorsOfPodcastsInWatchlist");

        for(Entry<String, String> podcast : podcasts)
            System.out.println(podcast);
    }

=======
=======
>>>>>>> 463092ce9e88689d910a301663b9d9a1008798e2
        test.showMostNumerousCategoriesTest();
        test.showSuggestedPodcastsBasedOnCategoryOfPodcastsUserLikedTest();

        manager.closeConnection();
    }
<<<<<<< HEAD
>>>>>>> c0d11c1 (Added Graph Queries)
=======
>>>>>>> 463092ce9e88689d910a301663b9d9a1008798e2
}
