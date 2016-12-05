package phannin.ged2gv;

import java.io.*;
import java.util.*;

/**
 * Created by pasi on 11.9.2016.
 */
public class InMemoryPedigree implements Pedigree {
    private Map<String, Person> persons = new HashMap<>();
    private Map<String, Family> families = new HashMap<>();

    private Filter filter = new Filter();

    @Override
    public void addPerson(String id, Person person) {
        persons.put(id, person);
    }

    @Override
    public void addFamily(String id, Family family) {
        families.put(id, family);
    }

    @Override
    public Person getPerson(String id) {
        return persons.get(id);
    }

    @Override
    public Family getFamily(String id) {
        return families.get(id);
    }

    @Override
    public void clearFilter() {
        filter = new Filter();
    }

    public Filter filterAllAncestors(String personId) {
        Person person = persons.get(personId);

        if (person != null) { //&& generation<10
            filter.addPerson(personId);

            Family fam = families.get(person.getParentsId());
            if (fam != null) {
                filter.addFamily(fam.getId());
                filterAllAncestors(fam.getHusband());

                filterAllAncestors(fam.getWife());

            }
            return filter;
        }
        return filter;
    }

    public Filter filterWithAncestors(String personId, String[] targetPersons) {
        Person person = persons.get(personId);
/*        if (person != null && person.isKuuluuSukuun()) {
            debug(person.getFullName() + " on jo");
            return filter;
        }

        if (person != null) { //&& generation<10
            if (Arrays.asList(targetPersons).contains(personId)) {
                debug(person.getFullName() + " on H�nninen");
                person.setKuuluuSukuun(true);
            }
            Family fam = families.get(person.getParentsId());
            if (fam != null) {
                if (filterWithAncestors(fam.getHusband(), targetPersons)) {
                    person.setKuuluuSukuun(true);
                    debug(person.getFullName() + " is� on H�nnisi�");
                }
                if (filterWithAncestors(fam.getWife(), targetPersons)) {
                    debug(person.getFullName() + " �iti on H�nnisi�");
                    person.setKuuluuSukuun(true);
                }
                if (person.isKuuluuSukuun()) {
                    fam.setSelected(true);
                    Person isa = persons.get(fam.getHusband());
                    if (isa != null && !isa.isMukaan()) {
                        debug(isa.getFullName() + " lapsi on H�nnisi�");
                        isa.setOnPuoliso(true);
                    }
                    Person aiti = persons.get(fam.getWife());
                    if (aiti != null && !aiti.isMukaan()) {
                        debug(aiti.getFullName() + " lapsi on H�nnisi�");
                        aiti.setOnPuoliso(true);
                    }
                }
            }
            return person.isKuuluuSukuun();

        }
        */
        return filter;
    }

    private static void debug(String str) {
//		writer.println(str);
    }
}
