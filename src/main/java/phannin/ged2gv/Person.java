package phannin.ged2gv;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class Person implements Entity {

    private String id = "";
    private String surname = "";
    private String firstname = "";
    private String sex = "";
    private String familyId = "";
    private String parentsId;
    private Event birth;
    private Event death;
    private List<String> notes = new ArrayList<>();
    private List<SourceRef> sources = new ArrayList<>();


    public Person() {
    }

    public Person(String id) {
        super();
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getParentsId() {
        return parentsId;
    }

    public void setParentsId(String parentsId) {
        this.parentsId = parentsId;
    }


    public Event getBirth() {
        return birth;
    }

    public void setBirth(Event birth) {
        this.birth = birth;
    }

    public Event getDeath() {
        return death;
    }

    public void setDeath(Event death) {
        this.death = death;
    }
/*

    @JsonIgnore
    public boolean isMukaan() {
        return this.kuuluuSukuun || this.onPuoliso;
    }
//	public void setSelected(boolean mukaan) {
//		this.mukaan = mukaan;
//	}
*/

    private Event currentEvent;

    public void addLine(String line) {
        String[] tokens = line.split(" ", 3);

        if (tokens[0].equals("1"))
            currentEvent = null;

        if (tokens[0].equals("2") && tokens[1].equals("GIVN")) //		2 GIVN Basilius Jaakonpoika
            this.firstname = tokens[2];
        if (tokens[0].equals("2") && tokens[1].equals("SURN") && this.surname.length() == 0) //		2 SURN Hänninen
            this.surname = tokens[2];
        if (tokens[0].equals("1") && tokens[1].equals("SEX")) //		1 SEX M
            this.sex = tokens[2];
        if (tokens[0].equals("1") && tokens[1].equals("FAMC")) //
            this.parentsId = tokens[2];
        if (tokens[0].equals("1") && tokens[1].equals("FAMS")) //
            this.familyId = tokens[2];
        if (tokens[0].equals("1") && tokens[1].equals("NOTE")) //
            this.notes.add(tokens[2]);
        if (tokens[0].equals("1") && tokens[1].equals("BIRT")) {
            this.birth = new Event();
            currentEvent = this.birth;
        }
        if (tokens[0].equals("1") && tokens[1].equals("DEAT")) {
            this.death = new Event();
            currentEvent = this.death;
        }
        if (tokens[0].equals("1") && tokens[1].equals("SOUR")) {
            SourceRef source = new SourceRef(tokens[2]);
            this.sources.add(source);
            currentEvent = source;
        }

        if (tokens[0].equals("2") && tokens[1].equals("DATE")) {
            if (currentEvent != null)
                currentEvent.setTime(tokens[2]);
        }
        if (tokens[0].equals("2") && tokens[1].equals("PLAC")) {
            if (currentEvent != null)
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
                notes.add(tokens[2]);
        }
        if (tokens[0].equals("2") && tokens[1].equals("PAGE")) //
            ((SourceRef) currentEvent).setPage(tokens[2]);

    }


    @JsonIgnore
    public String getBirthYear() {
        if (this.birth != null)
            return this.birth.getYear();
        else
            return "";
    }

    @JsonIgnore
    public String getDeathYear() {
        if (this.death != null)
            return this.death.getYear();
        else
            return "";
    }

    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", surname='" + surname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", sex='" + sex + '\'' +
                ", familyId='" + familyId + '\'' +
                ", parentsId='" + parentsId + '\'' +
                ", birth=" + birth +
                ", death=" + death +
                ", notes=" + notes +
                ", sources=" + sources +
                '}';
    }
}
