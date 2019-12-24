package barisic.newsgetter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Locale;

import barisic.newsgetter.activities.SettingsActivity;
import barisic.newsgetter.adapters.ViewPagerAdapter;
import barisic.newsgetter.fragments.NewsFragment;

public class MainActivity extends AppCompatActivity{

    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        //setLocale("hr");
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabLayout);
        imageView = findViewById(R.id.imageView);
        viewPager= findViewById(R.id.viewPager);

        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> domains = new ArrayList<>();
        ArrayList<String> listedSources = new ArrayList<>();

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Intent intent = getIntent();

//        adapter.addFragment(new NewsFragment("jutarnji.hr"), "Jutarnji");

        if(intent.getExtras() != null){
            Log.d("MAIN_SOURCES", "onCreate: " + intent.getExtras().getStringArrayList("domains"));

            names = intent.getExtras().getStringArrayList("names");
            domains = intent.getExtras().getStringArrayList("domains");
        }

        adapter.addFragment(new NewsFragment("bbc-news"), "BBC NEWS");
        adapter.addFragment(new NewsFragment("the-new-york-times"), "New York Times");

        for(int i = 0; i < adapter.getCount(); i++){
            listedSources.add(adapter.getPageTitle(i));
            Log.d("TITLE", "PAGE TITLE: " + adapter.getPageTitle(i));
        }

        if(names != null && domains != null){
            for(int i = 0; i < domains.size(); i++){
                Log.d("TITLE2", "NAME: " + names.get(i));
                if(!listedSources.contains(names.get(i))){
                    adapter.addFragment(new NewsFragment(domains.get(i)), names.get(i));
                }
            }
        }

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        //USE ASYNC TASK TO LOAD IMAGE
        for (int i = 0; i < adapter.getCount(); i++){
            Log.d("TAG", "onCreate: ");
        }
//        tabLayout.getTabAt(0).set

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

    }
}
