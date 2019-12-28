package barisic.newsgetter.adapters;

import android.content.Intent;
import android.util.Log;
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
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

import barisic.newsgetter.ArticleSingle;
import barisic.newsgetter.R;
import barisic.newsgetter.news_api_classes.Article;

public class ArticlesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Article> dataset;
    ArrayList<Integer> vhPosition = new ArrayList<>();

    public ArticlesRecyclerViewAdapter(List<Article> list){
        dataset = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_item, parent, false);
        final CustomViewHolder viewHolder = new CustomViewHolder(view, viewType);

        viewHolder.articleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Otvori clanak u browseru
                /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(viewHolder.articleUrl));
                v.getContext().startActivity(browserIntent);*/

                //Otvori clanak u article single-u
                Intent intent = new Intent(v.getContext(), ArticleSingle.class);
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

                if(viewHolder.viewSwitcher.getCurrentView() == viewHolder.likeContainer){

                    viewHolder.viewSwitcher.showNext();

                    vhPosition.add(viewHolder.getAdapterPosition());
                    Log.d("LIKED_ID", "onClick: " + viewHolder.getLayoutPosition());
                    Log.d("ARRAYLIST", "onClick: " + vhPosition.toString());

                }else{
                    viewHolder.viewSwitcher.showPrevious();
                    vhPosition.remove((Integer)viewHolder.getAdapterPosition());
                    Log.d("ARRAYLIST", "onClick: " + vhPosition.toString());
                }
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final CustomViewHolder viewHolder = (CustomViewHolder) holder;
        String imageUrl = dataset.get(position).getImageUrl();

        viewHolder.tvDate.setText(dataset.get(position).getDate());
        viewHolder.tvTitle.setText(dataset.get(position).getTitle());
        viewHolder.articleUrl = dataset.get(position).getArticleUrl();
        viewHolder.articleDescription.setText(dataset.get(position).getDescription());
        //Postavljanje slike u ImageView s Picasso library-em
        if(imageUrl != null && !imageUrl.matches("")){
            Picasso.get().load(imageUrl).into(viewHolder.articleImage);
        }

        //Provjera pozicije clanka (u adapteru) i postavljanje buttona u like ili dislike drawable kod RecyclerView elemenata
        if(!vhPosition.isEmpty() && !vhPosition.contains(viewHolder.getAdapterPosition()) && viewHolder.viewSwitcher.getCurrentView() == viewHolder.dislikeContainer){
            viewHolder.viewSwitcher.showPrevious();
        }
        else if(!vhPosition.isEmpty() && vhPosition.contains(viewHolder.getAdapterPosition()) && viewHolder.viewSwitcher.getCurrentView() == viewHolder.likeContainer){
            viewHolder.viewSwitcher.showNext();
        }
        Log.d("BINDER", "onBindViewHolder: " + viewHolder.getAdapterPosition());
        //-----------------------------------------------------
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

        public CustomViewHolder(@NonNull View itemView, int viewType){
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