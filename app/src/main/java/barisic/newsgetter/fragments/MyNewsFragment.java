package barisic.newsgetter.fragments;

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
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.List;


import barisic.newsgetter.R;
import barisic.newsgetter.adapters.ViewPagerAdapter;
import barisic.newsgetter.db_classes.Source;
import barisic.newsgetter.helper_classes.SourceViewModel;

public class MyNewsFragment extends Fragment {

    private final String TAG = "MyNewsFragment";
    private static BottomNavigationView bottomNavigationView;

    public static MyNewsFragment newInstance(BottomNavigationView bottomNavigation){
        bottomNavigationView = bottomNavigation;

        MyNewsFragment fragment = new MyNewsFragment();
        return fragment;
    }

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SourceViewModel sourceViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_my_news_item, container, false);

        tabLayout = view.findViewById(R.id.news_tab_layout);
        viewPager = view.findViewById(R.id.news_view_pager);

        final ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());

        sourceViewModel = ViewModelProviders.of(this).get(SourceViewModel.class);

        sourceViewModel.getSourcesUpdate().observe(this, new Observer<List<Source>>() {
            @Override
            public void onChanged(List<Source> sources) {
                Log.d(TAG, "onCreateView: SourceViewModel");
                //POSTAVLJANJE FRAGMENATA U ViewPagerAdapter
                adapter.resetFragments(sources);

                if(adapter.getCount() == 0){
                    /*Intent intent = new Intent(view.getContext(), SettingsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(intent);*/

                    bottomNavigationView.setSelectedItemId(R.id.nav_sources);


                    Toast.makeText(view.getContext(), getResources().getString(R.string.no_sources_warning), Toast.LENGTH_LONG).show();
                }
            }
        });

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}
