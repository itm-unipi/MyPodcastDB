package it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Admin;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public class AdminMongo {

    // ------------------------------- CRUD OPERATION ----------------------------------- //

    // --------- CREATE --------- //

    public boolean addAdmin(Admin admin) {
        MongoManager manager = MongoManager.getInstance();

        Document newAdmin = new Document()
                .append("name", admin.getName())
                .append("password", admin.getPassword())
                .append("email", admin.getEmail());

        try {
            manager.getCollection("admin").insertOne(newAdmin);
            admin.setId(newAdmin.getObjectId("_id").toString());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // ---------- READ ---------- //

    public Admin findAdminById(String id) {
        MongoManager manager = MongoManager.getInstance();

        try (MongoCursor<Document> cursor = manager.getCollection("admin").find(eq("_id", new ObjectId(id))).iterator()) {
            if (cursor.hasNext()) {
                Document admin = cursor.next();

                // admin attributes
                String name = admin.getString("name");
                String password = admin.getString("password");
                String email = admin.getString("email");

                Admin newAdmin = new Admin(id, name, password, email);
                return newAdmin;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public Admin findAdminByName(String name) {
        MongoManager manager = MongoManager.getInstance();

        try (MongoCursor<Document> cursor = manager.getCollection("admin").find(eq("name", name)).iterator()) {
            if (cursor.hasNext()) {
                Document admin = cursor.next();

                // admin attributes
                String id = admin.getObjectId("_id").toString();
                String password = admin.getString("password");
                String email = admin.getString("email");

                Admin newAdmin = new Admin(id, name, password, email);
                return newAdmin;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public Admin findAdminByEmail(String email) {
        MongoManager manager = MongoManager.getInstance();

        try (MongoCursor<Document> cursor = manager.getCollection("admin").find(eq("email", email)).iterator()) {
            if (cursor.hasNext()) {
                Document admin = cursor.next();

                // admin attributes
                String id = admin.getObjectId("_id").toString();
                String name = admin.getString("name");
                String password = admin.getString("password");

                Admin newAdmin = new Admin(id, name, password, email);
                return newAdmin;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    // --------- UPDATE --------- //

    public boolean updateAdmin(Admin admin) {
        MongoManager manager = MongoManager.getInstance();

        try {
            Bson filter = eq("_id", new ObjectId(admin.getId()));
            Bson update = combine(
                    set("name", admin.getName()),
                    set("password", admin.getPassword()),
                    set("email", admin.getEmail())
            );

            UpdateResult result = manager.getCollection("admin").updateOne(filter, update);
            return result.getModifiedCount() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // --------- DELETE --------- //

    public boolean deleteAdminById(String id) {
        MongoManager manager = MongoManager.getInstance();

        try {
            DeleteResult result = manager.getCollection("admin").deleteOne(eq("_id", new ObjectId(id)));
            return result.getDeletedCount() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAdminByName(String name) {
        MongoManager manager = MongoManager.getInstance();

        try {
            DeleteResult result = manager.getCollection("admin").deleteOne(eq("name", name));
            return result.getDeletedCount() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAdminByEmail(String email) {
        MongoManager manager = MongoManager.getInstance();

        try {
            DeleteResult result = manager.getCollection("admin").deleteOne(eq("email", email));
            return result.getDeletedCount() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------------------------------------------------------------------------------- //
}
