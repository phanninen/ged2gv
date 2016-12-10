package phannin.ged2gv;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class Family implements Entity, Comparable<Family> {
    private String id = "";
    private String husband = "";
    private String wife = "";
    private List<String> children = new ArrayList<>();

    private Event marriage;

    public Family() {
    }

    public Family(String id) {
        this.id = id;
    }


    public String getId() {
        return id;
    }


    public String getHusband() {
        return husband;
    }

    public void setHusband(String husband) {
        this.husband = husband;
    }

    public String getWife() {
        return wife;
    }

    public void setWife(String wife) {
        this.wife = wife;
    }

    public List<String> getChildren() {
        return children;
    }

    public void setChildren(List<String> children) {
        this.children = children;
    }


    public Event getMarriage() {
        return marriage;
    }


    public void setMarriage(Event marriage) {
        this.marriage = marriage;
    }


    private Event currentEvent;

    @Override
    public void addLine(String line) {
//		1 HUSB @I1415@
//		1 WIFE @I1438@
//		1 CHIL @I1548@
        String[] tokens = line.split(" ", 3);
        if (tokens[0].equals("1"))
            currentEvent = null;

        if (tokens[0].equals("1") && tokens[1].equals("HUSB")) //
            this.husband = tokens[2];
        if (tokens[0].equals("1") && tokens[1].equals("WIFE")) //
            this.wife = tokens[2];
        if (tokens[0].equals("1") && tokens[1].equals("CHIL")) //
            this.children.add(tokens[2]);
        if (tokens[0].equals("1") && tokens[1].equals("MARR")) {
            this.marriage = new Event();
            currentEvent = this.marriage;
        }
        if (tokens[0].equals("2") && tokens[1].equals("DATE")) {
            if (currentEvent != null)
                currentEvent.setTime(tokens[2]);
        }
        if (tokens[0].equals("2") && tokens[1].equals("PLAC")) {
            if (currentEvent != null)
                currentEvent.setPlace(tokens[2]);
        }


    }

    @JsonIgnore
    public String getPlainId() {
        String[] tokens = this.id.split("@");
        return tokens[1];
    }


    @JsonIgnore
    public String getYear() {
        if (this.marriage != null)
            return this.marriage.getYear();
        else
            return "";
    }

    @JsonIgnore
    public boolean hasHusbamd() {
        return this.husband != null && !this.husband.isEmpty();
    }

    @JsonIgnore
    public boolean hasWife() {
        return this.wife != null && !this.wife.isEmpty();
    }

    @Override
    public int compareTo(Family o) {
        return o.getYear().compareTo(this.getYear());
    }

}
