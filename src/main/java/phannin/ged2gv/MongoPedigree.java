package phannin.ged2gv;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import phannin.ged2gv.domain.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;

/**
 * Created by pasi on 30.11.2016.
 */
public class MongoPedigree implements Pedigree {
    private static final String PERSONS = "persons";
    private static final String FAMILIES = "families";
    private static final String SOURCES = "sources";
    private static final String NOTES = "notes";
    private final MongoDatabase db;

    public MongoPedigree(String db) {
        MongoClient mongoClient = new MongoClient();
        this.db = mongoClient.getDatabase(db);


    }

    @Override
    public Map<String, Person> getPersons() {
        return new HashMap<>();

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

    @Override
    public void addSource(Source source) {
        storeToCollection(SOURCES, source.getId(), source);

    }

    @Override
    public void addNote(Note note) {
        storeToCollection(NOTES, note.getId(), note);

    }

    @Override
    public Source getSource(String id) {
        try {
            Document doc = find(SOURCES, id);
            return doc != null ? (Source) toObject(doc, Source.class) : null;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

    @Override
    public Note getNote(String id) {
        try {
            Document doc = find(NOTES, id);
            return doc != null ? (Note) toObject(doc, Note.class) : null;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

    @Override
    public void dump() {

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

    private Object toObject(Document doc, Class clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(doc.toJson(), clazz);

    }
}
