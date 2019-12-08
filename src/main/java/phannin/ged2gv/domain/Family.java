package phannin.ged2gv.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class Family implements Entity, Comparable<Family> {
    private String id = "";
    private String husband = "";
    private String wife = "";
    private List<String> children = new ArrayList<>();
    private final List<String> notes = new ArrayList<>();
    private final List<SourceRef> sources = new ArrayList<>();


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


    @JsonIgnore
    public String getYear() {
        if (this.marriage != null)
            return this.marriage.getYear();
        else
            return "";
    }
    @JsonIgnore
    public String getPlainYear() {
        if (this.marriage != null)
            return this.marriage.getPlainYear();
        else
            return "";
    }

    @JsonIgnore
    public boolean hasHusband() {
        return this.husband != null && !this.husband.isEmpty();
    }

    @JsonIgnore
    public boolean hasWife() {
        return this.wife != null && !this.wife.isEmpty();
    }

    @Override
    public int compareTo(Family o) {
        return o.getPlainYear().compareTo(this.getPlainYear());
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
