package phannin.ged2gv;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by pasi on 5.12.2016.
 */
public class Filter {
    private Set<String> persons = new HashSet<>();
    private Set<String> families = new HashSet<>();

    public void addPerson(String id) {
        persons.add(id);
    }

    public void addFamily(String id) {
        families.add(id);

    }

    public boolean containsPerson(String id) {
        return true;
    }

    public boolean containsFamily(String Id) {
        return true;
    }

    public Set<String> allPersons() {
        return persons;
    }

    public Set<String> allFamiles() {
        return families;
    }
}
