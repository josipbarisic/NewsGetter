package barisic.newsgetter.helper_classes;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import barisic.newsgetter.news_api_classes.Article;
import barisic.newsgetter.news_api_classes.NewsApiArticles;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateArticlesApiCall implements Callback<NewsApiArticles> {

    private static ArrayList<Article> articles = new ArrayList<>();
    private static String url;

    public static void loadArticles(String domain){
        UpdateArticlesApiCall instance = new UpdateArticlesApiCall();
        url = "everything?sources="+ domain +"&pageSize=100&apiKey=38fbf5c450684e339b0e300b7bd7f8ea";

        ApiManager.getInstance().getArticlesService().getNews(url).enqueue(instance);
        Log.d("API_CALLER", "LOADING SOURCE ARTICLES..." + domain.toUpperCase());
    }

    public static ArrayList<Article> getArticles(){
        return articles;
    }

    @Override
    public void onResponse(Call<NewsApiArticles> call, Response<NewsApiArticles> response) {
        Log.d("API_CALLER", "onResponse: ONRESPONSE");
        if(response.isSuccessful()){
            Log.d("API_CALLER", "onResponse: " + response.body().getResponse().toString());
            articles = response.body().getResponse();
            for(Article art: articles){
                Log.d("API_CALLER", "onResponse: " + art.getTitle());
            }
        }
    }

    @Override
    public void onFailure(Call<NewsApiArticles> call, Throwable t) {
        Log.d("API_CALLER", "onResponse: FAILED");
    }
}
