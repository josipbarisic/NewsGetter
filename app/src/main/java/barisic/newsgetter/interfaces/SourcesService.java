package barisic.newsgetter.interfaces;

import barisic.newsgetter.helper_classes.NewsApiSources;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface SourcesService {
    @GET
    Call<NewsApiSources> getApiSources(@Url String domain);
}
