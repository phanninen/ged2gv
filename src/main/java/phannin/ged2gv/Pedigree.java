package phannin.ged2gv;

import phannin.ged2gv.domain.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by pasi on 11.9.2016.
 */
public interface Pedigree {
    Map<String, Person> getPersons();
//    Map<String, Family> getFamilies();

    void addPerson(Person person);

    void addFamily(Family family);

    void addSource(Source family);

    void addNote(Note note);

    Person getPerson(String id);

    Family getFamily(String id);

    Source getSource(String id);

    Note getNote(String id);

    default void load(String gedcomFile) throws IOException {

        GedcomParser parser = new GedcomParser();
        parser.parse(gedcomFile);
        for (Entity entity : parser.resultList()) {
            if (entity instanceof Person)
                addPerson((Person) entity);
            if (entity instanceof Family)
                addFamily((Family) entity);
            if (entity instanceof Source)
                addSource((Source) entity);
            if (entity instanceof Note)
                addNote((Note) entity);

        }

    }

    default List<String> getSpouse(String id) {
        Person p = getPerson(id);
        List<String> result = new ArrayList<>();
        for (String fid : p.getFamilyId()) {
            Family f = getFamily(fid);
            if (f.getHusband().equals(id))
                result.add(f.getWife());
            else {
                result.add(f.getHusband());
            }
        }
        return result;
    }

    void dump();

}
