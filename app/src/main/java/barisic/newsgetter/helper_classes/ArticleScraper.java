package barisic.newsgetter.helper_classes;

import android.os.AsyncTask;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Pattern;

public class ArticleScraper {
    private static String url = "";
    private static String sArticleText = "";
    final static String TAG = "WEBVIEW";
    private static WebView wvArticle;

    public ArticleScraper(String articleUrl, WebView wv){
        this.url = articleUrl;
        this.wvArticle = wv;
    }

    public static WebView getArticle(){
        new AsyncTask<String, Integer, Document>(){

            @Override
            protected Document doInBackground(String... params) {
                Document doc = null;
                try {
                    doc = Jsoup.connect(params[0]).get();
                }catch (IOException e){
                    Log.d(TAG, "doInBackground: " + e);
                }
                return doc;
            }

            @Override
            protected void onPostExecute(Document html) {
                sArticleText = html.toString();
                sArticleText = sArticleText.replace("\"", "'");

                wvArticle.loadDataWithBaseURL("", sArticleText, "text/html", "utf-8", "");
            }
        }.execute(url);

        return wvArticle;
    }
}
