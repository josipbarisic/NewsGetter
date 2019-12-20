package barisic.newsgetter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import barisic.newsgetter.helper_classes.ArticleScraper;

public class ArticleSingle extends AppCompatActivity {

    WebView webArticle;
    Button browseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_single);

        webArticle = findViewById(R.id.tvArticleText);
        browseButton = findViewById(R.id.browseButton);

        Intent i = getIntent();

        if(i.getExtras() != null){

            final String url = i.getExtras().getString("url");

//            webArticle = new ArticleScraper(url, webArticle).getArticle();
            webArticle.loadUrl(url);
            /*webArticle.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    view.loadUrl(url);
                    return true;
                }
            });
            webArticle.loadUrl(url);*/
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
