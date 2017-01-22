package phannin.ged2gv;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pasi on 11.9.2016.
 */
public interface Pedigree {
//    Map<String, Person> getPersons();
//    Map<String, Family> getFamilies();

    void addPerson(Person person);

    void addFamily(Family family);

    void addSource(Source family);

    void addNote(Note note);

    Person getPerson(String id);

    Family getFamily(String id);

    Source getSource(String id);

    default void load(String gedcomFile) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        Entity currentEntity = new Person(null);
        InputStreamReader reader = new InputStreamReader(new FileInputStream(gedcomFile), "utf-8");
        BufferedReader br = new BufferedReader(reader);
        String line;
        while ((line = br.readLine()) != null) {
            String[] tokens = line.split(" ", 4);
            if (tokens[0].equals("0") && tokens.length > 2) {
                if (tokens[2].equals("INDI")) {
                    if (currentEntity.getId() != null)
                        addPerson((Person) currentEntity);
                    currentEntity = new Person(tokens[1]);
                } else if (tokens[2].equals("FAM")) {
                    if (currentEntity instanceof Person)
                        addPerson((Person) currentEntity);
                    else
                        addFamily((Family) currentEntity);
                    currentEntity = new Family(tokens[1]);
                } else if (tokens[2].equals("SOUR")) {
                    if (currentEntity instanceof Family)
                        addFamily((Family) currentEntity);
                    else
                        addSource((Source) currentEntity);
                    currentEntity = new Source(tokens[1]);
                } else if (tokens[2].equals("NOTE")) {
                    if (currentEntity instanceof Source)
                        addSource((Source) currentEntity);
                    else
                        addNote((Note) currentEntity);
                    currentEntity = new Note(tokens[1], tokens[3]);
                }
                System.out.println(line);
//                else
//                    System.out.println(tokens[2]);
            } else if (!tokens[0].equals("0")) {
                currentEntity.addLine(line);
            }
        }
        br.close();
    }

    void dump();

}
