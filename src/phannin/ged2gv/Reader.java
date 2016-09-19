package phannin.ged2gv;

import phannin.ged2gv.Entity;
import phannin.ged2gv.Person;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


public class Reader {

    static PrintWriter writer;
    static ColorMapper colorMapper = new ColorMapper();

    public static void main(String[] args) throws Exception {

        Pedigree sukupuu = new Pedigree();
        sukupuu.load("data/puujalka.ged");

        Map<String, Person> persons = sukupuu.getPersons();
        Map<String, Family> families = sukupuu.getFamilies();


//		printMap(persons);
//		printFamilyMap(families, persons);
//		filterPersons(persons, families, "@I2@");
//		filterAllPersons(persons, families, "@I5@");
        String person = "@I0047@";
        filterAllPersons(persons, families, person);
        writer = new PrintWriter("results/pedigree.dot", "UTF-8");
        writer.println("digraph G {rankdir=LR;");
        printFamilyTree2(families, persons, person, 0);
        printMap(persons);
        printFamilyMap(families, persons);
        writer.println("}");
        writer.close();

    }

    private static boolean filterAllPersons(Map<String, Person> persons, Map<String, Family> families, String personId) {
        Person person = persons.get(personId);

        if (person != null) { //&& generation<10
            person.setKuuluuSukuun(true);

            Family fam = families.get(person.getVanhemmatId());
            if (fam != null) {
                fam.setMukaan(true);
                filterAllPersons(persons, families, fam.getMies());

                filterAllPersons(persons, families, fam.getVaimo());

            }
            return true;
        }
        return false;
    }

    private static boolean filterPersons(Map<String, Person> persons, Map<String, Family> families, String personId) {
        Person person = persons.get(personId);
        if (person != null && person.isKuuluuSukuun()) {
            debug(person.getFullName() + " on jo");
            return true;
        }
        if (person != null) { //&& generation<10
            if (person.getSurname().startsWith("Stu")) {
                debug(person.getFullName() + " on H�nninen");
                person.setKuuluuSukuun(true);
            }
            Family fam = families.get(person.getVanhemmatId());
            if (fam != null) {
                if (filterPersons(persons, families, fam.getMies())) {
                    person.setKuuluuSukuun(true);
                    debug(person.getFullName() + " is� on H�nnisi�");
                }
                if (filterPersons(persons, families, fam.getVaimo())) {
                    debug(person.getFullName() + " �iti on H�nnisi�");
                    person.setKuuluuSukuun(true);
                }
                if (person.isKuuluuSukuun()) {
                    fam.setMukaan(true);
                    Person isa = persons.get(fam.getMies());
                    if (isa != null && !isa.isMukaan()) {
                        debug(isa.getFullName() + " lapsi on H�nnisi�");
                        isa.setOnPuoliso(true);
                    }
                    Person aiti = persons.get(fam.getVaimo());
                    if (aiti != null && !aiti.isMukaan()) {
                        debug(aiti.getFullName() + " lapsi on H�nnisi�");
                        aiti.setOnPuoliso(true);
                    }
                }
            }
            return person.isKuuluuSukuun();
        }
        return false;
    }

    private static void debug(String str) {
//		writer.println(str);
    }

    private static void printFamilyTree(Map<String, Family> families, Map<String, Person> persons, String personId, int generation) {
        Person person = persons.get(personId);
//		person.setMukaan(true);
        if (person != null && person.isMukaan()) { //&& generation<10
            Family fam = families.get(person.getVanhemmatId());
            if (fam != null) {

                if (persons.containsKey(fam.getMies()) && !persons.get(fam.getMies()).isMukaan()) {
                    printFamilyTree(families, persons, fam.getMies(), generation + 1);
                }
                outputConnection(person, persons.get(fam.getMies()));


                if (persons.containsKey(fam.getVaimo()) && !persons.get(fam.getVaimo()).isMukaan()) {
                    printFamilyTree(families, persons, fam.getVaimo(), generation + 1);
                }
                outputConnection(person, persons.get(fam.getVaimo()));


            } else {
//				printPersonData(generation, person);

            }
        }
    }

    private static void printFamilyTree2(Map<String, Family> families, Map<String, Person> persons, String personId, int generation) {
        Person person = persons.get(personId);
//		person.setMukaan(true);
        if (person != null && person.isMukaan()) { //&& generation<10
            Family fam = families.get(person.getVanhemmatId());
            if (fam != null) {

                if (persons.containsKey(fam.getMies()) && (!persons.get(fam.getMies()).isMukaan() || true)) {
                    printFamilyTree2(families, persons, fam.getMies(), generation + 1);
                }
                outputConnection(person, fam);
                outputConnection(fam, persons.get(fam.getMies()), 1);


                if (persons.containsKey(fam.getVaimo()) && (!persons.get(fam.getVaimo()).isMukaan() || true)) {
                    printFamilyTree2(families, persons, fam.getVaimo(), generation + 1);
                }
                outputConnection(fam, persons.get(fam.getVaimo()), 1);

                //			outputConnection( families.get(person.getVanhemmatId()), persons.get(fam.getMies()),persons.get(fam.getVaimo()));
                //			outputConnection( persons.get(fam.getMies()),persons.get(fam.getVaimo()));

            } else {
//				printPersonData(generation, person);

            }
        }
    }

    private static void outputConnection(Family family, Person person, Integer len) {
        if (person != null && person.isMukaan()) {
            writer.println("\"" + family.getId() + "\" -> \"" + person.getId() + "\" [weight=100 " + colorMapper.getLineColor(person) + " len=" + len.toString() + " ];");
            //		person.setMukaan(true);
        }

    }

    private static void outputConnection(Family family, Person person) {
        if (person != null) {
            writer.println("\"" + family.getId() + "\" -> \"" + person.getId() + "\" {weight=1 len=1 " + colorMapper.getColor(person) + "};");
            //		person.setMukaan(true);
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

    private static void outputConnection(Person person, Family family) {
        if (family != null && family.isMukaan()) {

            writer.println("\"" + person.getId() + "\" -> \"" + family.getId() + "\" [" + colorMapper.getLineColor(person) + "];");
//			family.setMukaan(true);
        }
    }

    private static void outputConnection(Person person, Person parent) {
        if (parent != null && person != null) {
            writer.println("\"" + person.getId() + "\" -> \"" + parent.getId() + "\" [dir=none constraint=false color=white len=1 weight=1000];");
//			parent.setMukaan(true);
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

    private static void printFamilyMap(Map<String, Family> families, Map<String, Person> persons) {
        //TODO ryhmittely 10-vuotis ryhmiin {rank=same;
        List<Family> orderedFamiles = new ArrayList<Family>(families.values());
        Collections.sort(orderedFamiles);
        String currentRank = "";
        for (Family fam : orderedFamiles)
            if (fam.isMukaan()) {
                String rank = fam.getYear().length() > 0 ? fam.getYear().substring(0, 3) : "";
                if (!currentRank.isEmpty() && !rank.equals(currentRank))
                    writer.println("}");
                if (!rank.equals(currentRank) && !rank.isEmpty()) {
                    writer.println("{rank = same;");
                }
                currentRank = rank;
                Person mies = persons.containsKey(fam.getMies()) ? persons.get(fam.getMies()) : new Person(null);
                writer.println("\"" + fam.getId() + "\" [shape=circle style=filled " + colorMapper.getColor(mies) + " label=\"" + fam.getYear() + "\"];");

            }
        if (!currentRank.isEmpty())
            writer.println("}");
    }

    private static void printMap(Map<String, Person> persons) {
        for (Person person : persons.values()) {
            if (person.isMukaan()) {
                String color = colorMapper.getColor(person);
                writer.println("\"" + person.getId() + "\" [shape=box style=filled fontname=helvetica " + color +
                        " label=\"" + person.getFirstname() + "\n" +
                        person.getSurname() + "\n" +
                        "(" + person.getBirthYear() + " - " + person.getDeathYear() + ")" +
                        "\"];");
            }

        }

    }


}
