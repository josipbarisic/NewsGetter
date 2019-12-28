package barisic.newsgetter.db_classes;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sources_table")
public class Source {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private String domain;

    public Source(String name, String domain) {
        this.name = name;
        this.domain = domain;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDomain() {
        return domain;
    }
}
