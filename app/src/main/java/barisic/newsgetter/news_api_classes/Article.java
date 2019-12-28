package barisic.newsgetter.news_api_classes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Article {
    String title;
    String url;
    String urlToImage;
    String description;
    String publishedAt;

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
}
