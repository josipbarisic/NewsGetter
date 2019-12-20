package barisic.newsgetter.adapters;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import barisic.newsgetter.MainActivity;
import barisic.newsgetter.R;

public class SourcesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<String> namesDataset;
    ArrayList<String> domainsDataset;
    final String logoScraper = "https://logo.clearbit.com/";

    public SourcesRecyclerViewAdapter(ArrayList<String> names, ArrayList<String> domains){
        this.namesDataset = names;
        this.domainsDataset = domains;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sources_list_item, parent, false);
        final CustomViewHolder viewHolder = new CustomViewHolder(view);

        viewHolder.sourceContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            @TargetApi(16)
            public void onClick(View v) {
                viewHolder.sourceContainer.setBackground(v.getResources().getDrawable(R.drawable.space_drawable));
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CustomViewHolder viewHolder = (CustomViewHolder) holder;
        String domainName = domainsDataset.get(position);

        if(!domainName.matches("")){
            Picasso.get().load(logoScraper + domainName).into(viewHolder.imageView);
        }
        viewHolder.tvSourceName.setText(namesDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return domainsDataset.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout sourceContainer;
        ImageView imageView;
        TextView tvSourceName;

        public CustomViewHolder(View view){
            super(view);

            sourceContainer = view.findViewById(R.id.sourceContainer);
            imageView = view.findViewById(R.id.sourceImageView);
            tvSourceName = view.findViewById(R.id.tvSourceName);
        }

    }
}
