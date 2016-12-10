package phannin.ged2gv;

import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by pasi on 20.11.2016.
 */
public class PedigreeWriter {
    private PrintWriter writer;
    private ColorMapper colorMapper;

    public PedigreeWriter(String filename) throws Exception {
        writer = new PrintWriter(filename, "UTF-8");
        colorMapper = new ColorMapper();

    }

    public void writePedigree(Pedigree pedigree, Filter filter, String person) {
        writer.println("digraph G {rankdir=LR;");
        writeConnections(pedigree, filter, person, 0);
        printMap(pedigree, filter);
        printFamilyMap(pedigree, filter);
        //   printGroups(pedigree, filter);
        writer.println("}");
        writer.close();
    }

    private void writeConnections(Pedigree pedigree, Filter filter, String personId, int generation) {
        Person person = pedigree.getPerson(personId);
        if (person != null) { //&& generation<10
            Family fam = pedigree.getFamily(person.getParentsId());
            if (fam != null) {

                if (fam.hasHusbamd() && filter.containsPerson(fam.getHusband())) {
                    writeConnections(pedigree, filter, fam.getHusband(), generation + 1);
                    outputConnection(fam, pedigree.getPerson(fam.getHusband()), 10);
                }
                if (filter.containsPerson(fam.getHusband()) || filter.containsPerson(fam.getWife()))
                    outputConnection(person, fam);


                if (fam.hasWife() && filter.containsPerson(fam.getWife())) {
                    writeConnections(pedigree, filter, fam.getWife(), generation + 1);
                    outputConnection(fam, pedigree.getPerson(fam.getWife()), 10);
                }


            }
        }
    }

    private void outputConnection(Family family, Person person, Integer len) {
        if (person != null) {
            writer.println("\"" + family.getId() + "\" -> \"" + person.getId() + "\" [weight=100 " + colorMapper.getLineColor(person) + " len=" + len.toString() + " ];");
        }

    }


    private void outputConnection(Person person, Family family) {
        if (family != null) {

            writer.println("\"" + person.getId() + "\" -> \"" + family.getId() + "\" [" + colorMapper.getLineColor(person) + " weight=10 constraint=true];");
        }
    }


    private void printFamilyMap(Pedigree pedigree, Filter filter) {
        //TODO ryhmittely 10-vuotis ryhmiin {rank=same;
        List<Family> orderedFamiles =
                filter.allFamiles().stream()
                        .map(pedigree::getFamily).sorted()
                        .collect(Collectors.toList());

        String currentRank = "";
        for (Family fam : orderedFamiles) {
            String rank = fam.getYear().length() > 0 ? fam.getYear().substring(0, 3) : "";
            if (!currentRank.isEmpty() && !rank.equals(currentRank))
                writer.println("}");
            if (!rank.equals(currentRank) && !rank.isEmpty()) {
                writer.println("{rank = same;");
            }
            currentRank = rank;
            Person mies = fam.hasHusbamd() ? pedigree.getPerson(fam.getHusband()) : new Person(null);
            writer.println("\"" + fam.getId() + "\" [shape=circle style=filled " + colorMapper.getColor(mies) + " label=\"" + fam.getYear() + "\"];");

        }
        if (!currentRank.isEmpty())
            writer.println("}");

    }

    private void printMap(Pedigree pedigree, Filter filter) {
        filter.allPersons().forEach((p) -> printPerson(pedigree.getPerson(p)));

    }

    private void printGroups(Pedigree pedigree, Filter filter) {
        Map<String, Set<String>> groupMap = new HashMap<>();
        filter.allPersons().forEach((p) -> {
            Person person = pedigree.getPerson(p);
            if (person.getSurname() != null && !person.getSurname().isEmpty()) {
                if (!groupMap.containsKey(person.getSurname()))
                    groupMap.put(person.getSurname(), new HashSet<>());
                groupMap.get(person.getSurname()).add(person.getId());
            }

        });
        filter.allFamiles().forEach((f) -> {
            Family fam = pedigree.getFamily(f);
            if (fam.getHusband() != null && !fam.getHusband().isEmpty()) {
                Person h = pedigree.getPerson(fam.getHusband());
                if (h.getSurname() != null && !h.getSurname().isEmpty())
                    groupMap.get(h.getSurname()).add(f);
            }
        });

        groupMap.keySet().forEach((sn -> {
            writer.println("subgraph \"cluster_" + sn + "\" {");
            groupMap.get(sn).forEach((id) -> writer.println("\"" + id + "\";"));
            writer.println("}");
        }));
    }

    private void printPerson(Person person) {
        String color = colorMapper.getColor(person);
        writer.println("\"" + person.getId() + "\" [shape=box style=filled fontname=helvetica " + color +
                " label=\"" + person.getFirstname() + "\n" +
                person.getSurname() + "\n" +
                "(" + person.getBirthYear() + " - " + person.getDeathYear() + ")" +
                "\"];");

    }


    /*

    private static void printFamilyTree(Map<String, Family> families, Map<String, Person> persons, String personId, int generation) {
        Person person = persons.get(personId);
//		person.setSelected(true);
        if (person != null && person.isSelected()) { //&& generation<10
            Family fam = families.get(person.getParentsId());
            if (fam != null) {

                if (persons.containsKey(fam.getHusband()) && !persons.get(fam.getHusband()).isSelected()) {
                    printFamilyTree(families, persons, fam.getHusband(), generation + 1);
                }
                outputConnection(person, persons.get(fam.getHusband()));


                if (persons.containsKey(fam.getWife()) && !persons.get(fam.getWife()).isSelected()) {
                    printFamilyTree(families, persons, fam.getWife(), generation + 1);
                }
                outputConnection(person, persons.get(fam.getWife()));


            } else {
//				printPersonData(generation, person);

            }
        }
    }

    private static void printFamilyTree2(Map<String, Family> families, Map<String, Person> persons, String personId, int generation) {
        Person person = persons.get(personId);
//		person.setSelected(true);
        if (person != null && person.isSelected()) { //&& generation<10
            Family fam = families.get(person.getParentsId());
            if (fam != null) {

                if (persons.containsKey(fam.getHusband()) && (!persons.get(fam.getHusband()).isSelected() || true)) {
                    printFamilyTree2(families, persons, fam.getHusband(), generation + 1);
                }
                outputConnection(person, fam);
                outputConnection(fam, persons.get(fam.getHusband()), 1);


                if (persons.containsKey(fam.getWife()) && (!persons.get(fam.getWife()).isSelected() || true)) {
                    printFamilyTree2(families, persons, fam.getWife(), generation + 1);
                }
                outputConnection(fam, persons.get(fam.getWife()), 1);

                //			outputConnection( families.get(person.getParentsId()), persons.get(fam.getHusband()),persons.get(fam.getWife()));
                //			outputConnection( persons.get(fam.getHusband()),persons.get(fam.getWife()));

            } else {
//				printPersonData(generation, person);

            }
        }
    }

    private static void outputConnection(Family family, Person person, Integer len) {
        if (person != null && person.isSelected()) {
            writer.println("\"" + family.getId() + "\" -> \"" + person.getId() + "\" [weight=100 " + colorMapper.getLineColor(person) + " len=" + len.toString() + " ];");
            //		person.setSelected(true);
        }

    }

    private static void outputConnection(Family family, Person person) {
        if (person != null) {
            writer.println("\"" + family.getId() + "\" -> \"" + person.getId() + "\" {weight=1 len=1 " + colorMapper.getColor(person) + "};");
            //		person.setSelected(true);
        }

    }

    private static void outputConnection(Family family, Person mies, Person vaimo) {
        writer.println("subgraph cluster_" + family.getPlainId() + " {");
        writer.println("style=filled;");
        writer.println("color=lightgrey;");
        writer.println("rank=same;");
        writer.println("\"" + family.getId() + "\" -> {\"" +
                (mies != null ? mies.getId() : "") + "\" " + "\"" +
                (vaimo != null ? vaimo.getId() : "") + "\"}");
        writer.println("}");


    }



    private static void outputConnection(Person person, Person parent) {
        if (parent != null && person != null) {
            writer.println("\"" + person.getId() + "\" -> \"" + parent.getId() + "\" [dir=none constraint=false color=white len=1 weight=1000];");
//			parent.setSelected(true);
        }
    }

    private static void printPersonData(int generation, Person person) {
        for (int i = 0; i < generation; i++)
            writer.print("   ");
        if (person.getSex().equals("M"))
            writer.print("/");
        else
            writer.print("\\");
        writer.println(person.getFirstname() + " " + person.getSurname());
    }

*/

}
