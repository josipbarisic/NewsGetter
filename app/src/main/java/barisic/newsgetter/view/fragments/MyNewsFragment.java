package barisic.newsgetter.view.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import java.util.List;
import barisic.newsgetter.R;
import barisic.newsgetter.view.adapters.ViewPagerAdapter;
import barisic.newsgetter.model.db_classes.Source;
import barisic.newsgetter.viewmodel.db_classes.SourceViewModel;

public class MyNewsFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

//    private final String TAG = "MyNewsFragment";
    private static BottomNavigationView bottomNavigationView;

    public static MyNewsFragment newInstance(BottomNavigationView bottomNavigation){
        bottomNavigationView = bottomNavigation;

        return new MyNewsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_my_news_item, container, false);

        tabLayout = view.findViewById(R.id.news_tab_layout);
        viewPager = view.findViewById(R.id.news_view_pager);

        final ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager(), getContext());

        SourceViewModel sourceViewModel = ViewModelProviders.of(this).get(SourceViewModel.class);

        sourceViewModel.getSourcesUpdate().observe(this, new Observer<List<Source>>() {
            @Override
            public void onChanged(List<Source> sources) {
                //POSTAVLJANJE FRAGMENATA U ViewPagerAdapter
                adapter.resetFragments(sources);

                if(adapter.getCount() == 0){

                    viewPager.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.GONE);

                    bottomNavigationView.getMenu().getItem(0).setEnabled(false);
                    bottomNavigationView.setSelectedItemId(R.id.nav_sources);
                }
                else{
                    bottomNavigationView.getMenu().getItem(0).setEnabled(true);
                    viewPager.setVisibility(View.VISIBLE);
                    tabLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}
