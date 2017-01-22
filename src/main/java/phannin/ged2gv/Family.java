package phannin.ged2gv;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class Family implements Entity, Comparable<Family> {
    private String id = "";
    private String husband = "";
    private String wife = "";
    private List<String> children = new ArrayList<>();
    private List<String> notes = new ArrayList<>();
    private List<SourceRef> sources = new ArrayList<>();


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

    public List<String> getNotes() {
        return notes;
    }

    public List<SourceRef> getSources() {
        return sources;
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
        if (tokens[0].equals("1") && tokens[1].equals("NOTE")) // Family Note
            this.notes.add(tokens[2]);
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
            this.sources.add(source);
            currentEvent = source;
        }
        if (tokens[0].equals("2") && tokens[1].equals("NOTE")) {// Event Note
            if (currentEvent != null)
                currentEvent.getNotes().add(tokens[2]);
            else
                notes.add(tokens[2]);
        }
        if (tokens[0].equals("2") && tokens[1].equals("PAGE")) //
            ((SourceRef) currentEvent).setPage(tokens[2]);



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

    @Override
    public String toString() {
        return "Family{" +
                "id='" + id + '\'' +
                ", husband='" + husband + '\'' +
                ", wife='" + wife + '\'' +
                ", children=" + children +
                ", notes=" + notes +
                ", sources=" + sources +
                ", marriage=" + marriage +
                '}';
    }
}
