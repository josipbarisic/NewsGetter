package barisic.newsgetter.fragments;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import barisic.newsgetter.R;

public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";

    private ViewSwitcher viewSwitcher;
    private TextView header;

    public static SearchFragment newInstance(){
        return new SearchFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_search, container, false);

        if(getFragmentManager() != null){
            getFragmentManager().beginTransaction()
                    .replace(R.id.recommended_news_layout, new NewsFragment("-recommended-news-", getContext()))
                    .commit();
        }

        SearchView searchView = view.findViewById(R.id.search_view);
        viewSwitcher = view.findViewById(R.id.view_switcher);
        header = view.findViewById(R.id.header_text);

        final FrameLayout recommendedLayout = view.findViewById(R.id.recommended_news_layout);
        final FrameLayout searchedLayout = view.findViewById(R.id.searched_news_layout);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: QUERY");
                if (!query.matches("") && getFragmentManager() != null){
                    getFragmentManager().beginTransaction()
                            .replace(R.id.searched_news_layout, new NewsFragment("searchQuery-" + query, getContext()))
                            .commit();
                    if(viewSwitcher.getCurrentView() == recommendedLayout){
                        viewSwitcher.showNext();
                        header.setText(R.string.results_text);
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.matches("")){
                    if(viewSwitcher.getCurrentView() == searchedLayout){
                        viewSwitcher.showPrevious();
                        header.setText(R.string.recommended_text);
                    }
                }
                return true;
            }
        });

        return view;
    }
}
