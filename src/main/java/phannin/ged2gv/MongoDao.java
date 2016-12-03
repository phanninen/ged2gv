package phannin.ged2gv;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by pasi on 30.11.2016.
 */
public class MongoDao {
    private MongoDatabase db;

    public MongoDao() {
        MongoClient mongoClient = new MongoClient();
        this.db = mongoClient.getDatabase("test");

    }

    public void storePedigree(Pedigree sukupuu) {

        sukupuu.getPersons().forEach((k, v) -> storeToCollection("persons", k, v));
        sukupuu.getFamilies().forEach((k, v) -> storeToCollection("families", k, v));
    }

    private void storeToCollection(String collection, String k, Entity v) {
        try {
            if (exists(collection, k))
                update(collection, k, v);
            else
                insert(collection, k, v);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    private boolean exists(String collection, String id) {
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("id", id);
        return this.db.getCollection(collection).count(whereQuery) > 0;

    }

    private void insert(String collection, String id, Entity entity) throws IOException {
        this.db.getCollection(collection).insertOne(getDocument(entity));

    }

    private void update(String collection, String id, Entity entity) throws IOException {

        Document doc = getDocument(entity);
        this.db.getCollection(collection).updateOne(new Document("id", id), new Document("$set", doc));


    }

    private Document getDocument(Object object) throws IOException {
        StringWriter writer = new StringWriter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(writer, object);
        String json = writer.toString();
        return Document.parse(json);
    }
}
