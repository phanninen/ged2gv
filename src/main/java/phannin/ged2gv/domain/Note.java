package phannin.ged2gv.domain;

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

    @Override
    public void addLine(String line) {
        String[] tokens = line.split(" ", 3);

        if (tokens[0].equals("1") && tokens[1].equals("CONC"))
            this.teksti.append(tokens[2]);
        if (tokens[0].equals("1") && tokens[1].equals("CONT"))
            this.teksti.append("\n").append(tokens[2]);

    }

    @Override
    public String getId() {
        return id;
    }


    public String getTeksti() {
        return teksti.toString();
    }


    @Override
    public String toString() {
        return "Note{" +
                "id='" + id + '\'' +
                ", teksti='" + teksti + '\'' +
                '}';
    }
}
