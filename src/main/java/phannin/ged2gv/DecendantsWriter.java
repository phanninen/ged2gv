package phannin.ged2gv;

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

    public void writeDecendants(Pedigree pedigree, Filter filter, String person) {
        writer.println("digraph G {rankdir=LR;");
        writeConnections(pedigree, filter, person);
        writePersons(pedigree, filter, person);
        writeFamilies(pedigree, filter, person);
        //   printGroups(pedigree, filter);
        writer.println("}");
        writer.close();
    }

    private void writeConnections(Pedigree pedigree, Filter filter, String personId) {
        Person person = pedigree.getPerson(personId);
        System.out.println(person.toString());
        if (person != null) { //&& generation<10
            Family fam = pedigree.getFamily(person.getFamilyId());
            if (fam != null) {
                //               outputFamily(fam, pedigree);
                if (fam.getHusband().equals(personId) && fam.getWife() != null)
                    outputConnection(pedigree.getPerson(fam.getWife()), fam);
                if (fam.getWife().equals(personId) && fam.getHusband() != null)
                    outputConnection(pedigree.getPerson(fam.getHusband()), fam);
                outputConnection(person, fam);
                for (String child : fam.getChildren()) {
                    outputConnection(fam, pedigree.getPerson(child), 100);
                    writeConnections(pedigree, filter, child);
                }


            }
        }
    }

    private void writePersons(Pedigree pedigree, Filter filter, String personId) {
        Person person = pedigree.getPerson(personId);
        if (person != null) { //&& generation<10
            printPerson(person);
            Family fam = pedigree.getFamily(person.getFamilyId());
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

    private void writeFamilies(Pedigree pedigree, Filter filter, String personId) {
        Person person = pedigree.getPerson(personId);
        if (person != null) { //&& generation<10
            Family fam = pedigree.getFamily(person.getFamilyId());
            if (fam != null) {
                Person mies = fam.hasHusbamd() ? pedigree.getPerson(fam.getHusband()) : new Person(null);
                writer.println("\"" + fam.getId() + "\" [shape=circle style=filled " + colorMapper.getColor(mies) + " label=\"" + fam.getYear() + "\"];");

                for (String child : fam.getChildren()) {
                    writeFamilies(pedigree, filter, child);
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
        if (family != null && person != null) {

            writer.println("\"" + person.getId() + "\" -> \"" + family.getId() + "\" [" + colorMapper.getLineColor(person) + " weight=10 constraint=true];");
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
