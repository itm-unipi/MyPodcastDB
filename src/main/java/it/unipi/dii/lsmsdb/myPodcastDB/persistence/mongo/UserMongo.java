package it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Accumulators.*;
import static com.mongodb.client.model.Sorts.*;
import static com.mongodb.client.model.Accumulators.avg;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public class UserMongo {

    // ------------------------------- CRUD OPERATION ----------------------------------- //

    // --------- CREATE --------- //

    public boolean addUser(User user) {
        MongoManager manager = MongoManager.getInstance();

        Document newUser = new Document()
                .append("username", user.getUsername())
                .append("password", user.getPassword())
                .append("name", user.getName())
                .append("surname", user.getSurname())
                .append("email", user.getEmail())
                .append("country", user.getCountry())
                .append("picturePath", user.getPicturePath())
                .append("favouriteGenre", user.getFavouriteGenre())
                .append("age", user.getAge())
                .append("gender", user.getGender());

        try {
            manager.getCollection("user").insertOne(newUser);
            user.setId(newUser.getObjectId("_id").toString());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // ---------- READ ---------- //

    public User findUserById(String id) {
        MongoManager manager = MongoManager.getInstance();

        try (MongoCursor<Document> cursor = manager.getCollection("user").find(eq("_id", new ObjectId(id))).iterator()) {
            if (cursor.hasNext()) {
                Document user = cursor.next();

                // user attributes
                String username = user.getString("username");
                String password = user.getString("password");
                String name = user.getString("name");
                String surname = user.getString("surname");
                String email = user.getString("email");
                String country = user.getString("country");
                String picturePath = user.getString("picturePath");
                String favouriteGenre = user.getString("favouriteGenre");
                int age = user.getInteger("age");
                String gender = user.getString("gender");

                User newUser = new User(id, username, password, name, surname, email, country, picturePath, favouriteGenre, age, gender);
                return newUser;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public User findUserByUsername(String username) {
        MongoManager manager = MongoManager.getInstance();

        try (MongoCursor<Document> cursor = manager.getCollection("user").find(eq("username", username)).iterator()) {
            if (cursor.hasNext()) {
                Document user = cursor.next();

                // user attributes
                String id = user.getObjectId("_id").toString();
                String password = user.getString("password");
                String name = user.getString("name");
                String surname = user.getString("surname");
                String email = user.getString("email");
                String country = user.getString("country");
                String picturePath = user.getString("picturePath");
                String favouriteGenre = user.getString("favouriteGenre");
                int age = user.getInteger("age");
                String gender = user.getString("gender");

                User newUser = new User(id, username, password, name, surname, email, country, picturePath, favouriteGenre, age, gender);
                return newUser;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public User findUserByEmail(String email) {
        MongoManager manager = MongoManager.getInstance();

        try (MongoCursor<Document> cursor = manager.getCollection("user").find(eq("email", email)).iterator()) {
            if (cursor.hasNext()) {
                Document user = cursor.next();

                // user attributes
                String id = user.getObjectId("_id").toString();
                String username = user.getString("username");
                String password = user.getString("password");
                String name = user.getString("name");
                String surname = user.getString("surname");
                String country = user.getString("country");
                String picturePath = user.getString("picturePath");
                String favouriteGenre = user.getString("favouriteGenre");
                int age = user.getInteger("age");
                String gender = user.getString("gender");

                User newUser = new User(id, username, password, name, surname, email, country, picturePath, favouriteGenre, age, gender);
                return newUser;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public List<User> findUsersByCountry(String country, int limit) {
        MongoManager manager = MongoManager.getInstance();

        try (MongoCursor<Document> cursor = manager.getCollection("user").find(eq("country", country)).limit(limit).iterator()) {
            List<User> users = new ArrayList<>();

            while (cursor.hasNext()) {
                Document user = cursor.next();

                // user attributes
                String id = user.getObjectId("_id").toString();
                String username = user.getString("username");
                String password = user.getString("password");
                String name = user.getString("name");
                String surname = user.getString("surname");
                String email = user.getString("email");
                String picturePath = user.getString("picturePath");
                String favouriteGenre = user.getString("favouriteGenre");
                int age = user.getInteger("age");
                String gender = user.getString("gender");

                User newUser = new User(id, username, password, name, surname, email, country, picturePath, favouriteGenre, age, gender);
                users.add(newUser);
            }

            return users;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // --------- UPDATE --------- //

    public boolean updateUser(User user) {
        MongoManager manager = MongoManager.getInstance();

        try {
            Bson filter = eq("_id", new ObjectId(user.getId()));
            Bson update = combine(
                    set("username", user.getUsername()),
                    set("password", user.getPassword()),
                    set("name", user.getName()),
                    set("surname", user.getSurname()),
                    set("email", user.getEmail()),
                    set("country", user.getCountry()),
                    set("picturePath", user.getPicturePath()),
                    set("favouriteGenre", user.getFavouriteGenre()),
                    set("age", user.getAge()),
                    set("gender", user.getGender())
            );

            UpdateResult result = manager.getCollection("user").updateOne(filter, update);
            return result.getModifiedCount() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // --------- DELETE --------- //

    public boolean deleteUserById(String id) {
        MongoManager manager = MongoManager.getInstance();

        try {
            DeleteResult result = manager.getCollection("user").deleteOne(eq("_id", new ObjectId(id)));
            return result.getDeletedCount() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUserByUsername(String username) {
        MongoManager manager = MongoManager.getInstance();

        try {
            DeleteResult result = manager.getCollection("user").deleteOne(eq("username", username));
            return result.getDeletedCount() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------------------------------------------------------------------------------- //

    // ------------------------------- AGGREGATION QUERY -------------------------------- //

    public List<Entry<String, Float>> showAverageAgeOfUsersPerFavouriteCategory(int lim) {
        MongoManager manager = MongoManager.getInstance();

        /*
        db.user.aggregate([
            { $group: { _id: { favouriteGenre: "$favouriteGenre"}, avgAge: { $avg: "$age"} } },
            { $project: { _id: 0, favouriteGenre: "$_id.favouriteGenre", avgAge: "$avgAge" } }
        ])
         */

        try {
            Bson group = group("$favouriteGenre", avg("avg", "$age"));
            Bson projectionsFields = fields(excludeId(), computed("FavouriteCategory", "$_id"), computed("avgAge", "$avg"));
            Bson projection = project(projectionsFields);
            Bson limit = limit(lim);

            List<Entry<String, Float>> avgList = new ArrayList<>();

            for (Document doc : manager.getCollection("user").aggregate(Arrays.asList(group, projection, limit))) {
                double avg = doc.getDouble("avgAge");
                Entry<String, Float> test = new AbstractMap.SimpleEntry<String, Float>(doc.getString("FavouriteCategory"), (float) avg);
                avgList.add(test);
            }

            if (avgList.isEmpty())
                return null;

            return avgList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Entry<String, Float>> showAverageAgeOfUsersPerCountry(int lim) {
        MongoManager manager = MongoManager.getInstance();

        /*
        db.user.aggregate([
            { $group: { _id: { country: "$country"}, avgAge: { $avg: "$age"} } },
            { $project: { _id: 0, Country: "$_id.country", avgAge: "$avgAge" } }
        ])
        */

        try {
            Bson group = group("$country", avg("avg", "$age"));
            Bson projectionsFields = fields(excludeId(), computed("Country", "$_id"), computed("avgAge", "$avg"));
            Bson projection = project(projectionsFields);
            Bson limit = limit(lim);

            List<Entry<String, Float>> avgList = new ArrayList<>();

            for (Document doc : manager.getCollection("user").aggregate(Arrays.asList(group, projection, limit))) {
                double avg = doc.getDouble("avgAge");
                Entry<String, Float> test = new AbstractMap.SimpleEntry<String, Float>(doc.getString("Country"), (float) avg);
                avgList.add(test);
            }

            if (avgList.isEmpty())
                return null;

            return avgList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Entry<String, Integer>> showNumberOfUsersPerCountry(int limit) {
        MongoManager manager = MongoManager.getInstance();

        try {
            Bson group = group("$country", sum("num", 1));
            Bson sort = sort(descending("num"));
            Bson project = project(fields(
                    excludeId(),
                    computed("country", "$_id"),
                    computed("users", "$num")
            ));
            Bson limitRes = limit(limit);

            List<Document> results = manager.getCollection("user").aggregate(Arrays.asList(group, sort, project, limitRes)).into(new ArrayList<>());
            if(results.isEmpty())
                return null;

            List<Entry<String, Integer>> countries = new ArrayList<>();
            for (Document result : results){
                Entry<String, Integer> country = new AbstractMap.SimpleEntry<>(result.getString("country"), result.getInteger("users"));
                countries.add(country);
            }

            return countries;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public List<String> showFavouriteCategoryForGender(String gender, int limit) {

        MongoManager manager = MongoManager.getInstance();

        try {
            Bson match = match(eq("gender", gender));
            Bson group = group("$favouriteGenre", sum("num", 1));
            Bson sort = sort(descending("num"));
            Bson project = project(fields(excludeId(), computed("category", "$_id")));
            Bson limitRes = limit(limit);

            List<Document> results = manager.getCollection("user").aggregate(Arrays.asList(match, group, sort, project, limitRes)).into(new ArrayList<>());
            if(results.isEmpty())
                return null;
            List<String> categories = new ArrayList<>();
            for (Document category : results)
                categories.add(category.getString("category"));
            return categories;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ---------------------------------------------------------------------------------- //
}
