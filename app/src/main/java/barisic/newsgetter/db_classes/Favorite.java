package barisic.newsgetter.db_classes;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorites_table")
public class Favorite {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String url;
    private String urlToImage;
    private String description;
    private String publishedAt;

    public Favorite(String title, String url, String urlToImage, String description, String publishedAt){
        this.title = title;
        this.url = url;
        this.urlToImage = urlToImage;
        this.description = description;
        this.publishedAt = publishedAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getDescription() {
        return description;
    }

    public String getPublishedAt() {
        return publishedAt;
    }
}
