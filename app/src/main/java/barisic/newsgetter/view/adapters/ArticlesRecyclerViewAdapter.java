package barisic.newsgetter.view.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import barisic.newsgetter.view.activities.ArticleSingleActivity;
import barisic.newsgetter.R;
import barisic.newsgetter.model.db_classes.Favorite;
import barisic.newsgetter.viewmodel.db_classes.FavoriteViewModel;
import barisic.newsgetter.model.api_classes.Article;

public class ArticlesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Article> dataset;
    private ArrayList<Integer> vhPosition = new ArrayList<>();

    private FavoriteViewModel favoriteViewModel;
    private String fragmentName;
    private LifecycleOwner lifecycleOwner;

    private Article article = null;
    private Favorite favorite = null;

    public ArticlesRecyclerViewAdapter(List<Article> list, FavoriteViewModel viewModel, String fragment, LifecycleOwner owner){
        dataset = list;
        favoriteViewModel = viewModel;
        fragmentName = fragment;
        lifecycleOwner = owner;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_item, parent, false);
        final CustomViewHolder viewHolder = new CustomViewHolder(view);

        viewHolder.articleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ArticleSingleActivity.class);
                intent.putExtra("url", viewHolder.articleUrl);

                v.getContext().startActivity(intent);

            }
        });

        viewHolder.shareContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.viewSwitcher.startAnimation(AnimationUtils.loadAnimation(v.getContext(), R.anim.slide_out_left));
                viewHolder.shareContainer.startAnimation(AnimationUtils.loadAnimation(v.getContext(), android.R.anim.slide_out_right));

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);

                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, viewHolder.articleUrl);
                v.getContext().startActivity(shareIntent);
            }
        });



        final Animation inAnimation = AnimationUtils.loadAnimation(view.getContext(), android.R.anim.slide_in_left);
        final Animation outAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.slide_out_left);

        viewHolder.viewSwitcher.setInAnimation(inAnimation);
        viewHolder.viewSwitcher.setOutAnimation(outAnimation);

        //toggle like/dislike
        viewHolder.viewSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(dataset != null){
                    article = dataset.get(viewHolder.getAdapterPosition());

                    favorite = new Favorite(article.getTitle(), article.getArticleUrl(), article.getImageUrl(), article.getDescription(), article.getDate());
                }

                if(viewHolder.viewSwitcher.getCurrentView() == viewHolder.likeContainer){
                    favoriteViewModel.insertFavorite(favorite);

                    notifyDataSetChanged();
                }
                else{
                    favoriteViewModel.deleteFavorite(favorite.getUrl());

                    if(fragmentName.equals("FavoritesFragment")){
                        dataset.remove(article);
                    }
                    notifyDataSetChanged();
                }
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final CustomViewHolder viewHolder = (CustomViewHolder) holder;
        String imageUrl = dataset.get(position).getImageUrl();

        if(fragmentName.equals("FavoritesFragment")){
            viewHolder.tvDate.setText(dataset.get(position).getPublishedAt());
        }
        else{
            viewHolder.tvDate.setText(dataset.get(position).getDate());
        }

        viewHolder.tvTitle.setText(dataset.get(position).getTitle());
        viewHolder.articleUrl = dataset.get(position).getArticleUrl();
        viewHolder.articleDescription.setText(dataset.get(position).getDescription());

        //Postavljanje slike u ImageView s Picasso library-em
        if(imageUrl != null && !imageUrl.matches("")){
            Picasso.get().load(imageUrl).resize(1280, 850).onlyScaleDown().into(viewHolder.articleImage);
        }

        favoriteViewModel.getAllFavorites().observe(lifecycleOwner, new Observer<List<Favorite>>() {
            @Override
            public void onChanged(List<Favorite> favorites) {
                vhPosition.clear();
                if(favorites != null){
                    for(Favorite favorite: favorites){
                        if(viewHolder.articleUrl.equals(favorite.getUrl())){
                            vhPosition.add(viewHolder.getAdapterPosition());
                        }
                    }
                }
                //Provjera pozicije clanka (u adapteru) i postavljanje buttona u like ili dislike drawable kod RecyclerView elemenata
                if(!vhPosition.isEmpty() && !vhPosition.contains(viewHolder.getAdapterPosition()) && viewHolder.viewSwitcher.getCurrentView() == viewHolder.dislikeContainer){
                    viewHolder.viewSwitcher.showPrevious();
                }
                else if(!vhPosition.isEmpty() && vhPosition.contains(viewHolder.getAdapterPosition()) && viewHolder.viewSwitcher.getCurrentView() == viewHolder.likeContainer){
                    viewHolder.viewSwitcher.showNext();
                }
                else if(vhPosition.isEmpty() && viewHolder.viewSwitcher.getCurrentView() == viewHolder.dislikeContainer){
                    viewHolder.viewSwitcher.showPrevious();
                }
                //-----------------------------------------------------
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        TextView tvDate;
        TextView tvTitle;
        ImageView articleImage;
        TextView articleDescription;
        ConstraintLayout articleLayout;
        ViewSwitcher viewSwitcher;
        LinearLayout likeContainer;
        LinearLayout dislikeContainer;
        LinearLayout shareContainer;

        String articleUrl;

        private CustomViewHolder(@NonNull View itemView){
            super(itemView);

            tvDate = itemView.findViewById(R.id.tvDate);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            articleImage = itemView.findViewById(R.id.articleImage);
            articleDescription = itemView.findViewById(R.id.tvDescription);
            articleLayout = itemView.findViewById(R.id.clArticle);
            likeContainer = itemView.findViewById(R.id.likeContainer);
            dislikeContainer = itemView.findViewById(R.id.dislikeContainer);
            shareContainer = itemView.findViewById(R.id.shareContainer);
            viewSwitcher = itemView.findViewById(R.id.viewSwitcher);
        }
    }
}