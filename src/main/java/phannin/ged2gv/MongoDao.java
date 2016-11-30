package phannin.ged2gv;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

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


        sukupuu.getPersons().forEach((k, v) -> {
            if (exists(k))
                update(k, v);
            else
                insert(k, v);

        });
    }

    private boolean exists(String id) {
        return true;
    }

    private void insert(String id, Person person) {
        this.db.getCollection("persons").insertOne(
                new Document("id", id)
                        .append("firstname", person.getFirstname())
                        .append("surname", person.getSurname())
        );

    }

    private void update(String id, Person person) {
        this.db.getCollection("persons").updateOne(
                new Document("id", id),
                new Document("$set", new Document()
                        .append("firstname", person.getFirstname())
                        .append("surname", person.getSurname())
                        .append("sex", person.getSex()))
        );
    }
}
