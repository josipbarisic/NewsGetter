package barisic.newsgetter.helper_classes;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import barisic.newsgetter.news_api_classes.NewsApiSources;
import barisic.newsgetter.news_api_classes.Source;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateDatabase implements Callback<NewsApiSources> {

    private final static String url = "sources?apiKey=38fbf5c450684e339b0e300b7bd7f8ea";
    private final static String TAG = "Firebase UpdateDatabase";

    private static FirebaseDatabase database;
    private static DatabaseReference dbReference;
    private ArrayList<Source> sources;

    public static void updateInstance(){
        UpdateDatabase instance = new UpdateDatabase();

        database = FirebaseDatabase.getInstance();

        dbReference = database.getReference("sources");
        ApiManager.getInstance().getSourcesService().getApiSources(url).enqueue(instance);

        Log.d(TAG, "updateInstance: " + dbReference.toString());
    }

    @Override
    public void onResponse(Call<NewsApiSources> call, Response<NewsApiSources> response) {

        String id;
        if(response.isSuccessful()){
            sources = response.body().getSources();

            for(Source source: sources){
                id = dbReference.push().getKey();
                dbReference.child(id).child("name").setValue(source.getName());
                dbReference.child(id).child("domain").setValue(source.getSourceId());
                dbReference.child(id).child("url").setValue(source.getUrl());
            }
        }

    }

    @Override
    public void onFailure(Call<NewsApiSources> call, Throwable t) {

    }
}
