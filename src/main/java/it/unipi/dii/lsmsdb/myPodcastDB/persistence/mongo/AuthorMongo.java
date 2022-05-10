package it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo;

import com.mongodb.client.MongoCursor;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class AuthorMongo {

    // ------------------------------- CRUD OPERATION ----------------------------------- //

    // --------- CREATE --------- //

    public boolean addAuthor(Author author) {
        MongoManager manager = MongoManager.getInstance();

        //name, email, password, podcasts
        try {
            Document newAuthor = new Document("name", author.getName())
                    .append("email", author.getEmail())
                    .append("password", author.getPassword())
                    .append("podcasts", new ArrayList());

            //add newAuthor
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

        try (MongoCursor<Document> cursor = manager.getCollection("author").find(eq("_id", new ObjectId(id))).iterator()) {
            if (cursor.hasNext()) {
                Document author = cursor.next();

                String authorName = author.getString("name");
                String authorPassword = author.getString("password");
                String authorEmail = author.getString("email");

                Author newAuthor = new Author(id, authorName, authorPassword, authorEmail);

                // retrieve author's podcasts
                List<Document> podcasts = author.getList("podcasts", Document.class);
                for (Document podcast : podcasts) {
                    String podcastId = podcast.getString("podcastId");
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

        try (MongoCursor<Document> cursor = manager.getCollection("author").find(eq("name", name)).iterator()) {
            if (cursor.hasNext()) {
                Document author = cursor.next();

                String authorId = author.getObjectId("_id").toString();
                String authorPassword = author.getString("password");
                String authorEmail = author.getString("email");

                Author newAuthor = new Author(authorId, name, authorPassword, authorEmail);

                // retrieve author's podcasts
                List<Document> podcasts = author.getList("podcasts", Document.class);
                for (Document podcast : podcasts) {
                    String podcastId = podcast.getString("podcastId");
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

        try (MongoCursor<Document> cursor = manager.getCollection("author").find(eq("email", email)).iterator()) {
            if (cursor.hasNext()) {
                Document author = cursor.next();

                String authorId = author.getObjectId("_id").toString();
                String authorName = author.getString("name");
                String authorPassword = author.getString("password");

                Author newAuthor = new Author(authorId, authorName, authorPassword, email);

                // retrieve author's podcasts
                List<Document> podcasts = author.getList("podcasts", Document.class);
                for (Document podcast : podcasts) {
                    String podcastId = podcast.getString("podcastId");
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

        try (MongoCursor<Document> cursor = manager.getCollection("author").find(eq("podcasts.podcastId", podcastId)).iterator()) {
            if (cursor.hasNext()) {
                Document author = cursor.next();

                String authorId = author.getObjectId("_id").toString();
                String authorName = author.getString("name");
                String authorPassword = author.getString("password");
                String authorEmail = author.getString("email");

                Author newAuthor = new Author(authorId, authorName, authorPassword, authorEmail);

                List<Document> podcasts = author.getList("podcasts", Document.class);
                for(Document podcast : podcasts) {
                    String podId = podcast.getString("podcastId");
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
        List<Author> authors= new ArrayList();

        try (MongoCursor<Document> cursor = manager.getCollection("author").find(eq("podcasts.podcastName", podcastName)).iterator()) {
            int counter = 0;

            while (cursor.hasNext()) {
                Document author = cursor.next();

                String authorId = author.getObjectId("_id").toString();
                String authorName = author.getString("name");
                String authorPassword = author.getString("password");
                String authorEmail = author.getString("email");

                Author newAuthor = new Author(authorId, authorName, authorPassword, authorEmail);

                List<Document> podcasts = author.getList("podcasts", Document.class);
                for(Document podcast : podcasts) {
                    String podId = podcast.getString("podcastId");
                    String podName = podcast.getString("podcastName");
                    String podcastDate = podcast.getString("podcastReleaseDate").replace("T", " "). replace("Z", "");
                    Date podcastReleaseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(podcastDate);

                    newAuthor.addPodcast(podId, podName, podcastReleaseDate);
                }

                authors.add(newAuthor);

                if (limit != 0) {
                    counter += 1;
                    if (counter == limit)
                        break;
                }
            }

            return authors;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // --------- UPDATE --------- //

    public boolean updateAuthor(Author author) {
        return false;
    }

    public boolean addPodcastToAuthor(String authorId, Podcast podcast) { return false; }

    // --------- DELETE --------- //

    public boolean deleteAuthorById(String id) {
        return false;
    }

    public boolean deleteAuthorByName(String id) {
        return false;
    }

    public boolean deletePodcastOfAuthor(String authorId, String podcastId) { return false; }

    public int deleteAllPodcastsOfAuthor(String authorId) {
        return -1;
    }

    // ---------------------------------------------------------------------------------- //
}
