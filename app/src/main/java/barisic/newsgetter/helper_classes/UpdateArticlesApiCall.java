package barisic.newsgetter.helper_classes;
import android.util.Log;

import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import barisic.newsgetter.adapters.ArticlesRecyclerViewAdapter;
import barisic.newsgetter.news_api_classes.Article;
import barisic.newsgetter.news_api_classes.NewsApiArticles;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateArticlesApiCall{

    private static FirebaseDatabase database = FirebaseDatabase.getInstance();

    private static ArrayList<Article> articles = new ArrayList<>();

    public static void loadArticles(final String domain) {

        final DatabaseReference dbArticles = database.getReference("articles");
        final DatabaseReference dbSource = database.getReference("sources");
        dbSource.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snap: dataSnapshot.getChildren()){
                    final String snapKey = snap.getKey();
                    String snapDomain = String.valueOf(snap.child("domain").getValue());
                    if(snapDomain.matches(domain)){

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        Date dateNow = new Date();
                        final String dateTimeNow = dateFormat.format(dateNow);

                        String updatedDateTime = String.valueOf(snap.child("updated").getValue());
                        long dateTimeDifference = 0;


                        try{
                            Date now = dateFormat.parse(dateTimeNow);
                            Date updated = dateFormat.parse(updatedDateTime);

                            dateTimeDifference = (now.getTime() - updated.getTime())/60000;

                            Log.d("DIFFERENCE", "onDataChange: " + dateTimeDifference);
                        }
                        catch (Exception e){
                            Log.d("PARSE_EXCEPTION", "onDataChange: " + e);
                        }

                        if(dateTimeDifference >= 180){

                            dbArticles.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snap: dataSnapshot.getChildren()){
                                        if(String.valueOf(snap.child("source_id").getValue()).equals(snapKey)){
                                            snap.getRef().removeValue();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            String url;

                            if (domain.equals("jutarnji.hr") || domain.equals("24sata.hr")) {
                                url = "everything?domains=" + domain + "&pageSize=100&apiKey=38fbf5c450684e339b0e300b7bd7f8ea";
                            } else if (domain.equals("-recommended-news-")) {
                                url = "top-headlines?country=us&pageSize=100&apiKey=38fbf5c450684e339b0e300b7bd7f8ea";
                            } else {
                                url = "everything?sources=" + domain + "&pageSize=100&apiKey=38fbf5c450684e339b0e300b7bd7f8ea";
                            }
                            ApiManager.getInstance().getArticlesService().getNews(url).enqueue(new Callback<NewsApiArticles>() {
                                @Override
                                public void onResponse(Call<NewsApiArticles> call, Response<NewsApiArticles> response) {
                                    if (response.body() != null && response.isSuccessful()) {
                                        articles = response.body().getResponse();
                                        dbSource.child(snapKey).child("updated").setValue(dateTimeNow);
                                        Log.d("UpdateArticleApiCall", "Updating source: " + domain);
                                        String articleId;
                                        for (Article article : articles) {
                                            Log.d("UpdateApiCall", "News Item FOR_LOOP_ARTICLES: " + article.getTitle());
                                            articleId = dbArticles.push().getKey();
                                            dbArticles.child(articleId).child("source_domain").setValue(domain);
                                            dbArticles.child(articleId).child("source_id").setValue(snapKey);
                                            dbArticles.child(articleId).child("title").setValue(article.getTitle());
                                            dbArticles.child(articleId).child("date").setValue(article.getDate());
                                            dbArticles.child(articleId).child("image").setValue(article.getImageUrl());
                                            dbArticles.child(articleId).child("description").setValue(article.getDescription());
                                            dbArticles.child(articleId).child("url").setValue(article.getArticleUrl());
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<NewsApiArticles> call, Throwable t) {

                                }
                            });

                        }
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*final String sourceDomain = domain;
        String url;

        if (domain.equals("jutarnji.hr") || domain.equals("24sata.hr")) {
            url = "everything?domains=" + domain + "&pageSize=100&apiKey=38fbf5c450684e339b0e300b7bd7f8ea";
        } else if (domain.equals("-recommended-news-")) {
            url = "top-headlines?country=us&pageSize=100&apiKey=38fbf5c450684e339b0e300b7bd7f8ea";
        } else {
            url = "everything?sources=" + domain + "&pageSize=100&apiKey=38fbf5c450684e339b0e300b7bd7f8ea";
        }

        ApiManager.getSourcesInstance().getArticlesService().getNews(url).enqueue(new Callback<NewsApiArticles>() {
            @Override
            public void onResponse(Call<NewsApiArticles> call, Response<NewsApiArticles> response) {
                if (response.body() != null && response.isSuccessful()) {
                    articles = response.body().getResponse();
                    Log.d("UpdateArticleApiCall", "Updating source: " + sourceDomain);

                    for (Article article : articles) {
                        Log.d("UpdateApiCall", "News Item FOR_LOOP_ARTICLES: " + article.getTitle());
                        *//*articleId = dbArticles.push().getKey();
                        dbArticles.child(articleId).child("source_domain").setValue(sourceDomain);
                        dbArticles.child(articleId).child("title").setValue(article.getTitle());
                        dbArticles.child(articleId).child("date").setValue(article.getDate());
                        dbArticles.child(articleId).child("image").setValue(article.getImageUrl());
                        dbArticles.child(articleId).child("description").setValue(article.getDescription());
                        dbArticles.child(articleId).child("url").setValue(article.getArticleUrl());*//*
                    }
                }
            }

            @Override
            public void onFailure(Call<NewsApiArticles> call, Throwable t) {

            }
        });*/
    }
}
