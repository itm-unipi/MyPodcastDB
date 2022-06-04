package it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Updates.*;

public class AuthorMongo {

    // ------------------------------- CRUD OPERATION ----------------------------------- //

    // --------- CREATE --------- //

    public boolean addAuthor(Author author) {
        MongoManager manager = MongoManager.getInstance();

        try {
            Document newAuthor = new Document("name", author.getName())
                    .append("email", author.getEmail())
                    .append("password", author.getPassword())
                    .append("podcasts", new ArrayList<>());

            manager.getCollection("author").insertOne(newAuthor);
            author.setId(newAuthor.getObjectId("_id").toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------- READ ---------- //

    public Author findAuthorById(String id) {
        MongoManager manager = MongoManager.getInstance();

        try {
            Bson filter = eq("_id", new ObjectId(id));
            MongoCursor<Document> cursor = manager.getCollection("author").find(filter).iterator();

            if (cursor.hasNext()) {
                Document author = cursor.next();

                String authorName = author.getString("name");
                String authorPassword = author.getString("password");
                String authorEmail = author.getString("email");
                String authorPicture = author.getString("picturePath");

                Author newAuthor = new Author(id, authorName, authorPassword, authorEmail, authorPicture);

                // retrieve author's podcasts
                List<Document> podcasts = author.getList("podcasts", Document.class);
                for (Document podcast : podcasts) {
                    String podcastId = podcast.getObjectId("podcastId").toString();
                    String podcastName = podcast.getString("podcastName");
                    String podcastDate = podcast.getString("podcastReleaseDate").replace("T", " "). replace("Z", "");
                    Date podcastReleaseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(podcastDate);
                    String podcastArtwork = podcast.getString("artworkUrl600");
                    String podcastCategory = podcast.getString("primaryCategory");

                    newAuthor.addPodcast(new Podcast(podcastId, podcastName, podcastReleaseDate, podcastArtwork, podcastCategory));
                }

                return newAuthor;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public Author findAuthorByName(String name) {
        MongoManager manager = MongoManager.getInstance();

        try {
            Bson filter = eq("name", name);
            MongoCursor<Document> cursor = manager.getCollection("author").find(filter).iterator();

            if (cursor.hasNext()) {
                Document author = cursor.next();

                String authorId = author.getObjectId("_id").toString();
                String authorPassword = author.getString("password");
                String authorEmail = author.getString("email");
                String authorPicture = author.getString("picturePath");

                Author newAuthor = new Author(authorId, name, authorPassword, authorEmail, authorPicture);

                // retrieve author's podcasts
                List<Document> podcasts = author.getList("podcasts", Document.class);
                for (Document podcast : podcasts) {
                    String podcastId = podcast.getObjectId("podcastId").toString();
                    String podcastName = podcast.getString("podcastName");
                    String podcastDate = podcast.getString("podcastReleaseDate").replace("T", " "). replace("Z", "");
                    Date podcastReleaseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(podcastDate);
                    String podcastArtwork = podcast.getString("artworkUrl600");
                    String podcastCategory = podcast.getString("primaryCategory");

                    newAuthor.addPodcast(new Podcast(podcastId, podcastName, podcastReleaseDate, podcastArtwork, podcastCategory));
                }

                return newAuthor;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public Author findAuthorByEmail(String email) {
        MongoManager manager = MongoManager.getInstance();

        try {
            Bson filter = eq("email", email);
            MongoCursor<Document> cursor = manager.getCollection("author").find(filter).iterator();

            if (cursor.hasNext()) {
                Document author = cursor.next();

                String authorId = author.getObjectId("_id").toString();
                String authorName = author.getString("name");
                String authorPassword = author.getString("password");
                String authorPicture = author.getString("picturePath");

                Author newAuthor = new Author(authorId, authorName, authorPassword, email, authorPicture);

                // retrieve author's podcasts
                List<Document> podcasts = author.getList("podcasts", Document.class);
                for (Document podcast : podcasts) {
                    String podcastId = podcast.getObjectId("podcastId").toString();
                    String podcastName = podcast.getString("podcastName");
                    String podcastDate = podcast.getString("podcastReleaseDate").replace("T", " "). replace("Z", "");
                    Date podcastReleaseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(podcastDate);
                    String podcastArtwork = podcast.getString("artworkUrl600");
                    String podcastCategory = podcast.getString("primaryCategory");

                    newAuthor.addPodcast(new Podcast(podcastId, podcastName, podcastReleaseDate, podcastArtwork, podcastCategory));
                }

                return newAuthor;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public Author findAuthorByPodcastId(String podcastId) {
        MongoManager manager = MongoManager.getInstance();

        try {
            Bson filter = eq("podcasts.podcastId", new ObjectId(podcastId));
            MongoCursor<Document> cursor = manager.getCollection("author").find(filter).iterator();

            if (cursor.hasNext()) {
                Document author = cursor.next();

                String authorId = author.getObjectId("_id").toString();
                String authorName = author.getString("name");
                String authorPassword = author.getString("password");
                String authorEmail = author.getString("email");
                String authorPicture = author.getString("picturePath");

                Author newAuthor = new Author(authorId, authorName, authorPassword, authorEmail, authorPicture);

                List<Document> podcasts = author.getList("podcasts", Document.class);
                for(Document podcast : podcasts) {
                    String podId = podcast.getObjectId("podcastId").toString();
                    String podcastName = podcast.getString("podcastName");
                    String podcastDate = podcast.getString("podcastReleaseDate").replace("T", " "). replace("Z", "");
                    Date podcastReleaseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(podcastDate);
                    String podcastArtwork = podcast.getString("artworkUrl600");
                    String podcastCategory = podcast.getString("primaryCategory");

                    newAuthor.addPodcast(new Podcast(podId, podcastName, podcastReleaseDate, podcastArtwork, podcastCategory));
                }

                return newAuthor;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public List<Author> findAuthorsByPodcastName(String podcastName, int limit) {
        MongoManager manager = MongoManager.getInstance();

        try {
            Bson filter = eq("podcasts.podcastName", podcastName);
            MongoCursor<Document> cursor = manager.getCollection("author").find(filter).limit(limit).iterator();

            List<Author> authors = new ArrayList<>();

            while (cursor.hasNext()) {
                Document author = cursor.next();

                String authorId = author.getObjectId("_id").toString();
                String authorName = author.getString("name");
                String authorPassword = author.getString("password");
                String authorEmail = author.getString("email");
                String authorPicture = author.getString("picturePath");

                Author newAuthor = new Author(authorId, authorName, authorPassword, authorEmail, authorPicture);

                List<Document> podcasts = author.getList("podcasts", Document.class);
                for (Document podcast : podcasts) {
                    String podId = podcast.getObjectId("podcastId").toString();
                    String podName = podcast.getString("podcastName");
                    String podcastDate = podcast.getString("podcastReleaseDate").replace("T", " "). replace("Z", "");
                    Date podcastReleaseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(podcastDate);
                    String podcastArtwork = podcast.getString("artworkUrl600");
                    String podcastCategory = podcast.getString("primaryCategory");

                    newAuthor.addPodcast(new Podcast(podId, podName, podcastReleaseDate, podcastArtwork, podcastCategory));
                }

                authors.add(newAuthor);
            }

            return authors;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // --------- UPDATE --------- //

    public boolean updateAuthor(Author author) {
        MongoManager manager = MongoManager.getInstance();

        try {
            Bson filter = eq("_id", new ObjectId(author.getId()));
            Bson update = combine(set("name", author.getName()),
                    set("password", author.getPassword()),
                    set("email", author.getEmail())
            );

            UpdateResult result = manager.getCollection("author").updateOne(filter, update);
            return result.getMatchedCount() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePodcastOfAuthor(String authorId, int podcastIndex, String podcastId, String podcastName, String podcastReleaseDate, String category, String artworkUrl) {
        MongoManager manager = MongoManager.getInstance();

        try {
            Bson filter = eq("_id", new ObjectId(authorId));
            Bson update = combine(set("podcasts." + podcastIndex + ".podcastName", podcastName),
                    set("podcasts." + podcastIndex + ".podcastReleaseDate", podcastReleaseDate),
                    set("podcasts." + podcastIndex + ".category", category),
                    set("podcasts." + podcastIndex + ".artworkUrl600", artworkUrl));

            UpdateResult result = manager.getCollection("author").updateOne(filter, update);
            return result.getMatchedCount() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addPodcastToAuthor(String authorId, Podcast podcast) {
        MongoManager manager = MongoManager.getInstance();

        try {
            // Creating a new document for the podcast
            Document newPodcast = new Document("podcastId", new ObjectId())
                    .append("podcastName", podcast.getName())
                    .append("podcastReleaseDate", podcast.getReleaseDateAsString());

            Bson filter = eq( "_id", new ObjectId(authorId));
            Bson setUpdate = push("podcasts", newPodcast);

            UpdateResult result = manager.getCollection("author").updateOne(filter, setUpdate);
            // TODO: remove setId
            podcast.setId(newPodcast.getObjectId("podcastId").toString());

            return result.getModifiedCount() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // --------- DELETE --------- //

    public boolean deleteAuthorById(String authorId) {
        MongoManager manager = MongoManager.getInstance();

        try {
            DeleteResult result = manager.getCollection("author").deleteOne(eq("_id", new ObjectId(authorId)));
            return result.getDeletedCount() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAuthorByName(String authorName) {
        MongoManager manager = MongoManager.getInstance();

        try {
            DeleteResult result = manager.getCollection("author").deleteOne(eq("name", authorName));
            return result.getDeletedCount() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePodcastOfAuthor(String authorId, String podcastId) {
        MongoManager manager = MongoManager.getInstance();

        try {
            Bson filterAuthor = eq("_id", new ObjectId(authorId));
            Bson update = pull("podcasts", new Document("podcastId", new ObjectId(podcastId)));
            UpdateResult result = manager.getCollection("author").updateOne(filterAuthor, update);

            return result.getModifiedCount() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAllPodcastsOfAuthor(String authorId) {
        MongoManager manager = MongoManager.getInstance();

        try {
            manager.getCollection("author").updateOne(
                    eq("_id", new ObjectId(authorId)),
                    set("podcasts", new ArrayList<>()));

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------------------------------------------------------------------------------- //
}
