package barisic.newsgetter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import barisic.newsgetter.fragments.FavoritesFragment;
import barisic.newsgetter.fragments.MyNewsFragment;
import barisic.newsgetter.fragments.SearchFragment;
import barisic.newsgetter.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity{

    String TAG = "MainActivity";

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_nav);

//        UpdateDatabaseSources.updateInstance();

        final Fragment myNewsFragment = MyNewsFragment.newInstance(bottomNavigationView);
        final Fragment searchFragment = SearchFragment.newInstance();
        final Fragment settingsFragment = SettingsFragment.newInstance();
        final Fragment favoritesFragment = FavoritesFragment.newInstance();
        final FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().add(R.id.main_frame, myNewsFragment).commit();
        fragmentManager.beginTransaction().add(R.id.main_frame, searchFragment).hide(searchFragment).commit();
        fragmentManager.beginTransaction().add(R.id.main_frame, settingsFragment).hide(settingsFragment).commit();
        fragmentManager.beginTransaction().add(R.id.main_frame, favoritesFragment).hide(favoritesFragment).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            Fragment activeFragment = myNewsFragment;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.nav_news:
                        fragmentManager.beginTransaction().hide(activeFragment).show(myNewsFragment).commit();
                        activeFragment = myNewsFragment;
                        return true;
                    case R.id.nav_search:
                        fragmentManager.beginTransaction().hide(activeFragment).show(searchFragment).commit();
                        activeFragment = searchFragment;
                        return true;
                    case R.id.nav_sources:
                        fragmentManager.beginTransaction().hide(activeFragment).show(settingsFragment).commit();
                        activeFragment = settingsFragment;
                        return true;
                    case R.id.nav_favorites:
                        fragmentManager.beginTransaction().hide(activeFragment).show(favoritesFragment).commit();
                        activeFragment = favoritesFragment;
                        return true;
                }
                return false;
            }
        });
    }
}
