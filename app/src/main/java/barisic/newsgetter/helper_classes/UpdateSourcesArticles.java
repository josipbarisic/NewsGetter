package barisic.newsgetter.helper_classes;

import android.util.Log;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UpdateSourcesArticles {

    private static FirebaseDatabase database;
    private static DatabaseReference dbSources;

    private static ArrayList<String> sourcesList = new ArrayList<>();
    private static String sourceDomain;

//    private static String sourceId;

    public static void update(ArrayList<String> sources){

        sourcesList = sources;

        /*database = FirebaseDatabase.getSourcesInstance();
        dbSources = database.getReference("sources");

        dbSources.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snap: dataSnapshot.getChildren()){

//                    sourceId = snap.getKey();
                    sourceDomain = String.valueOf(snap.child("domain").getValue());

//                    dbSource = dbSources.child(sourceId);

                    Log.d("UpdateSourceArticles", "Updating articles for " + snap.child("name").getValue() + "...");

                    *//*

                    try{
                        Thread.sleep(200);
                    }
                    catch (InterruptedException e){
                        Log.d("INTERRUPTED_EXCEPTION", "onDataChange: " + e);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        for (String source: sourcesList) {
//            UpdateArticlesApiCall.loadArticles(source);
            Log.d("SOURCE_ITEM", "PASSED: " + source);
        }

//        Log.d("SOURCES_LIST", "After update: " + sourcesList.toString());
    }
}
