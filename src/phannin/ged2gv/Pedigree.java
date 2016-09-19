package phannin.ged2gv;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pasi on 11.9.2016.
 */
class Pedigree {
    private Map<String, Person> persons = new HashMap<String, Person>();
    private Map<String, Family> families = new HashMap<String, Family>();

    public Map<String, Person> getPersons() {
        return persons;
    }

    public void setPersons(Map<String, Person> persons) {
        this.persons = persons;
    }

    public Map<String, Family> getFamilies() {
        return families;
    }

    public void setFamilies(Map<String, Family> families) {
        this.families = families;
    }

    public void load(String gedcomFile)  throws FileNotFoundException, UnsupportedEncodingException,IOException{
        Entity currentEntity =new Person(null);
        InputStreamReader reader= new InputStreamReader(new FileInputStream(gedcomFile), "utf-8");
//		InputStreamReader reader= new InputStreamReader(new FileInputStream("e:\\pasi\\workspace\\javagedcom\\data\\tommiska.ged"), "utf-8");
        BufferedReader br = new BufferedReader(reader);
        String line;
        while ((line = br.readLine()) != null) {
            String[] tokens=line.split(" ");
            if (tokens[0].equals("0") && tokens.length>2) {
                if (tokens[2].equals("INDI")) {
                    currentEntity=new Person(tokens[1]);
                    persons.put(tokens[1], (Person)currentEntity);
                }
                if (tokens[2].equals("FAM")) {
                    currentEntity=new Family(tokens[1]);
                    families.put(tokens[1], (Family)currentEntity);
                }

            }
            else if (!tokens[0].equals("0")) {
                currentEntity.addLine(line);
            }
        }
        br.close();
    }
}
