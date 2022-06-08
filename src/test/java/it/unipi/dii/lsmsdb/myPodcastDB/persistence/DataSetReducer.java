package it.unipi.dii.lsmsdb.myPodcastDB.persistence;

import com.mongodb.client.MongoCursor;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.MongoManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.ReviewMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.UserMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.Neo4jManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.UserNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.service.UserPageService;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ConfigManager;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class DataSetReducer {

    public DataSetReducer(){

    }

    public List<String> findUsers(int limit) {
        MongoManager manager = MongoManager.getInstance();

        try (MongoCursor<Document> cursor = manager.getCollection("user").find().limit(limit).iterator()) {
            List<String> users = new ArrayList<>();

            while (cursor.hasNext()) {
                Document user = cursor.next();
                String username = user.getString("username");
                users.add(username);
            }

            return users;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int deleteUsers(UserMongo userMongoManager, UserNeo4j userNeo4jManager, ReviewMongo reviewMongoManager,  String username){
        int res;

        //check if user exists in mongo
        if(userMongoManager.findUserByUsername(username) == null)
            res = 1;
            //check if user exists in neo4j
        else if(!userNeo4jManager.findUserByUsername(username))
            res = 2;
            //check delete operation failure
        else if(!userMongoManager.deleteUserByUsername(username))
            res = 3;
        else if(!userNeo4jManager.deleteUser(username)){
            res =  4;
        }
        else if(reviewMongoManager.deleteReviewsByAuthorUsername(username) == 0){
            res = 0;
        }
        else
            res = 0;

        return res;
    }


    public static void main(String[] args) {


        Logger.initialize();
        ConfigManager.importConfig("config.xml", "src/main/java/it/unipi/dii/lsmsdb/myPodcastDB/utility/schema.xsd");

        DataSetReducer dr = new DataSetReducer();

        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        List<String> users = dr.findUsers(750000);

        UserMongo userMongoManager = new UserMongo();
        UserNeo4j userNeo4jManager = new UserNeo4j();
        ReviewMongo reviewMongoManager = new ReviewMongo();
        int i = 0;
        for(String username : users){
            dr.deleteUsers(userMongoManager, userNeo4jManager, reviewMongoManager, username);
            i++;
            if(i % 100 == 0)
               System.out.println("Removed : " + i);

        }

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();


    }
}
