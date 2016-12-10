package phannin.ged2gv;

import com.fasterxml.jackson.annotation.JsonIgnore;

class Event {

    private String time = "";
    private String place = "";

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

    @JsonIgnore
    public String getYear() {
        if (this.time.length() > 4)
            return this.time.substring(this.time.length() - 4);
        else
            return this.time;
    }


}
