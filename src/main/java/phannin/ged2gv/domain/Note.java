package phannin.ged2gv.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by pasi on 8.1.2017.
 */
public class Note implements Entity {
    private String id;
    private StringBuffer teksti = new StringBuffer("");

    public Note() {

    }

    public Note(String id, String teksti) {
        this.id = id;
        this.teksti.append(teksti);
    }

    public void setTeksti(StringBuffer teksti) {
        this.teksti = teksti;
    }


    @Override
    public String getId() {
        return id;
    }


    public String getTeksti() {
        return teksti.toString();
    }

    @JsonIgnore
    public StringBuffer getTekstiBuffer() {
        return teksti;
    }


    @Override
    public String toString() {
        return "Note{" +
                "id='" + id + '\'' +
                ", teksti='" + teksti + '\'' +
                '}';
    }
}
