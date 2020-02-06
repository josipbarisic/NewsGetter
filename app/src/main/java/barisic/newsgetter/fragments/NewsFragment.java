package barisic.newsgetter.fragments;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
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

    private ArticlesRecyclerViewAdapter adapter;
    private ArrayList<Article> articlesCache = new ArrayList<>();

//    private final String TAG = "NewsFragment";


    public NewsFragment(String domain, Context context){

        String apiKey = getRandomApiKey(context);

        if(domain.equals("jutarnji.hr") || domain.equals("24sata.hr")){
            url = "everything?domains="+ domain +"&pageSize=100&apiKey="+apiKey;
        }
        else if(domain.contains("-recommended-news-")){
            url = "top-headlines?country=us&pageSize=100&apiKey="+apiKey;
        }
        else if(domain.contains("searchQuery-")){
            url = "everything?q=" + domain.replace("searchQuery-", "") + "&sortBy=publishedAt&pageSize=100&apiKey="+apiKey;
        }
        else{
            url = "everything?sources="+ domain +"&pageSize=100&apiKey="+apiKey;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news, container, false);

        if(articlesCache.isEmpty()){
            ApiManager.getInstance().getArticlesService().getNews(url).enqueue(this);
        }
        else{
            initRecyclerView(view, articlesCache);
        }

        return view;
    }

    @Override
    public void onResponse(@NonNull Call<NewsApiArticles> call, Response<NewsApiArticles> response) {
        if(response.body() != null && response.isSuccessful()){
            NewsApiArticles apiResponse = response.body();
            articlesCache = apiResponse.getResponse();

            View view = getView();
            if(view != null){
                initRecyclerView(view, articlesCache);
            }
        }

    }

    @Override
    public void onFailure(@NonNull Call<NewsApiArticles> call, @NonNull Throwable t) {

    }

    private void initRecyclerView(View view, ArrayList<Article> articles){
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
    }

    private String getRandomApiKey(Context context) {
        double random = (Math.random() * 31);
        String[] apiKeys = context.getResources().getStringArray(R.array.apiKeys);

        for (int i = 0; i < apiKeys.length; i++) {
            if ((int) random == i) {
                return apiKeys[i];
            }
        }
        return apiKeys[0];
    }
}
