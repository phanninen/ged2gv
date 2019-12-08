package phannin.ged2gv;

import phannin.ged2gv.domain.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pasi on 23.5.2017.
 */
public class GedcomParser {
    private Entity currentEntity = new Person(null);
    private List<Entity> resultList;


    public List<Entity> resultList() {
        return this.resultList;
    }

    public void parse(String gedcomFile) throws IOException {

        resultList = new ArrayList<>();
        InputStreamReader reader = new InputStreamReader(new FileInputStream(gedcomFile), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(reader);
        String line;
        while ((line = br.readLine()) != null) {
            String[] tokens = line.split(" ", 4);
            if (tokens[0].equals("0") && tokens.length > 2) {
                if (tokens[2].equals("INDI")) {
                    if (currentEntity.getId() != null)
                        resultList.add(currentEntity);
                    currentEntity = new Person(tokens[1]);
                } else if (tokens[2].equals("FAM")) {
                    resultList.add(currentEntity);
                    currentEntity = new Family(tokens[1]);
                } else if (tokens[2].equals("SOUR")) {
                    resultList.add(currentEntity);
                    currentEntity = new Source(tokens[1]);
                } else if (tokens[2].equals("NOTE")) {
                    resultList.add(currentEntity);
                    currentEntity = new Note(tokens[1], tokens[3]);
                }
//                System.out.println(line);
//                else
//                    System.out.println(tokens[2]);
            } else if (!tokens[0].equals("0")) {
//                currentEntity.addLine(line);
                if (currentEntity instanceof Person)
                    addPersonLine((Person) currentEntity, line);
                if (currentEntity instanceof Family)
                    addFamilyLine((Family) currentEntity, line);
                if (currentEntity instanceof Source)
                    addSourceLine((Source) currentEntity, line);
                if (currentEntity instanceof Note)
                    addNoteLine((Note) currentEntity, line);
            }
        }
        br.close();
    }

    private Event currentEvent;

    private void addPersonLine(Person person, String line) {
        String[] tokens = line.split(" ", 3);

        if (tokens[0].equals("1"))
            currentEvent = null;

        if (tokens[0].equals("2") && tokens[1].equals("GIVN")) //		2 GIVN Basilius Jaakonpoika
            person.setFirstname(tokens[2]);
        if (tokens[0].equals("2") && tokens[1].equals("NSFX")) //       2 NSFX Heikinpoika
            person.setPatronym(tokens[2]);
        if (tokens[0].equals("2") && tokens[1].equals("SURN") && person.getSurname().length() == 0) //		2 SURN Hänninen
            person.setSurname(tokens[2]);
        if (tokens[0].equals("1") && tokens[1].equals("SEX")) //		1 SEX M
            person.setSex(tokens[2]);
        if (tokens[0].equals("1") && tokens[1].equals("FAMC")) //
            person.setParentsId(tokens[2]);
        if (tokens[0].equals("1") && tokens[1].equals("FAMS")) //
            person.getFamilyId().add(tokens[2]);
        if (tokens[0].equals("1") && tokens[1].equals("NOTE")) //
            person.getNotes().add(tokens[2]);
        if (tokens[0].equals("1") && tokens[1].equals("BIRT")) {
            person.setBirth(new Event());
            currentEvent = person.getBirth();
        }
        if (tokens[0].equals("1") && tokens[1].equals("DEAT")) {
            person.setDeath(new Event());
            currentEvent = person.getDeath();
        }
        if (tokens[0].equals("1") && tokens[1].equals("SOUR")) {
            SourceRef source = new SourceRef(tokens[2]);
            person.getSources().add(source);
            currentEvent = source;
        }

        if (tokens[0].equals("2") && tokens[1].equals("DATE")) {
            if (currentEvent != null)
                currentEvent.setTime(tokens[2]);
        }
        if (tokens[0].equals("2") && tokens[1].equals("PLAC")) {
            if (currentEvent != null && tokens.length > 2)
                currentEvent.setPlace(tokens[2]);
        }
        if (tokens[0].equals("2") && tokens[1].equals("SOUR")) {
            if (currentEvent != null) {
                SourceRef source = new SourceRef(tokens[2]);
                currentEvent.getSources().add(source);

            }
        }
        if (tokens[0].equals("3") && tokens[1].equals("PAGE")) //
            if (currentEvent != null)
                currentEvent.getSources().get(currentEvent.getSources().size() - 1).setPage(tokens[2]);
        if (tokens[0].equals("3") && tokens[1].equals("NOTE")) //
            if (currentEvent != null) {
                if (currentEvent.getSources().isEmpty())
                    currentEvent.getNotes().add(tokens[2]);
                else
                    currentEvent.getSources().get(currentEvent.getSources().size() - 1).getNotes().add(tokens[2]);
            }
        if (tokens[0].equals("2") && tokens[1].equals("NOTE")) {//
            if (currentEvent != null)
                currentEvent.getNotes().add(tokens[2]);
            else
                person.getNotes().add(tokens[2]);
        }
        if (tokens[0].equals("2") && tokens[1].equals("PAGE")) //
            ((SourceRef) currentEvent).setPage(tokens[2]);

    }

    private void addFamilyLine(Family family, String line) {
        String[] tokens = line.split(" ", 3);
        if (tokens[0].equals("1"))
            currentEvent = null;

        if (tokens[0].equals("1") && tokens[1].equals("HUSB")) //
            family.setHusband(tokens[2]);
        if (tokens[0].equals("1") && tokens[1].equals("WIFE")) //
            family.setWife(tokens[2]);
        if (tokens[0].equals("1") && tokens[1].equals("CHIL")) //
            family.getChildren().add(tokens[2]);
        if (tokens[0].equals("1") && tokens[1].equals("NOTE")) // Family Note
            family.getNotes().add(tokens[2]);
        if (tokens[0].equals("1") && tokens[1].equals("MARR")) {
            family.setMarriage(new Event());
            currentEvent = family.getMarriage();
        }
        if (tokens[0].equals("2") && tokens[1].equals("DATE")) {
            if (currentEvent != null)
                currentEvent.setTime(tokens[2]);
        }
        if (tokens[0].equals("2") && tokens[1].equals("PLAC")) {
            if (currentEvent != null)
                currentEvent.setPlace(tokens[2]);
        }
        if (tokens[0].equals("2") && tokens[1].equals("SOUR")) { // Event Source
            if (currentEvent != null) {
                SourceRef source = new SourceRef(tokens[2]);
                currentEvent.getSources().add(source);

            }
        }
        if (tokens[0].equals("3") && tokens[1].equals("NOTE")) // Event Source Note
            if (currentEvent != null)
                currentEvent.getSources().get(currentEvent.getSources().size() - 1).getNotes().add(tokens[2]);
        if (tokens[0].equals("3") && tokens[1].equals("PAGE")) // Event Source Page
            if (currentEvent != null)
                currentEvent.getSources().get(currentEvent.getSources().size() - 1).setPage(tokens[2]);

        if (tokens[0].equals("1") && tokens[1].equals("SOUR")) { //Family Source
            SourceRef source = new SourceRef(tokens[2]);
            family.getSources().add(source);
            currentEvent = source;
        }
        if (tokens[0].equals("2") && tokens[1].equals("NOTE")) {// Event Note
            if (currentEvent != null)
                currentEvent.getNotes().add(tokens[2]);
            else
                family.getNotes().add(tokens[2]);
        }
        if (tokens[0].equals("2") && tokens[1].equals("PAGE")) //
            ((SourceRef) currentEvent).setPage(tokens[2]);


    }

    private void addSourceLine(Source source, String line) {
        String[] tokens = line.split(" ", 3);

        if (tokens[0].equals("1") && tokens[1].equals("TITL"))
            source.setTitle(tokens[2]);
        if (tokens[0].equals("1") && tokens[1].equals("PUBL"))
            source.setPublisher(tokens[2]);
        if (tokens[0].equals("1") && tokens[1].equals("AUTH"))
            source.setAuthor(tokens[2]);
    }

    private void addNoteLine(Note note, String line) {
        String[] tokens = line.split(" ", 3);

        if (tokens[0].equals("1") && tokens[1].equals("CONC"))
            note.getTekstiBuffer().append(tokens[2]);
        if (tokens[0].equals("1") && tokens[1].equals("CONT"))
            note.getTekstiBuffer().append("\n").append(tokens[2]);

    }
}
