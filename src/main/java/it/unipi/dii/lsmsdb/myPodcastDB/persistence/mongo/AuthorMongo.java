package it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

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
            Bson filter = Filters.eq("_id", new ObjectId(id));
            MongoCursor<Document> cursor = manager.getCollection("author").find(filter).iterator();

            if (cursor.hasNext()) {
                Document author = cursor.next();

                String authorName = author.getString("name");
                String authorPassword = author.getString("password");
                String authorEmail = author.getString("email");

                Author newAuthor = new Author(id, authorName, authorPassword, authorEmail);

                // retrieve author's podcasts
                List<Document> podcasts = author.getList("podcasts", Document.class);
                for (Document podcast : podcasts) {
                    String podcastId = podcast.getObjectId("podcastId").toString();
                    String podcastName = podcast.getString("podcastName");
                    String podcastDate = podcast.getString("podcastReleaseDate").replace("T", " "). replace("Z", "");
                    Date podcastReleaseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(podcastDate);

                    newAuthor.addPodcast(podcastId, podcastName, podcastReleaseDate);
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
            Bson filter = Filters.eq("name", name);
            MongoCursor<Document> cursor = manager.getCollection("author").find(filter).iterator();

            if (cursor.hasNext()) {
                Document author = cursor.next();

                String authorId = author.getObjectId("_id").toString();
                String authorPassword = author.getString("password");
                String authorEmail = author.getString("email");

                Author newAuthor = new Author(authorId, name, authorPassword, authorEmail);

                // retrieve author's podcasts
                List<Document> podcasts = author.getList("podcasts", Document.class);
                for (Document podcast : podcasts) {
                    String podcastId = podcast.getObjectId("podcastId").toString();
                    String podcastName = podcast.getString("podcastName");
                    String podcastDate = podcast.getString("podcastReleaseDate").replace("T", " "). replace("Z", "");
                    Date podcastReleaseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(podcastDate);

                    newAuthor.addPodcast(podcastId, podcastName, podcastReleaseDate);
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
            Bson filter = Filters.eq("email", email);
            MongoCursor<Document> cursor = manager.getCollection("author").find(filter).iterator();

            if (cursor.hasNext()) {
                Document author = cursor.next();

                String authorId = author.getObjectId("_id").toString();
                String authorName = author.getString("name");
                String authorPassword = author.getString("password");

                Author newAuthor = new Author(authorId, authorName, authorPassword, email);

                // retrieve author's podcasts
                List<Document> podcasts = author.getList("podcasts", Document.class);
                for (Document podcast : podcasts) {
                    String podcastId = podcast.getObjectId("podcastId").toString();
                    String podcastName = podcast.getString("podcastName");
                    String podcastDate = podcast.getString("podcastReleaseDate").replace("T", " "). replace("Z", "");
                    Date podcastReleaseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(podcastDate);

                    newAuthor.addPodcast(podcastId, podcastName, podcastReleaseDate);
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
            Bson filter = Filters.eq("podcasts.podcastId", new ObjectId(podcastId));
            MongoCursor<Document> cursor = manager.getCollection("author").find(filter).iterator();

            if (cursor.hasNext()) {
                Document author = cursor.next();

                String authorId = author.getObjectId("_id").toString();
                String authorName = author.getString("name");
                String authorPassword = author.getString("password");
                String authorEmail = author.getString("email");

                Author newAuthor = new Author(authorId, authorName, authorPassword, authorEmail);

                List<Document> podcasts = author.getList("podcasts", Document.class);
                for(Document podcast : podcasts) {
                    String podId = podcast.getObjectId("podcastId").toString();
                    String podcastName = podcast.getString("podcastName");
                    String podcastDate = podcast.getString("podcastReleaseDate").replace("T", " "). replace("Z", "");
                    Date podcastReleaseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(podcastDate);

                    newAuthor.addPodcast(podId, podcastName, podcastReleaseDate);
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
            Bson filter = Filters.eq("podcasts.podcastName", podcastName);
            MongoCursor<Document> cursor = manager.getCollection("author").find(filter).limit(limit).iterator();

            List<Author> authors = new ArrayList<>();

            while (cursor.hasNext()) {
                Document author = cursor.next();

                String authorId = author.getObjectId("_id").toString();
                String authorName = author.getString("name");
                String authorPassword = author.getString("password");
                String authorEmail = author.getString("email");

                Author newAuthor = new Author(authorId, authorName, authorPassword, authorEmail);

                List<Document> podcasts = author.getList("podcasts", Document.class);
                for (Document podcast : podcasts) {
                    String podId = podcast.getObjectId("podcastId").toString();
                    String podName = podcast.getString("podcastName");
                    String podcastDate = podcast.getString("podcastReleaseDate").replace("T", " "). replace("Z", "");
                    Date podcastReleaseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(podcastDate);

                    newAuthor.addPodcast(podId, podName, podcastReleaseDate);
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
            Bson updates = combine(set("name", author.getName()),
                    set("password", author.getPassword()),
                    set("email", author.getEmail())
            );

            manager.getCollection("author").updateOne(filter, updates);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePodcastOfAuthor(String authorId, String podcastId, String podcastName, String podcastReleaseDate) {
        MongoManager manager = MongoManager.getInstance();

        try {
            Bson filter = Filters.eq("_id", new ObjectId(authorId));
            MongoCursor<Document> cursor = manager.getCollection("author").find(filter).iterator();

            if (cursor.hasNext()) {
                Document author = cursor.next();

                List<Document> podcasts = author.getList("podcasts", Document.class);
                for (Document podcast : podcasts) {
                    if (podcastId.equals(podcast.getObjectId("podcastId").toString())) {
                        Bson podcastFilter = and(eq("_id", new ObjectId(authorId)), eq("podcasts.podcastId", new ObjectId(podcastId)));
                        Bson podcastUpdates = combine(set("podcasts.$.podcastName", podcastName), set("podcasts.$.podcastReleaseDate", podcastReleaseDate));

                        manager.getCollection("author").updateOne(podcastFilter, podcastUpdates);
                    }
                }
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addPodcastToAuthor(String authorId, Podcast podcast) {
        MongoManager manager = MongoManager.getInstance();

        try {
            Document newPodcast = new Document("podcastId", new ObjectId())
                    .append("podcastName", podcast.getName())
                    .append("podcastReleaseDate", podcast.getReleaseDateAsString());

            Bson filter = Filters.eq( "_id", new ObjectId(authorId));
            Bson setUpdate = Updates.push("podcasts", newPodcast);

            manager.getCollection("author").updateOne(filter, setUpdate);
            podcast.setId(newPodcast.getObjectId("podcastId").toString());

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // --------- DELETE --------- //

    public boolean deleteAuthorById(String authorId) {
        MongoManager manager = MongoManager.getInstance();

        try {
            manager.getCollection("author").deleteOne(eq("_id", new ObjectId(authorId)));
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAuthorByName(String authorName) {
        MongoManager manager = MongoManager.getInstance();

        try {
            manager.getCollection("author").deleteOne(eq("name", authorName));
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePodcastOfAuthor(String authorId, String podcastId) {
        MongoManager manager = MongoManager.getInstance();

        try {
            Bson fields = new Document().append("podcasts", new Document().append("podcastId", new ObjectId(podcastId)));
            Bson update = new Document("$pull", fields);

            manager.getCollection("author").updateOne(eq("_id", new ObjectId(authorId)), update);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAllPodcastsOfAuthor(String authorId) {
        MongoManager manager = MongoManager.getInstance();

        try {
            manager.getCollection("author").updateOne(
                    Filters.eq("_id", new ObjectId(authorId)),
                    Updates.set("podcasts", new ArrayList<>()));

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------------------------------------------------------------------------------- //
}
