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

    Person getPerson(String id);

    Family getFamily(String id);

    default void load(String gedcomFile) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        Entity currentEntity = new Person(null);
        InputStreamReader reader = new InputStreamReader(new FileInputStream(gedcomFile), "utf-8");
        BufferedReader br = new BufferedReader(reader);
        String line;
        while ((line = br.readLine()) != null) {
            String[] tokens = line.split(" ");
            if (tokens[0].equals("0") && tokens.length > 2) {
                if (tokens[2].equals("INDI")) {
                    if (currentEntity.getId() != null)
                        addPerson((Person) currentEntity);
                    currentEntity = new Person(tokens[1]);
                }
                if (tokens[2].equals("FAM")) {
                    if (currentEntity instanceof Person)
                        addPerson((Person) currentEntity);
                    else
                        addFamily((Family) currentEntity);
                    currentEntity = new Family(tokens[1]);
                }

            } else if (!tokens[0].equals("0")) {
                currentEntity.addLine(line);
            }
        }
        br.close();
    }


}
