package phannin.ged2gv;

import phannin.ged2gv.domain.Family;
import phannin.ged2gv.domain.Person;

import java.io.PrintWriter;

/**
 * Created by pasi on 22.1.2017.
 */
public class DecendantsWriter {

    private PrintWriter writer;
    private ColorMapper colorMapper;

    public DecendantsWriter(String filename) throws Exception {
        writer = new PrintWriter(filename, "UTF-8");
        colorMapper = new ColorMapper();

    }

    public DecendantsWriter(PrintWriter writer) throws Exception {
        this.writer = writer;
        colorMapper = new ColorMapper();

    }

    public void writeDecendants(Pedigree pedigree, Filter filter, String person) {
        writer.println("digraph G {rankdir=LR;");
        writeConnections(pedigree, filter, person, true);
        writePersons(pedigree, filter, person);
        writeFamilies(pedigree, filter, person);
        //   printGroups(pedigree, filter);
        writer.println("}");
        writer.close();
    }

    public void writeTree(Pedigree pedigree, Filter filter, String person) {
        writeConnections(pedigree, filter, person, false);
        writePersons(pedigree, filter, person);
        writeFamilies(pedigree, filter, person);
    }

    private void writeConnections(Pedigree pedigree, Filter filter, String personId, boolean leftToRight) {
        Person person = pedigree.getPerson(personId);
        System.out.println(person.toString());
        if (person != null) { //&& generation<10
            for (String fid : person.getFamilyId()) {
                Family fam = pedigree.getFamily(fid);
                if (fam != null) {
                    //               outputFamily(fam, pedigree);
                    if (fam.getHusband().equals(personId) && fam.getWife() != null)
                        outputConnection(pedigree.getPerson(fam.getWife()), fam, leftToRight);
                    if (fam.getWife().equals(personId) && fam.getHusband() != null)
                        outputConnection(pedigree.getPerson(fam.getHusband()), fam, leftToRight);
                    outputConnection(person, fam, leftToRight);
                    for (String child : fam.getChildren()) {
                        outputConnection(fam, pedigree.getPerson(child), leftToRight);
                        writeConnections(pedigree, filter, child, leftToRight);
                    }


                }
            }
        }
    }

    private void writePersons(Pedigree pedigree, Filter filter, String personId) {
        Person person = pedigree.getPerson(personId);
        if (person != null) { //&& generation<10
            printPerson(person);
            for (String fid : person.getFamilyId()) {
                Family fam = pedigree.getFamily(fid);
                if (fam != null) {
                    //               outputFamily(fam, pedigree);
                    if (fam.getHusband().equals(personId) && fam.getWife() != null && !fam.getWife().isEmpty())
                        printPerson(pedigree.getPerson(fam.getWife()));
                    if (fam.getWife().equals(personId) && fam.getHusband() != null && !fam.getHusband().isEmpty())
                        printPerson(pedigree.getPerson(fam.getHusband()));
                    for (String child : fam.getChildren()) {
                        writePersons(pedigree, filter, child);
                    }


                }
            }
        }
    }

    private void writePersons2(Pedigree pedigree, Filter filter, String personId) {
        Person person = pedigree.getPerson(personId);

        if (person != null) { //&& generation<10
            //           printPerson(person);
            for (String fid : person.getFamilyId()) {
                Family fam = pedigree.getFamily(fid);
                if (fam != null) {
                    //               outputFamily(fam, pedigree);
                    if (fam.getHusband().equals(personId) && fam.getWife() != null && !fam.getWife().isEmpty())
                        printPerson(pedigree.getPerson(fam.getWife()));
                    if (fam.getWife().equals(personId) && fam.getHusband() != null && !fam.getHusband().isEmpty())
                        printPerson(pedigree.getPerson(fam.getHusband()));
                    for (String childId : fam.getChildren()) {
                        Person child = pedigree.getPerson(childId);
                        printPerson(child);
                    }
                    for (String child : fam.getChildren()) {
                        writePersons2(pedigree, filter, child);
                    }


                }
            }
        }
    }


    private void writeFamilies(Pedigree pedigree, Filter filter, String personId) {
        Person person = pedigree.getPerson(personId);
        if (person != null) { //&& generation<10
            for (String fid : person.getFamilyId()) {
                Family fam = pedigree.getFamily(fid);
                if (fam != null) {
                    Person mies = fam.hasHusbamd() ? pedigree.getPerson(fam.getHusband()) : new Person(null);
                    writer.println("\"" + fam.getId() + "\" [shape=circle style=filled " + colorMapper.getColor(mies) + " label=\"" + fam.getYear() + "\"];");

                    for (String child : fam.getChildren()) {
                        writeFamilies(pedigree, filter, child);
                    }


                }
            }
        }
    }

    private void outputConnection(Family family, Person person, boolean leftToRight) {
        if (person != null) {
            if (leftToRight)
                writer.println("\"" + family.getId() + "\" -> \"" + person.getId() + "\" [weight=1 " + colorMapper.getLineColor(person) + " ];");

            else
                writer.println("\"" + person.getId() + "\" -> \"" + family.getId() + "\" [weight=1 " + colorMapper.getLineColor(person) + " ];");
        }

    }


    private void outputConnection(Person person, Family family, boolean leftToRight) {
        if (family != null && person != null) {
            if (leftToRight)

                writer.println("\"" + person.getId() + "\" -> \"" + family.getId() + "\" [" + colorMapper.getLineColor(person) + " weight=1 constraint=true];");
            else
                writer.println("\"" + family.getId() + "\" -> \"" + person.getId() + "\" [" + colorMapper.getLineColor(person) + " weight=1 constraint=true];");

        }
    }

    private void printPerson(Person person) {
        String color = colorMapper.getColor(person);
        writer.println("\"" + person.getId() + "\" [shape=box style=filled fontname=helvetica " + color +
                " label=\"" + person.getFirstname() + "\n" +
                person.getSurname() + "\n" +
                "(" + person.getBirthYear() + " - " + person.getDeathYear() + ")" +
                "\"];");

    }

}
