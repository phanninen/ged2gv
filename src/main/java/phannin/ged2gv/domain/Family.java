package phannin.ged2gv.domain;

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
