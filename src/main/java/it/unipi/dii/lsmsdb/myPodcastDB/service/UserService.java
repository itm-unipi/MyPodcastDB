package it.unipi.dii.lsmsdb.myPodcastDB.service;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.*;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.AuthorNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.Neo4jManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.PodcastNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.UserNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.List;

public class UserService {

    //---------------- GIANLUCA ---------------------
    //-----------------------------------------------

    //----------------- BIAGIO ----------------------
    private AuthorMongo authorMongoManager;
    private AuthorNeo4j authorNeo4jManager;
    private UserNeo4j userNeo4jManager;
    private UserMongo userMongoManager;
    private PodcastMongo podcastMongoManager;
    private PodcastNeo4j podcastNeo4jManager;

    public UserService() {
        this.authorMongoManager = new AuthorMongo();
        this.authorNeo4jManager = new AuthorNeo4j();
        this.userNeo4jManager = new UserNeo4j();
        this.userMongoManager = new UserMongo();
        this.podcastMongoManager = new PodcastMongo();
        this.podcastNeo4jManager = new PodcastNeo4j();
    }

    /***** Loading author profile **********/
    public boolean loadAuthorProfileRegistered(Author author, List<Pair<Author, Boolean>> followed, int limit) {
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        // Getting the author object from Mongo
        Author foundAuthor = authorMongoManager.findAuthorByName(author.getName());
        author.copy(foundAuthor);

        // Checking if the user follows the visited author
        boolean followingAuthor = userNeo4jManager.findUserFollowsAuthor(((User)MyPodcastDB.getInstance().getSessionActor()).getUsername(), author.getName());

        // Getting the authors followed by the author visited
        List<Author> followedAuthor = authorNeo4jManager.showFollowedAuthorsByAuthor(author.getName(), limit);
        for (Author a: followedAuthor) {
            boolean following = userNeo4jManager.findUserFollowsAuthor(((User)MyPodcastDB.getInstance().getSessionActor()).getUsername(), a.getName());
            followed.add(new Pair<>(a, following));
        }

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();

        return followingAuthor;
    }

    public void loadAuthorProfileUnregistered(Author author, List<Pair<Author, Boolean>> followed, int limit) {
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        // Getting the author object from Mongo
        Author foundAuthor = authorMongoManager.findAuthorByName(author.getName());
        author.copy(foundAuthor);

        // Getting the authors followed by the author visited
        List<Author> followedAuthor = authorNeo4jManager.showFollowedAuthorsByAuthor(author.getName(), limit);
        for (Author a: followedAuthor)
            followed.add(new Pair<>(a, false));

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
    }

    /******+ Follow Author ********/
    public void followAuthor(String authorName) {
        Neo4jManager.getInstance().openConnection();

        if (userNeo4jManager.addUserFollowAuthor(((User)MyPodcastDB.getInstance().getSessionActor()).getUsername(), authorName))
            Logger.success("(User) You started following " + authorName);
        else
            Logger.error("(User) Error during the following operation");

        Neo4jManager.getInstance().closeConnection();
    }

    public void unfollowAuthor(String authorName) {
        Neo4jManager.getInstance().openConnection();

        if (userNeo4jManager.deleteUserFollowAuthor(((User)MyPodcastDB.getInstance().getSessionActor()).getUsername(), authorName))
            Logger.success("(User) You unfollowed " + authorName);
        else
            Logger.error("(User) Error during the unfollowing operation");

        Neo4jManager.getInstance().closeConnection();
    }

    public void searchRegistered(String searchText, List<Podcast> podcastsMatch, List<Pair<Author, Boolean>> authorsMatch, List<Pair<User, Boolean>> usersMatch, int limit, Triplet<Boolean, Boolean, Boolean> filters) {
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        // Searching for podcasts
        if (filters.getValue0()) {
            List<Podcast> podcasts = podcastMongoManager.searchPodcast(searchText, limit);
            if (podcasts != null)
                podcastsMatch.addAll(podcasts);
        }

        // Searching for authors
        if (filters.getValue1()) {
            List<Author> authors = authorMongoManager.searchAuthor(searchText, limit);
            for (Author authorFound : authors) {
                boolean followingAuthor = userNeo4jManager.findUserFollowsAuthor(((User) MyPodcastDB.getInstance().getSessionActor()).getUsername(), authorFound.getName());
                authorsMatch.add(new Pair<>(authorFound, followingAuthor));
            }
        }

        Logger.info("Authors found: " + authorsMatch);

        // Searching for users
        if (filters.getValue2()) {
            List<User> users = userMongoManager.searchUser(searchText, limit);
            for (User userFound : users) {
                boolean followingUser = userNeo4jManager.findUserFollowsUser(((User) MyPodcastDB.getInstance().getSessionActor()).getUsername(), userFound.getUsername());
                usersMatch.add(new Pair<>(userFound, followingUser));
            }
        }

        Logger.info("Users found: " + usersMatch);

        Neo4jManager.getInstance().closeConnection();
        MongoManager.getInstance().closeConnection();
    }

    public void searchUnregistered(String searchText, List<Podcast> podcastsMatch, List<Pair<Author, Boolean>> authorsMatch, List<Pair<User, Boolean>> usersMatch, int limit, Triplet<Boolean, Boolean, Boolean> filters) {
        MongoManager.getInstance().openConnection();

        // Searching for podcasts
        if (filters.getValue0()) {
            List<Podcast> podcasts = podcastMongoManager.searchPodcast(searchText, limit);
            if (podcasts != null)
                podcastsMatch.addAll(podcasts);
        }

        // Searching for authors
        if (filters.getValue1()) {
            List<Author> authors = authorMongoManager.searchAuthor(searchText, limit);
            for (Author authorFound : authors)
                authorsMatch.add(new Pair<>(authorFound, false));
        }

        MongoManager.getInstance().closeConnection();
    }

    public void followUser(String username) {
        Neo4jManager.getInstance().openConnection();

        if (userNeo4jManager.addUserFollowUser(((User) (MyPodcastDB.getInstance().getSessionActor())).getUsername(), username))
            Logger.success("(User) You started following " + username);
        else
            Logger.error("(User) Error during the following operation");

        Neo4jManager.getInstance().closeConnection();
    }

    public void unfollowUser(String username) {
        Neo4jManager.getInstance().openConnection();

        if (userNeo4jManager.deleteUserFollowUser(((User) (MyPodcastDB.getInstance().getSessionActor())).getUsername(), username))
            Logger.success("(User) You started following " + username);
        else
            Logger.error("(User) Error during the following operation");

        Neo4jManager.getInstance().closeConnection();
    }

    public void loadHomepageRegistered(List<Triplet<Podcast, Float, Boolean>> topRated, List<Pair<Podcast, Integer>> mostLikedPodcasts, List<Triplet<Author, Integer, Boolean>> mostFollowedAuthors, List<Podcast> watchlist, List<Podcast> topGenres, List<Podcast> basedOnFriends, List<Podcast> basedOnWatchlist, List<Pair<Author, Boolean>> suggestedAuthors, int limit) {
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        // Loading Top Rated Podcasts + Top podcast in user country
        List<Triplet<Podcast, Float, Boolean>> resultsTopRated = new ArrayList<>();
        Triplet<Podcast, Float, Boolean> topCountry = new Triplet<>(new Podcast("6b70d0098bb6c382ff41a274", "Hurtscast", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts6/v4/e3/83/18/e38318f0-98c1-db5f-de31-a2d937401bbe/mza_2491539306600660338.jpg/600x600bb.jpg"), 4.3f, true);
        resultsTopRated.add(topCountry);

        resultsTopRated.add(new Triplet<>(new Podcast("9e3816413fb3430e714c91fc", "Run Amok with Rex and Chuck", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts115/v4/a6/e0/06/a6e006e3-869d-8d05-bdfd-f3bc7282b5f0/mza_5306896777310247561.jpg/600x600bb.jpg"), 4.0f, false));
        resultsTopRated.add(new Triplet<>(new Podcast("fce2e20fe832f5f5e6148987", "Ingenious Basterds", "https://is3-ssl.mzstatic.com/image/thumb/Podcasts3/v4/3d/16/af/3d16af60-2e1d-69bd-c3d3-b83bebdc0bd3/mza_5286960706166095957.jpg/600x600bb.jpg"), 5.0f, false));
        resultsTopRated.add(new Triplet<>(new Podcast("ed70b4056b9415b7b43b4a52", "Strangers in Space", "https://is1-ssl.mzstatic.com/image/thumb/Podcasts115/v4/5f/9d/d4/5f9dd483-a0fc-e81c-ce59-aeaea09be6c3/mza_8658599441396400725.jpg/600x600bb.jpg"), 4.7f, false));
        resultsTopRated.add(new Triplet<>(new Podcast("35628503b2788fdcb7a1e145", "Orchard Recording Studio Podcast", "https://is1-ssl.mzstatic.com/image/thumb/Podcasts113/v4/b9/3c/48/b93c4823-474e-edf5-c9ad-73025a01176e/mza_2537085863686121083.jpg/600x600bb.jpg"), 4.5f, false));
        resultsTopRated.add(new Triplet<>(new Podcast("b8f759d95d3ed9d1e12f9430", "To The Batpoles! Batman 1966", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts125/v4/f8/f8/a1/f8f8a158-24b8-091a-fc43-213739b4bbb1/mza_16028619290499021301.jpg/600x600bb.jpg"), 4.65f, false));
        resultsTopRated.add(new Triplet<>(new Podcast("6b70d0098bb6c382ff41a274", "Hurtscast", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts6/v4/e3/83/18/e38318f0-98c1-db5f-de31-a2d937401bbe/mza_2491539306600660338.jpg/600x600bb.jpg"), 4.87f, false));

        if (resultsTopRated != null)
            topRated.addAll(resultsTopRated);

        // Loading Most Liked Podcasts
        List<Pair<Podcast, Integer>> resultsMostLikedPodcasts = new ArrayList<>();
        resultsMostLikedPodcasts.add(new Pair<>(new Podcast("956e4ed48c7856b7f57f71ad", "Into the Black", "https://is4-ssl.mzstatic.com/image/thumb/Podcasts115/v4/82/e2/c1/82e2c132-2275-25e7-d0af-30b34110c892/mza_12455054422451763750.jpg/600x600bb.jpg"), 567));
        resultsMostLikedPodcasts.add(new Pair<>(new Podcast("76f2901e32ba8672e3ee121b", "Bearded Ruckus", "https://is1-ssl.mzstatic.com/image/thumb/Podcasts123/v4/c1/0d/ee/c10dee1f-8d15-47cb-a9bf-28adcbd22ea7/mza_6387225195375893490.jpg/600x600bb.jpg"), 1767));
        resultsMostLikedPodcasts.add(new Pair<>(new Podcast("416618a9d2bb7c74c7a5b779", "Podcast But Outside", "https://is4-ssl.mzstatic.com/image/thumb/Podcasts116/v4/43/2e/4f/432e4f2a-3363-74fe-88c7-8a4e5866e419/mza_14697479480010952634.jpg/600x600bb.jpg"), 667));
        resultsMostLikedPodcasts.add(new Pair<>(new Podcast("5bc82a037b1cd1e4c21ad4e6", "Sermons at St. Nicholas", "https://is3-ssl.mzstatic.com/image/thumb/Podcasts123/v4/bc/26/61/bc2661c8-8cdb-c1e5-61cf-4929b6ba43a7/mza_7303017220217427746.jpg/600x600bb.jpg"), 555));
        resultsMostLikedPodcasts.add(new Pair<>(new Podcast("f49f9d110c037f1f39a668fc", "HOLD UP! We need to talk", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts118/v4/16/71/de/1671de46-df8b-9430-5b79-156954a147fe/mza_979148902187170876.jpg/600x600bb.jpg"), 853));
        resultsMostLikedPodcasts.add(new Pair<>(new Podcast("7f7f64a07837778030d8afc6", "Breaking Average", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts125/v4/b5/8a/49/b58a49be-2621-a98b-f94a-9c4d62fc3a3c/mza_4437068013901004299.jpg/600x600bb.jpg"), 745));
        resultsMostLikedPodcasts.add(new Pair<>(new Podcast("46f7ac981cdefd61b4f8683d", "Angry Dad Podcast", "https://is4-ssl.mzstatic.com/image/thumb/Podcasts125/v4/df/9e/8f/df9e8f0b-a576-0897-1715-c7870a33f12d/mza_14276480442415215195.jpg/600x600bb.jpg"), 30));

        if (resultsMostLikedPodcasts != null)
            mostLikedPodcasts.addAll(resultsMostLikedPodcasts);

        // Load Most Followed Author
        List<Pair<Author, Integer>> resultsMostFollowedAuthors = new ArrayList<>();
        resultsMostFollowedAuthors.add(new Pair<>(new Author("", "Michael Colosi", "/img/authors/author17.png"), 1283));
        resultsMostFollowedAuthors.add(new Pair<>(new Author("", "Rick Girard", "/img/authors/author15.png"), 1154));
        resultsMostFollowedAuthors.add(new Pair<>(new Author("", "DJ RAUL", "/img/authors/author2.png"), 1864));
        resultsMostFollowedAuthors.add(new Pair<>(new Author("", "Stoneface and Terminal", "/img/authors/author16.png"), 1246));
        resultsMostFollowedAuthors.add(new Pair<>(new Author("", "BSGE After Hours", "/img/authors/author2.png"), 1092));
        resultsMostFollowedAuthors.add(new Pair<>(new Author("", "Uche", "/img/authors/author9.png"), 2021));
        resultsMostFollowedAuthors.add(new Pair<>(new Author("", "Andy Southern", "/img/authors/author7.png"), 1353));

        for (Pair<Author, Integer> author: resultsMostFollowedAuthors) {
            boolean following = userNeo4jManager.findUserFollowsAuthor(((User) MyPodcastDB.getInstance().getSessionActor()).getUsername(), author.getValue0().getName());
            mostFollowedAuthors.add(new Triplet<>(author.getValue0(), author.getValue1(), following));
        }

        List<Podcast> podcasts = new ArrayList<>();
        List<Author> authors = new ArrayList<>();

        // Load podcasts in watchlist
        podcasts = podcastNeo4jManager.showPodcastsInWatchlist(((User)MyPodcastDB.getInstance().getSessionActor()), limit);
        if(podcasts != null)
            watchlist.addAll(podcasts);

        // Load your top genres
        podcasts = podcastNeo4jManager.showSuggestedPodcastsBasedOnCategoryOfPodcastsUserLiked(((User)MyPodcastDB.getInstance().getSessionActor()).getUsername(), limit);
        if(podcasts != null)
            topGenres.addAll(podcasts);

        // Load podcasts liked by followed users
        //podcasts = podcastNeo4jManager.showSuggestedPodcastsLikedByFollowedUsers(((User)MyPodcastDB.getInstance().getSessionActor()), limit);
        podcasts = null;
        if(podcasts != null)
            basedOnFriends.addAll(podcasts);

        // Load podcasts based on author in user's watchlist
        //podcasts = podcastNeo4jManager.showSuggestedPodcastsBasedOnAuthorsOfPodcastsInWatchlist(((User)MyPodcastDB.getInstance().getSessionActor()), limit);
        podcasts = null;
        if(podcasts != null)
            basedOnWatchlist.addAll(podcasts);

        // Load suggested authors
        authors = authorNeo4jManager.showSuggestedAuthorsFollowedByFollowedUser(((User)MyPodcastDB.getInstance().getSessionActor()).getUsername(), 6);

        if (authors != null) {
            for (Author author : authors) {
                boolean following = userNeo4jManager.findUserFollowsAuthor(((User) MyPodcastDB.getInstance().getSessionActor()).getUsername(), author.getName());
                suggestedAuthors.add(new Pair<Author, Boolean>(author, following));
            }
        }

        Neo4jManager.getInstance().closeConnection();
        MongoManager.getInstance().closeConnection();
    }

    public void loadHomepageUnregistered(List<Triplet<Podcast, Float, Boolean>> topRated, List<Pair<Podcast, Integer>> mostLikedPodcasts, List<Triplet<Author, Integer, Boolean>> mostFollowedAuthors, int limit) {
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        // Loading Top Rated Podcasts
        List<Triplet<Podcast, Float, Boolean>> resultsTopRated = new ArrayList<>();
        resultsTopRated.add(new Triplet<>(new Podcast("9e3816413fb3430e714c91fc", "Run Amok with Rex and Chuck", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts115/v4/a6/e0/06/a6e006e3-869d-8d05-bdfd-f3bc7282b5f0/mza_5306896777310247561.jpg/600x600bb.jpg"), 4.0f, false));
        resultsTopRated.add(new Triplet<>(new Podcast("fce2e20fe832f5f5e6148987", "Ingenious Basterds", "https://is3-ssl.mzstatic.com/image/thumb/Podcasts3/v4/3d/16/af/3d16af60-2e1d-69bd-c3d3-b83bebdc0bd3/mza_5286960706166095957.jpg/600x600bb.jpg"), 5.0f, false));
        resultsTopRated.add(new Triplet<>(new Podcast("ed70b4056b9415b7b43b4a52", "Strangers in Space", "https://is1-ssl.mzstatic.com/image/thumb/Podcasts115/v4/5f/9d/d4/5f9dd483-a0fc-e81c-ce59-aeaea09be6c3/mza_8658599441396400725.jpg/600x600bb.jpg"), 4.7f, false));
        resultsTopRated.add(new Triplet<>(new Podcast("35628503b2788fdcb7a1e145", "Orchard Recording Studio Podcast", "https://is1-ssl.mzstatic.com/image/thumb/Podcasts113/v4/b9/3c/48/b93c4823-474e-edf5-c9ad-73025a01176e/mza_2537085863686121083.jpg/600x600bb.jpg"), 4.5f, false));
        resultsTopRated.add(new Triplet<>(new Podcast("b8f759d95d3ed9d1e12f9430", "To The Batpoles! Batman 1966", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts125/v4/f8/f8/a1/f8f8a158-24b8-091a-fc43-213739b4bbb1/mza_16028619290499021301.jpg/600x600bb.jpg"), 4.65f, false));
        resultsTopRated.add(new Triplet<>(new Podcast("6b70d0098bb6c382ff41a274", "Hurtscast", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts6/v4/e3/83/18/e38318f0-98c1-db5f-de31-a2d937401bbe/mza_2491539306600660338.jpg/600x600bb.jpg"), 4.87f, false));

        if (resultsTopRated != null)
            topRated.addAll(resultsTopRated);

        // Loading Most Liked Podcasts
        List<Pair<Podcast, Integer>> resultsMostLikedPodcasts = new ArrayList<>();
        resultsMostLikedPodcasts.add(new Pair<>(new Podcast("956e4ed48c7856b7f57f71ad", "Into the Black", "https://is4-ssl.mzstatic.com/image/thumb/Podcasts115/v4/82/e2/c1/82e2c132-2275-25e7-d0af-30b34110c892/mza_12455054422451763750.jpg/600x600bb.jpg"), 567));
        resultsMostLikedPodcasts.add(new Pair<>(new Podcast("76f2901e32ba8672e3ee121b", "Bearded Ruckus", "https://is1-ssl.mzstatic.com/image/thumb/Podcasts123/v4/c1/0d/ee/c10dee1f-8d15-47cb-a9bf-28adcbd22ea7/mza_6387225195375893490.jpg/600x600bb.jpg"), 767));
        resultsMostLikedPodcasts.add(new Pair<>(new Podcast("416618a9d2bb7c74c7a5b779", "Podcast But Outside", "https://is4-ssl.mzstatic.com/image/thumb/Podcasts116/v4/43/2e/4f/432e4f2a-3363-74fe-88c7-8a4e5866e419/mza_14697479480010952634.jpg/600x600bb.jpg"), 667));
        resultsMostLikedPodcasts.add(new Pair<>(new Podcast("5bc82a037b1cd1e4c21ad4e6", "Sermons at St. Nicholas", "https://is3-ssl.mzstatic.com/image/thumb/Podcasts123/v4/bc/26/61/bc2661c8-8cdb-c1e5-61cf-4929b6ba43a7/mza_7303017220217427746.jpg/600x600bb.jpg"), 555));
        resultsMostLikedPodcasts.add(new Pair<>(new Podcast("f49f9d110c037f1f39a668fc", "HOLD UP! We need to talk", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts118/v4/16/71/de/1671de46-df8b-9430-5b79-156954a147fe/mza_979148902187170876.jpg/600x600bb.jpg"), 853));
        resultsMostLikedPodcasts.add(new Pair<>(new Podcast("7f7f64a07837778030d8afc6", "Breaking Average", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts125/v4/b5/8a/49/b58a49be-2621-a98b-f94a-9c4d62fc3a3c/mza_4437068013901004299.jpg/600x600bb.jpg"), 745));
        resultsMostLikedPodcasts.add(new Pair<>(new Podcast("46f7ac981cdefd61b4f8683d", "Angry Dad Podcast", "https://is4-ssl.mzstatic.com/image/thumb/Podcasts125/v4/df/9e/8f/df9e8f0b-a576-0897-1715-c7870a33f12d/mza_14276480442415215195.jpg/600x600bb.jpg"), 513));

        if (resultsMostLikedPodcasts != null)
            mostLikedPodcasts.addAll(resultsMostLikedPodcasts);

        // Load Most Followed Author
        List<Pair<Author, Integer>> resultsMostFollowedAuthors = new ArrayList<>();
        resultsMostFollowedAuthors.add(new Pair<>(new Author("", "Michael Colosi", "/img/authors/author17.png"), 1283));
        resultsMostFollowedAuthors.add(new Pair<>(new Author("", "Rick Girard", "/img/authors/author15.png"), 1154));
        resultsMostFollowedAuthors.add(new Pair<>(new Author("", "DJ RAUL", "/img/authors/author2.png"), 1864));
        resultsMostFollowedAuthors.add(new Pair<>(new Author("", "Stoneface and Terminal", "/img/authors/author16.png"), 1246));
        resultsMostFollowedAuthors.add(new Pair<>(new Author("", "BSGE After Hours", "/img/authors/author2.png"), 1092));
        resultsMostFollowedAuthors.add(new Pair<>(new Author("", "Uche", "/img/authors/author9.png"), 2021));
        resultsMostFollowedAuthors.add(new Pair<>(new Author("", "Andy Southern", "/img/authors/author7.png"), 1353));

        for (Pair<Author, Integer> author: resultsMostFollowedAuthors)
            mostFollowedAuthors.add(new Triplet<>(author.getValue0(), author.getValue1(), false));

        Neo4jManager.getInstance().closeConnection();
        MongoManager.getInstance().closeConnection();
    }

    //-----------------------------------------------

    //----------------- MATTEO ----------------------
    //-----------------------------------------------
}
