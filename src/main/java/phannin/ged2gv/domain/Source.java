package phannin.ged2gv.domain;

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


    public String toString() {
        return this.id + "/" + this.title + "/" + this.author + "/" + this.publisher;
    }
}
