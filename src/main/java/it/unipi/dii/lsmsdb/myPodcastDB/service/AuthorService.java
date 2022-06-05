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

    public int getAuthorLogin(Author author){
        int res = -1;
        MongoManager.getInstance().openConnection();
        Author newAuthor = authorMongoManager.findAuthorByName(author.getName());
        if(newAuthor == null)
            res = 1;
        else if(!author.getPassword().equals(newAuthor.getPassword()))
            res = 2;
        else {
            author.copy(newAuthor);
            res = 0;
        }

        MongoManager.getInstance().closeConnection();
        return res;
    }

    public int addAuthorSignUp(Author author){

        int res = -1;
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        //check if author with the same name already exists in mongo
        if(authorMongoManager.findAuthorByName(author.getName()) != null)
            res = 1;
        //check if author with the same name already exists in neo4j
        else if(authorNeo4jManager.findAuthorByName(author.getName()))
            res = 2;
        else {
            //check failure mongo operation
            if(!authorMongoManager.addAuthor(author))
                res = 3;
            //check failure neo4j operation
            else if(!authorNeo4jManager.addAuthor(author.getName(), author.getPicturePath())){
                authorMongoManager.deleteAuthorByName(author.getName());
                res = 4;
            }
            else
                res = 0;
        }

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
        return res;
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
