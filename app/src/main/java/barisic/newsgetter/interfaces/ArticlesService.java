package barisic.newsgetter.interfaces;

import barisic.newsgetter.news_api_classes.NewsApiArticles;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ArticlesService {
    @GET
    Call<NewsApiArticles> getNews(@Url String url);
}
