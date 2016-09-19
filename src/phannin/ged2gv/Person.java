package phannin.ged2gv;

import phannin.ged2gv.Entity;
import phannin.ged2gv.Event;

public class Person implements Entity {

    private String id = "";
    private String surname = "";
    private String firstname = "";
    private String sex = "";
    private String perheId = "";
    private String vanhemmatId;
    private Event syntyma;
    private Event kuolema;
    private boolean kuuluuSukuun = false;
    private boolean onPuoliso = false;


    public boolean isOnPuoliso() {
        return onPuoliso;
    }

    public void setOnPuoliso(boolean onPuoliso) {
        this.onPuoliso = onPuoliso;
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

    public String getPerheId() {
        return perheId;
    }

    public void setPerheId(String perheId) {
        this.perheId = perheId;
    }

    public String getVanhemmatId() {
        return vanhemmatId;
    }

    public void setVanhemmatId(String vanhemmatId) {
        this.vanhemmatId = vanhemmatId;
    }


    public Event getSyntyma() {
        return syntyma;
    }

    public void setSyntyma(Event syntyma) {
        this.syntyma = syntyma;
    }

    public Event getKuolema() {
        return kuolema;
    }

    public void setKuolema(Event kuolema) {
        this.kuolema = kuolema;
    }

    public boolean isMukaan() {
        return this.kuuluuSukuun || this.onPuoliso;
    }
//	public void setMukaan(boolean mukaan) {
//		this.mukaan = mukaan;
//	}

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
            this.vanhemmatId = tokens[2];
        if (tokens[0].equals("1") && tokens[1].equals("FAMS")) //
            this.perheId = tokens[2];
        if (tokens[0].equals("1") && tokens[1].equals("BIRT")) {
            this.syntyma = new Event();
            currentEvent = this.syntyma;
        }
        if (tokens[0].equals("1") && tokens[1].equals("DEAT")) {
            this.kuolema = new Event();
            currentEvent = this.kuolema;
        }
        if (tokens[0].equals("2") && tokens[1].equals("DATE")) {
            if (currentEvent != null)
                currentEvent.setTime(tokens[2]);
        }
    }


    public String getBirthYear() {
        if (this.syntyma != null)
            return this.syntyma.getYear();
        else
            return "";
    }

    public String getDeathYear() {
        if (this.kuolema != null)
            return this.kuolema.getYear();
        else
            return "";
    }

    public String getFullName() {
        return this.firstname + " " + this.surname;
    }

    public boolean isKuuluuSukuun() {
        return kuuluuSukuun;
    }

    public void setKuuluuSukuun(boolean kuuluuSukuun) {
        this.kuuluuSukuun = kuuluuSukuun;
    }
}
