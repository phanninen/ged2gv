package phannin.ged2gv;

import java.io.*;
import java.util.*;

/**
 * Created by pasi on 11.9.2016.
 */
public class InMemoryPedigree implements Pedigree {
    private Map<String, Person> persons = new HashMap<>();
    private Map<String, Family> families = new HashMap<>();


    @Override
    public void addPerson(Person person) {
        persons.put(person.getId(), person);
    }

    @Override
    public void addFamily(Family family) {
        families.put(family.getId(), family);
    }

    @Override
    public Person getPerson(String id) {
        return persons.get(id);
    }

    @Override
    public Family getFamily(String id) {
        return families.get(id);
    }





    private static void debug(String str) {
//		writer.println(str);
    }
}
