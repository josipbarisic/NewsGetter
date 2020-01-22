package barisic.newsgetter.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import barisic.newsgetter.R;
import barisic.newsgetter.TestActivity;
import barisic.newsgetter.adapters.SourcesRecyclerViewAdapter;
import barisic.newsgetter.helper_classes.ApiManager;
import barisic.newsgetter.helper_classes.SourceViewModel;
import barisic.newsgetter.news_api_classes.NewsApiSources;
import barisic.newsgetter.news_api_classes.Source;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends Fragment implements Callback<NewsApiSources> {

    private String url = "sources?apiKey=38fbf5c450684e339b0e300b7bd7f8ea";
    private RecyclerView recyclerView;
    private Button btnConfirm;
    private Button btnSelectUnselectAll;

    private ArrayList<String> namesList = new ArrayList<>();
    private ArrayList<String> domainsList = new ArrayList<>();
    private ArrayList<String> urlsList = new ArrayList<>();
    private ArrayList<Integer> selectedSources = new ArrayList<>();

    private SourceViewModel viewModel;

    public static SettingsFragment newInstance(){
        return new SettingsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        btnConfirm = view.findViewById(R.id.btnSelectSources);
//        btnSelectUnselectAll = view.findViewById(R.id.btnSelectUnselectAll);

        /*progressBar = findViewById(R.id.progressBar);

        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.MULTIPLY);*/

        ApiManager.getInstance().getSourcesService().getApiSources(url).enqueue(this);

        viewModel = ViewModelProviders.of(this).get(SourceViewModel.class);

        return view;
    }

    @Override
    public void onResponse(@NonNull Call<NewsApiSources> call, Response<NewsApiSources> response) {
        if(response.isSuccessful()){
            ArrayList<Source> sources = response.body().getSources();

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
                    if(source.getSourceId().equals("la-gaceta")){
                        namesList.add("Jutarnji List");
                        domainsList.add("jutarnji.hr");
                        urlsList.add("https://www.jutarnji.hr/");
                    }
                    namesList.add(source.getName());
                    domainsList.add(source.getSourceId());
                    urlsList.add(domain);
                    Log.d("NEWS", "onResponse: " + source.getName() + " SOURCE_ID: ///"+ source.getSourceId() +"/// NO."+ i++);
                }
            }

            initRecyclerView(getView(), namesList, domainsList, urlsList);
        }
    }

    @Override
    public void onFailure(Call<NewsApiSources> call, Throwable t) {

    }

    public void initRecyclerView(View view, ArrayList<String> names, ArrayList<String> domains, ArrayList<String> urls){
        recyclerView = view.findViewById(R.id.sources_recycler_view);
        final SourcesRecyclerViewAdapter adapter = new SourcesRecyclerViewAdapter(names, domains, urls, viewModel.getAllSources());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        /*btnSelectUnselectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedSources = adapter.getSelectedPositions();
                if(selectedSources.isEmpty()){
                    adapter.setSelectedPositions("all");
                    btnSelectUnselectAll.setText(R.string.unselect_all_text);
                }
                else{
                    adapter.setSelectedPositions("none");
                    btnSelectUnselectAll.setText(R.string.select_all_text);
                }
            }
        });*/

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), TestActivity.class);


                viewModel.deleteAllSources();
                Log.d("TAAG", "onClick: " + adapter.getSelectedPositions());

                selectedSources = adapter.getSelectedPositions();

                if(selectedSources != null && !selectedSources.isEmpty()){
                    for(int i = 0; i < selectedSources.size(); i++){
                        Log.d("SELECTED", "onClick: " + selectedSources.get(i) + " " + namesList.get(selectedSources.get(i)));
                        viewModel.insertSource(new barisic.newsgetter.db_classes.Source(namesList.get(selectedSources.get(i)), domainsList.get(selectedSources.get(i))));
                    }

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    v.getContext().startActivity(intent);
                }
                else{
                    Toast.makeText(getContext(), getResources().getString(R.string.no_sources_warning), Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
