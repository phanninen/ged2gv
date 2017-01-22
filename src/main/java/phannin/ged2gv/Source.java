package phannin.ged2gv;

/**
 * Created by pasi on 8.1.2017.
 */
public class Source implements Entity {
    private String id;
    private String title;
    private String publisher;
    private String author;

    public Source() {
    }

    public Source(String id) {
        super();
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void addLine(String line) {
        String[] tokens = line.split(" ", 3);

        if (tokens[0].equals("1") && tokens[1].equals("TITL"))
            this.title = tokens[2];
        if (tokens[0].equals("1") && tokens[1].equals("PUBL"))
            this.publisher = tokens[2];
        if (tokens[0].equals("1") && tokens[1].equals("AUTH"))
            this.author = tokens[2];

    }

    public String toString() {
        return this.id + "/" + this.title + "/" + this.author + "/" + this.publisher;
    }
}
