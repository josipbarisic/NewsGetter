package barisic.newsgetter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import java.net.NetworkInterface;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import barisic.newsgetter.adapters.RecyclerViewAdapter;
import barisic.newsgetter.helper_classes.ApiManager;
import barisic.newsgetter.helper_classes.Article;
import barisic.newsgetter.helper_classes.NewsApiResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsTest extends AppCompatActivity implements Callback<NewsApiResponse> {
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


        ApiManager.getInstance().service().getNews(url).enqueue(this);
    }

    @Override
    public void onResponse(Call<NewsApiResponse> call, Response<NewsApiResponse> response) {
        if(response.isSuccessful() && response.body() != null){
            NewsApiResponse news = response.body();
            ArrayList<Article> articles = news.getResponse();

            ArrayList<Article> filteredArticles = new ArrayList<>();

            //api filter
            for(Article article: articles){
                if(article.getArticleUrl().contains("jutarnji.hr/vijesti")){
                    filteredArticles.add(article);
                }

            }
            Log.d("RESULT", "onResponse: " + filteredArticles.size());

            if(filteredArticles.size() > 0){
                initRecyclerView(filteredArticles);
            }
            else{
                initRecyclerView(articles);
            }
        }
    }

    @Override
    public void onFailure(Call<NewsApiResponse> call, Throwable t) {
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
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(titles);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
