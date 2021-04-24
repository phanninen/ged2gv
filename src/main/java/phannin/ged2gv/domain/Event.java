package phannin.ged2gv.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class Event {

    private String time = "";
    private String place = "";
    protected final List<String> notes = new ArrayList<>();
    private final List<SourceRef> sources = new ArrayList<>();

    public void setTime(String time) {
        this.time = time;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public List<SourceRef> getSources() {
        return sources;
    }

    @JsonIgnore
    public String getYear() {
        if (this.time.contains("jälkeen")) {
            return ">"+this.time.substring(0,4);
        }
        if (this.time.length() > 4)
            return getPrefix(this.time)+this.time.substring(this.time.length() - 4);
        else
            return this.time;
    }
    @JsonIgnore
    public String getPlainYear() {
        if (this.time.contains("jälkeen")) {
            return ">"+this.time.substring(0,4);
        }
        if (this.time.length() > 4)
            return this.time.substring(this.time.length() - 4);
        else
            return this.time;
    }

    private String getPrefix(String time) {
        if (time.contains("AFT")) return ">";
        if (time.contains("BEF")) return "<";
        if (time.contains("ABT")) return("~");
        return "";
    }

    public List<String> getNotes() {
        return notes;
    }

    @Override
    public String toString() {
        return "Event{" +
                "time='" + time + '\'' +
                ", place='" + place + '\'' +
                ", notes=" + notes +
                ", sources=" + sources +
                '}';
    }
}
