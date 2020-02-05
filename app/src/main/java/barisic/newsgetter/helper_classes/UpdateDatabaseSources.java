package barisic.newsgetter.helper_classes;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import barisic.newsgetter.news_api_classes.NewsApiSources;
import barisic.newsgetter.news_api_classes.Source;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateDatabaseSources {

    private final static String url = "sources?apiKey=38fbf5c450684e339b0e300b7bd7f8ea";
    private final static String TAG = "Firebase UpdateSources";

    private static FirebaseDatabase database;
    private static DatabaseReference dbReference;

    public static void updateInstance(){

        database = FirebaseDatabase.getInstance();

        dbReference = database.getReference("sources");
        ApiManager.getInstance().getSourcesService().getApiSources(url).enqueue(new Callback<NewsApiSources>() {
            @Override
            public void onResponse(Call<NewsApiSources> call, Response<NewsApiSources> response) {
                String id;
                String domain;

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy    HH:mm");
                Date date = new Date();
                String dateTime = dateFormat.format(date);

                if(response.isSuccessful() && response.body() != null){

                    ArrayList<Source> sources = response.body().getSources();


                    id = dbReference.push().getKey();
                    dbReference.child(id).child("name").setValue("24sata");
                    dbReference.child(id).child("domain").setValue("24sata.hr");
                    dbReference.child(id).child("url").setValue("https://www.24sata.hr/");
                    dbReference.child(id).child("updated").setValue(dateTime);

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
                                dbReference.child(id).child("updated").setValue(dateTime);
                            }
                            id = dbReference.push().getKey();
                            dbReference.child(id).child("name").setValue(source.getName());
                            dbReference.child(id).child("domain").setValue(source.getSourceId());
                            dbReference.child(id).child("url").setValue(domain);
                            dbReference.child(id).child("updated").setValue(dateTime);
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<NewsApiSources> call, Throwable t) {

            }
        });
    }
}
