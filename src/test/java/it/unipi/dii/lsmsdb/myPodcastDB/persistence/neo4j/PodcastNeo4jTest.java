package it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;

import java.util.List;
import java.util.Map.Entry;

public class PodcastNeo4jTest {

    PodcastNeo4j podcastNeo4j;

    PodcastNeo4jTest(){ podcastNeo4j = new PodcastNeo4j();}

    public static void main(String[] args){

        Neo4jManager manager = Neo4jManager.getInstance();
        manager.openConnection();
        PodcastNeo4jTest test = new PodcastNeo4jTest();

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

}
