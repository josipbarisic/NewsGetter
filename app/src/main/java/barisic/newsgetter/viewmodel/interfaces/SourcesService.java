package barisic.newsgetter.viewmodel.interfaces;

import barisic.newsgetter.model.api_classes.NewsApiSources;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface SourcesService {
    @GET
    Call<NewsApiSources> getApiSources(@Url String domain);
}
