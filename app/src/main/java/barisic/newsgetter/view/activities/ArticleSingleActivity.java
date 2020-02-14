package barisic.newsgetter.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import barisic.newsgetter.R;

public class ArticleSingleActivity extends AppCompatActivity {

    WebView webArticle;
    Button browseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_single);

        webArticle = findViewById(R.id.web_view);
        browseButton = findViewById(R.id.browseButton);

        Intent i = getIntent();

        if(i.getExtras() != null){

            final String url = i.getExtras().getString("url");

            webArticle.getSettings().setJavaScriptEnabled(true);
            //enable opening links in WebView
            webArticle.setWebViewClient(new WebViewClient(){
                @TargetApi(21)
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    view.loadUrl(request.getUrl().toString());

                    return true;
                }
            });
            webArticle.loadUrl(url);

            browseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                }
            });
        }
    }
}
