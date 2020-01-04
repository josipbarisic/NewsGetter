package barisic.newsgetter.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import barisic.newsgetter.R;
import barisic.newsgetter.adapters.ArticlesRecyclerViewAdapter;
import barisic.newsgetter.helper_classes.ApiManager;
import barisic.newsgetter.news_api_classes.Article;
import barisic.newsgetter.news_api_classes.NewsApiArticles;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewsFragment extends Fragment implements Callback<NewsApiArticles> {

    String url;
    static String domain;

    //datum za url parametar from (OVERCOMPLICATED)
    DateFormat dfy = new SimpleDateFormat("yyyy");
    DateFormat dfm = new SimpleDateFormat("MM");
    DateFormat dfd = new SimpleDateFormat("dd");
    Date oDate = new Date();
    int day = Integer.parseInt(dfd.format(oDate)) - 2;

    String date = dfy.format(oDate) + "-" + dfm.format(oDate) + "-" + day;

    //MOVE LOGIC FROM CONSTRUCTOR (POTENTIAL CRASH)
    public NewsFragment(){

//        this.url = "everything?domains="+ domain +"&pageSize=100&from="+ date +"&apiKey=38fbf5c450684e339b0e300b7bd7f8ea";
//        this.url = "everything?sources="+ domain +"&pageSize=100&apiKey=38fbf5c450684e339b0e300b7bd7f8ea";
    }

    public static NewsFragment newInstance(String fragmentDomain){
        domain = fragmentDomain;
        NewsFragment fragment = new NewsFragment();

        if(domain.equals("jutarnji.hr") || domain.equals("24sata.hr")){
            fragment.url = "everything?domains="+ domain +"&pageSize=100&apiKey=38fbf5c450684e339b0e300b7bd7f8ea";
            Log.d("JUTARNJI", "NewsFragment: " + domain);
        }
        else{
            fragment.url = "everything?sources="+ domain +"&pageSize=100&apiKey=38fbf5c450684e339b0e300b7bd7f8ea";
            Log.d("ELSE", "NewsFragment: " + domain);
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ApiManager.getInstance().getArticlesService().getNews(url).enqueue(this);

        return view;
    }

    @Override
    public void onResponse(Call<NewsApiArticles> call, Response<NewsApiArticles> response) {
        if(response.isSuccessful() && response != null){
            NewsApiArticles apiResponse = response.body();
            ArrayList<Article> articles = apiResponse.getResponse();
            Log.d("SOURCE_ARTICLES", "onResponse: " + articles.size());

            View view = getView();
            if(view != null){
                initRecyclerView(view, articles);
            }
        }

    }

    @Override
    public void onFailure(Call<NewsApiArticles> call, Throwable t) {

    }

    private void initRecyclerView(View view, ArrayList<Article> articles){
        RecyclerView recyclerView = view.findViewById(R.id.news_recycler_view);
        ArticlesRecyclerViewAdapter adapter = new ArticlesRecyclerViewAdapter(articles);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
