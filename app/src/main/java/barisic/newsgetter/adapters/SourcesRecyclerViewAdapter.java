package barisic.newsgetter.adapters;

import android.annotation.TargetApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import barisic.newsgetter.R;
import barisic.newsgetter.interfaces.SourceSelectHandler;

public class SourcesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<String> namesDataset;
    ArrayList<String> domainsDataset;
    ArrayList<String> urlsDataset;

    ArrayList<Integer> selectedPositions = new ArrayList<>();

    final String logoScraper = "https://logo.clearbit.com/";

    public SourcesRecyclerViewAdapter(ArrayList<String> names, ArrayList<String> domains, ArrayList<String> urls){
        this.namesDataset = names;
        this.domainsDataset = domains;
        this.urlsDataset = urls;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sources_list_item, parent, false);
        final CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final CustomViewHolder viewHolder = (CustomViewHolder) holder;
        String url = urlsDataset.get(position);

        if(!url.matches("")){
            Picasso.get().load(logoScraper + url).into(viewHolder.unselectedImageView);
            Picasso.get().load(logoScraper + url).into(viewHolder.selectedImageView);
        }

        viewHolder.viewSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            @TargetApi(16)
            public void onClick(View v) {
                if(viewHolder.viewSwitcher.getCurrentView() == viewHolder.unselectedLayout){
                    viewHolder.viewSwitcher.showNext();

                    selectedPositions.add(viewHolder.getAdapterPosition());
                }
                else{
                    viewHolder.viewSwitcher.showPrevious();

                    selectedPositions.remove((Integer) viewHolder.getAdapterPosition());
                }
                Log.d("SELECTED_POSNS", "Sources onClick: " + selectedPositions);
            }
        });

        if(!selectedPositions.isEmpty() && selectedPositions.contains(viewHolder.getAdapterPosition()) && viewHolder.viewSwitcher.getCurrentView() == viewHolder.unselectedLayout){
            viewHolder.viewSwitcher.showNext();
        }
        else if(viewHolder.viewSwitcher.getCurrentView() == viewHolder.selectedLayout){
            viewHolder.viewSwitcher.showPrevious();
        }

        viewHolder.tvUnselectedSourceName.setText(namesDataset.get(position));
        Log.d("binder", "onBindViewHolder: " + viewHolder.tvUnselectedSourceName.getText());
        viewHolder.tvSelectedSourceName.setText(namesDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return domainsDataset.size();
    }

    public ArrayList<Integer> getSelectedPositions(){
        return selectedPositions;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        ViewSwitcher viewSwitcher;
        LinearLayout unselectedLayout;
        LinearLayout selectedLayout;

        ImageView unselectedImageView;
        ImageView selectedImageView;
        TextView tvUnselectedSourceName;
        TextView tvSelectedSourceName;

        public CustomViewHolder(View view){
            super(view);

            viewSwitcher = view.findViewById(R.id.viewSwitcher);
            unselectedLayout = view.findViewById(R.id.unselectedLayout);
            selectedLayout = view.findViewById(R.id.selectedLayout);

            unselectedImageView = view.findViewById(R.id.unselectedImageView);
            selectedImageView = view.findViewById(R.id.selectedImageView);

            tvUnselectedSourceName = view.findViewById(R.id.tvUnselectedSourceName);
            tvSelectedSourceName = view.findViewById(R.id.tvSelectedSourceName);
        }

    }
}
