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

import java.util.Locale;

import barisic.newsgetter.activities.SettingsActivity;
import barisic.newsgetter.adapters.ViewPagerAdapter;
import barisic.newsgetter.fragments.NewsFragment;

public class MainActivity extends AppCompatActivity{

    /*Button oBtnHeadlines;
    Button oBtnHome;
    Button oSearchButton;
    EditText etSearch;
    String searchQuery;*/

    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView imageView;


    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        //setLocale("hr");
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabLayout);
        imageView = findViewById(R.id.imageView);
        viewPager= findViewById(R.id.viewPager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new NewsFragment("jutarnji.hr"), "Jutarnji");
        adapter.addFragment(new NewsFragment("bbc.co.uk"), "BBC");
        adapter.addFragment(new NewsFragment("nytimes.com"), "New York Times");
        adapter.addFragment(new NewsFragment("elmundo.es"), "El Mundo");
        adapter.addFragment(new NewsFragment("techcrunch.com"), "Techcrunch");

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

                startActivity(intent);
            }
        });

        /*oBtnHome = findViewById(R.id.homeButton);
        oBtnHeadlines = findViewById(R.id.headlinesButton);
        oSearchButton = findViewById(R.id.searchButton);
        etSearch = findViewById(R.id.etSearch);

        oBtnHeadlines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NewsTest.class);
                url = "top-headlines?language=en&pageSize=100&apiKey=38fbf5c450684e339b0e300b7bd7f8ea";
                i.putExtra("url", url);

                startActivity(i);
            }
        });

        oBtnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NewsTest.class);
                //url = "everything?q=&domains=24sata.hr,jutarnji.hr&pageSize=100&from=" + date + "&sortBy=publishedAt&apiKey=38fbf5c450684e339b0e300b7bd7f8ea";
                url = "everything?q=&domains=jutarnji.hr&pageSize=100&from=" + "&sortBy=publishedAt&apiKey=38fbf5c450684e339b0e300b7bd7f8ea";
                i.putExtra("url", url);

                startActivity(i);
            }
        });


        oSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchQuery = etSearch.getText().toString();

                if(!searchQuery.matches("")){
                    Intent i = new Intent(MainActivity.this, NewsTest.class);
                    url = "everything?q="+ searchQuery +"&language=en&pageSize=100&from="+ date +"&sortBy=publishedAt&apiKey=38fbf5c450684e339b0e300b7bd7f8ea";
                    i.putExtra("url", url);

                    startActivity(i);
                }
                else{
                    Toast.makeText(getApplicationContext(), getText(R.string.search_warning).toString(), Toast.LENGTH_LONG).show();
                }
            }
        });*/

    }

    private void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

    }
}
