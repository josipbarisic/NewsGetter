package barisic.newsgetter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity{

    Button oBtnHeadlines;
    Button oBtnHome;
    Button oSearchButton;
    EditText etSearch;
    String searchQuery;

    //datum za url parametar from (OVERCOMPLICATED)
//    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat dfy = new SimpleDateFormat("yyyy");
    DateFormat dfm = new SimpleDateFormat("MM");
    DateFormat dfd = new SimpleDateFormat("dd");
    Date oDate = new Date();
    int day = Integer.parseInt(dfd.format(oDate)) - 2;

    String date = dfy.format(oDate) + "-" + dfm.format(oDate) + "-" + day;

    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //,setLocale("hr");
        setContentView(R.layout.activity_main);

        oBtnHeadlines = findViewById(R.id.headlinesButton);
        oBtnHome = findViewById(R.id.homeButton);
        oSearchButton = findViewById(R.id.searchButton);
        etSearch = findViewById(R.id.etSearch);

        oBtnHeadlines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NewsTest.class);
                url = "top-headlines?country=gb&apiKey=38fbf5c450684e339b0e300b7bd7f8ea";
                i.putExtra("url", url);

                startActivity(i);
            }
        });

        oBtnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NewsTest.class);
                //url = "everything?q=&domains=24sata.hr,jutarnji.hr&pageSize=100&from=" + date + "&sortBy=publishedAt&apiKey=38fbf5c450684e339b0e300b7bd7f8ea";
                url = "everything?q=&domains=jutarnji.hr&pageSize=100&from=" + date + "&sortBy=publishedAt&apiKey=38fbf5c450684e339b0e300b7bd7f8ea";
                i.putExtra("url", url);

                startActivity(i);
            }
        });


        oSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchQuery = etSearch.getText().toString();

                if(!searchQuery.matches("")){
                    Intent i = new Intent(MainActivity.this, NewsTest.class);
                    url = "everything?q="+ searchQuery +"&language=en&pageSize=100&from="+ date +"&sortBy=publishedAt&apiKey=38fbf5c450684e339b0e300b7bd7f8ea";
                    i.putExtra("url", url);

                    startActivity(i);
                }
                else{
                    Toast.makeText(getApplicationContext(), getText(R.string.search_warning).toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

    }
}
