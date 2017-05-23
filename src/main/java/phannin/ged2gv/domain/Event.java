package phannin.ged2gv.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class Event {

    private String time = "";
    private String place = "";
    protected List<String> notes = new ArrayList<>();
    private List<SourceRef> sources = new ArrayList<>();

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public List<SourceRef> getSources() {
        return sources;
    }

    @JsonIgnore
    public String getYear() {
        if (this.time.length() > 4)
            return this.time.substring(this.time.length() - 4);
        else
            return this.time;
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
