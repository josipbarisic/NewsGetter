package barisic.newsgetter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import barisic.newsgetter.adapters.ArticlesRecyclerViewAdapter;
import barisic.newsgetter.helper_classes.ApiManager;
import barisic.newsgetter.news_api_classes.Article;
import barisic.newsgetter.news_api_classes.NewsApiArticles;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsTest extends AppCompatActivity implements Callback<NewsApiArticles> {
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_test);

        Log.d("MAC", "onCreate: MAC ADDRESS -> " + getMacAddr());

        Intent intent = getIntent();

        if (intent.getExtras() != null) {
            url = intent.getExtras().get("url").toString();
        }
//        Toast.makeText(getApplicationContext(), date, Toast.LENGTH_LONG).show();

        ApiManager.getInstance().getArticlesService().getNews(url).enqueue(this);
    }

    @Override
    public void onResponse(Call<NewsApiArticles> call, Response<NewsApiArticles> response) {
        if(response.isSuccessful() && response.body() != null){
            NewsApiArticles news = response.body();
            ArrayList<Article> articles = news.getResponse();

            ArrayList<Article> filteredArticlesJutarnji = new ArrayList<>();
            ArrayList<Article> filteredArticlesGuardian = new ArrayList<>();

            String newspaper = "";

            //api filter
            for(Article article: articles){
                if(article.getArticleUrl().contains("jutarnji.hr/vijesti")){
                    filteredArticlesJutarnji.add(article);
                    newspaper = "jutarnji";
                }
                if(article.getArticleUrl().contains("theguardian")){
                    filteredArticlesGuardian.add(article);
                    newspaper = "guardian";
                }
                Log.d("URLS", "onResponse: " + article.getArticleUrl());
            }

            switch (newspaper){
                case "jutarnji":
                    initRecyclerView(filteredArticlesJutarnji);
                    break;
                case "guardian":
                    initRecyclerView(filteredArticlesGuardian);
                    break;
                default:
                    initRecyclerView(articles);
                    break;
            }
        }
    }

    @Override
    public void onFailure(Call<NewsApiArticles> call, Throwable t) {
        t.printStackTrace();
    }

    /*public void helpMe(ArrayList<Article> articles){
        String TAG = "HELP";
        for (Article article: articles){

            newsTitles.add(article.getTitle());
        }
        Log.d(TAG, "onResponse: " + newsTitles.toString());

        initRecyclerView(articles);
    }*/

    //RESEARCH MATERIAL
    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "";
    }

    public void initRecyclerView(ArrayList<Article> titles){
        final RecyclerView recyclerView = findViewById(R.id.news_recycler_view);
        ArticlesRecyclerViewAdapter adapter = new ArticlesRecyclerViewAdapter(titles);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
