package it.unipi.dii.lsmsdb.myPodcastDB.service;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.AuthorMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.MongoManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.AuthorNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.Neo4jManager;

import java.util.List;

public class AuthorService {

    //---------------- GIANLUCA ---------------------
    private AuthorMongo authorMongoManager;
    private AuthorNeo4j authorNeo4jManager;

    public AuthorService() {
        this.authorMongoManager = new AuthorMongo();
        this.authorNeo4jManager = new AuthorNeo4j();
    }

    public boolean getAuthorLogin(Author author){
        boolean res;
        MongoManager.getInstance().openConnection();
        Author newAuthor = authorMongoManager.findAuthorByName(author.getName());
        if(newAuthor == null || !author.getPassword().equals(newAuthor.getPassword()))
            res = false;
        else {
            author.copy(newAuthor);
            res = true;
        }

        MongoManager.getInstance().closeConnection();
        return res;
    }

    public boolean addAuthorSignUp(Author author){

        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        Author newAuthor = authorMongoManager.findAuthorByName(author.getName());
        if(newAuthor != null){
            MongoManager.getInstance().closeConnection();
            Neo4jManager.getInstance().closeConnection();
            return false;
        }
        else {
            if(!authorMongoManager.addAuthor(author)){
                MongoManager.getInstance().closeConnection();
                Neo4jManager.getInstance().closeConnection();
                return false;
            }
            else if(!authorNeo4jManager.addAuthor(author.getName(), author.getPicturePath())){
                authorMongoManager.deleteAuthorByName(author.getName());
                MongoManager.getInstance().closeConnection();
                Neo4jManager.getInstance().closeConnection();
                return false;
            }

            MongoManager.getInstance().closeConnection();
            Neo4jManager.getInstance().closeConnection();
            return true;
        }
    }

     //-----------------------------------------------

    //----------------- BIAGIO ----------------------

    public void loadAuthorOwnProfile(Author author, List<Author> followed, int limit) {
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        Author foundAuthor = authorMongoManager.findAuthorByName(author.getName());
        author.copy(foundAuthor);

        List<Author> followedAuthor = authorNeo4jManager.showFollowedAuthorsByAuthor(author.getName(), limit);
        if (followedAuthor != null && !followedAuthor.isEmpty())
            followed.addAll(followedAuthor);

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
    }


    //-----------------------------------------------

    //----------------- MATTEO ----------------------
    //-----------------------------------------------
}
