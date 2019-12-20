package barisic.newsgetter.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import barisic.newsgetter.MainActivity;
import barisic.newsgetter.R;
import barisic.newsgetter.adapters.SourcesRecyclerViewAdapter;
import barisic.newsgetter.helper_classes.ApiManager;
import barisic.newsgetter.helper_classes.NewsApiArticles;
import barisic.newsgetter.helper_classes.NewsApiSources;
import barisic.newsgetter.helper_classes.Source;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity implements Callback<NewsApiSources> {

//    ProgressBar progressBar;
    String url = "sources?apiKey=38fbf5c450684e339b0e300b7bd7f8ea";
    Button btnConfirm;
    ArrayList<String> namesList = new ArrayList<>();
    ArrayList<String> domainsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnConfirm = findViewById(R.id.btnSelectSources);

        /*progressBar = findViewById(R.id.progressBar);

        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.MULTIPLY);*/

        ApiManager.getInstance().getSourcesService().getApiSources(url).enqueue(this);

    }

    @Override
    public void onResponse(Call<NewsApiSources> call, Response<NewsApiSources> response) {
        if(response.isSuccessful() && response != null){
            ArrayList<Source> sources = response.body().getSources();

            int i = 1;
            String domain;
            for (Source source: sources){
                URL url = null;
                try {
                    url = new URL(source.getUrl());
                }catch (MalformedURLException e){
                    Log.d("URL MALFORMED", "onResponse: " + e);
                }
                domain = url.getHost();

                namesList.add(source.getName());
                domainsList.add(source.getSourceId());
                Log.d("NEWS", "onResponse: " + source.getName() + " SOURCE_ID: ///"+ source.getSourceId() +"/// NO."+ i++);
            }

            /*String domain;
            int i = 1;
            for (Source source: sources){
                domain = source.getUrl();

                URL url = null;
                try {
                    url = new URL(domain);
                }catch (MalformedURLException e){
                    Log.d("URL MALFORMED", "onResponse: " + e);
                }
                domain = url.getHost();

                //FILTRIRANJE STRINGA DOMENE
                if(domain.contains("www.")){
                    domain = domain.replace("www.", "");
                }
                if(domain.substring(0, 5).matches("news[.]") && !domain.contains("com.au")){
                    domain = domain.replace("news.", "");
                }
                if(domain.contains(".go")){
                    domain = domain.replace(".go", "");
                }

                //PROVJERA DUPLIKATA U LISTI
                if(!domainsList.contains(domain)){
                    namesList.add(source.getName());
                    domainsList.add(domain);
                    Log.d("NEWS", "onResponse: " + domain + " source: " + source.getName() + " SOURCE_ID: ///"+ source.getSourceId() +"/// NO." + i);
                    i++;
                }
            }*/
            initRecyclerView(namesList, domainsList);

            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    intent.putExtra("names", namesList);
                    intent.putExtra("domains", domainsList);

                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onFailure(Call<NewsApiSources> call, Throwable t) {
        //
    }

    public void initRecyclerView(ArrayList<String> names, ArrayList<String> domains){
        RecyclerView recyclerView = findViewById(R.id.sources_recycler_view);
        SourcesRecyclerViewAdapter adapter = new SourcesRecyclerViewAdapter(names, domains);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }
}
