package barisic.newsgetter.fragments;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import barisic.newsgetter.R;
import barisic.newsgetter.adapters.ArticlesRecyclerViewAdapter;
import barisic.newsgetter.db_classes.Favorite;
import barisic.newsgetter.helper_classes.ApiManager;
import barisic.newsgetter.helper_classes.FavoriteViewModel;
import barisic.newsgetter.helper_classes.UpdateArticlesApiCall;
import barisic.newsgetter.news_api_classes.Article;
import barisic.newsgetter.news_api_classes.NewsApiArticles;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewsFragment extends Fragment {

    private String url;
    private String domainHolder;

    private ArticlesRecyclerViewAdapter adapter;

    private final static String TAG = "NewsFragment";

    ArrayList<Article> articles = new ArrayList<>();
    Article article;

    FavoriteViewModel favoriteViewModel;

    public NewsFragment(String source_domain){
        domainHolder = source_domain;

        Log.d(TAG, "newInstance: " + domainHolder);

        if(domainHolder.contains("searchQuery-")){
            favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);
            url = "everything?q=" + domainHolder.replace("searchQuery-", "") + "&sortBy=publishedAt&pageSize=100&apiKey=39bfc7dc081544caad92115e59fe9722";
            ApiManager.getInstance().getArticlesService().getNews(url).enqueue(new Callback<NewsApiArticles>() {
                @Override
                public void onResponse(@NonNull Call<NewsApiArticles> call, @NonNull Response<NewsApiArticles> response) {
                    if(response.body() != null && response.isSuccessful()){
                        NewsApiArticles apiResponse = response.body();
                        articles = apiResponse.getResponse();

                        View view = getView();
                        if(view != null){
                            initRecyclerView(view, articles, favoriteViewModel);
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<NewsApiArticles> call, @NonNull Throwable t) {

                }
            });
        }
        else{
            loadArticles(domainHolder, favoriteViewModel);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_news, container, false);

        if(!domainHolder.contains("searchQuery-")){
            favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);
            initRecyclerView(view, articles, favoriteViewModel);
        }

        return view;
    }

    private void initRecyclerView(View view, ArrayList<Article> articles, FavoriteViewModel fvm){
        Log.d("NewsFragment", "Initializing RV articles: " + articles.toString());

        if(fvm != null){
            fvm.getAllFavorites().observe(this, new Observer<List<Favorite>>() {
                @Override
                public void onChanged(List<Favorite> favorites) {
                    adapter.notifyDataSetChanged();
                }
            });

            RecyclerView recyclerView = view.findViewById(R.id.news_recycler_view);
            adapter = new ArticlesRecyclerViewAdapter(articles, fvm, "NewsFragment", getViewLifecycleOwner());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    private void loadArticles(final String domain, final FavoriteViewModel fvm){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbArticles = database.getReference("articles");

        dbArticles.orderByChild("date").limitToLast(100).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                articles.clear();
                for(DataSnapshot snap: dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: FIREBASE_DOMAIN: " + snap.child("source_domain").getValue() + " ||| PASSED_DOMAIN: " + domain + " CONDITION: " );
                        if(domain.matches(String.valueOf(snap.child("source_domain").getValue()))){
                            Log.d(TAG, "ACCEPTED: LOADING " + snap.child("source_domain").getValue() + "...");

                            article = new Article();
                            article.setTitle(String.valueOf(snap.child("title").getValue()));
                            article.setPublishedAt(String.valueOf(snap.child("date").getValue()));
                            article.setUrlToImage(String.valueOf(snap.child("image").getValue()));
                            article.setDescription(String.valueOf(snap.child("description").getValue()));
                            article.setUrl(String.valueOf(snap.child("url").getValue()));

                            Log.d(TAG, "onDataChange: ARTICLE: " + snap.getKey() + " || DESCRIPTION: " + article.getDescription());

                            articles.add(0, article);
                        }
//                        Log.d(TAG, "onDataChange: " + snap.child("source_domain").getValue());
                }
                if(getView() != null){
                    initRecyclerView(getView(), articles, fvm);
                    adapter.notifyDataSetChanged();
                }

                Log.d(TAG, "onDataChange: FRAGMENT: " + domain + " ARTICLES: " + articles.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
