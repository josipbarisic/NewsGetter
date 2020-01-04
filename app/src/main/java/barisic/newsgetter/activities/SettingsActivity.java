package barisic.newsgetter.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import barisic.newsgetter.MainActivity;
import barisic.newsgetter.R;
import barisic.newsgetter.adapters.SourcesRecyclerViewAdapter;
import barisic.newsgetter.helper_classes.ApiManager;
import barisic.newsgetter.helper_classes.SourceViewModel;
import barisic.newsgetter.news_api_classes.NewsApiSources;
import barisic.newsgetter.news_api_classes.Source;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity implements Callback<NewsApiSources> {

//    ProgressBar progressBar;
    String url = "sources?apiKey=38fbf5c450684e339b0e300b7bd7f8ea";
    RecyclerView recyclerView;
    Button btnConfirm;

    ArrayList<String> namesList = new ArrayList<>();
    ArrayList<String> domainsList = new ArrayList<>();
    ArrayList<String> urlsList = new ArrayList<>();

    ArrayList<Integer> selectedSources = new ArrayList<>();

    private SourceViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnConfirm = findViewById(R.id.btnSelectSources);

        /*progressBar = findViewById(R.id.progressBar);

        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.MULTIPLY);*/

        ApiManager.getInstance().getSourcesService().getApiSources(url).enqueue(this);

        viewModel = ViewModelProviders.of(this).get(SourceViewModel.class);
        /*viewModel.getSourcesUpdate().observe(this, new Observer<List<barisic.newsgetter.db_classes.Source>>() {
            @Override
            public void onChanged(List<barisic.newsgetter.db_classes.Source> sources) {
                for(barisic.newsgetter.db_classes.Source source: sources){
                    Log.d("LISTNOW", "onChanged: " + source.getName());
                }
            }
        });*/
    }

    @Override
    public void onResponse(Call<NewsApiSources> call, Response<NewsApiSources> response) {
        if(response.isSuccessful() && response != null){
            ArrayList<Source> sources = response.body().getSources();

            namesList.add("Jutarnji List");
            domainsList.add("jutarnji.hr");
            urlsList.add("https://www.jutarnji.hr/");

            namesList.add("24sata");
            domainsList.add("24sata.hr");
            urlsList.add("https://www.24sata.hr/");

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

                if(!source.getSourceId().matches("google-news-(.*)")){
                    namesList.add(source.getName());
                    domainsList.add(source.getSourceId());
                    urlsList.add(domain);
                    Log.d("NEWS", "onResponse: " + source.getName() + " SOURCE_ID: ///"+ source.getSourceId() +"/// NO."+ i++);
                }
            }

            initRecyclerView(namesList, domainsList, urlsList);
        }
    }

    @Override
    public void onFailure(Call<NewsApiSources> call, Throwable t) {
        //
    }

    public void initRecyclerView(ArrayList<String> names, ArrayList<String> domains, ArrayList<String> urls){
        recyclerView = findViewById(R.id.sources_recycler_view);
        final SourcesRecyclerViewAdapter adapter = new SourcesRecyclerViewAdapter(names, domains, urls, viewModel.getAllSources());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

//        viewModel.getSourcesUpdate().observe(this, new Observer<List<barisic.newsgetter.db_classes.Source>>() {
//            @Override
//            public void onChanged(List<barisic.newsgetter.db_classes.Source> sources) {
////                adapter.checkSelectedSources(viewModel.getAllSources());
//                Log.d("LISTNOW", "onChanged: " + sources.toString());
//            }
//
//        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), MainActivity.class);


                viewModel.deleteAllSources();
                Log.d("TAAG", "onClick: " + adapter.getSelectedPositions());

                selectedSources = adapter.getSelectedPositions();

                for(int i = 0; i < selectedSources.size(); i++){
                    Log.d("SELECTED", "onClick: " + selectedSources.get(i) + " " + namesList.get(selectedSources.get(i)));
                    viewModel.insertSource(new barisic.newsgetter.db_classes.Source(namesList.get(selectedSources.get(i)), domainsList.get(selectedSources.get(i))));
                }

//                    viewModel.insertSource  (source);
                    /*for(int i = 0; i < adapter.getItemCount(); i++){
                        if(adapter.getItemId(i) != n){
                            viewModel.deleteSource();
                        }
                    }*/
//                }

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                v.getContext().startActivity(intent);
            }
        });
    }
}
