package phannin.ged2gv;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pasi on 8.1.2017.
 */
public class SourceRef extends Event {
    private String sourceId;
    private String page;

    public SourceRef(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }


    @Override
    public String toString() {
        return "SourceRef{" +
                "sourceId='" + sourceId + '\'' +
                ", page='" + page + '\'' +
                ", notes=" + super.notes +
                '}';
    }
}
