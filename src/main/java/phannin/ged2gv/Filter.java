package phannin.ged2gv;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by pasi on 5.12.2016.
 */
public class Filter {
    private Set<String> persons = new HashSet<>();
    private Set<String> families = new HashSet<>();

    public void addPerson(String id) {
        if (id != null && !id.isEmpty())
            persons.add(id);
    }

    public void addFamily(String id) {
        if (id != null && !id.isEmpty())
            families.add(id);

    }

    public boolean containsPerson(String id) {
        return persons.contains(id);
    }

    public boolean containsFamily(String id) {
        return families.contains(id);
    }

    public Set<String> allPersons() {
        return persons;
    }

    public Set<String> allFamiles() {
        return families;
    }

    public static Filter factory() {
        return new Filter();
    }

    public Filter allAncestors(String personId, Pedigree pedigree) {

        Person person = pedigree.getPerson(personId);

        if (person != null) { //&& generation<10
            addPerson(personId);

            Family fam = pedigree.getFamily(person.getParentsId());
            if (fam != null) {
                addFamily(fam.getId());
                allAncestors(fam.getHusband(), pedigree);

                allAncestors(fam.getWife(), pedigree);

            }
        }
        return this;
    }

    public Filter forAncestors(String personId, String[] targetPersons, Pedigree pedigree) {
        Person person = pedigree.getPerson(personId);
        if (person != null && containsPerson(person.getId())) {
            debug(person.getId() + " on jo");
            return this;
        }

        if (person != null) { //&& generation<10
            if (Arrays.asList(targetPersons).contains(personId)) {
                debug(person.getId() + " selected");
                addPerson(personId);
            }
            Family fam = pedigree.getFamily(person.getParentsId());
            if (fam != null) {
                if (forAncestors(fam.getHusband(), targetPersons, pedigree).containsPerson(fam.getHusband())) {
                    addPerson(personId);
                    debug(person.getId() + " isä kuuluu joukkoon");
                }
                if (forAncestors(fam.getWife(), targetPersons, pedigree).containsPerson(fam.getWife())) {
                    debug(person.getId() + " äiti kuuluu joukkoon");
                    addPerson(personId);
                }
                if (containsPerson(personId)) {
                    addFamily(fam.getId());
                    addPerson(fam.getHusband());
                    addPerson(fam.getWife());
                }
            }
            return this;

        }

        return this;
    }


    private static void debug(String str) {

        //System.out.println(str);
    }
}
