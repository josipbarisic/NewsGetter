package barisic.newsgetter.viewmodel.news_api_classes;

import barisic.newsgetter.viewmodel.interfaces.ArticlesService;
import barisic.newsgetter.viewmodel.interfaces.SourcesService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Singleton klasa koja upravlja api pozivima.
 */
public class ApiManager {
    private static ApiManager instance;
    private ArticlesService articlesService;
    private SourcesService sourcesService;
    private ApiManager(){
        Retrofit.Builder builder = new Retrofit.Builder();
        //postavljanje retrofit-a
        Retrofit retrofit = builder.baseUrl("https://newsapi.org/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        articlesService = retrofit.create(ArticlesService.class);
        sourcesService = retrofit.create(SourcesService.class);

    }
    public static ApiManager getInstance(){
        if (instance == null){
            instance = new ApiManager();
        }
        return instance;
    }
    public ArticlesService getArticlesService(){
        return articlesService;
    }

    public SourcesService getSourcesService(){
        return sourcesService;
    }
}
