package phannin.ged2gv;

import phannin.ged2gv.domain.Family;
import phannin.ged2gv.domain.Person;

import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by pasi on 20.11.2016.
 */
public class PedigreeWriter {
    private final PrintWriter writer;
    private final ColorMapper colorMapper;

    public PedigreeWriter(String filename) throws Exception {
        writer = new PrintWriter(filename, "UTF-8");
        colorMapper = new ColorMapper();

    }

    public PedigreeWriter(PrintWriter writer) throws Exception {
        this.writer = writer;
        colorMapper = new ColorMapper();

    }

    public void writePedigree(Pedigree pedigree, Filter filter, String person) {
        writer.println("digraph G {rankdir=LR;");
        writeTree(pedigree, filter, person);
        //   printGroups(pedigree, filter);
        writer.println("}");
        writer.close();
    }

    public void writeTree(Pedigree pedigree, Filter filter, String person) {
        writeConnections(pedigree, filter, person, 0);
        printMap(pedigree, filter);
        printFamilyMap(pedigree, filter);
    }

    private void writeConnections(Pedigree pedigree, Filter filter, String personId, int generation) {
        Person person = pedigree.getPerson(personId);
        if (person != null) { //&& generation<10
            Family fam = pedigree.getFamily(person.getParentsId());
            if (fam != null) {
                //outputFamily(fam, pedigree);
                if (fam.hasHusbamd() && filter.containsPerson(fam.getHusband())) {
                    writeConnections(pedigree, filter, fam.getHusband(), generation + 1);
                    outputConnection(fam, pedigree.getPerson(fam.getHusband()));
                }
                if (filter.containsPerson(fam.getHusband()) || filter.containsPerson(fam.getWife())) {
                    outputConnection(person, fam);
                }
                if (filter.containsPerson(fam.getHusband()) && filter.containsPerson(fam.getWife())) {
                    outputConnection(pedigree.getPerson(fam.getHusband()), pedigree.getPerson(fam.getWife()));
                }

                if (fam.hasWife() && filter.containsPerson(fam.getWife())) {
                    writeConnections(pedigree, filter, fam.getWife(), generation + 1);
                    outputConnection(fam, pedigree.getPerson(fam.getWife()));
                }


            }
        }
    }

    private void outputConnection(Family family, Person person) {
        if (person != null) {
            writer.println("\"" + family.getId() + "\" -> \"" + person.getId() + "\" [weight=1 " + colorMapper.getLineColor(person) + " ];");
        }

    }


    private void outputConnection(Person person, Family family) {
        if (family != null) {

            writer.println("\"" + person.getId() + "\" -> \"" + family.getId() + "\" [" + colorMapper.getLineColor(person) + " weight=1 constraint=true];");
        }
    }

    private void outputConnection(Person person, Person person2) {
        if (person != null && person2 != null) {

            //   writer.println("\"" + person.getId() + "\" -> \"" + person2.getId() + "\" ["  + " weight=1 constraint=false];");
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

    private void outputFamily(Family fam, Pedigree pedigree) {
        System.out.println("-------------------");
        Person mies = pedigree.getPerson(fam.getHusband());
        if (mies != null)
            System.out.println("Mies: " + mies.getFirstname() + " " + mies.getSurname());
        Person nainen = pedigree.getPerson(fam.getWife());
        if (nainen != null)
            System.out.println("Vaimo: " + nainen.getFirstname() + " " + nainen.getSurname());
    }


}
