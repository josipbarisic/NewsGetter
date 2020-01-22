package barisic.newsgetter.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import barisic.newsgetter.R;
import barisic.newsgetter.adapters.ArticlesRecyclerViewAdapter;
import barisic.newsgetter.db_classes.Favorite;
import barisic.newsgetter.helper_classes.ApiManager;
import barisic.newsgetter.helper_classes.FavoriteViewModel;
import barisic.newsgetter.news_api_classes.Article;
import barisic.newsgetter.news_api_classes.NewsApiArticles;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewsFragment extends Fragment implements Callback<NewsApiArticles> {

    private String url;
    private static String domain;

    static ArticlesRecyclerViewAdapter adapter;

    private final static String TAG = "NewsFragment";

    //datum za url parametar from (OVERCOMPLICATED)
    DateFormat dfy = new SimpleDateFormat("yyyy");
    DateFormat dfm = new SimpleDateFormat("MM");
    DateFormat dfd = new SimpleDateFormat("dd");
    Date oDate = new Date();
    int day = Integer.parseInt(dfd.format(oDate)) - 2;

    String date = dfy.format(oDate) + "-" + dfm.format(oDate) + "-" + day;

    public static NewsFragment newInstance(String fragmentDomain){
        domain = fragmentDomain;
        NewsFragment fragment = new NewsFragment();

        if(domain.equals("jutarnji.hr") || domain.equals("24sata.hr")){
            fragment.url = "everything?domains="+ domain +"&pageSize=100&apiKey=38fbf5c450684e339b0e300b7bd7f8ea";
            Log.d("JUTARNJI", "NewsFragment: " + domain);
        }
        else if(domain.contains("-recommended-news-")){
            fragment.url = "top-headlines?country=us&pageSize=100&apiKey=38fbf5c450684e339b0e300b7bd7f8ea";
        }
        else if(domain.contains("searchQuery-")){
            Log.d(TAG, "newInstance: " + domain + "|||" + domain.replace("searchQuery-", ""));
            fragment.url = "everything?q=" + domain.replace("searchQuery-", "") + "&sortBy=publishedAt&pageSize=100&apiKey=38fbf5c450684e339b0e300b7bd7f8ea";
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
        if(response.isSuccessful()){
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
        Log.d("NewsFragment", "onCreateView: " + articles);
        FavoriteViewModel favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);

        favoriteViewModel.getAllFavorites().observe(this, new Observer<List<Favorite>>() {
            @Override
            public void onChanged(List<Favorite> favorites) {
                adapter.notifyDataSetChanged();
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.news_recycler_view);
        adapter = new ArticlesRecyclerViewAdapter(articles, favoriteViewModel, "NewsFragment", getViewLifecycleOwner());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //Listener za infinite loading
        /*recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                final int DIRECTION_DOWN = 1;

                if(!recyclerView.canScrollVertically(DIRECTION_DOWN)){
                    Toast.makeText(getContext(), "Last item" + adapter.getItemCount(), Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }

    public static void updateAdapter(){
        adapter.notifyDataSetChanged();
    }
}
