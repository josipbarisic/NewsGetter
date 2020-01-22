package barisic.newsgetter.helper_classes;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;

import barisic.newsgetter.news_api_classes.Article;
import barisic.newsgetter.news_api_classes.NewsApiArticles;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateSourceArticles{

    private static FirebaseDatabase database;
    private static DatabaseReference dbRef;
    private static DatabaseReference dbSource;

    private static String sourceId;
    private static String sourceDomain;

    private static ArrayList<Article> articles = new ArrayList<>();

    public static void update(){

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("sources");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snap: dataSnapshot.getChildren()){

                    sourceId = snap.getKey();
                    sourceDomain = snap.child("domain").getValue().toString();

                    dbSource = dbRef.child(sourceId);

                    Log.d("UpdateArticles", "onDataChange: " + sourceDomain + " || " + dbSource);

                    UpdateArticlesApiCall.loadArticles(sourceDomain);
                    articles = UpdateArticlesApiCall.getArticles();

                    for (Article article: articles){
                        Log.d("DBTAG", "onResponse: " + article.getTitle());
                        dbSource.child("articles").child("title").setValue(article.getTitle());
                        dbSource.child("articles").child("date").setValue(article.getDate());
                        dbSource.child("articles").child("image").setValue(article.getImageUrl());
                        dbSource.child("articles").child("description").setValue(article.getDescription());
                        dbSource.child("articles").child("url").setValue(article.getArticleUrl());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
