package phannin.ged2gv;

import phannin.ged2gv.domain.Family;
import phannin.ged2gv.domain.Person;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class DotWriter {
    private PrintWriter writer;
    private ColorMapper colorMapper;

    public DotWriter(String filename) throws Exception {
        writer = new PrintWriter(filename, StandardCharsets.UTF_8.name());
        colorMapper = new ColorMapper();
    }

    public DotWriter(PrintWriter writer) {
        this.writer = writer;
        colorMapper = new ColorMapper();
    }

    protected void initWriter() {
        writer.println("digraph G {rankdir=LR;");
    }

    protected void closeWriter() {
        writer.println("}");
        writer.close();
    }

    protected void writeFamilyNode(Pedigree pedigree, Family fam) {
        Person mies = fam.hasHusband() ? pedigree.getPerson(fam.getHusband()) : new Person(null);
        writer.println("\"" + fam.getId() + "\" [shape=circle style=filled " + colorMapper.getColor(mies) + " label=\"" + fam.getYear() + "\"];");
    }

    protected void writePersonNode(Person person) {
        String color = colorMapper.getColor(person);
        writer.println("\"" + person.getId() + "\" [shape=box style=filled fontname=helvetica " + color +
                " label=\"" + person.getFirstname() + "\n" +
                person.getSurname() + "\n" +
                "(" + person.getBirthYear() + " - " + person.getDeathYear() + ")" +
                "\"];");

    }

    protected void writeFamilyToChildConnector(Family family, Person person, boolean leftToRight) {
        if (person != null) {
            if (leftToRight)
                writer.println("\"" + family.getId() + "\" -> \"" + person.getId() + "\" [weight=1 " + colorMapper.getLineColor(person) + " ];");

            else
                writer.println("\"" + person.getId() + "\" -> \"" + family.getId() + "\" [weight=1 " + colorMapper.getLineColor(person) + " ];");
        }

    }

    protected void writeParentToFamilyConnector(Person person, Family family, boolean leftToRight) {
        if (family != null && person != null) {
            if (leftToRight)

                writer.println("\"" + person.getId() + "\" -> \"" + family.getId() + "\" [" + colorMapper.getLineColor(person) + " weight=1 constraint=true];");
            else
                writer.println("\"" + family.getId() + "\" -> \"" + person.getId() + "\" [" + colorMapper.getLineColor(person) + " weight=1 constraint=true];");

        }
    }

    protected void writeRankStart() {
        writer.println("{rank = same;");
    }

    protected void writeRankEnd() {
        writer.println("}");
    }

}
