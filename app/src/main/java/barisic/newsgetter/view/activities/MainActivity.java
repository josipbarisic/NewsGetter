package barisic.newsgetter.view.activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import barisic.newsgetter.R;
import barisic.newsgetter.view.fragments.FavoritesFragment;
import barisic.newsgetter.view.fragments.MyNewsFragment;
import barisic.newsgetter.view.fragments.SearchFragment;
import barisic.newsgetter.view.fragments.SettingsFragment;
import barisic.newsgetter.viewmodel.helper_classes.AlertBuilder;
import barisic.newsgetter.viewmodel.news_api_classes.ApiManager;
import barisic.newsgetter.model.api_classes.NewsApiSources;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity{

   private final String TAG = "MainActivity";

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkConnection();

        bottomNavigationView = findViewById(R.id.bottom_nav);

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

    public void checkConnection(){
        ApiManager.getInstance().getSourcesService().getApiSources("").enqueue(new Callback<NewsApiSources>() {
            @Override
            public void onResponse(@NonNull Call<NewsApiSources> call, @NonNull Response<NewsApiSources> response) {
                Log.d(TAG, "onResponse: PASSED");
            }

            @Override
            public void onFailure(@NonNull Call<NewsApiSources> call, @NonNull Throwable t) {
                AlertBuilder.newDialog(MainActivity.this, R.string.connection_message);
            }
        });
    }
}
