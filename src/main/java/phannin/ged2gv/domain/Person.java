package phannin.ged2gv.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class Person implements Entity, Comparable {

    private String id = "";
    private String surname = "";
    private String firstname = "";
    private String sex = "";
    private List<String> familyId = new ArrayList<>();
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

    public List<String> getFamilyId() {
        return familyId;
    }

    public void setFamilyId(List<String> familyId) {
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

    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }

    public List<SourceRef> getSources() {
        return sources;
    }

    public void setSources(List<SourceRef> sources) {
        this.sources = sources;
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

    @JsonIgnore
    public String getFullName() {
        return this.surname.isEmpty() ? this.firstname : this.surname + " " + this.firstname;
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

    @Override
    public int compareTo(Object o) {
        Person other = (Person) o;

        return (this.surname + " " + this.firstname + this.id).compareTo(other.surname + " " + other.firstname + other.id);
    }
}
