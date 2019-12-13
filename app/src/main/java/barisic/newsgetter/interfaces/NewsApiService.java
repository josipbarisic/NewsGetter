package barisic.newsgetter.interfaces;

import java.util.Date;

import barisic.newsgetter.helper_classes.NewsApiResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface NewsApiService {
    @GET
    Call<NewsApiResponse> getNews(@Url String url);
}
