package barisic.newsgetter.viewmodel.helper_classes;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import barisic.newsgetter.viewmodel.news_api_classes.ApiManager;
import barisic.newsgetter.model.api_classes.NewsApiSources;
import barisic.newsgetter.model.api_classes.Source;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateDatabaseSources implements Callback<NewsApiSources> {

    private final static String url = "sources?apiKey=38fbf5c450684e339b0e300b7bd7f8ea";
    private final static String TAG = "Firebase UpdateSources";

    private static DatabaseReference dbReference;

    public static void updateInstance(){
        UpdateDatabaseSources instance = new UpdateDatabaseSources();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        dbReference = database.getReference("sources");
        ApiManager.getInstance().getSourcesService().getApiSources(url).enqueue(instance);

        Log.d(TAG, "updateInstance: " + dbReference.toString());
    }

    @Override
    public void onResponse(@NonNull Call<NewsApiSources> call, Response<NewsApiSources> response) {

        String id;
        String domain;

        if(response.body() != null && response.isSuccessful()){

            ArrayList<Source> sources = response.body().getSources();


            id = dbReference.push().getKey();
            dbReference.child(id).child("name").setValue("24sata");
            dbReference.child(id).child("domain").setValue("24sata.hr");
            dbReference.child(id).child("url").setValue("https://www.24sata.hr/");

            for(Source source: sources){
                URL url = null;
                try {
                    url = new URL(source.getUrl());
                }catch (MalformedURLException e){
                    Log.d("URL MALFORMED", "onResponse: " + e);
                }
                domain = url.getHost();

                if(!source.getSourceId().matches("google-news-(.*)")){
                    if(source.getSourceId().equals("la-gaceta")){
                        id = dbReference.push().getKey();
                        dbReference.child(id).child("name").setValue("Jutarnji List");
                        dbReference.child(id).child("domain").setValue("jutarnji.hr");
                        dbReference.child(id).child("url").setValue("https://www.jutarnji.hr/");
                    }
                    id = dbReference.push().getKey();
                    dbReference.child(id).child("name").setValue(source.getName());
                    dbReference.child(id).child("domain").setValue(source.getSourceId());
                    dbReference.child(id).child("url").setValue(domain);
                }

            }
        }

    }

    @Override
    public void onFailure(@NonNull Call<NewsApiSources> call,@NonNull Throwable t) {

    }
}
