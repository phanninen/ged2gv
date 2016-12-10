package phannin.ged2gv;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.IOException;
import java.io.StringWriter;

import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;

/**
 * Created by pasi on 30.11.2016.
 */
public class MongoPedigree implements Pedigree {
    public static final String PERSONS = "persons";
    public static final String FAMILIES = "families";
    private MongoDatabase db;

    public MongoPedigree() {
        MongoClient mongoClient = new MongoClient();
        this.db = mongoClient.getDatabase("test");

    }

    @Override
    public Person getPerson(String id) {
        try {
            Document doc = find(PERSONS, id);
            return doc != null ? toPerson(doc) : null;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Family getFamily(String id) {

        try {
            Document doc = find(FAMILIES, id);
            return doc != null ? toFamily(doc) : null;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

    @Override
    public void addPerson(Person person) {
        storeToCollection(PERSONS, person.getId(), person);
    }

    @Override
    public void addFamily(Family family) {
        storeToCollection(FAMILIES, family.getId(), family);
    }


/*    public void storePedigree(Pedigree sukupuu) {

        sukupuu.getPersons().forEach((k, v) -> storeToCollection("persons", k, v));
        sukupuu.getFamilies().forEach((k, v) -> storeToCollection("families", k, v));
    }*/

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

    private Document find(String collection, String id) {
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("id", id);
        return db.getCollection(collection).find(whereQuery).projection(fields(excludeId())).first();


    }

    private boolean exists(String collection, String id) {
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("id", id);
        return this.db.getCollection(collection).count(whereQuery) > 0;

    }

    private void insert(String collection, String id, Entity entity) throws IOException {
        this.db.getCollection(collection).insertOne(toDocument(entity));

    }

    private void update(String collection, String id, Entity entity) throws IOException {

        Document doc = toDocument(entity);
        this.db.getCollection(collection).updateOne(new Document("id", id), new Document("$set", doc));


    }

    private Document toDocument(Object object) throws IOException {
        StringWriter writer = new StringWriter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(writer, object);
        String json = writer.toString();
        return Document.parse(json);
    }

    private Person toPerson(Document doc) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(doc.toJson(), Person.class);

    }

    private Family toFamily(Document doc) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(doc.toJson(), Family.class);

    }
}
