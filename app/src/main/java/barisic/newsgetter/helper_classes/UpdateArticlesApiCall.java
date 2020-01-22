package barisic.newsgetter.helper_classes;

import android.util.Log;

import java.util.ArrayList;

import barisic.newsgetter.news_api_classes.Article;
import barisic.newsgetter.news_api_classes.NewsApiArticles;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateArticlesApiCall implements Callback<NewsApiArticles> {

    private static UpdateArticlesApiCall instance;
    private static ArrayList<Article> articles = new ArrayList<>();
    private static String url;

    public static UpdateArticlesApiCall getInstance(){
        if(instance == null){
            instance = new UpdateArticlesApiCall();
        }
        return instance;
    }

    public static void loadArticles(String domain){
        url = "everything?sources="+ domain +"&pageSize=100&apiKey=38fbf5c450684e339b0e300b7bd7f8ea";

        ApiManager.getInstance().getArticlesService().getNews(url).enqueue(UpdateArticlesApiCall.getInstance());
        Log.d("API_CALLER", "getArticles: SHIT");
    }

    public static ArrayList<Article> getArticles(){
        return articles;
    }

    @Override
    public void onResponse(Call<NewsApiArticles> call, Response<NewsApiArticles> response) {
        if(response.isSuccessful()){
            articles = response.body().getResponse();
            for(Article art: articles){
                Log.d("API_CALLER", "onResponse: " + art.getTitle());
            }
        }
    }

    @Override
    public void onFailure(Call<NewsApiArticles> call, Throwable t) {

    }
}
