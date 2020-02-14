package barisic.newsgetter.model.api_classes;

public class Article {
    private String title;
    private String url;
    private String urlToImage;
    private String description;
    private String publishedAt;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getTitle() {
        return title;
    }

    public String getArticleUrl() {
        return url;
    }

    public String getImageUrl() {
        return urlToImage;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        String date;

        String hours = publishedAt.substring(11, 13);
        String minutes = publishedAt.substring(14, 16);

        String day = publishedAt.substring(8, 10);
        String month = publishedAt.substring(5, 7);
        String year = publishedAt.substring(0, 4);

        date = day +"/"+ month +"/"+ year +"   "+ hours +":"+ minutes +" GMT";

        return date;
    }

    public String getPublishedAt() {
        return publishedAt;
    }
}
