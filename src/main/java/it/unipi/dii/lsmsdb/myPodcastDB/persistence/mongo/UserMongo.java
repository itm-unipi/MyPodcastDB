package it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo;

import com.mongodb.client.MongoCursor;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

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
                .append("pictureSmall", user.getPictureSmall())
                .append("pictureMedium", user.getPictureMedium())
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
                String pictureSmall = user.getString("pictureSmall");
                String pictureMedium = user.getString("pictureMedium");
                String favouriteGenre = user.getString("favouriteGenre");
                int age = user.getInteger("age");
                String gender = user.getString("gender");

                User newUser = new User(id, username, password, name, surname, email, country, pictureSmall, pictureMedium, favouriteGenre, age, gender);
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
                String pictureSmall = user.getString("pictureSmall");
                String pictureMedium = user.getString("pictureMedium");
                String favouriteGenre = user.getString("favouriteGenre");
                int age = user.getInteger("age");
                String gender = user.getString("gender");

                User newUser = new User(id, username, password, name, surname, email, country, pictureSmall, pictureMedium, favouriteGenre, age, gender);
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
                String pictureSmall = user.getString("pictureSmall");
                String pictureMedium = user.getString("pictureMedium");
                String favouriteGenre = user.getString("favouriteGenre");
                int age = user.getInteger("age");
                String gender = user.getString("gender");

                User newUser = new User(id, username, password, name, surname, email, country, pictureSmall, pictureMedium, favouriteGenre, age, gender);
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

        try (MongoCursor<Document> cursor = manager.getCollection("user").find(eq("country", country)).iterator()) {
            List<User> users = new ArrayList<>();

            int counter = 0;
            while (cursor.hasNext()) {
                Document user = cursor.next();

                // user attributes
                String id = user.getObjectId("_id").toString();
                String username = user.getString("username");
                String password = user.getString("password");
                String name = user.getString("name");
                String surname = user.getString("surname");
                String email = user.getString("email");
                String pictureSmall = user.getString("pictureSmall");
                String pictureMedium = user.getString("pictureMedium");
                String favouriteGenre = user.getString("favouriteGenre");
                int age = user.getInteger("age");
                String gender = user.getString("gender");

                User newUser = new User(id, username, password, name, surname, email, country, pictureSmall, pictureMedium, favouriteGenre, age, gender);
                users.add(newUser);

                if (limit != 0) {
                    counter += 1;
                    if (counter == limit)
                        break;
                }
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
            manager.getCollection("user").updateOne(
                    eq("_id", new ObjectId(user.getId())),
                    combine(set("username", user.getUsername()),
                            set("password", user.getPassword()),
                            set("name", user.getName()),
                            set("surname", user.getSurname()),
                            set("email", user.getEmail()),
                            set("country", user.getCountry()),
                            set("pictureSmall", user.getPictureSmall()),
                            set("pictureMedium", user.getPictureMedium()),
                            set("favouriteGenre", user.getFavouriteGenre()),
                            set("age", user.getAge()),
                            set("gender", user.getGender())
                    )
            );
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // --------- DELETE --------- //

    public boolean deleteUserById(String id) {
        MongoManager manager = MongoManager.getInstance();

        try {
            manager.getCollection("user").deleteOne(eq("_id", new ObjectId(id)));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean deleteUserByUsername(String username) {
        MongoManager manager = MongoManager.getInstance();

        try {
            manager.getCollection("user").deleteOne(eq("username", username));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // ---------------------------------------------------------------------------------- //

    // ------------------------------- AGGREGATION QUERY -------------------------------- //

    public List<Entry<String, Float>> showAverageAgeOfUsersPerFavouriteCategory(int limit) {
        return null;
    }

    public List<Entry<String, Float>> showAverageAgeOfUsersPerCountry(int limit) {
        return null;
    }

    public List<Entry<String, Integer>> showNumberOfUsersPerCountry(int limit) {
        return null;
    }

    public String showFavouriteCategoryForGender(String gender) {
        return null;
    }

    // ---------------------------------------------------------------------------------- //
}
