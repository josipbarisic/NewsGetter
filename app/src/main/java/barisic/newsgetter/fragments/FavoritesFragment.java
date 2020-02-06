package barisic.newsgetter.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
import barisic.newsgetter.helper_classes.FavoriteViewModel;
import barisic.newsgetter.news_api_classes.Article;

public class FavoritesFragment extends Fragment {

    private ArticlesRecyclerViewAdapter adapter;
    private FavoriteViewModel favoriteViewModel;

    private TextView tvNoFavorites;

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);

        tvNoFavorites = view.findViewById(R.id.tvNoFavorites);

        Button btnDeleteAll = view.findViewById(R.id.btnDeleteAllFavorites);

        btnDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoriteViewModel.deleteAllFavorites();
            }
        });

        initRecyclerView(view);

        return view;
    }

    private void initRecyclerView(View view) {
        final RecyclerView recyclerView = view.findViewById(R.id.favorites_recycler_view);

        final ArrayList<Article> articles = new ArrayList<>();

        favoriteViewModel.getAllFavorites().observe(this, new Observer<List<Favorite>>() {
            @Override
            public void onChanged(List<Favorite> favorites) {
                articles.clear();
                for (Favorite favorite : favorites) {
                    Article article = new Article();
                    article.setTitle(favorite.getTitle());
                    article.setUrl(favorite.getUrl());
                    article.setUrlToImage(favorite.getUrlToImage());
                    article.setDescription(favorite.getDescription());
                    article.setPublishedAt(favorite.getPublishedAt());

                    articles.add(article);
                }
                adapter.notifyDataSetChanged();

                if(adapter.getItemCount() != 0){
                    recyclerView.setBackgroundColor(getResources().getColor(R.color.fullBlack));
                    tvNoFavorites.setText("");
                }
                else{
                    recyclerView.setBackgroundColor(getResources().getColor(R.color.spaceCadet));
                    tvNoFavorites.setText(R.string.no_favorites);
                }
            }
        });

        adapter = new ArticlesRecyclerViewAdapter(articles, favoriteViewModel, "FavoritesFragment", getViewLifecycleOwner());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
