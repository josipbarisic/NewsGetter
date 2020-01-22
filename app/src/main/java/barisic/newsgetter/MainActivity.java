package barisic.newsgetter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.Locale;

import barisic.newsgetter.activities.SettingsActivity;
import barisic.newsgetter.adapters.ViewPagerAdapter;
import barisic.newsgetter.db_classes.Source;
import barisic.newsgetter.helper_classes.SourceViewModel;

public class MainActivity extends AppCompatActivity{

    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView imageView;

    private static final String TAG = "MAIN_ACT";

    private SourceViewModel sourceViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setLocale("hr");
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabLayout);
        imageView = findViewById(R.id.imageView);
        viewPager= findViewById(R.id.viewPager);

        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        sourceViewModel = ViewModelProviders.of(this).get(SourceViewModel.class);
        sourceViewModel.getSourcesUpdate().observe(this, new Observer<List<Source>>() {
            @Override
            public void onChanged(List<Source> sources) {
                //POSTAVLJANJE FRAGMENATA U ViewPagerAdapter
                adapter.resetFragments(sources);

                if(adapter.getCount() == 0){
                    Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

//                    startActivity(intent);
//                    finish();

                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_sources_warning), Toast.LENGTH_LONG).show();
                }
            }
        });

        Log.d(TAG, "onCreate: " + adapter.getCount());

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);

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
