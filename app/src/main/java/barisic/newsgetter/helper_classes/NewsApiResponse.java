package barisic.newsgetter.helper_classes;

import androidx.annotation.NonNull;

import java.util.ArrayList;


public class NewsApiResponse {
    ArrayList<Article> articles;

    public ArrayList<Article> getResponse() {
        return articles;
    }

    /*@NonNull
    @Override
    public String toString() {
        return articles.toString();
    }*/
}
